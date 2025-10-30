package com.smartcare.cloud.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 家属与老人关联关系实体 用于控制家属对老人信息的访问权限
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "家属老人关联关系实体")
@TableName("family_elderly_relation")
public class FamilyElderlyRelation {

    @Schema(description = "关联ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "家属用户ID")
    @TableField("family_user_id")
    private Long familyId;

    @Schema(description = "老人ID")
    @TableField("elderly_id")
    private Long elderlyId;

    @Schema(description = "关系类型：child-子女,spouse-配偶,parent-父母,sibling-兄弟姐妹,grandchild-孙辈,other-其他")
    @TableField("relationship_type")
    private String relationshipType;
    
    @Schema(description = "关系名称：儿子、女儿、儿媳、女婿、孙子、孙女等")
    @TableField("relationship_name")
    private String relationshipName;

    @Schema(description = "访问权限级别(已废弃,使用authorized_operations替代)")
    @TableField(exist = false)
    private String accessLevel;
    
    @Schema(description = "是否为主要联系人：0-否, 1-是")
    @TableField("is_primary")
    private Integer isPrimary;
    
    @Schema(description = "是否为紧急联系人：0-否, 1-是")
    @TableField("is_emergency")
    private Integer isEmergency;
    
    @Schema(description = "联系优先级：1-最高,5-最低")
    @TableField("contact_priority")
    private Integer contactPriority;

    @Schema(description = "关联状态：0-无效, 1-有效")
    @TableField("status")
    private Integer status;

    @Schema(description = "关联开始时间")
    @TableField("start_date")
    private LocalDateTime startDate;

    @Schema(description = "关联结束时间")
    @TableField("end_date")
    private LocalDateTime endDate;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建者ID")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @Schema(description = "更新者ID")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @Schema(description = "删除标记：0-未删除, 1-已删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Integer getIsEmergency() {
        return isEmergency;
    }

    public void setIsEmergency(Integer isEmergency) {
        this.isEmergency = isEmergency;
    }

    public Integer getContactPriority() {
        return contactPriority;
    }

    public void setContactPriority(Integer contactPriority) {
        this.contactPriority = contactPriority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
