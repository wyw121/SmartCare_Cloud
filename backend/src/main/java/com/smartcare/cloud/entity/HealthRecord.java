package com.smartcare.cloud.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 健康记录实体类
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
@TableName("t_health_record")
public class HealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID - 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老人ID - 关联老人表
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 医生ID - 关联医生表
     */
    @TableField("doctor_id")
    private Long doctorId;

    /**
     * 记录类型 (1:体检记录 2:就诊记录 3:用药记录 4:监测记录)
     */
    @TableField("record_type")
    private Integer recordType;

    /**
     * 记录日期
     */
    @TableField("record_date")
    private LocalDateTime recordDate;

    /**
     * 血压-收缩压
     */
    @TableField("systolic_pressure")
    private Integer systolicPressure;

    /**
     * 血压-舒张压
     */
    @TableField("diastolic_pressure")
    private Integer diastolicPressure;

    /**
     * 心率 (次/分钟)
     */
    @TableField("heart_rate")
    private Integer heartRate;

    /**
     * 体温 (摄氏度)
     */
    @TableField("body_temperature")
    private BigDecimal bodyTemperature;

    /**
     * 血糖值 (mmol/L)
     */
    @TableField("blood_glucose")
    private BigDecimal bloodGlucose;

    /**
     * 体重 (kg)
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 身高 (cm)
     */
    @TableField("height")
    private BigDecimal height;

    /**
     * BMI指数
     */
    @TableField("bmi")
    private BigDecimal bmi;

    /**
     * 症状描述
     */
    @TableField("symptoms")
    private String symptoms;

    /**
     * 诊断结果
     */
    @TableField("diagnosis")
    private String diagnosis;

    /**
     * 治疗方案
     */
    @TableField("treatment")
    private String treatment;

    /**
     * 用药信息
     */
    @TableField("medication")
    private String medication;

    /**
     * 医生建议
     */
    @TableField("doctor_advice")
    private String doctorAdvice;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 异常标识 (0:正常 1:异常)
     */
    @TableField("is_abnormal")
    private Integer isAbnormal;

    /**
     * 紧急程度 (1:低 2:中 3:高 4:紧急)
     */
    @TableField("urgency_level")
    private Integer urgencyLevel;

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
     * 是否删除 (0:未删除 1:已删除)
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
