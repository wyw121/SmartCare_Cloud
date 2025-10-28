package com.smartcare.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.base-path:./uploads}")
    private String uploadBasePath;

    /**
     * 静态资源处理
     * 配置文件访问路径映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件访问路径映射
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadBasePath + "/");
    }
}
