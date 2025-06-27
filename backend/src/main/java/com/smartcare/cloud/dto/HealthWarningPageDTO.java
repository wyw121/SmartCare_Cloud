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

    // Getter and Setter methods
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

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public Integer getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(Integer warningLevel) {
        this.warningLevel = warningLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "HealthWarningPageDTO{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", elderlyId=" + elderlyId +
                ", elderlyName='" + elderlyName + '\'' +
                ", warningType='" + warningType + '\'' +
                ", warningLevel=" + warningLevel +
                ", status=" + status +
                ", handlerId=" + handlerId +
                ", handlerName='" + handlerName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}
