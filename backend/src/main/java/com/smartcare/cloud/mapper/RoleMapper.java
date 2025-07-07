package com.smartcare.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.entity.Permission;
import com.smartcare.cloud.entity.Role;

/**
 * 角色Mapper接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode} AND is_deleted = 0")
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 获取角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Select("SELECT p.* FROM sys_permission p "
            + "JOIN sys_role_permission rp ON p.id = rp.permission_id "
            + "WHERE rp.role_id = #{roleId} AND p.is_deleted = 0 AND p.status = 1 "
            + "ORDER BY p.sort_order")
    List<Permission> selectRolePermissions(@Param("roleId") Long roleId);

    /**
     * 删除角色权限关联
     *
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissions(@Param("roleId") Long roleId);

    /**
     * 批量插入角色权限关联
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 分页查询角色列表
     *
     * @param page 分页参数
     * @param keyword 关键字
     * @param status 状态
     * @return 角色列表
     */
    Page<Role> selectRolePage(Page<Role> page, @Param("keyword") String keyword, @Param("status") Integer status);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM sys_role WHERE role_code = #{roleCode} AND is_deleted = 0 "
            + "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int countByRoleCode(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);

    /**
     * 删除单个角色权限关联
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    void deleteRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 获取角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    @Select("SELECT permission_id FROM sys_role_permission WHERE role_id = #{roleId}")
    List<Long> selectRolePermissionIds(@Param("roleId") Long roleId);

    /**
     * 检查角色是否拥有指定权限
     *
     * @param roleId 角色ID
     * @param permissionCode 权限编码
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM sys_role_permission rp "
            + "JOIN sys_permission p ON rp.permission_id = p.id "
            + "WHERE rp.role_id = #{roleId} AND p.permission_code = #{permissionCode} "
            + "AND p.is_deleted = 0 AND p.status = 1")
    int checkRolePermission(@Param("roleId") Long roleId, @Param("permissionCode") String permissionCode);
}
