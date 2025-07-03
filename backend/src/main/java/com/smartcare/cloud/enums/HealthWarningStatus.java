package com.smartcare.cloud.enums;

/**
 * 健康预警状态枚举
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public enum HealthWarningStatus {
    
    PENDING(0, "未处理", "#d9d9d9", "预警刚生成，尚未被查看"),
    VIEWED(1, "已查看", "#1890ff", "预警已被医护人员查看"),
    PROCESSING(2, "处理中", "#faad14", "正在处理该预警"),
    HANDLED(3, "已处理", "#52c41a", "预警已处理完成"),
    IGNORED(4, "已忽略", "#f5222d", "预警被标记为忽略");
    
    private final Integer status;
    private final String description;
    private final String color;
    private final String detail;
    
    HealthWarningStatus(Integer status, String description, String color, String detail) {
        this.status = status;
        this.description = description;
        this.color = color;
        this.detail = detail;
    }
    
    public Integer getStatus() {
        return status;
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
     * 根据状态数值获取枚举值
     */
    public static HealthWarningStatus fromStatus(Integer status) {
        for (HealthWarningStatus warningStatus : values()) {
            if (warningStatus.status.equals(status)) {
                return warningStatus;
            }
        }
        return null;
    }
    
    /**
     * 根据描述获取枚举值
     */
    public static HealthWarningStatus fromDescription(String description) {
        for (HealthWarningStatus warningStatus : values()) {
            if (warningStatus.description.equals(description)) {
                return warningStatus;
            }
        }
        return null;
    }
}
