package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartcare.cloud.dto.PasswordChangeDTO;
import com.smartcare.cloud.dto.UserUpdateDTO;
import com.smartcare.cloud.entity.User;
import com.smartcare.cloud.service.PermissionService;
import com.smartcare.cloud.service.RoleService;
import com.smartcare.cloud.service.UserAuthService;
import com.smartcare.cloud.util.JwtUtil;
import com.smartcare.cloud.util.ResponseResult;
import com.smartcare.cloud.vo.UserInfoVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/info")
    public ResponseResult<UserInfoVO> getCurrentUserInfo(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            UserInfoVO userInfo = userAuthService.getUserInfo(userId);
            if (userInfo != null) {
                return ResponseResult.success(userInfo);
            } else {
                return ResponseResult.error("用户不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新当前用户信息
     */
    @Operation(summary = "更新当前用户信息", description = "更新当前登录用户的基本信息")
    @PutMapping("/info")
    public ResponseResult<Void> updateCurrentUserInfo(@RequestBody UserUpdateDTO updateDTO, HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            updateDTO.setId(userId);
            boolean success = userAuthService.updateUserInfo(updateDTO);
            if (success) {
                return ResponseResult.success("用户信息更新成功");
            } else {
                return ResponseResult.error("用户信息更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 修改当前用户密码
     */
    @Operation(summary = "修改当前用户密码", description = "修改当前登录用户的密码")
    @PutMapping("/password")
    public ResponseResult<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO, HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            passwordChangeDTO.setUserId(userId);
            boolean success = userAuthService.changePassword(passwordChangeDTO);
            if (success) {
                return ResponseResult.success("密码修改成功");
            } else {
                return ResponseResult.error("密码修改失败，请检查原密码是否正确");
            }
        } catch (Exception e) {
            return ResponseResult.error("修改密码失败：" + e.getMessage());
        }
    }

    /**
     * 上传用户头像
     */
    @Operation(summary = "上传用户头像", description = "上传当前登录用户的头像")
    @PutMapping("/avatar")
    public ResponseResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            String avatarUrl = userAuthService.uploadAvatar(userId, file);
            if (avatarUrl != null) {
                Map<String, String> result = new HashMap<>();
                result.put("avatarUrl", avatarUrl);
                return ResponseResult.success(result);
            } else {
                return ResponseResult.error("头像上传失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("上传头像失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户统计数据
     */
    @Operation(summary = "获取用户统计数据", description = "获取当前登录用户的统计数据")
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Object>> getUserStatistics(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            Map<String, Object> statistics = userAuthService.getUserStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            return ResponseResult.error("获取用户统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户活动日志
     */
    @Operation(summary = "获取用户活动日志", description = "获取当前登录用户的最近活动记录")
    @GetMapping("/activities")
    public ResponseResult<List<Map<String, Object>>> getUserActivities(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return ResponseResult.error("用户未登录或token无效");
            }

            List<Map<String, Object>> activities = userAuthService.getUserActivities(userId);
            return ResponseResult.success(activities);
        } catch (Exception e) {
            return ResponseResult.error("获取用户活动失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户列表", description = "根据角色编码获取用户列表")
    @GetMapping("/list")
    public ResponseResult<List<User>> getUserList(@RequestParam(value = "roleCode", required = false) String roleCode) {
        try {
            List<User> users;
            if (roleCode != null && !roleCode.isEmpty()) {
                users = userAuthService.getUsersByRoleCode(roleCode);
            } else {
                users = userAuthService.getAllUsers();
            }
            return ResponseResult.success(users);
        } catch (Exception e) {
            return ResponseResult.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情")
    @GetMapping("/{userId}")
    public ResponseResult<User> getUserById(@PathVariable Long userId) {
        try {
            User user = userAuthService.getUserById(userId);
            if (user != null) {
                return ResponseResult.success(user);
            } else {
                return ResponseResult.error("用户不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取用户详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    @PutMapping("/{userId}/status")
    public ResponseResult<Void> updateUserStatus(@PathVariable Long userId,
            @RequestParam Integer status) {
        try {
            boolean success = userAuthService.updateUserStatus(userId, status);
            if (success) {
                return ResponseResult.success("用户状态更新成功");
            } else {
                return ResponseResult.error("用户状态更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新用户状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "重置用户密码", description = "重置用户密码为默认密码")
    @PutMapping("/{userId}/password/reset")
    public ResponseResult<Void> resetUserPassword(@PathVariable Long userId) {
        try {
            boolean success = userAuthService.resetUserPassword(userId);
            if (success) {
                return ResponseResult.success("密码重置成功");
            } else {
                return ResponseResult.error("密码重置失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("重置密码失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户权限", description = "获取用户的所有权限")
    @GetMapping("/{userId}/permissions")
    public ResponseResult<List<String>> getUserPermissions(@PathVariable Long userId) {
        try {
            List<String> permissions = userAuthService.getUserPermissions(userId);
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取用户权限失败：" + e.getMessage());
        }
    }

}
