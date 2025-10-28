package com.smartcare.cloud.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.service.DoctorService;
import com.smartcare.cloud.service.HealthRecordService;
import com.smartcare.cloud.vo.ResponseResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计报表门面服务
 * 
 * 职责：
 * 1. 聚合多个服务的统计数据
 * 2. 提供综合性的统计报表接口
 * 3. 简化Controller层的复杂统计逻辑
 * 
 * @author SmartCare Team
 * @date 2025-10-28
 */
@Service
public class ReportFacadeService {

    private static final Logger log = LoggerFactory.getLogger(ReportFacadeService.class);

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private HealthWarningService healthWarningService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HealthRecordService healthRecordService;

    /**
     * 获取系统综合统计数据
     * 包含：老人总数、预警总数、医生总数、健康记录数等
     * 
     * @return 综合统计数据
     */
    public ResponseResult<Map<String, Object>> getComprehensiveStatistics() {
        try {
            log.info("获取系统综合统计数据");

            Map<String, Object> statistics = new HashMap<>();

            // 1. 老人统计
            long elderlyCount = elderlyService.count();
            statistics.put("elderlyCount", elderlyCount);

            // 2. 预警统计
            long warningCount = healthWarningService.count();
            long activeWarningCount = healthWarningService.lambdaQuery()
                    .eq(com.smartcare.cloud.entity.HealthWarning::getStatus, "PENDING")
                    .count();
            statistics.put("totalWarnings", warningCount);
            statistics.put("activeWarnings", activeWarningCount);

            // 3. 医生统计
            long doctorCount = doctorService.count();
            statistics.put("doctorCount", doctorCount);

            // 4. 健康记录统计
            long healthRecordCount = healthRecordService.count();
            statistics.put("healthRecordCount", healthRecordCount);

            // 5. 计算告警率
            if (elderlyCount > 0) {
                double warningRate = (double) activeWarningCount / elderlyCount * 100;
                statistics.put("warningRate", String.format("%.2f%%", warningRate));
            } else {
                statistics.put("warningRate", "0.00%");
            }

            log.info("系统综合统计数据获取成功");
            return ResponseResult.success(statistics);

        } catch (Exception e) {
            log.error("获取系统综合统计数据失败", e);
            return ResponseResult.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取健康状况分布统计
     * 
     * @return 健康状况分布
     */
    public ResponseResult<Map<String, Object>> getHealthStatusDistribution() {
        try {
            log.info("获取健康状况分布统计");

            Map<String, Object> distribution = new HashMap<>();

            // 统计各健康状态的老人数量
            long healthyCount = elderlyService.lambdaQuery()
                    .eq(com.smartcare.cloud.entity.Elderly::getHealthStatus, "HEALTHY")
                    .count();
            long subHealthCount = elderlyService.lambdaQuery()
                    .eq(com.smartcare.cloud.entity.Elderly::getHealthStatus, "SUBHEALTH")
                    .count();
            long sickCount = elderlyService.lambdaQuery()
                    .eq(com.smartcare.cloud.entity.Elderly::getHealthStatus, "SICK")
                    .count();
            long seriousCount = elderlyService.lambdaQuery()
                    .eq(com.smartcare.cloud.entity.Elderly::getHealthStatus, "SERIOUS")
                    .count();

            distribution.put("HEALTHY", healthyCount);
            distribution.put("SUBHEALTH", subHealthCount);
            distribution.put("SICK", sickCount);
            distribution.put("SERIOUS", seriousCount);

            long total = healthyCount + subHealthCount + sickCount + seriousCount;
            distribution.put("total", total);

            log.info("健康状况分布统计获取成功");
            return ResponseResult.success(distribution);

        } catch (Exception e) {
            log.error("获取健康状况分布统计失败", e);
            return ResponseResult.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警趋势分析
     * 
     * @param days 统计天数
     * @return 预警趋势数据
     */
    public ResponseResult<Map<String, Object>> getWarningTrend(int days) {
        try {
            log.info("获取预警趋势分析，天数：{}", days);

            Map<String, Object> trend = new HashMap<>();

            // 统计最近N天的预警数量
            // TODO: 需要根据实际数据库结构实现日期统计
            
            // 临时返回模拟数据
            trend.put("days", days);
            trend.put("message", "预警趋势分析功能待实现");

            log.info("预警趋势分析获取成功");
            return ResponseResult.success(trend);

        } catch (Exception e) {
            log.error("获取预警趋势分析失败", e);
            return ResponseResult.error("获取趋势数据失败：" + e.getMessage());
        }
    }
}
