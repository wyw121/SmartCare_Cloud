package com.smartcare.cloud.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.smartcare.cloud.event.ElderlyUpdatedEvent;

/**
 * 老人信息更新事件监听器
 * 
 * 使用事件驱动模式的优势：
 * 1. 解耦：监听器与发布者互不依赖
 * 2. 扩展性：可随时添加新的监听器
 * 3. 异步处理：不阻塞主流程
 * 
 * @author SmartCare Team
 * @date 2024-01-27
 */
@Component
public class ElderlyEventListener {

    private static final Logger log = LoggerFactory.getLogger(ElderlyEventListener.class);

    /**
     * 处理老人信息更新事件
     * 异步执行，不阻塞主流程
     */
    @Async
    @EventListener
    public void handleElderlyUpdated(ElderlyUpdatedEvent event) {
        try {
            log.info("接收到老人信息更新事件，操作类型：{}，老人ID：{}", 
                event.getOperationType(), 
                event.getElderly().getId());

            switch (event.getOperationType()) {
                case "CREATE":
                    handleCreate(event);
                    break;
                case "UPDATE":
                    handleUpdate(event);
                    break;
                case "DELETE":
                    handleDelete(event);
                    break;
                default:
                    log.warn("未知的操作类型：{}", event.getOperationType());
            }

        } catch (Exception e) {
            log.error("处理老人信息更新事件失败", e);
        }
    }

    /**
     * 处理创建事件
     */
    private void handleCreate(ElderlyUpdatedEvent event) {
        log.info("处理老人信息创建事件，老人姓名：{}", event.getElderly().getName());
        
        // 可以在这里执行：
        // 1. 发送欢迎通知
        // 2. 创建默认健康档案
        // 3. 记录操作日志
        // 4. 更新统计数据
    }

    /**
     * 处理更新事件
     */
    private void handleUpdate(ElderlyUpdatedEvent event) {
        log.info("处理老人信息更新事件，老人姓名：{}", event.getElderly().getName());
        
        // 可以在这里执行：
        // 1. 发送变更通知给家属
        // 2. 检查是否需要重新评估健康状况
        // 3. 记录变更历史
        // 4. 同步到其他系统
    }

    /**
     * 处理删除事件
     */
    private void handleDelete(ElderlyUpdatedEvent event) {
        log.info("处理老人信息删除事件，老人ID：{}", event.getElderly().getId());
        
        // 可以在这里执行：
        // 1. 清理相关数据
        // 2. 发送通知
        // 3. 归档历史记录
        // 4. 更新统计数据
    }
}
