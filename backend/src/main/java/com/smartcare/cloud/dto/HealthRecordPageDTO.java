package com.smartcare.cloud.dto;

import java.time.LocalDate;

/**
 * 健康记录分页查询DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public class HealthRecordPageDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 老人ID
     */
    private Long elderlyId;

    /**
     * 老人姓名
     */
    private String elderlyName;

    /**
     * 记录类型 (体检、住院、门诊、药物、康复等)
     */
    private String recordType;

    /**
     * 记录开始日期
     */
    private LocalDate startDate;

    /**
     * 记录结束日期
     */
    private LocalDate endDate;

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 异常状态 (0-正常, 1-异常)
     */
    private Integer abnormalStatus;
}
