package com.smartcare.cloud.enums;

/**
 * 健康预警类型枚举
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public enum HealthWarningType {
    
    BLOOD_PRESSURE("血压异常", "血压监测值超出正常范围"),
    BLOOD_SUGAR("血糖异常", "血糖监测值超出正常范围"),
    HEART_RATE("心率异常", "心率监测值超出正常范围"),
    BODY_TEMPERATURE("体温异常", "体温监测值超出正常范围"),
    LONG_TERM_INACTIVITY("长期不活动", "长时间缺乏运动或活动"),
    MEDICATION_NOT_TAKEN("药物未按时服用", "未按医嘱按时服药"),
    FALL_DETECTION("跌倒检测", "检测到老人跌倒"),
    SLEEP_ABNORMAL("睡眠异常", "睡眠质量异常或失眠"),
    EMERGENCY_CALL("紧急呼叫", "老人主动触发紧急求助"),
    ENVIRONMENT_ABNORMAL("环境异常", "居住环境存在安全隐患"),
    COGNITIVE_ABNORMAL("认知异常", "认知功能异常表现"),
    SOCIAL_ISOLATION("社交孤立", "长期缺乏社交活动"),
    DEVICE_ABNORMAL("设备异常", "监测设备故障或异常"),
    NUTRITION_DEFICIENCY("营养不良", "营养摄入不足或不均衡");
    
    private final String description;
    private final String detail;
    
    HealthWarningType(String description, String detail) {
        this.description = description;
        this.detail = detail;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getDetail() {
        return detail;
    }
    
    /**
     * 根据描述获取枚举值
     */
    public static HealthWarningType fromDescription(String description) {
        for (HealthWarningType type : values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        return null;
    }
}
