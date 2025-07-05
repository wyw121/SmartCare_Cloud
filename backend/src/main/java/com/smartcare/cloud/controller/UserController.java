package com.smartcare.cloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.entity.User;
import com.smartcare.cloud.service.PermissionService;
import com.smartcare.cloud.service.RoleService;
import com.smartcare.cloud.service.UserAuthService;
import com.smartcare.cloud.util.ResponseResult;

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

    @Operation(summary = "统计用户数量", description = "按角色统计用户数量")
    @GetMapping("/statistics")
    public ResponseResult<Object> getUserStatistics() {
        try {
            java.util.Map<String, Object> statistics = new java.util.HashMap<>();
            statistics.put("adminCount", userAuthService.countUsersByRoleCode("admin"));
            statistics.put("doctorCount", userAuthService.countUsersByRoleCode("doctor"));
            statistics.put("familyCount", userAuthService.countUsersByRoleCode("family"));
            statistics.put("totalCount", userAuthService.getTotalUserCount());
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            return ResponseResult.error("获取用户统计失败：" + e.getMessage());
        }
    }

}
