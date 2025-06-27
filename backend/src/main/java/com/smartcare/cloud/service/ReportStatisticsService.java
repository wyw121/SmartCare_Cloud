package com.smartcare.cloud.service;

import java.util.Map;

/**
 * 统计报表服务接口
 *
 * @author SmartCare Team
 * @since 2025-06-27
 */
public interface ReportStatisticsService {

    /**
     * 获取数据概览统计
     *
     * @return 数据概览统计信息
     */
    Map<String, Object> getOverviewStatistics();

    /**
     * 获取健康状况统计
     *
     * @return 健康状况统计信息
     */
    Map<String, Object> getHealthStatusStatistics();

    /**
     * 获取预警统计分析
     *
     * @return 预警统计分析信息
     */
    Map<String, Object> getWarningAnalysis();

    /**
     * 获取医疗服务统计
     *
     * @return 医疗服务统计信息
     */
    Map<String, Object> getMedicalServiceStatistics();

    /**
     * 获取趋势分析数据
     *
     * @param timeRange 时间范围
     * @return 趋势分析数据
     */
    Map<String, Object> getTrendAnalysis(String timeRange);

    /**
     * 获取照护等级统计
     *
     * @return 照护等级统计信息
     */
    Map<String, Object> getCareLevelStatistics();

    /**
     * 获取设备使用统计
     *
     * @return 设备使用统计信息
     */
    Map<String, Object> getEquipmentUsageStatistics();
}
