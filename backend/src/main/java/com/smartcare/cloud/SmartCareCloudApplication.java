package com.smartcare.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 智慧医养大数据公共服务平台启动类
 *
 * @author GitHub Copilot
 * @date 2025-06-25
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.smartcare.cloud")
public class SmartCareCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCareCloudApplication.class, args);
        System.out.println("=================================");
        System.out.println("智慧医养平台后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/swagger-ui/");
        System.out.println("=================================");
    }

}
