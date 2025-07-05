package com.smartcare.cloud.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 系统用户实体类 支持三类角色：管理员(admin)、医生(doctor)、家属(family)
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "系统用户实体")
@TableName("sys_user")
public class User {

    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    @JsonIgnore
    private String password;

    @Schema(description = "真实姓名")
    @TableField("real_name")
    private String realName;

    @Schema(description = "性别：1-男，0-女")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "头像URL")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "部门")
    @TableField("department")
    private String department;

    @Schema(description = "角色编码")
    @TableField("role_code")
    private String roleCode;

    @Schema(description = "角色名称")
    @TableField("role_name")
    private String roleName;

    // 医生专用字段
    @Schema(description = "医生职称")
    @TableField("doctor_title")
    private String doctorTitle;

    @Schema(description = "医生专业特长")
    @TableField("doctor_speciality")
    private String doctorSpeciality;

    @Schema(description = "医生执业证书号")
    @TableField("doctor_license_number")
    private String doctorLicenseNumber;

    @Schema(description = "从业年限")
    @TableField("doctor_experience_years")
    private Integer doctorExperienceYears;

    // 家属专用字段
    @Schema(description = "与老人关系")
    @TableField("family_relationship")
    private String familyRelationship;

    @Schema(description = "关联的老人ID列表，逗号分隔")
    @TableField("family_elderly_ids")
    private String familyElderlyIds;

    @Schema(description = "状态：1-启用，0-禁用")
    @TableField("status")
    private Integer status;

    @Schema(description = "登录次数")
    @TableField("login_count")
    private Integer loginCount;

    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    @TableField("last_login_ip")
    private String lastLoginIp;

    @Schema(description = "备注描述")
    @TableField("description")
    private String description;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建者ID")
    @TableField("create_by")
    private Long createBy;

    @Schema(description = "更新者ID")
    @TableField("update_by")
    private Long updateBy;

    @Schema(description = "是否删除：0-未删除，1-已删除")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    // 关联对象（不存储到数据库）
    @Schema(description = "用户角色列表")
    @TableField(exist = false)
    private List<Role> roles;

    @Schema(description = "用户权限列表")
    @TableField(exist = false)
    private List<Permission> permissions;

    // 构造方法
    public User() {
    }

    public User(String username, String password, String realName, String roleCode, String roleName) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.status = 1;
        this.loginCount = 0;
        this.isDeleted = 0;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getDoctorLicenseNumber() {
        return doctorLicenseNumber;
    }

    public void setDoctorLicenseNumber(String doctorLicenseNumber) {
        this.doctorLicenseNumber = doctorLicenseNumber;
    }

    public Integer getDoctorExperienceYears() {
        return doctorExperienceYears;
    }

    public void setDoctorExperienceYears(Integer doctorExperienceYears) {
        this.doctorExperienceYears = doctorExperienceYears;
    }

    public String getFamilyRelationship() {
        return familyRelationship;
    }

    public void setFamilyRelationship(String familyRelationship) {
        this.familyRelationship = familyRelationship;
    }

    public String getFamilyElderlyIds() {
        return familyElderlyIds;
    }

    public void setFamilyElderlyIds(String familyElderlyIds) {
        this.familyElderlyIds = familyElderlyIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", realName='" + realName + '\''
                + ", gender=" + gender
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", roleCode='" + roleCode + '\''
                + ", roleName='" + roleName + '\''
                + ", status=" + status
                + ", createTime=" + createTime
                + '}';
    }
}
