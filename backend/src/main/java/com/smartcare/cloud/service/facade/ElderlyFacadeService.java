package com.smartcare.cloud.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartcare.cloud.dto.AssessmentReportDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.HealthAssessmentService;
import com.smartcare.cloud.service.HealthRecordService;
import com.smartcare.cloud.vo.ResponseResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 老人信息门面服务
 * 
 * 职责：
 * 1. 封装跨模块的复杂业务逻辑
 * 2. 组合多个Service的调用结果
 * 3. 降低Controller与多个Service的直接耦合
 * 
 * 使用Facade模式的优势：
 * - 简化接口：为复杂的子系统提供简单统一的接口
 * - 解耦：Controller不需要知道多个Service的细节
 * - 灵活：易于修改内部实现而不影响外部调用
 *
 * @author SmartCare Team
 * @date 2024-01-27
 */
@Service
public class ElderlyFacadeService {

    private static final Logger log = LoggerFactory.getLogger(ElderlyFacadeService.class);

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private HealthAssessmentService healthAssessmentService;

    /**
     * 获取老人完整信息（包含健康档案）
     * 
     * @param elderlyId 老人ID
     * @return 老人完整信息
     */
    public ResponseResult<Map<String, Object>> getCompleteElderlyInfo(Long elderlyId) {
        try {
            log.info("获取老人完整信息，ID：{}", elderlyId);

            // 1. 获取老人基本信息
            Elderly elderly = elderlyService.getById(elderlyId);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 2. 获取健康记录（最近10条）
            ResponseResult<IPage<HealthRecord>> healthRecordsResult = 
                healthRecordService.getHealthRecordsByElderlyId(elderlyId, 1L, 10L, null);

            // 3. 组合结果
            Map<String, Object> completeInfo = new HashMap<>();
            completeInfo.put("elderly", elderly);
            completeInfo.put("healthRecords", healthRecordsResult.isSuccess() ? 
                healthRecordsResult.getData().getRecords() : null);
            completeInfo.put("healthRecordsTotal", healthRecordsResult.isSuccess() ? 
                healthRecordsResult.getData().getTotal() : 0);

            log.info("成功获取老人完整信息，ID：{}", elderlyId);
            return ResponseResult.success(completeInfo);

        } catch (Exception e) {
            log.error("获取老人完整信息失败，ID：{}", elderlyId, e);
            return ResponseResult.error("获取完整信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取老人健康档案
     * 
     * @param elderlyId 老人ID
     * @return 健康档案
     */
    public ResponseResult<Object> getElderlyHealthRecords(Long elderlyId) {
        try {
            log.info("获取老人健康档案，ID：{}", elderlyId);

            // 验证老人是否存在
            if (elderlyService.getById(elderlyId) == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 调用健康记录服务
            ResponseResult<IPage<HealthRecord>> result = 
                healthRecordService.getHealthRecordsByElderlyId(elderlyId, 1L, 10L, null);

            if (result.isSuccess()) {
                IPage<HealthRecord> page = result.getData();
                Map<String, Object> data = new HashMap<>();
                data.put("records", page.getRecords());
                data.put("total", page.getTotal());
                data.put("current", page.getCurrent());
                data.put("size", page.getSize());

                return ResponseResult.success(data);
            }

            return ResponseResult.error("获取健康档案失败");

        } catch (Exception e) {
            log.error("获取老人健康档案失败，ID：{}", elderlyId, e);
            return ResponseResult.error("获取健康档案失败");
        }
    }

    /**
     * 添加健康记录
     * 
     * @param elderlyId 老人ID
     * @param healthRecord 健康记录
     * @return 操作结果
     */
    public ResponseResult<Void> addHealthRecord(Long elderlyId, HealthRecord healthRecord) {
        try {
            log.info("添加健康记录，老人ID：{}", elderlyId);

            // 验证老人是否存在
            if (elderlyService.getById(elderlyId) == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 设置老人ID
            healthRecord.setElderlyId(elderlyId);

            // 调用健康记录服务
            return healthRecordService.addHealthRecord(healthRecord);

        } catch (Exception e) {
            log.error("添加健康记录失败，ID：{}", elderlyId, e);
            return ResponseResult.error("添加健康记录失败");
        }
    }

    /**
     * 生成健康评估报告
     * 
     * @param elderlyId 老人ID
     * @return 评估报告
     */
    public ResponseResult<Object> generateAssessmentReport(Long elderlyId) {
        try {
            log.info("生成健康评估报告，老人ID：{}", elderlyId);

            // 获取老人信息
            Elderly elderly = elderlyService.getById(elderlyId);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 使用健康评估服务生成报告
            AssessmentReportDTO report = healthAssessmentService.generateAssessmentReport(elderly);

            return ResponseResult.success(report);

        } catch (Exception e) {
            log.error("生成健康评估报告失败，ID：{}", elderlyId, e);
            return ResponseResult.error("生成评估报告失败：" + e.getMessage());
        }
    }

    /**
     * 获取老人健康摘要
     * 包含基本信息、最新健康记录、评估报告
     * 
     * @param elderlyId 老人ID
     * @return 健康摘要
     */
    public ResponseResult<Map<String, Object>> getHealthSummary(Long elderlyId) {
        try {
            log.info("获取老人健康摘要，ID：{}", elderlyId);

            // 1. 获取老人基本信息
            Elderly elderly = elderlyService.getById(elderlyId);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 2. 获取最新健康记录（最近3条）
            ResponseResult<IPage<HealthRecord>> recentRecords = 
                healthRecordService.getHealthRecordsByElderlyId(elderlyId, 1L, 3L, null);

            // 3. 生成评估报告
            AssessmentReportDTO assessment = healthAssessmentService.generateAssessmentReport(elderly);

            // 4. 组合结果
            Map<String, Object> summary = new HashMap<>();
            summary.put("elderly", elderly);
            summary.put("recentRecords", recentRecords.isSuccess() ? 
                recentRecords.getData().getRecords() : null);
            summary.put("assessment", assessment);

            log.info("成功获取老人健康摘要，ID：{}", elderlyId);
            return ResponseResult.success(summary);

        } catch (Exception e) {
            log.error("获取老人健康摘要失败，ID：{}", elderlyId, e);
            return ResponseResult.error("获取健康摘要失败：" + e.getMessage());
        }
    }
}
