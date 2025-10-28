package com.smartcare.cloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 支持STOMP协议的WebSocket消息代理
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理
     * - /topic: 用于广播消息(一对多)
     * - /queue: 用于点对点消息(一对一)
     * - /app: 应用程序消息前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用简单代理,用于广播和点对点消息
        registry.enableSimpleBroker("/topic", "/queue");
        
        // 设置应用程序消息前缀
        registry.setApplicationDestinationPrefixes("/app");
        
        // 设置用户目的地前缀(点对点消息)
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 注册STOMP端点
     * 客户端连接WebSocket的入口点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册WebSocket端点,允许跨域访问
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();  // 使用SockJS作为WebSocket的降级选项
    }
}
