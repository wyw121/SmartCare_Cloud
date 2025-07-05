package com.smartcare.cloud.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.entity.User;

/**
 * 用户Mapper接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 查询用户及其角色信息
     *
     * @param userId 用户ID
     * @return 用户信息（包含角色）
     */
    User selectUserWithRoles(@Param("userId") Long userId);

    /**
     * 查询用户的权限信息
     *
     * @param userId 用户ID
     * @return 用户信息（包含权限）
     */
    User selectUserPermissions(@Param("userId") Long userId);

    /**
     * 根据角色编码查询用户列表
     *
     * @param roleCode 角色编码
     * @return 用户列表
     */
    List<User> selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 影响行数
     */
    int updateLoginInfo(@Param("userId") Long userId,
            @Param("loginTime") LocalDateTime loginTime,
            @Param("loginIp") String loginIp);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    Boolean checkUsernameExists(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    Boolean checkEmailExists(@Param("email") String email);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    Boolean checkPhoneExists(@Param("phone") String phone);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param username 用户名（可选）
     * @param realName 真实姓名（可选）
     * @param roleCode 角色编码（可选）
     * @param status 状态（可选）
     * @return 用户分页列表
     */
    Page<User> selectUserPage(Page<User> page,
            @Param("username") String username,
            @Param("realName") String realName,
            @Param("roleCode") String roleCode,
            @Param("status") Integer status);

    /**
     * 统计用户数量
     *
     * @param roleCode 角色编码
     * @param status 状态（可选）
     * @return 用户数量
     */
    Integer countByRoleCode(@Param("roleCode") String roleCode,
            @Param("status") Integer status);

}
