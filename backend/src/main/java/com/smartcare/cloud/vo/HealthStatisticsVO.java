package com.smartcare.cloud.vo;

import java.util.List;
import java.util.Map;

/**
 * 健康统计数据视图对象
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
public class HealthStatisticsVO {

    /**
     * 健康状态分布
     */
    private List<HealthStatusDistribution> healthStatusDistribution;

    /**
     * 年龄段健康分布
     */
    private List<AgeHealthDistribution> ageHealthDistribution;

    /**
     * 健康风险评估
     */
    private HealthRiskAssessment healthRiskAssessment;

    /**
     * 健康趋势数据
     */
    private List<HealthTrend> healthTrends;

    /**
     * 总统计数据
     */
    private Map<String, Object> summary;

    /**
     * 健康状态分布项
     */
    public static class HealthStatusDistribution {

        private String status;      // 健康状态
        private Integer count;      // 数量
        private Double percentage;  // 百分比

        public HealthStatusDistribution() {
        }

        public HealthStatusDistribution(String status, Integer count, Double percentage) {
            this.status = status;
            this.count = count;
            this.percentage = percentage;
        }

        // Getters and Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    /**
     * 年龄段健康分布项
     */
    public static class AgeHealthDistribution {

        private String ageRange;    // 年龄段
        private Integer healthy;    // 健康人数
        private Integer subHealth;  // 亚健康人数
        private Integer chronic;    // 慢性病人数
        private Integer risk;       // 高风险人数

        public AgeHealthDistribution() {
        }

        public AgeHealthDistribution(String ageRange, Integer healthy, Integer subHealth, Integer chronic, Integer risk) {
            this.ageRange = ageRange;
            this.healthy = healthy;
            this.subHealth = subHealth;
            this.chronic = chronic;
            this.risk = risk;
        }

        // Getters and Setters
        public String getAgeRange() {
            return ageRange;
        }

        public void setAgeRange(String ageRange) {
            this.ageRange = ageRange;
        }

        public Integer getHealthy() {
            return healthy;
        }

        public void setHealthy(Integer healthy) {
            this.healthy = healthy;
        }

        public Integer getSubHealth() {
            return subHealth;
        }

        public void setSubHealth(Integer subHealth) {
            this.subHealth = subHealth;
        }

        public Integer getChronic() {
            return chronic;
        }

        public void setChronic(Integer chronic) {
            this.chronic = chronic;
        }

        public Integer getRisk() {
            return risk;
        }

        public void setRisk(Integer risk) {
            this.risk = risk;
        }
    }

    /**
     * 健康风险评估
     */
    public static class HealthRiskAssessment {

        private Double healthyRate;     // 健康率
        private Double chronicRate;     // 慢性病率
        private Double riskRate;        // 高风险率
        private Double careRate;        // 照护率
        private Double satisfactionRate; // 满意度

        public HealthRiskAssessment() {
        }

        public HealthRiskAssessment(Double healthyRate, Double chronicRate, Double riskRate, Double careRate, Double satisfactionRate) {
            this.healthyRate = healthyRate;
            this.chronicRate = chronicRate;
            this.riskRate = riskRate;
            this.careRate = careRate;
            this.satisfactionRate = satisfactionRate;
        }

        // Getters and Setters
        public Double getHealthyRate() {
            return healthyRate;
        }

        public void setHealthyRate(Double healthyRate) {
            this.healthyRate = healthyRate;
        }

        public Double getChronicRate() {
            return chronicRate;
        }

        public void setChronicRate(Double chronicRate) {
            this.chronicRate = chronicRate;
        }

        public Double getRiskRate() {
            return riskRate;
        }

        public void setRiskRate(Double riskRate) {
            this.riskRate = riskRate;
        }

        public Double getCareRate() {
            return careRate;
        }

        public void setCareRate(Double careRate) {
            this.careRate = careRate;
        }

        public Double getSatisfactionRate() {
            return satisfactionRate;
        }

        public void setSatisfactionRate(Double satisfactionRate) {
            this.satisfactionRate = satisfactionRate;
        }
    }

    /**
     * 健康趋势项
     */
    public static class HealthTrend {

        private String month;       // 月份
        private Integer healthy;    // 健康人数
        private Integer subHealth;  // 亚健康人数
        private Integer chronic;    // 慢性病人数
        private Integer risk;       // 高风险人数

        public HealthTrend() {
        }

        public HealthTrend(String month, Integer healthy, Integer subHealth, Integer chronic, Integer risk) {
            this.month = month;
            this.healthy = healthy;
            this.subHealth = subHealth;
            this.chronic = chronic;
            this.risk = risk;
        }

        // Getters and Setters
        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getHealthy() {
            return healthy;
        }

        public void setHealthy(Integer healthy) {
            this.healthy = healthy;
        }

        public Integer getSubHealth() {
            return subHealth;
        }

        public void setSubHealth(Integer subHealth) {
            this.subHealth = subHealth;
        }

        public Integer getChronic() {
            return chronic;
        }

        public void setChronic(Integer chronic) {
            this.chronic = chronic;
        }

        public Integer getRisk() {
            return risk;
        }

        public void setRisk(Integer risk) {
            this.risk = risk;
        }
    }

    // Main class Getters and Setters
    public List<HealthStatusDistribution> getHealthStatusDistribution() {
        return healthStatusDistribution;
    }

    public void setHealthStatusDistribution(List<HealthStatusDistribution> healthStatusDistribution) {
        this.healthStatusDistribution = healthStatusDistribution;
    }

    public List<AgeHealthDistribution> getAgeHealthDistribution() {
        return ageHealthDistribution;
    }

    public void setAgeHealthDistribution(List<AgeHealthDistribution> ageHealthDistribution) {
        this.ageHealthDistribution = ageHealthDistribution;
    }

    public HealthRiskAssessment getHealthRiskAssessment() {
        return healthRiskAssessment;
    }

    public void setHealthRiskAssessment(HealthRiskAssessment healthRiskAssessment) {
        this.healthRiskAssessment = healthRiskAssessment;
    }

    public List<HealthTrend> getHealthTrends() {
        return healthTrends;
    }

    public void setHealthTrends(List<HealthTrend> healthTrends) {
        this.healthTrends = healthTrends;
    }

    public Map<String, Object> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }
}
