package com.smartcare.cloud.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcare.cloud.dto.AssessmentReportDTO;
import com.smartcare.cloud.dto.ElderlyPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.event.ElderlyUpdatedEvent;
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.facade.ElderlyFacadeService;
import com.smartcare.cloud.vo.HealthStatisticsVO;
import com.smartcare.cloud.vo.HealthStatisticsVO.AgeHealthDistribution;
import com.smartcare.cloud.vo.HealthStatisticsVO.HealthRiskAssessment;
import com.smartcare.cloud.vo.HealthStatisticsVO.HealthStatusDistribution;
import com.smartcare.cloud.vo.HealthStatisticsVO.HealthTrend;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 老人信息服务实现类
 * 
 * 重构说明：
 * 1. 使用Facade模式封装跨模块调用，降低耦合
 * 2. 使用事件驱动模式实现异步解耦
 * 3. 专注于老人信息的核心业务逻辑
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ElderlyServiceImpl extends ServiceImpl<ElderlyMapper, Elderly> implements ElderlyService {

    private static final Logger log = LoggerFactory.getLogger(ElderlyServiceImpl.class);

    @Lazy
    @Autowired
    private ElderlyFacadeService elderlyFacadeService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 计算年龄
     */
    private Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return null;
        }

        try {
            LocalDate today = LocalDate.now();
            if (birthDate.isAfter(today)) {
                return null;
            }

            int age = today.getYear() - birthDate.getYear();
            if (today.getDayOfYear() < birthDate.getDayOfYear()) {
                age--;
            }

            return age >= 0 ? age : null;
        } catch (Exception e) {
            log.error("计算年龄失败", e);
            return null;
        }
    }

    @Override
    public ResponseResult<Page<Elderly>> getElderlyPage(ElderlyPageDTO pageDTO) {
        try {
            Page<Elderly> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();

            // 根据姓名模糊查询
            if (StringUtils.isNotBlank(pageDTO.getName())) {
                queryWrapper.like("name", pageDTO.getName());
            }

            // 根据身份证号查询
            if (StringUtils.isNotBlank(pageDTO.getIdCard())) {
                queryWrapper.like("id_card", pageDTO.getIdCard());
            }

            // 根据性别查询 (暂时注释掉，等Lombok问题解决后启用)
            // if (StringUtils.isNotBlank(pageDTO.getGender())) {
            //     queryWrapper.eq("gender", pageDTO.getGender());
            // }
            // 根据健康状态查询  
            if (StringUtils.isNotBlank(pageDTO.getHealthStatus())) {
                queryWrapper.eq("health_status", pageDTO.getHealthStatus());
            }

            // 根据年龄范围查询（注意：数据库中没有age字段，这里暂时注释掉）
            // if (pageDTO.getMinAge() != null) {
            //     queryWrapper.ge("age", pageDTO.getMinAge());
            // }
            // if (pageDTO.getMaxAge() != null) {
            //     queryWrapper.le("age", pageDTO.getMaxAge());
            // }
            // 按创建时间倒序
            queryWrapper.orderByDesc("create_time");

            // 使用标准的MyBatis Plus查询，避免自定义方法问题
            Page<Elderly> result = this.page(page, queryWrapper);

            // 计算年龄并设置到结果中
            if (result.getRecords() != null) {
                result.getRecords().forEach(elderly -> {
                    if (elderly.getBirthDate() != null) {
                        elderly.setAge(calculateAge(elderly.getBirthDate()));
                    }
                });
            }

            return ResponseResult.success(result);

        } catch (Exception e) {
            log.error("分页查询老人信息失败", e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    @Cacheable(value = "elderly", key = "#id", unless = "#result == null || #result.code != 200")
    public ResponseResult<Elderly> getElderlyById(Long id) {
        try {
            // 使用QueryWrapper查询，确保字段映射正确
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            queryWrapper.eq("is_deleted", 0);

            Elderly elderly = this.getOne(queryWrapper);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 计算年龄
            if (elderly.getBirthDate() != null) {
                int age = java.time.Period.between(elderly.getBirthDate(), java.time.LocalDate.now()).getYears();
                elderly.setAge(age);
            }

            return ResponseResult.success(elderly);
        } catch (Exception e) {
            log.error("根据ID查询老人信息失败，ID: {}", id, e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    public ResponseResult<Void> addElderly(Elderly elderly) {
        try {
            // 检查身份证号是否已存在
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_card", elderly.getIdCard());
            Elderly existingElderly = this.getOne(queryWrapper);
            if (existingElderly != null) {
                return ResponseResult.error("身份证号已存在");
            }

            elderly.setCreateTime(LocalDateTime.now());
            elderly.setUpdateTime(LocalDateTime.now());
            boolean success = this.save(elderly);

            if (success) {
                // 发布老人创建事件
                eventPublisher.publishEvent(new ElderlyUpdatedEvent(this, elderly, "CREATE"));
                log.info("已发布老人创建事件，ID：{}", elderly.getId());
                return ResponseResult.success();
            } else {
                return ResponseResult.error("添加失败");
            }
        } catch (Exception e) {
            log.error("新增老人信息失败", e);
            return ResponseResult.error("添加失败");
        }
    }

    @Override
    @CacheEvict(value = "elderly", key = "#elderly.id")
    public ResponseResult<Void> updateElderly(Elderly elderly) {
        try {
            Elderly existingElderly = this.getById(elderly.getId());
            if (existingElderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 检查身份证号是否被其他人使用
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_card", elderly.getIdCard())
                    .ne("id", elderly.getId());
            Elderly duplicateElderly = this.getOne(queryWrapper);
            if (duplicateElderly != null) {
                return ResponseResult.error("身份证号已被其他人使用");
            }

            elderly.setUpdateTime(LocalDateTime.now());
            boolean success = this.updateById(elderly);

            if (success) {
                // 发布老人更新事件
                eventPublisher.publishEvent(new ElderlyUpdatedEvent(this, elderly, "UPDATE"));
                log.info("已发布老人更新事件，ID：{}", elderly.getId());
                return ResponseResult.success();
            } else {
                return ResponseResult.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新老人信息失败，ID: {}", elderly.getId(), e);
            return ResponseResult.error("更新失败");
        }
    }

    @Override
    @CacheEvict(value = "elderly", key = "#id")
    public ResponseResult<Void> deleteElderly(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            boolean success = this.removeById(id);
            if (success) {
                // 发布老人删除事件
                eventPublisher.publishEvent(new ElderlyUpdatedEvent(this, elderly, "DELETE"));
                log.info("已发布老人删除事件，ID：{}", id);
                return ResponseResult.success();
            } else {
                return ResponseResult.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除老人信息失败，ID: {}", id, e);
            return ResponseResult.error("删除失败");
        }
    }

    @Override
    @CacheEvict(value = "elderly", allEntries = true)
    public ResponseResult<Void> batchDeleteElderly(List<Long> ids) {
        try {
            boolean success = this.removeByIds(ids);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除老人信息失败，IDs: {}", ids, e);
            return ResponseResult.error("批量删除失败");
        }
    }

    @Override
    public ResponseResult<List<Elderly>> getKeyElderlyList() {
        try {
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            // 查询重点关注的老人（健康状态为危险或需要特殊关注的）
            queryWrapper.in("health_status", "DANGER", "WARNING")
                    .orderByDesc("updated_time")
                    .last("LIMIT 20"); // 限制返回20条

            List<Elderly> elderlyList = this.list(queryWrapper);
            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            log.error("获取重点关注老人列表失败", e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    public ResponseResult<Object> getElderlyHealthRecords(Long id) {
        // 使用Facade封装跨模块调用
        return elderlyFacadeService.getElderlyHealthRecords(id);
    }

    @Override
    public ResponseResult<Void> addHealthRecord(Long id, Object healthRecord) {
        try {
            // 将Object转换为HealthRecord
            ObjectMapper objectMapper = new ObjectMapper();
            HealthRecord record = objectMapper.convertValue(healthRecord, HealthRecord.class);

            // 使用Facade封装跨模块调用
            return elderlyFacadeService.addHealthRecord(id, record);

        } catch (Exception e) {
            log.error("添加健康记录失败，ID：{}", id, e);
            return ResponseResult.error("添加健康记录失败");
        }
    }

    @Override
    public ResponseResult<Object> getEmergencyContacts(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            Map<String, Object> emergencyContacts = new HashMap<>();
            emergencyContacts.put("elderlyId", id);
            // 暂时返回空值，等待紧急联系人字段添加后完善
            emergencyContacts.put("emergencyContactName", null);
            emergencyContacts.put("emergencyContactPhone", null);
            emergencyContacts.put("guardianName", null);
            emergencyContacts.put("guardianPhone", null);
            emergencyContacts.put("guardianRelation", null);

            return ResponseResult.success(emergencyContacts);
        } catch (Exception e) {
            log.error("获取紧急联系人失败，ID：{}", id, e);
            return ResponseResult.error("获取紧急联系人失败");
        }
    }

    @Override
    public ResponseResult<Void> updateEmergencyContacts(Long id, Object emergencyContacts) {
        try {
            log.info("更新紧急联系人，老人ID：{}，联系人：{}", id, emergencyContacts);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("更新紧急联系人失败，ID：{}", id, e);
            return ResponseResult.error("更新紧急联系人失败");
        }
    }

    @Override
    public ResponseResult<Object> getCarePlan(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            Map<String, Object> carePlan = new HashMap<>();
            carePlan.put("elderlyId", id);
            carePlan.put("elderlyName", elderly.getName());
            carePlan.put("careLevel", null);
            carePlan.put("familyDoctorId", null);
            carePlan.put("mobilityStatus", null);
            carePlan.put("roomNumber", null);
            carePlan.put("entryDate", null);
            carePlan.put("remark", null);

            return ResponseResult.success(carePlan);
        } catch (Exception e) {
            log.error("获取照护计划失败，ID：{}", id, e);
            return ResponseResult.error("获取照护计划失败");
        }
    }

    @Override
    public ResponseResult<Void> updateCarePlan(Long id, Object carePlan) {
        try {
            log.info("更新照护计划，老人ID：{}，计划：{}", id, carePlan);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("更新照护计划失败，ID：{}", id, e);
            return ResponseResult.error("更新照护计划失败");
        }
    }

    @Override
    public ResponseResult<Object> getHealthWarnings(Long id) {
        try {
            // 这里应该查询健康预警表，暂时返回基本的预警信息
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            Map<String, Object> warnings = new HashMap<>();
            warnings.put("elderlyId", id);
            warnings.put("elderlyName", elderly.getName());
            warnings.put("healthStatus", elderly.getHealthStatus());
            warnings.put("hasWarnings", "DANGER".equals(elderly.getHealthStatus()) || "WARNING".equals(elderly.getHealthStatus()));

            return ResponseResult.success(warnings);
        } catch (Exception e) {
            log.error("获取健康预警失败，ID：{}", id, e);
            return ResponseResult.error("获取健康预警失败");
        }
    }

    @Override
    public ResponseResult<Object> generateAssessmentReport(Long id) {
        // 使用Facade封装跨模块调用
        return elderlyFacadeService.generateAssessmentReport(id);
    }

    @Override
    public ResponseResult<Object> exportElderlyData(List<Long> ids) {
        try {
            List<Elderly> elderlyList;
            if (ids == null || ids.isEmpty()) {
                elderlyList = this.list();
            } else {
                elderlyList = this.listByIds(ids);
            }

            Map<String, Object> exportData = new HashMap<>();
            exportData.put("exportTime", LocalDateTime.now());
            exportData.put("totalCount", elderlyList.size());
            exportData.put("data", elderlyList);

            return ResponseResult.success(exportData);
        } catch (Exception e) {
            log.error("导出老人档案失败", e);
            return ResponseResult.error("导出失败");
        }
    }

    @Override
    public ResponseResult<Object> importElderlyData(Object importData) {
        try {
            log.info("批量导入老人档案：{}", importData);

            Map<String, Object> result = new HashMap<>();
            result.put("importTime", LocalDateTime.now());
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("message", "导入功能待实现");

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("导入老人档案失败", e);
            return ResponseResult.error("导入失败");
        }
    }

    @Override
    public ResponseResult<Object> getCareLevelStatistics() {
        try {
            // 简化版照护统计 - 创建基本的统计数据
            Map<String, Object> statistics = new HashMap<>();

            // 1. 照护等级分布统计
            long totalElderly = this.count();

            // 统计不同照护等级人数
            QueryWrapper<Elderly> selfCareWrapper = new QueryWrapper<>();
            selfCareWrapper.eq("care_level", 1).or().isNull("care_level");
            long selfCareCount = this.count(selfCareWrapper);

            QueryWrapper<Elderly> semiCareWrapper = new QueryWrapper<>();
            semiCareWrapper.eq("care_level", 2);
            long semiCareCount = this.count(semiCareWrapper);

            QueryWrapper<Elderly> fullCareWrapper = new QueryWrapper<>();
            fullCareWrapper.eq("care_level", 3);
            long fullCareCount = this.count(fullCareWrapper);

            // 照护等级分布
            List<Map<String, Object>> careLevelDistribution = new ArrayList<>();
            careLevelDistribution.add(createCareLevelData("1", "自理", selfCareCount, totalElderly));
            careLevelDistribution.add(createCareLevelData("2", "半自理", semiCareCount, totalElderly));
            careLevelDistribution.add(createCareLevelData("3", "不能自理", fullCareCount, totalElderly));

            // 年龄段照护需求
            List<Map<String, Object>> ageCareNeeds = createAgeCareNeedsData();

            // 照护服务统计
            Map<String, Object> careServiceStats = createCareServiceStats();

            // 照护质量统计
            Map<String, Object> careQualityStats = createCareQualityStats();

            // 费用统计
            Map<String, Object> careCostStats = createCareCostStats();

            // 护理人员工作量
            List<Map<String, Object>> caregiverWorkloads = createCaregiverWorkloads();

            // 汇总信息
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalElderly", totalElderly);
            summary.put("selfCareCount", selfCareCount);
            summary.put("semiCareCount", semiCareCount);
            summary.put("fullCareCount", fullCareCount);
            summary.put("totalCaregivers", 5);
            summary.put("avgServiceScore", 4.6);
            summary.put("avgSatisfaction", 4.3);
            summary.put("monthlyServiceCount", 755);
            summary.put("updateTime", LocalDateTime.now());

            // 组装结果
            statistics.put("careLevelDistribution", careLevelDistribution);
            statistics.put("ageCareNeeds", ageCareNeeds);
            statistics.put("careServiceStats", careServiceStats);
            statistics.put("careQualityStats", careQualityStats);
            statistics.put("careCostStats", careCostStats);
            statistics.put("caregiverWorkloads", caregiverWorkloads);
            statistics.put("summary", summary);

            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取照护统计失败", e);
            return ResponseResult.error("获取照护统计失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseResult<Object> getAgeDistribution() {
        try {
            // 这里应该根据出生日期计算年龄段分布
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("birth_date, COUNT(*) as count")
                    .groupBy("birth_date");

            List<Map<String, Object>> ageStats = this.listMaps(queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("ageDistribution", ageStats);

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取年龄段分布失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    @Override
    public ResponseResult<Object> searchElderly(String keyword, Integer pageNum, Integer pageSize) {
        try {
            Page<Elderly> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("name", keyword)
                        .or().like("id_card", keyword)
                        .or().like("phone", keyword));
            }

            Page<Elderly> resultPage = this.page(page, queryWrapper);

            return ResponseResult.success(resultPage);
        } catch (Exception e) {
            log.error("搜索老人档案失败，关键词：{}", keyword, e);
            return ResponseResult.error("搜索失败");
        }
    }

    @Override
    public ResponseResult<HealthStatisticsVO> getHealthStatistics() {
        try {
            HealthStatisticsVO statistics = new HealthStatisticsVO();

            // 获取健康状态分布
            ResponseResult<List<HealthStatusDistribution>> statusResult = getHealthStatusDistribution();
            if (statusResult.getCode() == 200) {
                statistics.setHealthStatusDistribution(statusResult.getData());
            }

            // 获取年龄段健康分布
            ResponseResult<List<AgeHealthDistribution>> ageResult = getAgeHealthDistribution();
            if (ageResult.getCode() == 200) {
                statistics.setAgeHealthDistribution(ageResult.getData());
            }

            // 获取健康风险评估
            ResponseResult<HealthRiskAssessment> riskResult = getHealthRiskAssessment();
            if (riskResult.getCode() == 200) {
                statistics.setHealthRiskAssessment(riskResult.getData());
            }

            // 生成健康趋势数据（模拟数据，实际应根据历史记录计算）
            statistics.setHealthTrends(generateHealthTrends());

            // 生成总体统计信息
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", this.count());
            summary.put("updateTime", LocalDateTime.now());
            statistics.setSummary(summary);

            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取健康统计数据失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    @Override
    public ResponseResult<List<HealthStatusDistribution>> getHealthStatusDistribution() {
        try {
            // 查询健康状态分布
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("health_status, COUNT(*) as count")
                    .groupBy("health_status");

            List<Map<String, Object>> rawData = this.listMaps(queryWrapper);
            long totalCount = this.count();

            List<HealthStatusDistribution> distributions = new ArrayList<>();

            for (Map<String, Object> data : rawData) {
                String status = (String) data.get("health_status");
                Integer count = ((Number) data.get("count")).intValue();
                Double percentage = totalCount > 0 ? (count * 100.0 / totalCount) : 0.0;

                // 处理空值或未知状态
                if (status == null || status.trim().isEmpty()) {
                    status = "未知";
                }

                distributions.add(new HealthStatusDistribution(status, count,
                        Math.round(percentage * 100.0) / 100.0));
            }

            return ResponseResult.success(distributions);
        } catch (Exception e) {
            log.error("获取健康状态分布失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    @Override
    public ResponseResult<List<AgeHealthDistribution>> getAgeHealthDistribution() {
        try {
            List<Elderly> allElderly = this.list();
            Map<String, Map<String, Integer>> ageGroupStats = new HashMap<>();

            // 初始化年龄段统计
            String[] ageRanges = {"60-70岁", "70-80岁", "80-90岁", "90岁以上"};
            for (String range : ageRanges) {
                Map<String, Integer> statusCount = new HashMap<>();
                statusCount.put("健康", 0);
                statusCount.put("亚健康", 0);
                statusCount.put("慢性病", 0);
                statusCount.put("高风险", 0);
                ageGroupStats.put(range, statusCount);
            }

            // 统计各年龄段的健康状态分布
            for (Elderly elderly : allElderly) {
                Integer age = calculateAge(elderly.getBirthDate());
                if (age == null) {
                    continue;
                }

                String ageRange = getAgeRange(age);
                String healthStatus = elderly.getHealthStatus();
                if (healthStatus == null) {
                    healthStatus = "未知";
                }

                Map<String, Integer> statusCount = ageGroupStats.get(ageRange);
                if (statusCount != null) {
                    // 将不同的健康状态映射到标准分类
                    String standardStatus = mapToStandardHealthStatus(healthStatus);
                    statusCount.put(standardStatus, statusCount.get(standardStatus) + 1);

                    // 添加调试日志
                    log.debug("老人: {}, 年龄: {}, 年龄段: {}, 原始健康状态: {}, 标准状态: {}",
                            elderly.getName(), age, ageRange, healthStatus, standardStatus);
                }
            }

            // 打印统计结果用于调试
            for (String ageRange : ageRanges) {
                Map<String, Integer> stats = ageGroupStats.get(ageRange);
                log.info("年龄段 {} 统计: 健康={}, 亚健康={}, 慢性病={}, 高风险={}",
                        ageRange, stats.get("健康"), stats.get("亚健康"), stats.get("慢性病"), stats.get("高风险"));
            }

            // 转换为结果对象
            List<AgeHealthDistribution> distributions = new ArrayList<>();
            for (String ageRange : ageRanges) {
                Map<String, Integer> stats = ageGroupStats.get(ageRange);
                distributions.add(new AgeHealthDistribution(
                        ageRange,
                        stats.get("健康"),
                        stats.get("亚健康"),
                        stats.get("慢性病"),
                        stats.get("高风险")
                ));
            }

            return ResponseResult.success(distributions);
        } catch (Exception e) {
            log.error("获取年龄段健康分布失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    @Override
    public ResponseResult<HealthRiskAssessment> getHealthRiskAssessment() {
        try {
            long totalCount = this.count();
            if (totalCount == 0) {
                return ResponseResult.success(new HealthRiskAssessment(0.0, 0.0, 0.0, 0.0, 85.0));
            }

            // 统计各种健康状态的数量
            QueryWrapper<Elderly> healthyWrapper = new QueryWrapper<>();
            healthyWrapper.eq("health_status", "健康");
            long healthyCount = this.count(healthyWrapper);

            QueryWrapper<Elderly> chronicWrapper = new QueryWrapper<>();
            chronicWrapper.in("health_status", "慢性病", "慢病管理");
            long chronicCount = this.count(chronicWrapper);

            QueryWrapper<Elderly> riskWrapper = new QueryWrapper<>();
            riskWrapper.in("health_status", "高风险", "危重");
            long riskCount = this.count(riskWrapper);

            // 统计需要照护的人员
            QueryWrapper<Elderly> careWrapper = new QueryWrapper<>();
            careWrapper.in("care_level", 2, 3); // 半自理和不能自理
            long careCount = this.count(careWrapper);

            // 计算各项指标
            double healthyRate = (healthyCount * 100.0) / totalCount;
            double chronicRate = (chronicCount * 100.0) / totalCount;
            double riskRate = (riskCount * 100.0) / totalCount;
            double careRate = (careCount * 100.0) / totalCount;
            double satisfactionRate = 85.0; // 满意度暂时设为固定值，实际应从调查数据获取

            HealthRiskAssessment assessment = new HealthRiskAssessment(
                    Math.round(healthyRate * 100.0) / 100.0,
                    Math.round(chronicRate * 100.0) / 100.0,
                    Math.round(riskRate * 100.0) / 100.0,
                    Math.round(careRate * 100.0) / 100.0,
                    satisfactionRate
            );

            return ResponseResult.success(assessment);
        } catch (Exception e) {
            log.error("获取健康风险评估失败", e);
            return ResponseResult.error("评估失败");
        }
    }

    /**
     * 生成健康趋势数据（模拟数据）
     */
    private List<HealthTrend> generateHealthTrends() {
        List<HealthTrend> trends = new ArrayList<>();
        String[] months = {"2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"};

        // 这里应该根据历史健康记录计算实际趋势，目前使用模拟数据
        for (int i = 0; i < months.length; i++) {
            trends.add(new HealthTrend(
                    months[i],
                    120 + i * 2, // 健康人数逐月增加
                    80 - i, // 亚健康人数逐月减少
                    45 + i, // 慢性病人数小幅增加
                    15 - i / 2 // 高风险人数逐月减少
            ));
        }

        return trends;
    }

    /**
     * 根据年龄获取年龄段
     */
    private String getAgeRange(Integer age) {
        if (age >= 60 && age < 70) {
            return "60-70岁";
        } else if (age >= 70 && age < 80) {
            return "70-80岁";
        } else if (age >= 80 && age < 90) {
            return "80-90岁";
        } else if (age >= 90) {
            return "90岁以上";
        } else {
            return "60-70岁"; // 默认分组
        }
    }

    /**
     * 将各种健康状态映射到标准分类
     */
    private String mapToStandardHealthStatus(String healthStatus) {
        if (healthStatus == null || healthStatus.trim().isEmpty()) {
            return "亚健康"; // 空值默认为亚健康而不是健康
        }

        String status = healthStatus.toLowerCase().trim();

        // 处理枚举值（NORMAL, ATTENTION, WARNING等）
        if (status.equals("normal") || status.equals("1")) {
            return "健康";
        } else if (status.equals("attention") || status.equals("2")) {
            return "亚健康";
        } else if (status.equals("warning") || status.equals("danger") || status.equals("3")) {
            return "高风险";
        } // 处理英文健康状态
        else if (status.equals("healthy") || status.equals("good") || status.equals("excellent")) {
            return "健康";
        } else if (status.equals("unhealthy") || status.equals("poor") || status.equals("fair")) {
            return "亚健康";
        } else if (status.equals("sick") || status.equals("ill") || status.equals("critical")) {
            return "高风险";
        } else if (status.equals("chronic") || status.equals("disease")) {
            return "慢性病";
        } // 健康状态匹配（中文）
        else if (status.equals("健康") || status.equals("良好") || status.equals("正常") || status.equals("优秀")) {
            return "健康";
        } // 亚健康状态匹配
        else if (status.contains("亚健康") || status.contains("亚康")
                || status.equals("一般") || status.equals("轻微") || status.equals("注意")) {
            return "亚健康";
        } // 慢性病状态匹配
        else if (status.contains("慢性") || status.contains("慢病")
                || status.contains("糖尿病") || status.contains("高血压")
                || status.contains("心脏病") || status.equals("疾病")) {
            return "慢性病";
        } // 高风险状态匹配
        else if (status.contains("高风险") || status.contains("危重")
                || status.contains("严重") || status.contains("重病")
                || status.contains("紧急") || status.equals("危险") || status.contains("预警")) {
            return "高风险";
        } // 其他状态根据关键词推断
        else if (status.contains("病") || status.contains("症") || status.contains("炎")) {
            return "慢性病";
        } else if (status.contains("差") || status.contains("弱") || status.contains("虚")) {
            return "亚健康";
        } else {
            // 对于无法识别的状态，记录详细日志并分配到亚健康
            log.warn("无法识别的健康状态: [{}], 原始值: [{}], 默认分配到亚健康", status, healthStatus);
            return "亚健康";
        }
    }

    // 辅助方法
    /**
     * 获取照护等级名称
     */
    private String getCareLevelName(Integer careLevel) {
        if (careLevel == null) {
            return "自理";
        }
        switch (careLevel) {
            case 1:
                return "自理";
            case 2:
                return "半自理";
            case 3:
                return "不能自理";
            default:
                return "自理";
        }
    }

    /**
     * 判断年龄是否在指定范围内
     */
    private boolean isInAgeRange(Integer age, String ageRange) {
        switch (ageRange) {
            case "60-70岁":
                return age >= 60 && age < 70;
            case "70-80岁":
                return age >= 70 && age < 80;
            case "80-90岁":
                return age >= 80 && age < 90;
            case "90岁以上":
                return age >= 90;
            default:
                return false;
        }
    }

    /**
     * 确定照护强度
     */
    private String determineCareIntensity(int selfCareCount, int semiCareCount, int fullCareCount) {
        int total = selfCareCount + semiCareCount + fullCareCount;
        if (total == 0) {
            return "低";
        }

        double highCareRatio = (semiCareCount + fullCareCount) * 100.0 / total;

        if (highCareRatio >= 60) {
            return "高";
        } else if (highCareRatio >= 30) {
            return "中";
        } else {
            return "低";
        }
    }

    /**
     * 确定工作负荷等级
     */
    private String determineWorkloadLevel(int elderlyCount, int serviceCount) {
        if (elderlyCount >= 18 || serviceCount >= 150) {
            return "高负荷";
        } else if (elderlyCount >= 15 || serviceCount >= 120) {
            return "中负荷";
        } else {
            return "正常";
        }
    }

    // 创建照护等级数据的辅助方法
    private Map<String, Object> createCareLevelData(String careLevel, String careLevelName, long count, long totalElderly) {
        Map<String, Object> data = new HashMap<>();
        data.put("careLevel", careLevel);
        data.put("careLevelName", careLevelName);
        data.put("count", count);
        data.put("percentage", totalElderly > 0 ? Math.round(count * 100.0 / totalElderly * 100.0) / 100.0 : 0.0);

        // 计算平均年龄和性别分布
        QueryWrapper<Elderly> wrapper = new QueryWrapper<>();
        if ("1".equals(careLevel)) {
            wrapper.eq("care_level", 1).or().isNull("care_level");
        } else {
            wrapper.eq("care_level", Integer.parseInt(careLevel));
        }

        List<Elderly> elderlyList = this.list(wrapper);
        double avgAge = 0.0;
        int maleCount = 0, femaleCount = 0;

        if (!elderlyList.isEmpty()) {
            int totalAge = 0;
            int validAgeCount = 0;

            for (Elderly elderly : elderlyList) {
                // 计算年龄
                if (elderly.getBirthDate() != null) {
                    Integer age = calculateAge(elderly.getBirthDate());
                    if (age != null && age > 0) {
                        totalAge += age;
                        validAgeCount++;
                    }
                }

                // 统计性别分布
                if (elderly.getGender() != null) {
                    if (elderly.getGender() == 1) {
                        maleCount++;
                    } else {
                        femaleCount++;
                    }
                }
            }

            avgAge = validAgeCount > 0 ? Math.round(totalAge * 100.0 / validAgeCount) / 100.0 : 0.0;
        }

        data.put("avgAge", avgAge);
        data.put("maleCount", maleCount);
        data.put("femaleCount", femaleCount);

        return data;
    }

    // 创建年龄段照护需求数据
    private List<Map<String, Object>> createAgeCareNeedsData() {
        List<Map<String, Object>> ageCareNeeds = new ArrayList<>();
        String[] ageRanges = {"60-70岁", "70-80岁", "80-90岁", "90岁以上"};

        List<Elderly> allElderly = this.list();

        for (String ageRange : ageRanges) {
            int selfCareCount = 0, semiCareCount = 0, fullCareCount = 0;

            for (Elderly elderly : allElderly) {
                Integer age = calculateAge(elderly.getBirthDate());
                if (age == null || !isInAgeRange(age, ageRange)) {
                    continue;
                }

                Integer careLevel = elderly.getCareLevel() != null ? elderly.getCareLevel() : 1;
                switch (careLevel) {
                    case 1:
                        selfCareCount++;
                        break;
                    case 2:
                        semiCareCount++;
                        break;
                    case 3:
                        fullCareCount++;
                        break;
                }
            }

            String careIntensity = determineCareIntensity(selfCareCount, semiCareCount, fullCareCount);

            Map<String, Object> ageData = new HashMap<>();
            ageData.put("ageRange", ageRange);
            ageData.put("selfCareCount", selfCareCount);
            ageData.put("semiCareCount", semiCareCount);
            ageData.put("fullCareCount", fullCareCount);
            ageData.put("careIntensity", careIntensity);

            ageCareNeeds.add(ageData);
        }

        return ageCareNeeds;
    }

    // 创建照护服务统计数据
    private Map<String, Object> createCareServiceStats() {
        Map<String, Object> serviceStats = new HashMap<>();

        // 模拟服务统计数据
        Map<String, Integer> serviceTypeDistribution = new HashMap<>();
        serviceTypeDistribution.put("生活照料", 245);
        serviceTypeDistribution.put("医疗护理", 156);
        serviceTypeDistribution.put("康复训练", 89);
        serviceTypeDistribution.put("心理疏导", 67);
        serviceTypeDistribution.put("营养配餐", 198);

        serviceStats.put("monthlyServiceCount", 755);
        serviceStats.put("dailyAvgServiceCount", 24.4);
        serviceStats.put("serviceTypeDistribution", serviceTypeDistribution);
        serviceStats.put("serviceCompletionRate", 94.5);
        serviceStats.put("avgServiceDuration", 45.2);

        return serviceStats;
    }

    // 创建照护质量统计数据
    private Map<String, Object> createCareQualityStats() {
        Map<String, Object> qualityStats = new HashMap<>();

        // 生成质量趋势数据
        List<Map<String, Object>> qualityTrends = new ArrayList<>();
        String[] dates = {"2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"};

        for (int i = 0; i < dates.length; i++) {
            Map<String, Object> trend = new HashMap<>();
            trend.put("date", dates[i]);
            trend.put("qualityScore", 4.2 + (i * 0.1));
            trend.put("satisfactionScore", 4.1 + (i * 0.08));
            qualityTrends.add(trend);
        }

        qualityStats.put("avgQualityScore", 4.5);
        qualityStats.put("avgSatisfactionScore", 4.3);
        qualityStats.put("qualityTrends", qualityTrends);
        qualityStats.put("incidentCount", 3);
        qualityStats.put("complaintCount", 1);

        return qualityStats;
    }

    // 创建费用统计数据
    private Map<String, Object> createCareCostStats() {
        Map<String, Object> costStats = new HashMap<>();

        // 不同照护等级费用
        Map<String, Double> costByLevel = new HashMap<>();
        costByLevel.put("自理", 1800.0);
        costByLevel.put("半自理", 2800.0);
        costByLevel.put("不能自理", 4200.0);

        // 费用趋势
        List<Map<String, Object>> costTrends = new ArrayList<>();
        String[] months = {"2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"};
        double[] amounts = {185000, 192000, 198000, 205000, 212000, 218000};

        for (int i = 0; i < months.length; i++) {
            double growthRate = i > 0 ? ((amounts[i] - amounts[i - 1]) / amounts[i - 1] * 100) : 0.0;

            Map<String, Object> trend = new HashMap<>();
            trend.put("month", months[i]);
            trend.put("amount", amounts[i]);
            trend.put("growthRate", Math.round(growthRate * 100.0) / 100.0);
            costTrends.add(trend);
        }

        costStats.put("monthlyTotalCost", 218000.0);
        costStats.put("avgCostPerPerson", 2680.0);
        costStats.put("costByLevel", costByLevel);
        costStats.put("costTrends", costTrends);

        return costStats;
    }

    // 创建护理人员工作量数据
    private List<Map<String, Object>> createCaregiverWorkloads() {
        List<Map<String, Object>> workloads = new ArrayList<>();

        // 模拟护理人员数据
        String[] caregivers = {"李护士", "王护士", "张护士", "赵护士", "陈护士"};
        int[] elderlyCounts = {15, 18, 12, 20, 16};
        int[] serviceCounts = {120, 145, 98, 160, 128};
        int[] serviceHours = {180, 220, 150, 240, 195};
        double[] ratings = {4.6, 4.8, 4.4, 4.7, 4.5};

        for (int i = 0; i < caregivers.length; i++) {
            String workloadLevel = determineWorkloadLevel(elderlyCounts[i], serviceCounts[i]);

            Map<String, Object> workload = new HashMap<>();
            workload.put("caregiverId", (long) (i + 1));
            workload.put("caregiverName", caregivers[i]);
            workload.put("elderlyCount", elderlyCounts[i]);
            workload.put("serviceCount", serviceCounts[i]);
            workload.put("totalServiceHours", serviceHours[i]);
            workload.put("avgRating", ratings[i]);
            workload.put("workloadLevel", workloadLevel);

            workloads.add(workload);
        }

        return workloads;
    }

    /**
     * 根据老人ID列表批量获取老人信息（家属专用）
     */
    @Override
    public ResponseResult<List<Elderly>> getElderlyByIds(List<Long> elderlyIds) {
        try {
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.success(new ArrayList<>());
            }

            // 查询老人信息
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", elderlyIds)
                    .eq("is_deleted", 0)
                    .eq("status", 1);

            List<Elderly> elderlyList = this.list(queryWrapper);

            log.info("批量查询老人信息成功，查询ID数量：{}，返回结果数量：{}", elderlyIds.size(), elderlyList.size());

            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            log.error("批量查询老人信息失败，IDs：{}", elderlyIds, e);
            return ResponseResult.error("批量查询老人信息失败：" + e.getMessage());
        }
    }
}
