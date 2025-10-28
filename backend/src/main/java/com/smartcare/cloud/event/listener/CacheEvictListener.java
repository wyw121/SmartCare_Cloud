package com.smartcare.cloud.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.smartcare.cloud.event.ElderlyUpdatedEvent;
import com.smartcare.cloud.event.HealthWarningCreatedEvent;

/**
 * 缓存清除监听器
 * 
 * 功能：监听业务事件，自动清除相关缓存
 * 策略：事件驱动的缓存失效，保证数据一致性
 * 
 * @author SmartCare Team
 * @date 2024-10-28
 */
@Component
public class CacheEvictListener {

    private static final Logger log = LoggerFactory.getLogger(CacheEvictListener.class);

    @Autowired
    private CacheManager cacheManager;

    /**
     * 监听老人信息更新事件
     * 清除相关的缓存
     */
    @EventListener
    public void onElderlyUpdated(ElderlyUpdatedEvent event) {
        try {
            Long elderlyId = event.getElderly().getId();
            String operationType = event.getOperationType();
            
            log.info("老人信息{}，开始清除相关缓存，elderlyId: {}", operationType, elderlyId);

            // 1. 清除老人详情缓存
            evictCache("elderly", "id:" + elderlyId);
            
            // 2. 清除统计缓存（因为老人数据变化会影响统计）
            evictCacheAll("statistics");
            evictCacheAll("dashboard");
            
            // 3. 如果是健康状态更新，清除健康相关缓存
            evictCacheAll("healthRecord");
            
            log.info("老人信息缓存清除完成，elderlyId: {}", elderlyId);

        } catch (Exception e) {
            log.error("清除老人信息缓存失败", e);
        }
    }

    /**
     * 监听健康预警创建事件
     * 清除相关的缓存
     */
    @EventListener
    public void onHealthWarningCreated(HealthWarningCreatedEvent event) {
        try {
            log.info("健康预警创建，开始清除相关缓存");

            // 1. 清除健康预警列表缓存
            evictCacheAll("healthWarning");
            
            // 2. 清除统计缓存
            evictCacheAll("statistics");
            evictCacheAll("dashboard");
            
            log.info("健康预警缓存清除完成");

        } catch (Exception e) {
            log.error("清除健康预警缓存失败", e);
        }
    }

    /**
     * 清除指定缓存的指定key
     */
    private void evictCache(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            log.debug("清除缓存: {} -> {}", cacheName, key);
        }
    }

    /**
     * 清除指定缓存的所有数据
     */
    private void evictCacheAll(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.debug("清除所有缓存: {}", cacheName);
        }
    }
}
