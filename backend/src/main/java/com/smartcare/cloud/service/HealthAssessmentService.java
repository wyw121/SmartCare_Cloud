package com.smartcare.cloud.service;

import com.smartcare.cloud.dto.AssessmentReportDTO;
import com.smartcare.cloud.entity.Elderly;

/**
 * 健康评估服务接口
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
public interface HealthAssessmentService {

    /**
     * 生成健康评估报告
     *
     * @param elderly 老人信息
     * @return 评估报告
     */
    AssessmentReportDTO generateAssessmentReport(Elderly elderly);

    /**
     * 计算健康评分
     *
     * @param elderly 老人信息
     * @return 健康评分 (0-100)
     */
    Integer calculateHealthScore(Elderly elderly);

    /**
     * 评估健康风险
     *
     * @param elderly 老人信息
     * @return 风险等级 (LOW/MEDIUM/HIGH/CRITICAL)
     */
    String assessHealthRisk(Elderly elderly);

    /**
     * 生成健康建议
     *
     * @param elderly 老人信息
     * @return 健康建议列表
     */
    java.util.List<AssessmentReportDTO.HealthRecommendation> generateRecommendations(Elderly elderly);
}
