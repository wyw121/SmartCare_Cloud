package com.smartcare.cloud.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis Plus配置类
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * MyBatis Plus全局配置
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();

        // 逻辑删除配置
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");

        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    /**
     * 自动填充处理器
     */
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            // 插入时自动填充创建时间和更新时间
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            // 更新时自动填充更新时间（包括逻辑删除时的更新）
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
