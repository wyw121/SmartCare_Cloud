package com.smartcare.cloud.service.impl;

import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.MessagePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket消息推送服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePushServiceImpl implements MessagePushService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String TOPIC_WARNINGS = "/topic/warnings";
    private static final String TOPIC_NOTIFICATIONS = "/topic/notifications";
    private static final String QUEUE_WARNING = "/queue/warning";
    private static final String QUEUE_NOTIFICATION = "/queue/notification";

    @Override
    public void pushWarningToAll(HealthWarning warning) {
        try {
            Map<String, Object> message = buildWarningMessage(warning);
            messagingTemplate.convertAndSend(TOPIC_WARNINGS, message);
            log.info("推送健康预警到所有用户,预警ID:{},级别:{}", 
                    warning.getId(), warning.getLevel());
        } catch (Exception e) {
            log.error("推送健康预警到所有用户失败", e);
        }
    }

    @Override
    public void pushWarningToUser(Long userId, HealthWarning warning) {
        try {
            Map<String, Object> message = buildWarningMessage(warning);
            messagingTemplate.convertAndSendToUser(
                userId.toString(), 
                QUEUE_WARNING, 
                message
            );
            log.info("推送健康预警到用户{},预警ID:{},级别:{}", 
                    userId, warning.getId(), warning.getLevel());
        } catch (Exception e) {
            log.error("推送健康预警到用户{}失败", userId, e);
        }
    }

    @Override
    public void pushWarningToFamily(Long elderlyId, HealthWarning warning) {
        // TODO: 查询老人的家属列表,逐个推送
        // 这里需要注入FamilyService查询家属关系
        try {
            Map<String, Object> message = buildWarningMessage(warning);
            // 暂时推送到topic,实际应该查询家属后逐个推送
            messagingTemplate.convertAndSend(
                "/topic/family/" + elderlyId + "/warnings", 
                message
            );
            log.info("推送健康预警到老人{}的家属,预警ID:{}", 
                    elderlyId, warning.getId());
        } catch (Exception e) {
            log.error("推送健康预警到老人{}的家属失败", elderlyId, e);
        }
    }

    @Override
    public void pushNotificationToAll(String notification) {
        try {
            Map<String, Object> message = buildNotificationMessage(notification);
            messagingTemplate.convertAndSend(TOPIC_NOTIFICATIONS, message);
            log.info("推送系统通知到所有用户:{}", notification);
        } catch (Exception e) {
            log.error("推送系统通知到所有用户失败", e);
        }
    }

    @Override
    public void pushNotificationToUser(Long userId, String notification) {
        try {
            Map<String, Object> message = buildNotificationMessage(notification);
            messagingTemplate.convertAndSendToUser(
                userId.toString(), 
                QUEUE_NOTIFICATION, 
                message
            );
            log.info("推送系统通知到用户{}:{}", userId, notification);
        } catch (Exception e) {
            log.error("推送系统通知到用户{}失败", userId, e);
        }
    }

    /**
     * 构建预警消息
     */
    private Map<String, Object> buildWarningMessage(HealthWarning warning) {
        Map<String, Object> message = new HashMap<>();
        message.put("id", warning.getId());
        message.put("elderlyId", warning.getElderlyId());
        message.put("elderlyName", warning.getElderlyName());
        message.put("type", warning.getType());
        message.put("level", warning.getLevel());
        message.put("levelName", getLevelName(warning.getLevel()));
        message.put("content", warning.getContent());
        message.put("createdTime", formatDateTime(warning.getCreatedTime()));
        message.put("timestamp", System.currentTimeMillis());
        message.put("messageType", "warning");
        return message;
    }

    /**
     * 构建通知消息
     */
    private Map<String, Object> buildNotificationMessage(String notification) {
        Map<String, Object> message = new HashMap<>();
        message.put("content", notification);
        message.put("timestamp", System.currentTimeMillis());
        message.put("createdTime", formatDateTime(LocalDateTime.now()));
        message.put("messageType", "notification");
        return message;
    }

    /**
     * 获取预警级别名称
     */
    private String getLevelName(Integer level) {
        if (level == null) {
            return "未知";
        }
        switch (level) {
            case 1:
                return "低";
            case 2:
                return "中";
            case 3:
                return "高";
            case 4:
                return "紧急";
            default:
                return "未知";
        }
    }

    /**
     * 格式化日期时间
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
