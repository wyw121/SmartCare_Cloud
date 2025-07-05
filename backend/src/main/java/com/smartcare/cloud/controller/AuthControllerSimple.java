package com.smartcare.cloud.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 简化的认证控制器（临时版本）
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "用户认证", description = "用户登录、注册相关接口")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthControllerSimple {

    @Operation(summary = "测试登录", description = "简单的登录测试接口")
    @PostMapping("/test-login")
    public ResponseResult<String> testLogin(@RequestBody java.util.Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if ("admin".equals(username) && "123456".equals(password)) {
            return ResponseResult.success("登录成功");
        } else {
            return ResponseResult.error("用户名或密码错误");
        }
    }

    @Operation(summary = "测试注册", description = "简单的注册测试接口")
    @PostMapping("/test-register")
    public ResponseResult<String> testRegister(@RequestBody java.util.Map<String, Object> registerData) {
        String username = (String) registerData.get("username");
        String password = (String) registerData.get("password");
        String realName = (String) registerData.get("realName");

        if (username != null && password != null && realName != null) {
            return ResponseResult.success("注册成功");
        } else {
            return ResponseResult.error("注册信息不完整");
        }
    }

}
