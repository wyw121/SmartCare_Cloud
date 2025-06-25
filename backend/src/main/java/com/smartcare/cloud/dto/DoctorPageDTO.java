package com.smartcare.cloud.dto;

import lombok.Data;

/**
 * 医生分页查询DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Data
public class DoctorPageDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 医生姓名
     */
    private String name;

    /**
     * 医生工号
     */
    private String employeeNumber;

    /**
     * 科室
     */
    private String department;

    /**
     * 职称
     */
    private String title;

    /**
     * 专长
     */
    private String specialization;

    /**
     * 状态 (1-在职, 0-离职)
     */
    private Integer status;

    /**
     * 医院ID
     */
    private Long hospitalId;

    // 手动添加getter/setter方法（因为Lombok可能有问题）
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }
}
