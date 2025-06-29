package com.smartcare.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from SmartCare Cloud Backend!";
    }

    @GetMapping("/status")
    public String status() {
        return "Backend service is running successfully!";
    }
}
