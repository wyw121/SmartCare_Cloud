package com.smartcare.cloud.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 家属查看老人信息DTO 只包含家属有权限查看的字段，敏感信息已脱敏
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "家属查看老人信息DTO")
public class FamilyElderlyAccessDTO {

    @Schema(description = "老人ID")
    private Long elderlyId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "房间号")
    private String room;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "联系电话（脱敏）")
    private String phone;

    @Schema(description = "紧急联系人信息")
    private EmergencyContactDTO emergencyContact;

    @Schema(description = "健康状况概要")
    private String healthSummary;

    @Schema(description = "最后体检时间")
    private LocalDateTime lastCheckupTime;

    @Schema(description = "预警级别")
    private String alertLevel;

    @Schema(description = "入住时间")
    private LocalDateTime admissionTime;

    @Schema(description = "与家属关系")
    private String relationship;

    @Schema(description = "可查看权限级别")
    private String accessLevel;

    /**
     * 紧急联系人信息DTO
     */
    @Schema(description = "紧急联系人信息")
    public static class EmergencyContactDTO {

        @Schema(description = "联系人姓名")
        private String name;

        @Schema(description = "联系电话（脱敏）")
        private String phone;

        @Schema(description = "与老人关系")
        private String relationship;

        @Schema(description = "是否为主要联系人")
        private Boolean isPrimary;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public Boolean getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(Boolean isPrimary) {
            this.isPrimary = isPrimary;
        }
    }

    // Getters and Setters
    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EmergencyContactDTO getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContactDTO emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getHealthSummary() {
        return healthSummary;
    }

    public void setHealthSummary(String healthSummary) {
        this.healthSummary = healthSummary;
    }

    public LocalDateTime getLastCheckupTime() {
        return lastCheckupTime;
    }

    public void setLastCheckupTime(LocalDateTime lastCheckupTime) {
        this.lastCheckupTime = lastCheckupTime;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    public LocalDateTime getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(LocalDateTime admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
