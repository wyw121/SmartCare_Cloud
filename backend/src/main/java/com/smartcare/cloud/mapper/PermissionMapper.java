package com.smartcare.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.Permission;

/**
 * 权限Mapper接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    @Select("SELECT * FROM sys_permission WHERE permission_code = #{permissionCode} AND is_deleted = 0")
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT DISTINCT p.* FROM sys_permission p "
            + "JOIN sys_role_permission rp ON p.id = rp.permission_id "
            + "JOIN sys_role r ON rp.role_id = r.id "
            + "JOIN sys_user_role ur ON r.id = ur.role_id "
            + "WHERE ur.user_id = #{userId} AND p.is_deleted = 0 AND p.status = 1 "
            + "AND r.is_deleted = 0 AND r.status = 1 "
            + "ORDER BY p.sort_order")
    List<Permission> selectUserPermissions(@Param("userId") Long userId);

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
     * 获取所有权限列表
     *
     * @return 权限列表
     */
    @Select("SELECT * FROM sys_permission WHERE is_deleted = 0 ORDER BY sort_order")
    List<Permission> selectAllPermissions();

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM sys_permission WHERE permission_code = #{permissionCode} AND is_deleted = 0 "
            + "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int countByPermissionCode(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);
}
