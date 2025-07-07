package com.smartcare.cloud.controller;

import java.util.List;

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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.entity.Role;
import com.smartcare.cloud.service.RoleService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统角色管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "系统角色管理", description = "系统角色管理相关接口")
@RestController
@RequestMapping("/api/system/roles")
@CrossOrigin
public class SystemRoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色列表
     */
    @Operation(summary = "获取角色列表", description = "获取系统所有角色列表")
    @GetMapping("/all")
    public ResponseResult<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return ResponseResult.success(roles);
        } catch (Exception e) {
            return ResponseResult.error("获取角色列表失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询角色列表
     */
    @Operation(summary = "分页查询角色", description = "分页查询角色列表")
    @GetMapping
    public ResponseResult<Page<Role>> getRolePage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        try {
            Page<Role> page = new Page<>(current, size);
            Page<Role> result = roleService.getRolePage(page, keyword, status);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.error("获取角色列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取角色详情
     */
    @Operation(summary = "获取角色详情", description = "根据ID获取角色详情")
    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable Long id) {
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return ResponseResult.error("角色不存在");
            }
            return ResponseResult.success(role);
        } catch (Exception e) {
            return ResponseResult.error("获取角色详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建角色
     */
    @Operation(summary = "创建角色", description = "创建新的角色")
    @PostMapping
    public ResponseResult<Role> createRole(@RequestBody Role role) {
        try {
            // 检查角色编码是否存在
            if (roleService.checkRoleCodeExists(role.getRoleCode(), null)) {
                return ResponseResult.error("角色编码已存在");
            }

            boolean success = roleService.save(role);
            if (success) {
                return ResponseResult.success(role, "创建角色成功");
            } else {
                return ResponseResult.error("创建角色失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("创建角色失败：" + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @Operation(summary = "更新角色", description = "更新角色信息")
    @PutMapping("/{id}")
    public ResponseResult<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            Role existRole = roleService.getById(id);
            if (existRole == null) {
                return ResponseResult.error("角色不存在");
            }

            // 检查角色编码是否存在（排除当前记录）
            if (roleService.checkRoleCodeExists(role.getRoleCode(), id)) {
                return ResponseResult.error("角色编码已存在");
            }

            role.setId(id);
            boolean success = roleService.updateById(role);
            if (success) {
                return ResponseResult.success(role, "更新角色成功");
            } else {
                return ResponseResult.error("更新角色失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新角色失败：" + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除指定角色")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return ResponseResult.error("角色不存在");
            }

            // 检查是否有用户使用该角色
            // TODO: 添加用户角色关联检查
            boolean success = roleService.removeById(id);
            if (success) {
                return ResponseResult.success(null, "删除角色成功");
            } else {
                return ResponseResult.error("删除角色失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除角色失败：" + e.getMessage());
        }
    }

    /**
     * 获取角色权限
     */
    @Operation(summary = "获取角色权限", description = "获取指定角色的所有权限")
    @GetMapping("/{id}/permissions")
    public ResponseResult<List<Permission>> getRolePermissions(@PathVariable Long id) {
        try {
            List<Permission> permissions = roleService.getRolePermissions(id);
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取角色权限失败：" + e.getMessage());
        }
    }

    /**
     * 获取角色权限ID列表
     */
    @Operation(summary = "获取角色权限ID", description = "获取指定角色的权限ID列表")
    @GetMapping("/{id}/permission-ids")
    public ResponseResult<List<Long>> getRolePermissionIds(@PathVariable Long id) {
        try {
            List<Long> permissionIds = roleService.getRolePermissionIds(id);
            return ResponseResult.success(permissionIds);
        } catch (Exception e) {
            return ResponseResult.error("获取角色权限ID失败：" + e.getMessage());
        }
    }

    /**
     * 分配角色权限
     */
    @Operation(summary = "分配角色权限", description = "为角色分配权限")
    @PostMapping("/{id}/permissions")
    public ResponseResult<Void> assignPermissions(@PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return ResponseResult.error("角色不存在");
            }

            roleService.batchAssignPermissions(id, permissionIds);
            return ResponseResult.success(null, "分配权限成功");
        } catch (Exception e) {
            return ResponseResult.error("分配权限失败：" + e.getMessage());
        }
    }

    /**
     * 检查角色编码是否存在
     */
    @Operation(summary = "检查角色编码", description = "检查角色编码是否存在")
    @GetMapping("/check-code")
    public ResponseResult<Boolean> checkRoleCode(
            @Parameter(description = "角色编码") @RequestParam String roleCode,
            @Parameter(description = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = roleService.checkRoleCodeExists(roleCode, excludeId);
            return ResponseResult.success(!exists, exists ? "角色编码已存在" : "角色编码可用");
        } catch (Exception e) {
            return ResponseResult.error("检查角色编码失败：" + e.getMessage());
        }
    }
}
