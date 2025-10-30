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
 * 护工-老人关联关系实体类
 * 
 * 对应数据表: nurse_elderly_relation
 * 用于管理护工的照护分配和工作负荷
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nurse_elderly_relation")
public class NurseElderlyRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID (主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 护工ID
     */
    @TableField("nurse_id")
    private Long nurseId;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 照护等级 (1-一级照护, 2-二级照护, 3-三级照护)
     */
    @TableField("care_level")
    private Integer careLevel;

    /**
     * 开始照护日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束照护日期 (null表示仍在照护中)
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 状态 (0-无效, 1-有效)
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
