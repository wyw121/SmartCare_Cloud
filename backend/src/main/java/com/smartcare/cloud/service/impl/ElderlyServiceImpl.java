package com.smartcare.cloud.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.HealthAssessmentService;
import com.smartcare.cloud.service.HealthRecordService;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 老人信息服务实现类
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ElderlyServiceImpl extends ServiceImpl<ElderlyMapper, Elderly> implements ElderlyService {

    private static final Logger log = LoggerFactory.getLogger(ElderlyServiceImpl.class);

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private HealthAssessmentService healthAssessmentService;

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
    public ResponseResult<Void> deleteElderly(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            boolean success = this.removeById(id);
            if (success) {
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
    public ResponseResult<Object> getElderlyHealthStatistics() {
        try {
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("health_status, COUNT(*) as count")
                    .groupBy("health_status");

            List<Map<String, Object>> statistics = this.listMaps(queryWrapper);

            // 统计总数
            long totalCount = this.count();

            Map<String, Object> result = new HashMap<>();
            result.put("total", totalCount);
            result.put("healthStatusStats", statistics);

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取老人健康状态统计失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    @Override
    public ResponseResult<Object> getElderlyHealthRecords(Long id) {
        try {
            // 首先验证老人是否存在
            if (this.getById(id) == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 调用健康记录服务获取分页数据
            ResponseResult<IPage<HealthRecord>> result = healthRecordService.getHealthRecordsByElderlyId(id, 1L, 10L, null);

            if (result.isSuccess()) {
                IPage<HealthRecord> page = result.getData();
                // 转换为前端需要的格式
                Map<String, Object> data = new HashMap<>();
                data.put("records", page.getRecords());
                data.put("total", page.getTotal());
                data.put("current", page.getCurrent());
                data.put("size", page.getSize());

                return ResponseResult.success(data);
            } else {
                return ResponseResult.error("获取健康档案失败");
            }

        } catch (Exception e) {
            log.error("获取老人健康档案失败，ID：{}", id, e);
            return ResponseResult.error("获取健康档案失败");
        }
    }

    @Override
    public ResponseResult<Void> addHealthRecord(Long id, Object healthRecord) {
        try {
            // 首先验证老人是否存在
            if (this.getById(id) == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 将Object转换为HealthRecord
            ObjectMapper objectMapper = new ObjectMapper();
            HealthRecord record = objectMapper.convertValue(healthRecord, HealthRecord.class);
            record.setElderlyId(id);

            // 调用健康记录服务保存
            return healthRecordService.addHealthRecord(record);

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
        try {
            log.info("生成健康评估报告，老人ID：{}", id);
            
            // 获取老人信息
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }
            
            // 使用健康评估服务生成报告
            AssessmentReportDTO report = healthAssessmentService.generateAssessmentReport(elderly);
            
            return ResponseResult.success(report);
        } catch (Exception e) {
            log.error("生成健康评估报告失败，ID：{}", id, e);
            return ResponseResult.error("生成评估报告失败：" + e.getMessage());
        }
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
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("care_level, COUNT(*) as count")
                    .groupBy("care_level");

            List<Map<String, Object>> statistics = this.listMaps(queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("careLevelStats", statistics);

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取照护等级统计失败", e);
            return ResponseResult.error("统计失败");
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
}
