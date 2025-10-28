package com.smartcare.cloud.service.impl;

import com.smartcare.cloud.dto.AssessmentReportDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.service.HealthAssessmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 健康评估服务实现类 - 简化版本
 * 提供老人健康评估报告生成功能
 * 
 * @author SmartCare System
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class HealthAssessmentServiceImpl implements HealthAssessmentService {

    /**
     * 生成老人健康评估报告
     * 当前版本使用模拟数据，后续可集成真实健康数据
     * 
     * @param elderly 老人信息
     * @return 健康评估报告
     */
    @Override
    public AssessmentReportDTO generateAssessmentReport(Elderly elderly) {
        // 创建评估报告对象
        AssessmentReportDTO report = new AssessmentReportDTO();
        
        // 基本信息
        report.setElderlyId(elderly.getId());
        report.setElderlyName(elderly.getName());
        report.setAssessmentDate(LocalDateTime.now());
        
        // 计算健康评分
        Integer healthScore = calculateHealthScore(elderly);
        report.setOverallScore(healthScore);
        
        // 评估健康等级
        String healthLevel = getHealthLevel(healthScore);
        report.setOverallLevel(healthLevel);
        
        // 生成基本信息
        AssessmentReportDTO.BasicInfo basicInfo = generateBasicInfo(elderly);
        report.setBasicInfo(basicInfo);
        
        // 生成生理指标评估
        AssessmentReportDTO.PhysiologicalAssessment physiological = generatePhysiologicalAssessment(elderly);
        report.setPhysiological(physiological);
        
        // 生成健康风险评估
        List<AssessmentReportDTO.HealthRisk> healthRisks = generateHealthRisks(elderly);
        report.setHealthRisks(healthRisks);
        
        // 生成健康建议
        List<AssessmentReportDTO.HealthRecommendation> recommendations = generateRecommendations(elderly);
        report.setRecommendations(recommendations);
        
        // 生成健康趋势分析
        AssessmentReportDTO.HealthTrend trend = generateHealthTrend(elderly);
        report.setTrend(trend);
        
        return report;
    }
    
    /**
     * 计算健康评分
     * 
     * @param elderly 老人信息
     * @return 健康评分 (0-100)
     */
    @Override
    public Integer calculateHealthScore(Elderly elderly) {
        int baseScore = 85; // 基础分数
        
        // 根据年龄调整评分
        if (elderly.getAge() != null) {
            if (elderly.getAge() >= 80) {
                baseScore -= 15;
            } else if (elderly.getAge() >= 70) {
                baseScore -= 10;
            } else if (elderly.getAge() >= 60) {
                baseScore -= 5;
            }
        }
        
        // 根据健康状态调整评分（如果有的话）
        if (elderly.getHealthStatus() != null) {
            switch (elderly.getHealthStatus()) {
                case "健康":
                    baseScore += 5;
                    break;
                case "亚健康":
                    baseScore -= 5;
                    break;
                case "慢性病":
                    baseScore -= 15;
                    break;
                case "疾病":
                    baseScore -= 25;
                    break;
            }
        }
        
        // 确保评分在合理范围内
        return Math.max(Math.min(baseScore, 100), 30);
    }
    
    /**
     * 评估健康风险
     * 
     * @param elderly 老人信息
     * @return 风险等级 (LOW/MEDIUM/HIGH/CRITICAL)
     */
    @Override
    public String assessHealthRisk(Elderly elderly) {
        Integer healthScore = calculateHealthScore(elderly);
        
        if (healthScore >= 85) {
            return "LOW";
        } else if (healthScore >= 70) {
            return "MEDIUM";
        } else if (healthScore >= 50) {
            return "HIGH";
        } else {
            return "CRITICAL";
        }
    }
    
    /**
     * 生成健康建议
     * 
     * @param elderly 老人信息
     * @return 健康建议列表
     */
    @Override
    public List<AssessmentReportDTO.HealthRecommendation> generateRecommendations(Elderly elderly) {
        List<AssessmentReportDTO.HealthRecommendation> recommendations = new ArrayList<>();
        
        // 基础建议
        recommendations.add(new AssessmentReportDTO.HealthRecommendation(
            "生活方式", 
            "保持规律作息，每日充足睡眠7-8小时", 
            "HIGH", 
            "每日"
        ));
        
        recommendations.add(new AssessmentReportDTO.HealthRecommendation(
            "运动锻炼", 
            "适量运动，建议每天散步30分钟以上", 
            "HIGH", 
            "每日"
        ));
        
        recommendations.add(new AssessmentReportDTO.HealthRecommendation(
            "饮食营养", 
            "均衡饮食，多吃蔬菜水果，控制盐分摄入", 
            "HIGH", 
            "每日"
        ));
        
        // 根据健康评分给出针对性建议
        Integer healthScore = calculateHealthScore(elderly);
        if (healthScore < 70) {
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "健康监测", 
                "建议每月进行一次全面体检", 
                "HIGH", 
                "每月"
            ));
            
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "就医指导", 
                "如有不适症状，及时就医", 
                "HIGH", 
                "按需"
            ));
        } else if (healthScore < 85) {
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "健康监测", 
                "建议每3个月进行一次健康检查", 
                "MEDIUM", 
                "季度"
            ));
        } else {
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "健康监测", 
                "建议每半年进行一次常规体检", 
                "MEDIUM", 
                "半年"
            ));
        }
        
        // 根据年龄给出建议
        if (elderly.getAge() != null && elderly.getAge() >= 75) {
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "安全防护", 
                "注意家居安全，防止跌倒意外", 
                "HIGH", 
                "每日"
            ));
            
            recommendations.add(new AssessmentReportDTO.HealthRecommendation(
                "心理健康", 
                "保持社交活动，预防老年抑郁", 
                "MEDIUM", 
                "每周"
            ));
        }
        
        return recommendations;
    }
    
    /**
     * 获取健康等级描述
     */
    private String getHealthLevel(Integer healthScore) {
        if (healthScore >= 85) {
            return "健康";
        } else if (healthScore >= 70) {
            return "亚健康";
        } else if (healthScore >= 50) {
            return "需关注";
        } else {
            return "高风险";
        }
    }
    
    /**
     * 生成基本信息
     */
    private AssessmentReportDTO.BasicInfo generateBasicInfo(Elderly elderly) {
        AssessmentReportDTO.BasicInfo basicInfo = new AssessmentReportDTO.BasicInfo();
        
        basicInfo.setAge(elderly.getAge());
        
        // 转换gender为字符串
        if (elderly.getGender() != null) {
            basicInfo.setGender(elderly.getGender() == 1 ? "男" : "女");
        } else {
            basicInfo.setGender("未知");
        }
        
        // 使用模拟的身高体重数据（后续可从健康记录中获取）
        BigDecimal height = BigDecimal.valueOf(165.0); // 模拟身高 165cm
        BigDecimal weight = BigDecimal.valueOf(65.0);   // 模拟体重 65kg
        
        basicInfo.setHeight(height);
        basicInfo.setWeight(weight);
        
        // 计算BMI
        if (height != null && weight != null && height.doubleValue() > 0) {
            double heightInMeters = height.doubleValue() / 100.0;
            double bmi = weight.doubleValue() / (heightInMeters * heightInMeters);
            basicInfo.setBmi(BigDecimal.valueOf(Math.round(bmi * 100.0) / 100.0));
            
            // BMI状态判断
            if (bmi < 18.5) {
                basicInfo.setBmiStatus("偏瘦");
            } else if (bmi < 24) {
                basicInfo.setBmiStatus("正常");
            } else if (bmi < 28) {
                basicInfo.setBmiStatus("偏胖");
            } else {
                basicInfo.setBmiStatus("肥胖");
            }
        }
        
        // 设置健康状态和照护等级
        basicInfo.setHealthStatus(elderly.getHealthStatus() != null ? elderly.getHealthStatus() : "健康");
        
        // 转换照护等级为字符串
        if (elderly.getCareLevel() != null) {
            switch (elderly.getCareLevel()) {
                case 1:
                    basicInfo.setCareLevel("自理");
                    break;
                case 2:
                    basicInfo.setCareLevel("半自理");
                    break;
                case 3:
                    basicInfo.setCareLevel("不能自理");
                    break;
                default:
                    basicInfo.setCareLevel("自理");
                    break;
            }
        } else {
            basicInfo.setCareLevel("自理");
        }
        
        return basicInfo;
    }
    
    /**
     * 生成生理指标评估
     */
    private AssessmentReportDTO.PhysiologicalAssessment generatePhysiologicalAssessment(Elderly elderly) {
        AssessmentReportDTO.PhysiologicalAssessment physiological = new AssessmentReportDTO.PhysiologicalAssessment();
        
        // 血压评估（模拟数据）
        AssessmentReportDTO.VitalSignAssessment bloodPressure = new AssessmentReportDTO.VitalSignAssessment(
            "120/80 mmHg", "90-140/60-90 mmHg", "正常", 90, "血压水平正常，请继续保持"
        );
        physiological.setBloodPressure(bloodPressure);
        
        // 心率评估（模拟数据）
        AssessmentReportDTO.VitalSignAssessment heartRate = new AssessmentReportDTO.VitalSignAssessment(
            "72 次/分", "60-100 次/分", "正常", 85, "心率规律正常"
        );
        physiological.setHeartRate(heartRate);
        
        // 体温评估（模拟数据）
        AssessmentReportDTO.VitalSignAssessment bodyTemperature = new AssessmentReportDTO.VitalSignAssessment(
            "36.5 °C", "36.0-37.3 °C", "正常", 95, "体温正常"
        );
        physiological.setBodyTemperature(bodyTemperature);
        
        // 血糖评估（模拟数据）
        AssessmentReportDTO.VitalSignAssessment bloodGlucose = new AssessmentReportDTO.VitalSignAssessment(
            "5.6 mmol/L", "3.9-6.1 mmol/L", "正常", 88, "血糖水平正常"
        );
        physiological.setBloodGlucose(bloodGlucose);
        
        // 计算生理指标总评分
        int totalScore = (bloodPressure.getScore() + heartRate.getScore() + 
                         bodyTemperature.getScore() + bloodGlucose.getScore()) / 4;
        physiological.setTotalScore(totalScore);
        physiological.setLevel(getScoreLevel(totalScore));
        
        return physiological;
    }
    
    /**
     * 生成健康风险评估
     */
    private List<AssessmentReportDTO.HealthRisk> generateHealthRisks(Elderly elderly) {
        List<AssessmentReportDTO.HealthRisk> healthRisks = new ArrayList<>();
        
        // 根据年龄评估风险
        if (elderly.getAge() != null && elderly.getAge() >= 70) {
            healthRisks.add(new AssessmentReportDTO.HealthRisk(
                "心血管疾病风险", "中等", 65,
                "随着年龄增长，心血管疾病风险有所增加",
                "定期监测血压、血脂，保持低盐饮食，适量运动"
            ));
            
            healthRisks.add(new AssessmentReportDTO.HealthRisk(
                "跌倒风险", "中等", 60,
                "老年人平衡能力下降，存在跌倒风险",
                "居家环境安全改造，适当进行平衡训练，避免急速起身"
            ));
        }
        
        // 根据BMI评估风险
        AssessmentReportDTO.BasicInfo basicInfo = generateBasicInfo(elderly);
        if (basicInfo.getBmi() != null) {
            double bmi = basicInfo.getBmi().doubleValue();
            if (bmi >= 28) {
                healthRisks.add(new AssessmentReportDTO.HealthRisk(
                    "肥胖相关风险", "高等", 80,
                    "BMI超标，可能增加糖尿病、高血压等疾病风险",
                    "控制饮食，增加运动量，必要时咨询营养师"
                ));
            } else if (bmi < 18.5) {
                healthRisks.add(new AssessmentReportDTO.HealthRisk(
                    "营养不良风险", "中等", 70,
                    "体重偏轻，可能存在营养不良风险",
                    "增加营养摄入，关注蛋白质和维生素补充"
                ));
            }
        }
        
        // 如果没有特殊风险，添加一般性风险提示
        if (healthRisks.isEmpty()) {
            healthRisks.add(new AssessmentReportDTO.HealthRisk(
                "一般健康风险", "低等", 30,
                "目前整体健康状况良好，风险较低",
                "保持良好的生活习惯，定期体检"
            ));
        }
        
        return healthRisks;
    }
    
    /**
     * 生成健康趋势分析
     */
    private AssessmentReportDTO.HealthTrend generateHealthTrend(Elderly elderly) {
        AssessmentReportDTO.HealthTrend trend = new AssessmentReportDTO.HealthTrend();
        
        // 模拟趋势数据
        trend.setOverallTrend("稳定");
        trend.setBloodPressureTrend("稳定");
        trend.setHeartRateTrend("稳定");
        trend.setBloodGlucoseTrend("稳定");
        trend.setWeightTrend("稳定");
        
        // 生成趋势分析
        StringBuilder analysis = new StringBuilder();
        analysis.append("根据近期健康数据分析，");
        analysis.append(elderly.getName()).append("的整体健康状况保持稳定。");
        
        Integer healthScore = calculateHealthScore(elderly);
        if (healthScore >= 85) {
            analysis.append("各项生理指标均在正常范围内，建议继续保持现有的健康生活方式。");
        } else if (healthScore >= 70) {
            analysis.append("大部分指标正常，但需要关注某些方面的变化，建议加强健康监测。");
        } else {
            analysis.append("部分指标需要关注，建议及时就医咨询，调整生活方式。");
        }
        
        trend.setAnalysis(analysis.toString());
        
        return trend;
    }
    
    /**
     * 根据评分获取等级
     */
    private String getScoreLevel(Integer score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "一般";
        } else {
            return "需改善";
        }
    }
}
