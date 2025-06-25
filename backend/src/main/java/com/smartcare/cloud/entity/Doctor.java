package com.smartcare.cloud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医生实体类
 * 
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Data
@TableName("t_doctor")
public class Doctor {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 医生工号
     */
    @TableField("employee_number")
    private String employeeNumber;

    /**
     * 医生姓名
     */
    @TableField("name")
    private String name;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

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
     * 电子邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 科室
     */
    @TableField("department")
    private String department;

    /**
     * 职称
     */
    @TableField("title")
    private String title;

    /**
     * 专业特长
     */
    @TableField("specialization")
    private String specialization;

    /**
     * 从业年限
     */
    @TableField("experience_years")
    private Integer experienceYears;

    /**
     * 所属医院ID
     */
    @TableField("hospital_id")
    private Long hospitalId;

    /**
     * 所属医院名称
     */
    @TableField("hospital_name")
    private String hospitalName;

    /**
     * 执业证书号
     */
    @TableField("license_number")
    private String licenseNumber;

    /**
     * 状态 (1-在职, 0-离职)
     */
    @TableField("status")
    private Integer status;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
}
