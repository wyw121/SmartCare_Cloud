package com.smartcare.cloud.entity;

import java.io.Serializable;
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
 * 家属用户实体类
 * 
 * 对应数据表: family_user
 * 用于管理家属的基础信息(关联系统用户表)
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("family_user")
public class FamilyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 家属ID (主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联系统用户ID (与sys_user表关联)
     */
    @TableField("sys_user_id")
    private Long sysUserId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

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
     * 电子邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 状态 (1-启用, 0-禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 删除标记 (0-未删除, 1-已删除)
     */
    @TableField("is_deleted")
    private Integer isDeleted;

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
     * 创建者ID
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新者ID
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}
