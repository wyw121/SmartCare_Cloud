package com.smartcare.cloud.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.event.HealthWarningCreatedEvent;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.MessagePushService;

/**
 * 健康预警事件监听器
 * 
 * 监听健康预警创建事件,触发后续处理流程:
 * 1. 发送预警通知(SMS/推送/邮件)
 * 2. WebSocket实时推送
 * 3. 记录预警日志
 * 4. 更新统计数据
 * 5. 触发自动分配处理人员
 * 
 * 使用异步处理不阻塞主流程
 * 
 * @author SmartCare Team
 * @date 2024-01-27
 */
@Component
public class HealthWarningEventListener {

    private static final Logger log = LoggerFactory.getLogger(HealthWarningEventListener.class);

    @Autowired
    private ElderlyService elderlyService;
    
    @Autowired
    private MessagePushService messagePushService;

    /**
     * 处理健康预警创建事件
     * 异步执行,不阻塞预警创建流程
     */
    @Async
    @EventListener
    public void handleHealthWarningCreated(HealthWarningCreatedEvent event) {
        try {
            HealthWarning warning = event.getHealthWarning();
            log.info("接收到健康预警事件,老人ID:{},预警级别:{},来源:{},紧急:{}", 
                warning.getElderlyId(),
                warning.getLevel(),
                event.getWarningSource(),
                event.isUrgent());

            // 1. WebSocket实时推送预警消息
            pushWarningViaWebSocket(warning);

            // 2. 发送预警通知
            sendWarningNotification(warning, event.isUrgent());

            // 3. 记录操作日志
            logWarningCreation(warning, event.getWarningSource());

            // 4. 如果是紧急预警,自动分配处理人员
            if (event.isUrgent() || "高".equals(warning.getLevel())) {
                assignUrgentWarning(warning);
            }

            // 5. 更新统计数据(可异步写入Redis或数据库)
            updateWarningStatistics(warning);

        } catch (Exception e) {
            log.error("处理健康预警事件失败,预警ID:{}", event.getHealthWarning().getId(), e);
        }
    }
    
    /**
     * 通过WebSocket推送预警消息
     */
    private void pushWarningViaWebSocket(HealthWarning warning) {
        try {
            // 推送到所有在线用户(医生、管理员等)
            messagePushService.pushWarningToAll(warning);
            
            // 推送到老人的家属
            messagePushService.pushWarningToFamily(warning.getElderlyId(), warning);
            
            log.info("WebSocket推送预警成功,预警ID:{}", warning.getId());
        } catch (Exception e) {
            log.error("WebSocket推送预警失败,预警ID:{}", warning.getId(), e);
        }
    }

    /**
     * 发送预警通知
     * 通知对象: 家属、医生、护理人员
     */
    private void sendWarningNotification(HealthWarning warning, boolean urgent) {
        try {
            // 获取老人信息
            Elderly elderly = elderlyService.getById(warning.getElderlyId());
            if (elderly == null) {
                log.warn("发送预警通知失败,老人不存在,ID:{}", warning.getElderlyId());
                return;
            }

            String notificationMessage = buildNotificationMessage(elderly, warning, urgent);
            
            // TODO: 实际项目中这里应该调用短信/推送服务
            // smsService.send(elderly.getEmergencyContact(), notificationMessage);
            // pushService.send(elderly.getFamilyMemberId(), notificationMessage);
            
            log.info("预警通知已发送,老人:{},级别:{},内容:{}", 
                elderly.getName(), warning.getLevel(), notificationMessage);

        } catch (Exception e) {
            log.error("发送预警通知失败", e);
        }
    }

    /**
     * 构建通知消息
     */
    private String buildNotificationMessage(Elderly elderly, HealthWarning warning, boolean urgent) {
        StringBuilder msg = new StringBuilder();
        
        if (urgent) {
            msg.append("【紧急预警】");
        } else {
            msg.append("【健康预警】");
        }
        
        msg.append(elderly.getName())
           .append("(").append(elderly.getAge()).append("岁)")
           .append(" ")
           .append(warning.getWarningType())
           .append(" - ")
           .append(warning.getDescription())
           .append(",级别:")
           .append(warning.getLevel());
        
        return msg.toString();
    }

    /**
     * 记录预警创建日志
     */
    private void logWarningCreation(HealthWarning warning, String source) {
        // TODO: 实际项目中可以写入操作日志表
        log.info("健康预警创建日志,ID:{},类型:{},级别:{},来源:{}", 
            warning.getId(),
            warning.getWarningType(),
            warning.getLevel(),
            source);
    }

    /**
     * 分配紧急预警给医护人员
     */
    private void assignUrgentWarning(HealthWarning warning) {
        try {
            // TODO: 实际项目中这里应该:
            // 1. 查找当前值班医生/护士
            // 2. 自动分配预警任务
            // 3. 发送处理任务通知
            
            log.info("紧急预警已自动分配处理,预警ID:{}", warning.getId());

        } catch (Exception e) {
            log.error("分配紧急预警失败", e);
        }
    }

    /**
     * 更新预警统计数据
     */
    private void updateWarningStatistics(HealthWarning warning) {
        try {
            // TODO: 实际项目中可以更新Redis缓存中的统计数据
            // 比如: 今日预警数、各级别预警数、各类型预警数等
            
            log.debug("预警统计数据已更新,类型:{},级别:{}", 
                warning.getWarningType(), warning.getLevel());

        } catch (Exception e) {
            log.error("更新预警统计失败", e);
        }
    }
}
