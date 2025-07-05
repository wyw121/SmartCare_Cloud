package com.smartcare.cloud.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户注册DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "用户注册信息")
public class UserRegisterDTO {

    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度在3-50个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,50}$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20个字符")
    private String password;

    @Schema(description = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Schema(description = "真实姓名", required = true)
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String realName;

    @Schema(description = "性别：1-男，0-女")
    private Integer gender;

    @Schema(description = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "角色编码", required = true)
    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(admin|doctor|family)$", message = "角色编码必须是admin、doctor或family")
    private String roleCode;

    // 医生专用字段
    @Schema(description = "医生职称")
    private String doctorTitle;

    @Schema(description = "医生专业特长")
    private String doctorSpeciality;

    @Schema(description = "医生执业证书号")
    private String doctorLicenseNumber;

    @Schema(description = "从业年限")
    private Integer doctorExperienceYears;

    // 家属专用字段
    @Schema(description = "与老人关系")
    private String familyRelationship;

    @Schema(description = "关联的老人ID列表")
    private String familyElderlyIds;

    @Schema(description = "备注描述")
    private String description;

    @Schema(description = "验证码")
    private String captcha;

    @Schema(description = "验证码标识")
    private String captchaId;

    // Getters and Setters
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }
}
