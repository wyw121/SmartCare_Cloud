package com.smartcare.cloud.dto;

import javax.validation.constraints.Min;

/**
 * 老人档案分页查询DTO
 *
 * @author GitHub Copilot
 * @since 2025-06-25
 */
public class ElderlyPageDTO {

    /**
     * 页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    /**
     * 姓名（模糊查询）
     */
    private String name;

    /**
     * 性别 (MALE-男, FEMALE-女)
     */
    private String gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 健康状况 (HEALTHY-健康, SUBHEALTH-亚健康, SICK-疾病, SERIOUS-重病, DANGER-危险,
     * WARNING-预警)
     */
    private String healthStatus;

    /**
     * 照护等级 (1-自理, 2-半自理, 3-不能自理)
     */
    private Integer careLevel;

    /**
     * 家庭医生ID
     */
    private Long familyDoctorId;

    /**
     * 档案状态 (1-正常, 0-停用)
     */
    private Integer status;

    /**
     * 年龄范围 - 最小年龄
     */
    private Integer minAge;

    /**
     * 年龄范围 - 最大年龄
     */
    private Integer maxAge;

    /**
     * 排序字段
     */
    private String orderBy = "create_time";

    /**
     * 排序方向 (asc/desc)
     */
    private String orderDirection = "desc";

    /**
     * 获取当前页码 (MyBatis Plus 兼容方法)
     */
    public Integer getCurrent() {
        return this.pageNum;
    }

    /**
     * 获取每页大小 (MyBatis Plus 兼容方法)
     */
    public Integer getSize() {
        return this.pageSize;
    }

    // 手动添加getter/setter方法（由于Lombok可能有问题）
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(Integer careLevel) {
        this.careLevel = careLevel;
    }

    public Long getFamilyDoctorId() {
        return familyDoctorId;
    }

    public void setFamilyDoctorId(Long familyDoctorId) {
        this.familyDoctorId = familyDoctorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
