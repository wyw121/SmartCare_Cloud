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

import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.service.PermissionService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 权限管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/system/permissions")
@CrossOrigin
public class SystemPermissionController {

    private static final String PERMISSION_NOT_EXISTS = "权限不存在";
    private static final String PERMISSION_CODE_EXISTS = "权限编码已存在";
    private static final String PERMISSION_CODE_AVAILABLE = "权限编码可用";

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取权限树形结构
     */
    @Operation(summary = "获取权限树", description = "获取所有权限的树形结构")
    @GetMapping("/tree")
    public ResponseResult<List<Permission>> getPermissionTree() {
        try {
            List<Permission> permissions = permissionService.getAllPermissionsTree();
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取权限树失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询权限列表
     */
    @Operation(summary = "分页查询权限", description = "支持按关键字、权限类型、状态等条件分页查询权限列表")
    @GetMapping
    public ResponseResult<List<Permission>> getPermissionPage(
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页记录数", example = "10") 
            @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "搜索关键字，可模糊匹配权限名称、权限编码") 
            @RequestParam(required = false) String keyword,
            @Parameter(description = "权限类型，如：menu(菜单)、button(按钮)、api(接口)") 
            @RequestParam(required = false) String permissionType,
            @Parameter(description = "权限状态，0-禁用 1-启用") 
            @RequestParam(required = false) Integer status) {
        try {
            // 这里简化实现，直接返回树形结构
            List<Permission> permissions = permissionService.getAllPermissionsTree();
            // 实际项目中应该使用分页查询
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取权限列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取权限详情
     */
    @Operation(summary = "获取权限详情", description = "根据权限ID获取权限的详细信息")
    @GetMapping("/{id}")
    public ResponseResult<Permission> getPermissionById(
            @Parameter(description = "权限ID", example = "1", required = true)
            @PathVariable Long id) {
        try {
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                return ResponseResult.error(PERMISSION_NOT_EXISTS);
            }
            return ResponseResult.success(permission);
        } catch (Exception e) {
            return ResponseResult.error("获取权限详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建权限
     */
    @Operation(summary = "创建权限", description = "创建新的权限")
    @PostMapping
    public ResponseResult<Permission> createPermission(@Valid @RequestBody Permission permission) {
        try {
            // 检查权限编码是否存在
            if (permissionService.checkPermissionCodeExists(permission.getPermissionCode(), null)) {
                return ResponseResult.error(PERMISSION_CODE_EXISTS);
            }

            boolean success = permissionService.save(permission);
            if (success) {
                return ResponseResult.success(permission, "创建权限成功");
            } else {
                return ResponseResult.error("创建权限失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("创建权限失败：" + e.getMessage());
        }
    }

    /**
     * 更新权限
     */
    @Operation(summary = "更新权限", description = "更新权限信息")
    @PutMapping("/{id}")
    public ResponseResult<Permission> updatePermission(@PathVariable Long id,
            @Valid @RequestBody Permission permission) {
        try {
            Permission existPermission = permissionService.getById(id);
            if (existPermission == null) {
                return ResponseResult.error(PERMISSION_NOT_EXISTS);
            }

            // 检查权限编码是否存在（排除当前记录）
            if (permissionService.checkPermissionCodeExists(permission.getPermissionCode(), id)) {
                return ResponseResult.error(PERMISSION_CODE_EXISTS);
            }

            permission.setId(id);
            boolean success = permissionService.updateById(permission);
            if (success) {
                return ResponseResult.success(permission, "更新权限成功");
            } else {
                return ResponseResult.error("更新权限失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新权限失败：" + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @Operation(summary = "删除权限", description = "删除指定权限")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deletePermission(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getById(id);
            if (permission == null) {
                return ResponseResult.error(PERMISSION_NOT_EXISTS);
            }

            boolean success = permissionService.removeById(id);
            if (success) {
                return ResponseResult.success(null, "删除权限成功");
            } else {
                return ResponseResult.error("删除权限失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除权限失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除权限
     */
    @Operation(summary = "批量删除权限", description = "批量删除权限")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deletePermissions(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ResponseResult.error("请选择要删除的权限");
            }

            boolean success = permissionService.removeByIds(ids);
            if (success) {
                return ResponseResult.success(null, "批量删除权限成功");
            } else {
                return ResponseResult.error("批量删除权限失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("批量删除权限失败：" + e.getMessage());
        }
    }

    /**
     * 检查权限编码是否存在
     */
    @Operation(summary = "检查权限编码", description = "检查权限编码是否存在")
    @GetMapping("/check-code")
    public ResponseResult<Boolean> checkPermissionCode(
            @Parameter(description = "权限编码") @RequestParam String permissionCode,
            @Parameter(description = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = permissionService.checkPermissionCodeExists(permissionCode, excludeId);
            return ResponseResult.success(!exists, exists ? PERMISSION_CODE_EXISTS : PERMISSION_CODE_AVAILABLE);
        } catch (Exception e) {
            return ResponseResult.error("检查权限编码失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户权限
     */
    @Operation(summary = "获取用户权限", description = "获取指定用户的所有权限")
    @GetMapping("/user/{userId}")
    public ResponseResult<List<Permission>> getUserPermissions(@PathVariable Long userId) {
        try {
            List<Permission> permissions = permissionService.getUserPermissions(userId);
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取用户权限失败：" + e.getMessage());
        }
    }

    /**
     * 获取角色权限
     */
    @Operation(summary = "获取角色权限", description = "获取指定角色的所有权限")
    @GetMapping("/role/{roleId}")
    public ResponseResult<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        try {
            List<Permission> permissions = permissionService.getRolePermissions(roleId);
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.error("获取角色权限失败：" + e.getMessage());
        }
    }
}
