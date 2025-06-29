package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.mapper.HealthRecordMapper;
import com.smartcare.cloud.service.HealthRecordService;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 健康记录服务实现
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
@Service
public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthRecordService {

    private static final Logger log = LoggerFactory.getLogger(HealthRecordServiceImpl.class);

    @Override
    public ResponseResult<IPage<HealthRecord>> getHealthRecordsByElderlyId(Long elderlyId, Long current, Long size, Integer recordType) {
        try {
            // 创建分页对象
            Page<HealthRecord> page = new Page<>(current, size);

            // 构建查询条件
            QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("elderly_id", elderlyId);
            queryWrapper.eq("is_deleted", 0);

            // 如果指定了记录类型
            if (recordType != null && recordType > 0) {
                queryWrapper.eq("record_type", recordType);
            }

            // 按记录日期倒序排列
            queryWrapper.orderByDesc("record_date");

            // 执行分页查询
            IPage<HealthRecord> result = this.page(page, queryWrapper);

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("分页查询健康记录失败，老人ID: {}", elderlyId, e);
            return ResponseResult.error("查询健康记录失败");
        }
    }

    @Override
    public ResponseResult<Void> addHealthRecord(HealthRecord healthRecord) {
        try {
            // 设置创建时间
            healthRecord.setCreateTime(LocalDateTime.now());
            healthRecord.setUpdateTime(LocalDateTime.now());
            healthRecord.setIsDeleted(0);

            boolean success = this.save(healthRecord);

            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("添加健康记录失败");
            }
        } catch (Exception e) {
            log.error("添加健康记录失败", e);
            return ResponseResult.error("添加健康记录失败");
        }
    }

    @Override
    public ResponseResult<Void> updateHealthRecord(HealthRecord healthRecord) {
        try {
            HealthRecord existingRecord = this.getById(healthRecord.getId());
            if (existingRecord == null || existingRecord.getIsDeleted() == 1) {
                return ResponseResult.error("健康记录不存在");
            }

            // 设置更新时间
            healthRecord.setUpdateTime(LocalDateTime.now());

            boolean success = this.updateById(healthRecord);

            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("更新健康记录失败");
            }
        } catch (Exception e) {
            log.error("更新健康记录失败，ID: {}", healthRecord.getId(), e);
            return ResponseResult.error("更新健康记录失败");
        }
    }

    @Override
    public ResponseResult<Void> deleteHealthRecord(Long id) {
        try {
            HealthRecord healthRecord = this.getById(id);
            if (healthRecord == null || healthRecord.getIsDeleted() == 1) {
                return ResponseResult.error("健康记录不存在");
            }

            // 逻辑删除
            healthRecord.setIsDeleted(1);
            healthRecord.setUpdateTime(LocalDateTime.now());

            boolean success = this.updateById(healthRecord);

            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("删除健康记录失败");
            }
        } catch (Exception e) {
            log.error("删除健康记录失败，ID: {}", id, e);
            return ResponseResult.error("删除健康记录失败");
        }
    }

    @Override
    public ResponseResult<HealthRecord> getHealthRecordById(Long id) {
        try {
            QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            queryWrapper.eq("is_deleted", 0);

            HealthRecord healthRecord = this.getOne(queryWrapper);

            if (healthRecord == null) {
                return ResponseResult.error("健康记录不存在");
            }

            return ResponseResult.success(healthRecord);
        } catch (Exception e) {
            log.error("获取健康记录详情失败，ID: {}", id, e);
            return ResponseResult.error("获取健康记录失败");
        }
    }

    @Override
    public ResponseResult<HealthRecord> getLatestVitalSigns(Long elderlyId) {
        try {
            QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("elderly_id", elderlyId);
            queryWrapper.eq("is_deleted", 0);

            // 查询包含生命体征的记录
            queryWrapper.and(wrapper -> wrapper
                    .isNotNull("systolic_pressure")
                    .or().isNotNull("heart_rate")
                    .or().isNotNull("body_temperature")
                    .or().isNotNull("blood_glucose")
            );

            // 按记录日期倒序，取最新一条
            queryWrapper.orderByDesc("record_date");
            queryWrapper.last("LIMIT 1");

            HealthRecord latestRecord = this.getOne(queryWrapper);

            return ResponseResult.success(latestRecord);
        } catch (Exception e) {
            log.error("获取最新生命体征失败，老人ID: {}", elderlyId, e);
            return ResponseResult.error("获取最新生命体征失败");
        }
    }

    @Override
    public ResponseResult<Object> getHealthRecordStatistics(Long elderlyId) {
        try {
            QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("elderly_id", elderlyId);
            queryWrapper.eq("is_deleted", 0);

            // 总记录数
            long totalRecords = this.count(queryWrapper);

            // 近30天记录数
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            QueryWrapper<HealthRecord> recentWrapper = new QueryWrapper<>();
            recentWrapper.eq("elderly_id", elderlyId);
            recentWrapper.eq("is_deleted", 0);
            recentWrapper.ge("record_date", thirtyDaysAgo);
            long recentRecords = this.count(recentWrapper);

            // 异常记录数
            QueryWrapper<HealthRecord> abnormalWrapper = new QueryWrapper<>();
            abnormalWrapper.eq("elderly_id", elderlyId);
            abnormalWrapper.eq("is_deleted", 0);
            abnormalWrapper.eq("is_abnormal", 1);
            long warningRecords = this.count(abnormalWrapper);

            // 就诊医生数量
            QueryWrapper<HealthRecord> doctorWrapper = new QueryWrapper<>();
            doctorWrapper.eq("elderly_id", elderlyId);
            doctorWrapper.eq("is_deleted", 0);
            doctorWrapper.isNotNull("doctor_id");
            doctorWrapper.select("DISTINCT doctor_id");
            List<HealthRecord> doctorRecords = this.list(doctorWrapper);
            long doctorCount = doctorRecords.size();

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalRecords", totalRecords);
            statistics.put("recentRecords", recentRecords);
            statistics.put("warningRecords", warningRecords);
            statistics.put("doctorCount", doctorCount);

            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取健康记录统计失败，老人ID: {}", elderlyId, e);
            return ResponseResult.error("获取统计数据失败");
        }
    }
}
