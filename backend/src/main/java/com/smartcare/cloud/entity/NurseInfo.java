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
 * 护工信息实体类
 * 
 * 对应数据表: nurse_info
 * 用于管理护工的基础信息、工作状态和照护能力
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nurse_info")
public class NurseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 护工ID (主键,自增)
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
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 入职日期
     */
    @TableField("entry_date")
    private LocalDate entryDate;

    /**
     * 证书类型 (护理员证、养老护理员证等)
     */
    @TableField("certificate_type")
    private String certificateType;

    /**
     * 证书编号
     */
    @TableField("certificate_number")
    private String certificateNumber;

    /**
     * 工作区域
     */
    @TableField("work_area")
    private String workArea;

    /**
     * 最大照护人数 (默认5人)
     */
    @TableField("max_care_count")
    private Integer maxCareCount;

    /**
     * 状态 (0-离职, 1-在职, 2-休假)
     */
    @TableField("status")
    private Integer status;

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
