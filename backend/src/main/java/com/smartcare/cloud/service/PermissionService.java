package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.Permission;

/**
 * 权限服务接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取所有权限列表（树形结构）
     *
     * @return 权限列表
     */
    List<Permission> getAllPermissionsTree();

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getUserPermissions(Long userId);

    /**
     * 获取角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getRolePermissions(Long roleId);

    /**
     * 根据权限编码获取权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission getPermissionByCode(String permissionCode);

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkPermissionCodeExists(String permissionCode, Long excludeId);

    /**
     * 构建权限树
     *
     * @param permissions 权限列表
     * @return 权限树
     */
    List<Permission> buildPermissionTree(List<Permission> permissions);
}
