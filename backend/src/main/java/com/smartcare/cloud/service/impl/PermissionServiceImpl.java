package com.smartcare.cloud.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.mapper.PermissionMapper;
import com.smartcare.cloud.service.PermissionService;

/**
 * 权限服务实现类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAllPermissionsTree() {
        // 获取所有权限
        List<Permission> allPermissions = permissionMapper.selectAllPermissions();
        // 构建树形结构
        return buildPermissionTree(allPermissions);
    }

    @Override
    public List<Permission> getUserPermissions(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectUserPermissions(userId);
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        return permissionMapper.selectRolePermissions(roleId);
    }

    @Override
    public Permission getPermissionByCode(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }
        return permissionMapper.selectByPermissionCode(permissionCode);
    }

    @Override
    public boolean checkPermissionCodeExists(String permissionCode, Long excludeId) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        int count = permissionMapper.countByPermissionCode(permissionCode, excludeId);
        return count > 0;
    }

    @Override
    public List<Permission> buildPermissionTree(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取根节点（parentId为null或0）
        List<Permission> rootPermissions = permissions.stream()
                .filter(permission -> permission.getParentId() == null || permission.getParentId() == 0)
                .collect(Collectors.toList());

        // 为每个根节点构建子树
        for (Permission rootPermission : rootPermissions) {
            buildChildren(rootPermission, permissions);
        }

        return rootPermissions;
    }

    /**
     * 递归构建子权限
     *
     * @param parent 父权限
     * @param allPermissions 所有权限列表
     */
    private void buildChildren(Permission parent, List<Permission> allPermissions) {
        List<Permission> children = allPermissions.stream()
                .filter(permission -> Objects.equals(permission.getParentId(), parent.getId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parent.setChildren(children);
            // 递归构建子节点的子树
            for (Permission child : children) {
                buildChildren(child, allPermissions);
            }
        }
    }
}
