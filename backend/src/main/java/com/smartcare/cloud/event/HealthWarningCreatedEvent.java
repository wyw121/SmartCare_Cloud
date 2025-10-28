package com.smartcare.cloud.event;

import org.springframework.context.ApplicationEvent;

import com.smartcare.cloud.entity.HealthWarning;

/**
 * 健康预警创建事件
 * 
 * 当系统检测到健康异常并创建预警时发布此事件，
 * 触发后续处理流程(通知、记录、统计等)
 * 
 * @author SmartCare Team
 * @date 2024-01-27
 */
public class HealthWarningCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * 健康预警实体
     */
    private final HealthWarning healthWarning;

    /**
     * 预警来源(system/device/manual)
     */
    private final String source;

    /**
     * 是否需要紧急处理
     */
    private final boolean urgent;

    public HealthWarningCreatedEvent(Object source, HealthWarning healthWarning, String warningSource, boolean urgent) {
        super(source);
        this.healthWarning = healthWarning;
        this.source = warningSource;
        this.urgent = urgent;
    }

    public HealthWarning getHealthWarning() {
        return healthWarning;
    }

    public String getWarningSource() {
        return source;
    }

    public boolean isUrgent() {
        return urgent;
    }
}
