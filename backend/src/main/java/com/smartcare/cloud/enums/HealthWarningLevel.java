package com.smartcare.cloud.enums;

/**
 * 健康预警级别枚举
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public enum HealthWarningLevel {
    
    LOW(1, "低风险", "#52c41a", "轻微异常，需要关注"),
    MEDIUM(2, "中风险", "#faad14", "异常较明显，建议及时处理"),
    HIGH(3, "高风险", "#ff7a45", "严重异常，需要立即处理"),
    URGENT(4, "紧急", "#ff4d4f", "生命危险，需要紧急救援");
    
    private final Integer level;
    private final String description;
    private final String color;
    private final String detail;
    
    HealthWarningLevel(Integer level, String description, String color, String detail) {
        this.level = level;
        this.description = description;
        this.color = color;
        this.detail = detail;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getDetail() {
        return detail;
    }
    
    /**
     * 根据级别数值获取枚举值
     */
    public static HealthWarningLevel fromLevel(Integer level) {
        for (HealthWarningLevel warningLevel : values()) {
            if (warningLevel.level.equals(level)) {
                return warningLevel;
            }
        }
        return null;
    }
    
    /**
     * 根据描述获取枚举值
     */
    public static HealthWarningLevel fromDescription(String description) {
        for (HealthWarningLevel warningLevel : values()) {
            if (warningLevel.description.equals(description)) {
                return warningLevel;
            }
        }
        return null;
    }
}
