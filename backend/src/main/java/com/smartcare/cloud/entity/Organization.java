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
 * 机构信息实体类
 * 
 * 对应数据表: organization
 * 用于管理养老服务机构的基础信息
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID (主键,自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 机构编码 (唯一)
     */
    @TableField("org_code")
    private String orgCode;

    /**
     * 机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 机构类型 (nursing_home-养老院, hospital-医院, community-社区服务中心)
     */
    @TableField("org_type")
    private String orgType;

    /**
     * 机构级别 (1-一级, 2-二级, 3-三级)
     */
    @TableField("org_level")
    private Integer orgLevel;

    /**
     * 负责人姓名
     */
    @TableField("director_name")
    private String directorName;

    /**
     * 负责人电话
     */
    @TableField("director_phone")
    private String directorPhone;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 区县
     */
    @TableField("district")
    private String district;

    /**
     * 床位总数
     */
    @TableField("total_beds")
    private Integer totalBeds;

    /**
     * 已使用床位数
     */
    @TableField("occupied_beds")
    private Integer occupiedBeds;

    /**
     * 营业执照号
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 服务范围
     */
    @TableField("service_scope")
    private String serviceScope;

    /**
     * 状态 (0-停业, 1-营业, 2-装修)
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
