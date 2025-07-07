package com.smartcare.cloud.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.entity.Role;
import com.smartcare.cloud.mapper.RoleMapper;
import com.smartcare.cloud.service.RoleService;

/**
 * 角色服务实现类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getIsDeleted, 0)
                .eq(Role::getStatus, 1) // 只返回启用状态的角色
                .orderByAsc(Role::getSortOrder)
                .orderByDesc(Role::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        return roleMapper.selectRolePermissions(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除该角色的所有权限关联
        roleMapper.deleteRolePermissions(roleId);

        // 重新分配权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }
    }

    @Override
    public Page<Role> getRolePage(Page<Role> page, String keyword, Integer status) {
        return roleMapper.selectRolePage(page, keyword, status);
    }

    @Override
    public boolean checkRoleCodeExists(String roleCode, Long excludeId) {
        return roleMapper.countByRoleCode(roleCode, excludeId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePermissions(Long roleId, List<Long> permissionIds) {
        if (permissionIds != null && !permissionIds.isEmpty()) {
            // 这里需要实现删除指定权限的逻辑
            // 暂时使用简单实现
            for (Long permissionId : permissionIds) {
                roleMapper.deleteRolePermission(roleId, permissionId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAssignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除该角色的所有权限关联
        roleMapper.deleteRolePermissions(roleId);

        // 重新分配权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return roleMapper.selectRolePermissionIds(roleId);
    }

    @Override
    public boolean hasPermission(Long roleId, String permissionCode) {
        return roleMapper.checkRolePermission(roleId, permissionCode) > 0;
    }
}
