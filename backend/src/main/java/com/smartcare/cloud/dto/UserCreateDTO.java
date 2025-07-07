package com.smartcare.cloud.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户创建DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "用户创建DTO")
public class UserCreateDTO {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名必须是4-20位字母数字下划线")
    private String username;

    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "性别：1-男，0-女")
    private Integer gender;

    @Schema(description = "角色代码")
    @NotBlank(message = "角色代码不能为空")
    @Pattern(regexp = "^(admin|doctor|family)$", message = "角色代码必须是admin、doctor或family")
    private String roleCode;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    // 构造方法
    public UserCreateDTO() {
    }

    // Getter和Setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UserCreateDTO{"
                + "username='" + username + '\''
                + ", realName='" + realName + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", gender=" + gender
                + ", roleCode='" + roleCode + '\''
                + ", status=" + status
                + ", remark='" + remark + '\''
                + '}';
    }
}
