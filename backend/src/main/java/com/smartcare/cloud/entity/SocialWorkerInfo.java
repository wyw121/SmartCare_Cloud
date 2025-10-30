package com.smartcare.cloud.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社工信息实体类
 * 
 * 对应数据表: social_worker_info
 * 用于管理社工的基础信息和专业资质
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("social_worker_info")
public class SocialWorkerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社工ID (主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联系统用户ID (与sys_user表关联)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 所属机构ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 工号 (唯一)
     */
    @TableField("employee_number")
    private String employeeNumber;

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
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 专业领域 (心理咨询、康复指导、生活服务等)
     */
    @TableField("specialization")
    private String specialization;

    /**
     * 证书类型 (社会工作师证、心理咨询师证等)
     */
    @TableField("certificate_type")
    private String certificateType;

    /**
     * 状态 (0-离职, 1-在职, 2-休假)
     */
    @TableField("status")
    private Integer status;

    /**
     * 入职日期
     */
    @TableField("entry_date")
    private LocalDate entryDate;

    /**
     * 创建时间 (自动填充)
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间 (自动填充)
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记 (0-未删除, 1-已删除)
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
