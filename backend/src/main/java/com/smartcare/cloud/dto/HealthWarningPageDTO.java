package com.smartcare.cloud.dto;

import java.time.LocalDateTime;

/**
 * 健康预警分页查询DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public class HealthWarningPageDTO {

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
     * 预警类型
     */
    private String warningType;

    /**
     * 预警级别 (1-低风险, 2-中风险, 3-高风险, 4-紧急)
     */
    private Integer warningLevel;

    /**
     * 预警状态 (0-未处理, 1-已查看, 2-处理中, 3-已处理, 4-已忽略)
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 触发开始时间
     */
    private LocalDateTime startTime;

    /**
     * 触发结束时间
     */
    private LocalDateTime endTime;

    /**
     * 数据来源
     */
    private String dataSource;
}
