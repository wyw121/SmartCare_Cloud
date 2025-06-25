package com.smartcare.cloud.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 老人档案实体类
 *
 * @author GitHub Copilot
 * @since 2025-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("elderly")
public class Elderly {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    // 手动添加getter/setter方法（因为Lombok可能有问题）
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 性别 (1-男, 0-女)
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private LocalDate birthDate;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 家庭地址
     */
    @TableField("address")
    private String address;

    /**
     * 紧急联系人姓名
     */
    @TableField("emergency_contact_name")
    private String emergencyContactName;

    /**
     * 紧急联系人电话
     */
    @TableField("emergency_contact_phone")
    private String emergencyContactPhone;

    /**
     * 健康状况 (HEALTHY-健康, SUBHEALTH-亚健康, SICK-疾病, SERIOUS-重病, DANGER-危险,
     * WARNING-预警)
     */
    @TableField("health_status")
    private String healthStatus;

    /**
     * 照护等级 (1-自理, 2-半自理, 3-不能自理)
     */
    @TableField("care_level")
    private Integer careLevel;

    /**
     * 医保类型
     */
    @TableField("insurance_type")
    private String insuranceType;

    /**
     * 既往病史
     */
    @TableField("medical_history")
    private String medicalHistory;

    /**
     * 过敏史
     */
    @TableField("allergy_history")
    private String allergyHistory;

    /**
     * 家庭医生ID
     */
    @TableField("family_doctor_id")
    private Long familyDoctorId;

    /**
     * 档案状态 (1-正常, 0-停用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 是否删除 (1-已删除, 0-未删除)
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    // 继续补充剩余的getter/setter方法
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Integer getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(Integer careLevel) {
        this.careLevel = careLevel;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getAllergyHistory() {
        return allergyHistory;
    }

    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }

    public Long getFamilyDoctorId() {
        return familyDoctorId;
    }

    public void setFamilyDoctorId(Long familyDoctorId) {
        this.familyDoctorId = familyDoctorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
