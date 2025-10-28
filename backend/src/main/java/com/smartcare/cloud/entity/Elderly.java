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

/**
 * 老人档案实体类 - 基础字段版本
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Data
@TableName("elderly")
public class Elderly {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("id_card")
    private String idCard;

    @TableField("phone")
    private String phone;

    @TableField("address")
    private String address;

    @TableField("gender")
    private Integer gender; // 1-男，0-女

    @TableField("birth_date")
    private LocalDate birthDate;

    @TableField("health_status")
    private String healthStatus;

    @TableField("care_level")
    private Integer careLevel; // 照护等级:1-自理,2-半自理,3-不能自理

    @TableField("room_number")
    private String roomNumber; // 房间号

    @TableField("guardian_name")
    private String guardianName; // 监护人姓名

    @TableField("guardian_phone")
    private String guardianPhone; // 监护人电话

    @TableField("emergency_contact")
    private String emergencyContact; // 紧急联系人

    @TableField("emergency_phone")
    private String emergencyPhone; // 紧急联系人电话

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private Integer age; // 计算字段,不存储在数据库中
}
