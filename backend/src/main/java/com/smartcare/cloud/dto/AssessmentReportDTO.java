package com.smartcare.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 健康评估报告数据传输对象
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
@Schema(description = "健康评估报告")
public class AssessmentReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "老人ID")
    private Long elderlyId;

    @Schema(description = "老人姓名")
    private String elderlyName;

    @Schema(description = "评估日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assessmentDate;

    @Schema(description = "总体健康评分")
    private Integer overallScore;

    @Schema(description = "总体健康等级")
    private String overallLevel;

    @Schema(description = "基本信息")
    private BasicInfo basicInfo;

    @Schema(description = "生理指标评估")
    private PhysiologicalAssessment physiological;

    @Schema(description = "健康风险评估")
    private List<HealthRisk> healthRisks;

    @Schema(description = "健康建议")
    private List<HealthRecommendation> recommendations;

    @Schema(description = "近期健康趋势")
    private HealthTrend trend;

    // 构造函数
    public AssessmentReportDTO() {
    }

    public AssessmentReportDTO(Long elderlyId, String elderlyName, LocalDateTime assessmentDate,
            Integer overallScore, String overallLevel, BasicInfo basicInfo,
            PhysiologicalAssessment physiological, List<HealthRisk> healthRisks,
            List<HealthRecommendation> recommendations, HealthTrend trend) {
        this.elderlyId = elderlyId;
        this.elderlyName = elderlyName;
        this.assessmentDate = assessmentDate;
        this.overallScore = overallScore;
        this.overallLevel = overallLevel;
        this.basicInfo = basicInfo;
        this.physiological = physiological;
        this.healthRisks = healthRisks;
        this.recommendations = recommendations;
        this.trend = trend;
    }

    // Getter和Setter方法
    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public LocalDateTime getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDateTime assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public String getOverallLevel() {
        return overallLevel;
    }

    public void setOverallLevel(String overallLevel) {
        this.overallLevel = overallLevel;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public PhysiologicalAssessment getPhysiological() {
        return physiological;
    }

    public void setPhysiological(PhysiologicalAssessment physiological) {
        this.physiological = physiological;
    }

    public List<HealthRisk> getHealthRisks() {
        return healthRisks;
    }

    public void setHealthRisks(List<HealthRisk> healthRisks) {
        this.healthRisks = healthRisks;
    }

    public List<HealthRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<HealthRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public HealthTrend getTrend() {
        return trend;
    }

    public void setTrend(HealthTrend trend) {
        this.trend = trend;
    }

    /**
     * 基本信息
     */
    @Schema(description = "基本信息")
    public static class BasicInfo {

        @Schema(description = "年龄")
        private Integer age;

        @Schema(description = "性别")
        private String gender;

        @Schema(description = "身高(cm)")
        private BigDecimal height;

        @Schema(description = "体重(kg)")
        private BigDecimal weight;

        @Schema(description = "BMI")
        private BigDecimal bmi;

        @Schema(description = "BMI状态")
        private String bmiStatus;

        @Schema(description = "当前健康状态")
        private String healthStatus;

        @Schema(description = "照护等级")
        private String careLevel;

        public BasicInfo() {
        }

        public BasicInfo(Integer age, String gender, BigDecimal height, BigDecimal weight,
                BigDecimal bmi, String bmiStatus, String healthStatus, String careLevel) {
            this.age = age;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            this.bmi = bmi;
            this.bmiStatus = bmiStatus;
            this.healthStatus = healthStatus;
            this.careLevel = careLevel;
        }

        // Getter和Setter方法
        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public BigDecimal getHeight() {
            return height;
        }

        public void setHeight(BigDecimal height) {
            this.height = height;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public BigDecimal getBmi() {
            return bmi;
        }

        public void setBmi(BigDecimal bmi) {
            this.bmi = bmi;
        }

        public String getBmiStatus() {
            return bmiStatus;
        }

        public void setBmiStatus(String bmiStatus) {
            this.bmiStatus = bmiStatus;
        }

        public String getHealthStatus() {
            return healthStatus;
        }

        public void setHealthStatus(String healthStatus) {
            this.healthStatus = healthStatus;
        }

        public String getCareLevel() {
            return careLevel;
        }

        public void setCareLevel(String careLevel) {
            this.careLevel = careLevel;
        }
    }

    /**
     * 生理指标评估
     */
    @Schema(description = "生理指标评估")
    public static class PhysiologicalAssessment {

        @Schema(description = "血压评估")
        private VitalSignAssessment bloodPressure;

        @Schema(description = "心率评估")
        private VitalSignAssessment heartRate;

        @Schema(description = "体温评估")
        private VitalSignAssessment bodyTemperature;

        @Schema(description = "血糖评估")
        private VitalSignAssessment bloodGlucose;

        @Schema(description = "生理指标总评分")
        private Integer totalScore;

        @Schema(description = "生理指标等级")
        private String level;

        public PhysiologicalAssessment() {
        }

        public PhysiologicalAssessment(VitalSignAssessment bloodPressure, VitalSignAssessment heartRate,
                VitalSignAssessment bodyTemperature, VitalSignAssessment bloodGlucose,
                Integer totalScore, String level) {
            this.bloodPressure = bloodPressure;
            this.heartRate = heartRate;
            this.bodyTemperature = bodyTemperature;
            this.bloodGlucose = bloodGlucose;
            this.totalScore = totalScore;
            this.level = level;
        }

        // Getter和Setter方法
        public VitalSignAssessment getBloodPressure() {
            return bloodPressure;
        }

        public void setBloodPressure(VitalSignAssessment bloodPressure) {
            this.bloodPressure = bloodPressure;
        }

        public VitalSignAssessment getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(VitalSignAssessment heartRate) {
            this.heartRate = heartRate;
        }

        public VitalSignAssessment getBodyTemperature() {
            return bodyTemperature;
        }

        public void setBodyTemperature(VitalSignAssessment bodyTemperature) {
            this.bodyTemperature = bodyTemperature;
        }

        public VitalSignAssessment getBloodGlucose() {
            return bloodGlucose;
        }

        public void setBloodGlucose(VitalSignAssessment bloodGlucose) {
            this.bloodGlucose = bloodGlucose;
        }

        public Integer getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(Integer totalScore) {
            this.totalScore = totalScore;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    /**
     * 生命体征评估
     */
    @Schema(description = "生命体征评估")
    public static class VitalSignAssessment {

        @Schema(description = "当前值")
        private String currentValue;

        @Schema(description = "正常范围")
        private String normalRange;

        @Schema(description = "状态")
        private String status;

        @Schema(description = "评分")
        private Integer score;

        @Schema(description = "评价")
        private String evaluation;

        public VitalSignAssessment() {
        }

        public VitalSignAssessment(String currentValue, String normalRange, String status,
                Integer score, String evaluation) {
            this.currentValue = currentValue;
            this.normalRange = normalRange;
            this.status = status;
            this.score = score;
            this.evaluation = evaluation;
        }

        // Getter和Setter方法
        public String getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(String currentValue) {
            this.currentValue = currentValue;
        }

        public String getNormalRange() {
            return normalRange;
        }

        public void setNormalRange(String normalRange) {
            this.normalRange = normalRange;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getEvaluation() {
            return evaluation;
        }

        public void setEvaluation(String evaluation) {
            this.evaluation = evaluation;
        }
    }

    /**
     * 健康风险
     */
    @Schema(description = "健康风险")
    public static class HealthRisk {

        @Schema(description = "风险类型")
        private String riskType;

        @Schema(description = "风险等级")
        private String riskLevel;

        @Schema(description = "风险评分")
        private Integer riskScore;

        @Schema(description = "风险描述")
        private String description;

        @Schema(description = "预防措施")
        private String prevention;

        public HealthRisk() {
        }

        public HealthRisk(String riskType, String riskLevel, Integer riskScore,
                String description, String prevention) {
            this.riskType = riskType;
            this.riskLevel = riskLevel;
            this.riskScore = riskScore;
            this.description = description;
            this.prevention = prevention;
        }

        // Getter和Setter方法
        public String getRiskType() {
            return riskType;
        }

        public void setRiskType(String riskType) {
            this.riskType = riskType;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }

        public Integer getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(Integer riskScore) {
            this.riskScore = riskScore;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrevention() {
            return prevention;
        }

        public void setPrevention(String prevention) {
            this.prevention = prevention;
        }
    }

    /**
     * 健康建议
     */
    @Schema(description = "健康建议")
    public static class HealthRecommendation {

        @Schema(description = "建议类型")
        private String type;

        @Schema(description = "建议内容")
        private String content;

        @Schema(description = "优先级")
        private String priority;

        @Schema(description = "执行周期")
        private String cycle;

        public HealthRecommendation() {
        }

        public HealthRecommendation(String type, String content, String priority, String cycle) {
            this.type = type;
            this.content = content;
            this.priority = priority;
            this.cycle = cycle;
        }

        // Getter和Setter方法
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }
    }

    /**
     * 健康趋势
     */
    @Schema(description = "健康趋势")
    public static class HealthTrend {

        @Schema(description = "总体趋势")
        private String overallTrend;

        @Schema(description = "血压趋势")
        private String bloodPressureTrend;

        @Schema(description = "心率趋势")
        private String heartRateTrend;

        @Schema(description = "血糖趋势")
        private String bloodGlucoseTrend;

        @Schema(description = "体重趋势")
        private String weightTrend;

        @Schema(description = "趋势分析")
        private String analysis;

        public HealthTrend() {
        }

        public HealthTrend(String overallTrend, String bloodPressureTrend, String heartRateTrend,
                String bloodGlucoseTrend, String weightTrend, String analysis) {
            this.overallTrend = overallTrend;
            this.bloodPressureTrend = bloodPressureTrend;
            this.heartRateTrend = heartRateTrend;
            this.bloodGlucoseTrend = bloodGlucoseTrend;
            this.weightTrend = weightTrend;
            this.analysis = analysis;
        }

        // Getter和Setter方法
        public String getOverallTrend() {
            return overallTrend;
        }

        public void setOverallTrend(String overallTrend) {
            this.overallTrend = overallTrend;
        }

        public String getBloodPressureTrend() {
            return bloodPressureTrend;
        }

        public void setBloodPressureTrend(String bloodPressureTrend) {
            this.bloodPressureTrend = bloodPressureTrend;
        }

        public String getHeartRateTrend() {
            return heartRateTrend;
        }

        public void setHeartRateTrend(String heartRateTrend) {
            this.heartRateTrend = heartRateTrend;
        }

        public String getBloodGlucoseTrend() {
            return bloodGlucoseTrend;
        }

        public void setBloodGlucoseTrend(String bloodGlucoseTrend) {
            this.bloodGlucoseTrend = bloodGlucoseTrend;
        }

        public String getWeightTrend() {
            return weightTrend;
        }

        public void setWeightTrend(String weightTrend) {
            this.weightTrend = weightTrend;
        }

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }
    }
}
