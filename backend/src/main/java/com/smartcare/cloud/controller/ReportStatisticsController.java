package com.smartcare.cloud.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.service.ReportStatisticsService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 统计报表控制器
 *
 * @author SmartCare Team
 * @since 2025-06-27
 */
@Tag(name = "统计报表管理", description = "统计报表相关接口")
@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(ReportStatisticsController.class);

    @Autowired
    private ReportStatisticsService reportStatisticsService;

    /**
     * 获取数据概览统计
     */
    @Operation(summary = "获取数据概览统计")
    @GetMapping("/overview")
    public ResponseResult<Map<String, Object>> getOverviewStatistics() {
        log.info("获取数据概览统计");
        try {
            Map<String, Object> data = reportStatisticsService.getOverviewStatistics();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取数据概览统计失败", e);
            return ResponseResult.error("获取数据概览统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取健康状况统计
     */
    @Operation(summary = "获取健康状况统计")
    @GetMapping("/health-status")
    public ResponseResult<Map<String, Object>> getHealthStatusStatistics() {
        log.info("获取健康状况统计");
        try {
            Map<String, Object> data = reportStatisticsService.getHealthStatusStatistics();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取健康状况统计失败", e);
            return ResponseResult.error("获取健康状况统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警统计分析
     */
    @Operation(summary = "获取预警统计分析")
    @GetMapping("/warning-analysis")
    public ResponseResult<Map<String, Object>> getWarningAnalysis() {
        log.info("获取预警统计分析");
        try {
            Map<String, Object> data = reportStatisticsService.getWarningAnalysis();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取预警统计分析失败", e);
            return ResponseResult.error("获取预警统计分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取医疗服务统计
     */
    @Operation(summary = "获取医疗服务统计")
    @GetMapping("/medical-service")
    public ResponseResult<Map<String, Object>> getMedicalServiceStatistics() {
        log.info("获取医疗服务统计");
        try {
            Map<String, Object> data = reportStatisticsService.getMedicalServiceStatistics();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取医疗服务统计失败", e);
            return ResponseResult.error("获取医疗服务统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取趋势分析数据
     */
    @Operation(summary = "获取趋势分析数据")
    @GetMapping("/trend-analysis")
    public ResponseResult<Map<String, Object>> getTrendAnalysis(
            @Parameter(description = "时间范围") @RequestParam(defaultValue = "30") String timeRange) {
        log.info("获取趋势分析数据，时间范围：{}", timeRange);
        try {
            Map<String, Object> data = reportStatisticsService.getTrendAnalysis(timeRange);
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取趋势分析数据失败", e);
            return ResponseResult.error("获取趋势分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取照护等级统计
     */
    @Operation(summary = "获取照护等级统计")
    @GetMapping("/care-level")
    public ResponseResult<Map<String, Object>> getCareLevelStatistics() {
        log.info("获取照护等级统计");
        try {
            Map<String, Object> data = reportStatisticsService.getCareLevelStatistics();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取照护等级统计失败", e);
            return ResponseResult.error("获取照护等级统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备使用统计
     */
    @Operation(summary = "获取设备使用统计")
    @GetMapping("/equipment-usage")
    public ResponseResult<Map<String, Object>> getEquipmentUsageStatistics() {
        log.info("获取设备使用统计");
        try {
            Map<String, Object> data = reportStatisticsService.getEquipmentUsageStatistics();
            return ResponseResult.success(data);
        } catch (Exception e) {
            log.error("获取设备使用统计失败", e);
            return ResponseResult.error("获取设备使用统计失败：" + e.getMessage());
        }
    }
}
