package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.dto.UserLoginDTO;
import com.smartcare.cloud.dto.UserRegisterDTO;
import com.smartcare.cloud.entity.User;
import com.smartcare.cloud.mapper.RoleMapper;
import com.smartcare.cloud.mapper.UserMapper;
import com.smartcare.cloud.service.UserAuthService;
import com.smartcare.cloud.util.JwtUtil;
import com.smartcare.cloud.util.PasswordUtil;
import com.smartcare.cloud.vo.LoginVO;
import com.smartcare.cloud.vo.UserInfoVO;
import com.smartcare.cloud.vo.UserRegisterVO;

/**
 * 用户认证服务实现类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserMapper, User> implements UserAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    @Transactional
    public LoginVO login(UserLoginDTO loginDTO) {
        // 参数校验
        if (loginDTO == null || !StringUtils.hasText(loginDTO.getUsername())
                || !StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 验证密码
        if (!passwordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleCode());

        // 更新登录信息 - 临时禁用，字段不存在
        // updateLoginInfo(user.getId(), null);
        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(7200L); // 2小时

        // 用户信息
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setName(user.getRealName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setRoleCode(user.getRoleCode());
        userInfo.setRoleName(user.getRoleName());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setDepartment(user.getDepartment());

        loginVO.setUserInfo(userInfo);

        return loginVO;
    }

    @Override
    @Transactional
    public UserRegisterVO register(UserRegisterDTO registerDTO) {
        // 参数校验
        if (registerDTO == null || !StringUtils.hasText(registerDTO.getUsername())
                || !StringUtils.hasText(registerDTO.getPassword())
                || !StringUtils.hasText(registerDTO.getRealName())
                || !StringUtils.hasText(registerDTO.getRoleCode())) {
            throw new RuntimeException("必填信息不能为空");
        }

        // 检查用户名是否已存在
        if (!isUsernameAvailable(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (StringUtils.hasText(registerDTO.getPhone()) && !isPhoneAvailable(registerDTO.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(registerDTO.getEmail()) && !isEmailAvailable(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordUtil.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setGender(registerDTO.getGender());
        user.setPhone(registerDTO.getPhone());
        
        // 修复：将空字符串转换为null，避免数据库唯一约束冲突
        user.setEmail(StringUtils.hasText(registerDTO.getEmail()) ? registerDTO.getEmail() : null);
        
        user.setRoleCode(registerDTO.getRoleCode());
        user.setDepartment(registerDTO.getDepartment());
        user.setDescription(registerDTO.getDescription());
        user.setStatus(1);
        // user.setLoginCount(0); // 暂时注释掉，表中没有该字段
        user.setIsDeleted(0);

        // 设置角色名称
        String roleName;
        switch (registerDTO.getRoleCode()) {
            case "admin":
                roleName = "系统管理员";
                break;
            case "doctor":
                roleName = "医生";
                break;
            case "family":
                roleName = "家属";
                break;
            default:
                roleName = "普通用户";
                break;
        }
        user.setRoleName(roleName);

        // 根据角色设置专属字段
        if ("doctor".equals(registerDTO.getRoleCode())) {
            user.setDoctorTitle(registerDTO.getDoctorTitle());
            user.setDoctorSpeciality(registerDTO.getDoctorSpeciality());
            user.setDoctorLicenseNumber(registerDTO.getDoctorLicenseNumber());
            user.setDoctorExperienceYears(registerDTO.getDoctorExperienceYears());
        } else if ("family".equals(registerDTO.getRoleCode())) {
            user.setFamilyRelationship(registerDTO.getFamilyRelationship());
            user.setFamilyElderlyIds(registerDTO.getFamilyElderlyIds());
        }

        // 保存用户
        userMapper.insertUser(user);

        // 构建返回对象
        UserRegisterVO registerVO = new UserRegisterVO();
        registerVO.setUserId(user.getId());
        registerVO.setUsername(user.getUsername());
        registerVO.setName(user.getRealName());
        registerVO.setRoleCode(user.getRoleCode());
        registerVO.setRoleName(user.getRoleName());
        registerVO.setStatus("success");
        registerVO.setMessage("注册成功");

        return registerVO;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo);

        // 获取用户权限
        List<String> permissions = getUserPermissions(userId);
        userInfo.setPermissions(permissions);

        return userInfo;
    }

    @Override
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.selectById(userId);
    }

    @Override
    public List<User> getUsersByRoleCode(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return Arrays.asList();
        }
        return userMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<User> getAllUsers() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIsDeleted, 0);
        queryWrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return false;
        }

        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional
    public boolean resetUserPassword(Long userId) {
        if (userId == null) {
            return false;
        }

        String defaultPassword = "123456";
        String encodedPassword = passwordUtil.encode(defaultPassword);

        User user = new User();
        user.setId(userId);
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return Arrays.asList();
        }

        User user = userMapper.selectUserPermissions(userId);
        if (user != null && user.getPermissions() != null) {
            return user.getPermissions().stream()
                    .map(permission -> permission.getPermissionCode())
                    .collect(java.util.stream.Collectors.toList());
        }
        return Arrays.asList();
    }

    @Override
    public int countUsersByRoleCode(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return 0;
        }
        Integer count = userMapper.countByRoleCode(roleCode, 1);
        return count == null ? 0 : count;
    }

    @Override
    public int getTotalUserCount() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIsDeleted, 0);
        queryWrapper.eq(User::getStatus, 1);
        return Math.toIntExact(userMapper.selectCount(queryWrapper));
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        Boolean exists = userMapper.checkUsernameExists(username);
        return exists == null || !exists;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        if (!StringUtils.hasText(email)) {
            return true;
        }
        Boolean exists = userMapper.checkEmailExists(email);
        return exists == null || !exists;
    }

    @Override
    public boolean isPhoneAvailable(String phone) {
        if (!StringUtils.hasText(phone)) {
            return true;
        }
        Boolean exists = userMapper.checkPhoneExists(phone);
        return exists == null || !exists;
    }

    @Override
    public Page<User> getUserPage(Page<User> page, String username, String realName, String roleCode, Integer status) {
        return userMapper.selectUserPage(page, username, realName, roleCode, status);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        if (userId == null || !StringUtils.hasText(permissionCode)) {
            return false;
        }

        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }

    @Override
    @Transactional
    public void updateLoginInfo(Long userId, String loginIp) {
        // 临时禁用，字段不存在
        /*
        if (userId != null) {
            userMapper.updateLoginInfo(userId, LocalDateTime.now(), loginIp);
        }
         */
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        Boolean exists = userMapper.checkUsernameExists(username);
        return exists != null && exists;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        Boolean exists = userMapper.checkPhoneExists(phone);
        return exists != null && exists;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Boolean exists = userMapper.checkEmailExists(email);
        return exists != null && exists;
    }

}
