package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.dto.UserLoginDTO;
import com.smartcare.cloud.dto.UserRegisterDTO;
import com.smartcare.cloud.entity.User;
import com.smartcare.cloud.vo.LoginVO;
import com.smartcare.cloud.vo.UserInfoVO;
import com.smartcare.cloud.vo.UserRegisterVO;

/**
 * 用户认证服务接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface UserAuthService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(UserLoginDTO loginDTO);

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    UserRegisterVO register(UserRegisterDTO registerDTO);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 根据角色编码获取用户列表
     *
     * @param roleCode 角色编码
     * @return 用户列表
     */
    List<User> getUsersByRoleCode(String roleCode);

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @return 重置结果
     */
    boolean resetUserPassword(Long userId);

    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 统计角色用户数量
     *
     * @param roleCode 角色编码
     * @return 用户数量
     */
    int countUsersByRoleCode(String roleCode);

    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    int getTotalUserCount();

    /**
     * 验证用户名是否可用
     *
     * @param username 用户名
     * @return 是否可用
     */
    boolean isUsernameAvailable(String username);

    /**
     * 验证邮箱是否可用
     *
     * @param email 邮箱
     * @return 是否可用
     */
    boolean isEmailAvailable(String email);

    /**
     * 验证手机号是否可用
     *
     * @param phone 手机号
     * @return 是否可用
     */
    boolean isPhoneAvailable(String phone);

    /**
     * 分页查询用户
     *
     * @param page 分页参数
     * @param username 用户名（可选）
     * @param realName 真实姓名（可选）
     * @param roleCode 角色编码（可选）
     * @param status 状态（可选）
     * @return 用户分页列表
     */
    Page<User> getUserPage(Page<User> page, String username, String realName, String roleCode, Integer status);

    /**
     * 检查用户是否具有权限
     *
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否具有权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    void updateLoginInfo(Long userId, String loginIp);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);

    /**
     * 更新用户基本信息
     *
     * @param updateDTO 更新信息
     * @return 更新结果
     */
    boolean updateUserInfo(com.smartcare.cloud.dto.UserUpdateDTO updateDTO);

    /**
     * 修改用户密码
     *
     * @param passwordChangeDTO 密码修改信息
     * @return 修改结果
     */
    boolean changePassword(com.smartcare.cloud.dto.PasswordChangeDTO passwordChangeDTO);

    /**
     * 上传用户头像
     *
     * @param userId 用户ID
     * @param file 头像文件
     * @return 头像URL
     */
    String uploadAvatar(Long userId, org.springframework.web.multipart.MultipartFile file);

    /**
     * 获取用户统计数据
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    java.util.Map<String, Object> getUserStatistics(Long userId);

    /**
     * 获取用户活动记录
     *
     * @param userId 用户ID
     * @return 活动记录
     */
    java.util.List<java.util.Map<String, Object>> getUserActivities(Long userId);

}
