package com.smartcare.cloud.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.DoctorService;
import com.smartcare.cloud.vo.ResponseResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康预警门面服务
 * 
 * 职责：
 * 1. 聚合健康预警相关的多个服务调用
 * 2. 提供统一的业务接口给Controller层
 * 3. 降低Controller对多个Service的直接依赖
 * 
 * @author SmartCare Team
 * @date 2025-10-28
 */
@Service
public class HealthWarningFacadeService {

    private static final Logger log = LoggerFactory.getLogger(HealthWarningFacadeService.class);

    @Autowired
    private HealthWarningService healthWarningService;

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取健康预警详情（包含老人和医生信息）
     * 
     * @param warningId 预警ID
     * @return 预警详情（含关联信息）
     */
    public ResponseResult<Map<String, Object>> getWarningDetailWithRelations(Long warningId) {
        try {
            log.info("获取健康预警详情（含关联信息），ID：{}", warningId);

            // 1. 获取预警基本信息
            HealthWarning warning = healthWarningService.getById(warningId);
            if (warning == null) {
                return ResponseResult.error("健康预警信息不存在");
            }

            // 2. 获取关联的老人信息
            Object elderly = null;
            if (warning.getElderlyId() != null) {
                elderly = elderlyService.getById(warning.getElderlyId());
            }

            // 3. 获取处理医生信息（如果已分配）
            Object doctor = null;
            if (warning.getHandlerId() != null) {
                doctor = doctorService.getById(warning.getHandlerId());
            }

            // 4. 组合结果
            Map<String, Object> result = new HashMap<>();
            result.put("warning", warning);
            result.put("elderly", elderly);
            result.put("handler", doctor);

            log.info("成功获取健康预警详情（含关联信息），ID：{}", warningId);
            return ResponseResult.success(result);

        } catch (Exception e) {
            log.error("获取健康预警详情失败，ID：{}", warningId, e);
            return ResponseResult.error("获取预警详情失败：" + e.getMessage());
        }
    }

    /**
     * 批量处理预警
     * 
     * @param warningIds 预警ID列表
     * @param handlerId 处理人ID
     * @param handleResult 处理结果
     * @return 处理结果
     */
    public ResponseResult<Map<String, Object>> batchHandleWarnings(
            List<Long> warningIds, Long handlerId, String handleResult) {
        try {
            log.info("批量处理预警，数量：{}", warningIds.size());

            int successCount = 0;
            int failCount = 0;

            for (Long warningId : warningIds) {
                try {
                    HealthWarning warning = healthWarningService.getById(warningId);
                    if (warning != null) {
                        warning.setHandlerId(handlerId);
                        warning.setHandleResult(handleResult);
                        warning.setStatus("HANDLED");
                        healthWarningService.updateById(warning);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("处理预警失败，ID：{}", warningId, e);
                    failCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("total", warningIds.size());
            result.put("success", successCount);
            result.put("fail", failCount);

            log.info("批量处理预警完成，成功：{}，失败：{}", successCount, failCount);
            return ResponseResult.success(result);

        } catch (Exception e) {
            log.error("批量处理预警失败", e);
            return ResponseResult.error("批量处理失败：" + e.getMessage());
        }
    }

    /**
     * 获取老人的所有预警记录
     * 
     * @param elderlyId 老人ID
     * @return 预警列表
     */
    public ResponseResult<List<HealthWarning>> getElderlyWarnings(Long elderlyId) {
        try {
            log.info("获取老人预警记录，老人ID：{}", elderlyId);

            // 验证老人是否存在
            if (elderlyService.getById(elderlyId) == null) {
                return ResponseResult.error("老人信息不存在");
            }

            // 查询该老人的所有预警
            List<HealthWarning> warnings = healthWarningService.lambdaQuery()
                    .eq(HealthWarning::getElderlyId, elderlyId)
                    .orderByDesc(HealthWarning::getCreateTime)
                    .list();

            return ResponseResult.success(warnings);

        } catch (Exception e) {
            log.error("获取老人预警记录失败，老人ID：{}", elderlyId, e);
            return ResponseResult.error("获取预警记录失败：" + e.getMessage());
        }
    }
}
