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
}
