package com.smartcare.cloud.enums;

/**
 * 健康预警数据来源枚举
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public enum DataSource {
    
    BLOOD_PRESSURE_MONITOR("智能血压计", "BP_MONITOR", "自动血压监测设备"),
    GLUCOSE_MONITOR("血糖仪", "GLUCOSE_MONITOR", "血糖监测设备"),
    HEART_RATE_MONITOR("心率监测器", "HR_MONITOR", "心率检测设备"),
    THERMOMETER("体温计", "TEMP_MONITOR", "体温监测设备"),
    ACTIVITY_MONITOR("活动监测器", "ACTIVITY_MONITOR", "运动和活动监测设备"),
    MEDICINE_BOX("药盒监测器", "MED_MONITOR", "智能药盒设备"),
    FALL_DETECTOR("跌倒检测器", "FALL_DETECTOR", "跌倒检测传感器"),
    SLEEP_MONITOR("睡眠监测器", "SLEEP_MONITOR", "睡眠质量监测设备"),
    EMERGENCY_BUTTON("紧急按钮", "SOS_BUTTON", "紧急求助按钮"),
    ENVIRONMENT_SENSOR("环境传感器", "ENV_SENSOR", "环境监测传感器"),
    SMART_BAND("智能手环", "SMART_BAND", "可穿戴智能手环"),
    HEALTH_KIOSK("健康一体机", "HEALTH_KIOSK", "综合健康检测设备"),
    MOBILE_APP("移动APP", "MOBILE_APP", "手机应用程序"),
    MANUAL_INPUT("手动录入", "MANUAL", "人工手动输入"),
    SYSTEM_ANALYSIS("系统分析", "SYSTEM", "系统自动分析");
    
    private final String description;
    private final String code;
    private final String detail;
    
    DataSource(String description, String code, String detail) {
        this.description = description;
        this.code = code;
        this.detail = detail;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDetail() {
        return detail;
    }
    
    /**
     * 根据描述获取枚举值
     */
    public static DataSource fromDescription(String description) {
        for (DataSource source : values()) {
            if (source.description.equals(description)) {
                return source;
            }
        }
        return null;
    }
    
    /**
     * 根据代码获取枚举值
     */
    public static DataSource fromCode(String code) {
        for (DataSource source : values()) {
            if (source.code.equals(code)) {
                return source;
            }
        }
        return null;
    }
}
