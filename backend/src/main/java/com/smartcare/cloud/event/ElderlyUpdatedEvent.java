package com.smartcare.cloud.event;

import org.springframework.context.ApplicationEvent;

import com.smartcare.cloud.entity.Elderly;

/**
 * 老人信息更新事件
 * 
 * @author SmartCare Team
 * @date 2024-01-27
 */
public class ElderlyUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final Elderly elderly;
    private final String operationType; // CREATE, UPDATE, DELETE

    public ElderlyUpdatedEvent(Object source, Elderly elderly, String operationType) {
        super(source);
        this.elderly = elderly;
        this.operationType = operationType;
    }

    public Elderly getElderly() {
        return elderly;
    }

    public String getOperationType() {
        return operationType;
    }
}
