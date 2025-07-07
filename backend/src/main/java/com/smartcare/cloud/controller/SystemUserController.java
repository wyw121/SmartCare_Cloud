package com.smartcare.cloud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.dto.UserCreateDTO;
import com.smartcare.cloud.dto.UserUpdateDTO;
import com.smartcare.cloud.entity.User;
import com.smartcare.cloud.service.UserAuthService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统用户管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "系统用户管理", description = "系统用户管理相关接口")
@RestController
@RequestMapping("/api/system/users")
@CrossOrigin
public class SystemUserController {

    @Autowired
    private UserAuthService userAuthService;

    /**
     * 获取用户列表（分页）
     */
    @Operation(summary = "获取用户列表", description = "获取系统用户列表，支持分页和条件查询")
    @GetMapping
    public ResponseResult<IPage<User>> getUserList(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "角色ID") @RequestParam(required = false) Long roleId) {
        try {
            Page<User> page = new Page<>(current, size);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

            // 添加查询条件
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like(User::getUsername, keyword)
                        .or()
                        .like(User::getRealName, keyword)
                        .or()
                        .like(User::getEmail, keyword)
                        .or()
                        .like(User::getPhone, keyword)
                );
            }

            if (status != null) {
                queryWrapper.eq(User::getStatus, status);
            }

            if (roleId != null) {
                // 这里可以根据角色ID查询，暂时用角色代码替代
                if (roleId == 1) {
                    queryWrapper.eq(User::getRoleCode, "admin");
                } else if (roleId == 2) {
                    queryWrapper.eq(User::getRoleCode, "doctor");
                } else if (roleId == 3) {
                    queryWrapper.eq(User::getRoleCode, "family");
                }
            }

            // 排序
            queryWrapper.orderByDesc(User::getCreateTime);

            IPage<User> result = userAuthService.page(page, queryWrapper);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.error("获取用户列表失败：" + e.getMessage());
        }
    }

    /**
     * 创建用户
     */
    @Operation(summary = "创建用户", description = "创建新的系统用户")
    @PostMapping
    public ResponseResult<User> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = new User();
            user.setUsername(userCreateDTO.getUsername());
            user.setRealName(userCreateDTO.getRealName());
            user.setEmail(userCreateDTO.getEmail());
            user.setPhone(userCreateDTO.getPhone());
            user.setGender(userCreateDTO.getGender());
            user.setRoleCode(userCreateDTO.getRoleCode());
            user.setStatus(userCreateDTO.getStatus() != null ? userCreateDTO.getStatus() : Integer.valueOf(1));

            // 设置角色名称
            String roleName = getRoleNameByCode(userCreateDTO.getRoleCode());
            user.setRoleName(roleName);

            // 设置默认密码
            user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm"); // 123456

            boolean success = userAuthService.save(user);
            if (success) {
                return ResponseResult.success(user, "用户创建成功");
            } else {
                return ResponseResult.error("用户创建失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("创建用户失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户", description = "更新系统用户信息")
    @PutMapping("/{id}")
    public ResponseResult<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User user = userAuthService.getById(id);
            if (user == null) {
                return ResponseResult.error("用户不存在");
            }

            user.setRealName(userUpdateDTO.getRealName());
            user.setEmail(userUpdateDTO.getEmail());
            user.setPhone(userUpdateDTO.getPhone());
            user.setGender(userUpdateDTO.getGender());
            user.setRoleCode(userUpdateDTO.getRoleCode());
            user.setStatus(userUpdateDTO.getStatus());

            // 更新角色名称
            String roleName = getRoleNameByCode(userUpdateDTO.getRoleCode());
            user.setRoleName(roleName);

            boolean success = userAuthService.updateById(user);
            if (success) {
                return ResponseResult.success(user, "用户更新成功");
            } else {
                return ResponseResult.error("用户更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新用户失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "删除指定的系统用户")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteUser(@PathVariable Long id) {
        try {
            boolean success = userAuthService.removeById(id);
            if (success) {
                return ResponseResult.success("用户删除成功");
            } else {
                return ResponseResult.error("用户删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户", description = "批量删除系统用户")
    @DeleteMapping("/batch")
    public ResponseResult<Void> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            boolean success = userAuthService.removeByIds(ids);
            if (success) {
                return ResponseResult.success("批量删除成功");
            } else {
                return ResponseResult.error("批量删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("批量删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 切换用户状态
     */
    @Operation(summary = "切换用户状态", description = "启用或禁用用户")
    @PutMapping("/{id}/status")
    public ResponseResult<Void> toggleUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            boolean success = userAuthService.updateUserStatus(id, status);
            if (success) {
                return ResponseResult.success("用户状态更新成功");
            } else {
                return ResponseResult.error("用户状态更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新用户状态失败：" + e.getMessage());
        }
    }

    /**
     * 重置用户密码
     */
    @Operation(summary = "重置用户密码", description = "重置用户密码为默认密码")
    @PutMapping("/{id}/password/reset")
    public ResponseResult<Void> resetUserPassword(@PathVariable Long id) {
        try {
            boolean success = userAuthService.resetUserPassword(id);
            if (success) {
                return ResponseResult.success("密码重置成功");
            } else {
                return ResponseResult.error("密码重置失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("重置密码失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户名是否可用
     */
    @Operation(summary = "检查用户名", description = "检查用户名是否已被使用")
    @GetMapping("/check-username")
    public ResponseResult<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userAuthService.checkUsernameExists(username);
            return ResponseResult.success(!exists);
        } catch (Exception e) {
            return ResponseResult.error("检查用户名失败：" + e.getMessage());
        }
    }

    /**
     * 检查邮箱是否可用
     */
    @Operation(summary = "检查邮箱", description = "检查邮箱是否已被使用")
    @GetMapping("/check-email")
    public ResponseResult<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userAuthService.checkEmailExists(email);
            return ResponseResult.success(!exists);
        } catch (Exception e) {
            return ResponseResult.error("检查邮箱失败：" + e.getMessage());
        }
    }

    /**
     * 根据角色代码获取角色名称
     */
    private String getRoleNameByCode(String roleCode) {
        switch (roleCode) {
            case "admin":
                return "系统管理员";
            case "doctor":
                return "医生";
            case "family":
                return "家属";
            default:
                return "普通用户";
        }
    }
}
