package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.entity.Role;

/**
 * 角色服务接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<Role> getAllRoles();

    /**
     * 根据角色编码获取角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role getRoleByCode(String roleCode);

    /**
     * 获取角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getRolePermissions(Long roleId);

    /**
     * 分配角色权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 分页查询角色列表
     *
     * @param page 分页参数
     * @param keyword 关键字
     * @param status 状态
     * @return 角色列表
     */
    Page<Role> getRolePage(Page<Role> page, String keyword, Integer status);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkRoleCodeExists(String roleCode, Long excludeId);
}
