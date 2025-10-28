package com.smartcare.cloud.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.annotation.AuditLog;
import com.smartcare.cloud.dto.UserLoginDTO;
import com.smartcare.cloud.dto.UserRegisterDTO;
import com.smartcare.cloud.service.UserAuthService;
import com.smartcare.cloud.util.JwtUtil;
import com.smartcare.cloud.util.ResponseResult;
import com.smartcare.cloud.vo.LoginVO;
import com.smartcare.cloud.vo.UserInfoVO;
import com.smartcare.cloud.vo.UserRegisterVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户认证控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "用户认证", description = "用户登录、注册、权限验证相关接口")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "健康检查", description = "检查服务状态")
    @GetMapping("/health")
    public ResponseResult<String> health() {
        return ResponseResult.success("服务正常运行", "健康检查通过");
    }

    @AuditLog(module = "SYSTEM", type = "LOGIN", description = "用户登录: #loginDTO.username")
    @Operation(summary = "用户登录", description = "支持三类角色（管理员/医生/家属）的登录认证，返回JWT token")
    @PostMapping("/login")
    public ResponseResult<LoginVO> login(
            @Parameter(description = "登录信息，包含username(用户名)、password(密码)、roleCode(角色代码)", 
                      required = true,
                      example = "{\"username\": \"admin\", \"password\": \"123456\", \"roleCode\": \"admin\"}")
            @Validated @RequestBody UserLoginDTO loginDTO) {
        try {
            LoginVO loginVO = userAuthService.login(loginDTO);
            return ResponseResult.success(loginVO, "登录成功");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @AuditLog(module = "SYSTEM", type = "CREATE", description = "用户注册: #registerDTO.username")
    @Operation(summary = "用户注册", description = "支持三类角色的用户注册，需提供用户名、密码、角色等信息")
    @PostMapping("/register")
    public ResponseResult<UserRegisterVO> register(
            @Parameter(description = "注册信息，包含username(用户名)、password(密码)、realName(真实姓名)、roleCode(角色代码)等", 
                      required = true,
                      example = "{\"username\": \"doctor01\", \"password\": \"123456\", \"realName\": \"李医生\", \"roleCode\": \"doctor\"}")
            @Validated @RequestBody UserRegisterDTO registerDTO) {
        try {
            UserRegisterVO registerVO = userAuthService.register(registerDTO);
            return ResponseResult.success(registerVO, "注册成功");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Operation(summary = "获取当前用户信息", description = "根据token获取当前登录用户的详细信息")
    @GetMapping("/userinfo")
    public ResponseResult<UserInfoVO> getUserInfo(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseResult.error("未登录或token已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("token无效");
            }

            UserInfoVO userInfo = userAuthService.getUserInfo(userId);
            return ResponseResult.success(userInfo, "获取用户信息成功");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @AuditLog(module = "SYSTEM", type = "LOGOUT", description = "用户登出")
    @Operation(summary = "用户登出", description = "用户退出登录")
    @PostMapping("/logout")
    public ResponseResult<Void> logout(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token != null) {
                // 这里可以将token加入黑名单或Redis中标记为无效
                // 简化处理，由前端删除token
            }
            return ResponseResult.success(null, "退出登录成功");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Operation(summary = "刷新token", description = "刷新用户访问令牌")
    @PostMapping("/refresh")
    public ResponseResult<String> refreshToken(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseResult.error("token不能为空");
            }

            if (!jwtUtil.validateTokenFormat(token)) {
                return ResponseResult.error("token格式无效");
            }

            String newToken = jwtUtil.refreshToken(token);
            return ResponseResult.success(newToken, "token刷新成功");
        } catch (Exception e) {
            return ResponseResult.error("token刷新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "检查用户名可用性", description = "检查用户名是否已被使用")
    @GetMapping("/check-username")
    public ResponseResult<Boolean> checkUsername(@Parameter(description = "用户名") @RequestParam String username) {
        try {
            boolean exists = userAuthService.checkUsernameExists(username);
            return ResponseResult.success(!exists, exists ? "用户名已存在" : "用户名可用");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Operation(summary = "检查手机号可用性", description = "检查手机号是否已被使用")
    @GetMapping("/check-phone")
    public ResponseResult<Boolean> checkPhone(@Parameter(description = "手机号") @RequestParam String phone) {
        try {
            boolean exists = userAuthService.checkPhoneExists(phone);
            return ResponseResult.success(!exists, exists ? "手机号已存在" : "手机号可用");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Operation(summary = "检查邮箱可用性", description = "检查邮箱是否已被使用")
    @GetMapping("/check-email")
    public ResponseResult<Boolean> checkEmail(@Parameter(description = "邮箱") @RequestParam String email) {
        try {
            boolean exists = userAuthService.checkEmailExists(email);
            return ResponseResult.success(!exists, exists ? "邮箱已存在" : "邮箱可用");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Operation(summary = "验证token", description = "验证token的有效性")
    @GetMapping("/validate")
    public ResponseResult<Boolean> validateToken(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseResult.success(false, "token不存在");
            }

            boolean valid = jwtUtil.validateTokenFormat(token) && !jwtUtil.isTokenExpired(token);
            return ResponseResult.success(valid, valid ? "token有效" : "token无效或已过期");
        } catch (Exception e) {
            return ResponseResult.success(false, "token验证失败");
        }
    }

    /**
     * 从请求头中提取token
     *
     * @param request HTTP请求
     * @return token字符串
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
