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
 * 医生-老人关联关系实体类
 * 
 * 对应数据表: doctor_elderly_relation
 * 用于实现医生数据隔离,确保医生只能访问负责的老人数据
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doctor_elderly_relation")
public class DoctorElderlyRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID (主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private Long doctorId;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 关系类型
     * primary - 主治医生 (主要负责人)
     * assistant - 协助医生 (辅助诊疗)
     * consultant - 会诊医生 (临时会诊)
     */
    @TableField("relationship_type")
    private String relationshipType;

    /**
     * 开始负责日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束负责日期 (null表示仍在负责中)
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 状态 (0-无效, 1-有效)
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    /**
     * 删除标记 (0-未删除, 1-已删除)
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
