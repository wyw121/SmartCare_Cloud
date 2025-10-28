package com.smartcare.cloud.service;

import com.smartcare.cloud.entity.HealthWarning;

/**
 * WebSocket消息推送服务接口
 */
public interface MessagePushService {

    /**
     * 推送健康预警消息到所有在线用户
     *
     * @param warning 健康预警对象
     */
    void pushWarningToAll(HealthWarning warning);

    /**
     * 推送健康预警消息到指定用户
     *
     * @param userId 用户ID
     * @param warning 健康预警对象
     */
    void pushWarningToUser(Long userId, HealthWarning warning);

    /**
     * 推送健康预警消息到老人的所有家属
     *
     * @param elderlyId 老人ID
     * @param warning 健康预警对象
     */
    void pushWarningToFamily(Long elderlyId, HealthWarning warning);

    /**
     * 推送系统通知到所有在线用户
     *
     * @param notification 通知内容
     */
    void pushNotificationToAll(String notification);

    /**
     * 推送系统通知到指定用户
     *
     * @param userId 用户ID
     * @param notification 通知内容
     */
    void pushNotificationToUser(Long userId, String notification);
}
