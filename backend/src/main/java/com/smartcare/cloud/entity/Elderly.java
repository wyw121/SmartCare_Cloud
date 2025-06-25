package com.smartcare.cloud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalDate;

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
     * 健康状况 (HEALTHY-健康, SUBHEALTH-亚健康, SICK-疾病, SERIOUS-重病, DANGER-危险, WARNING-预警)
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
}
