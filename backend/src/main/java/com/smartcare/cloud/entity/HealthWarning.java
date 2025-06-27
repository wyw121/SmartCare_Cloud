package com.smartcare.cloud.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 健康预警实体
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@TableName("t_health_warning")
public class HealthWarning {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 老人姓名（冗余字段，便于查询）
     */
    @TableField("elderly_name")
    private String elderlyName;

    /**
     * 预警类型 (血压异常、血糖异常、心率异常、体温异常、长期不活动、药物未按时服用等)
     */
    @TableField("warning_type")
    private String warningType;

    /**
     * 预警级别 (1-低风险, 2-中风险, 3-高风险, 4-紧急)
     */
    @TableField("warning_level")
    private Integer warningLevel;

    /**
     * 预警标题
     */
    @TableField("title")
    private String title;

    /**
     * 预警内容描述
     */
    @TableField("content")
    private String content;

    /**
     * 触发数据（具体的异常数据值）
     */
    @TableField("trigger_data")
    private String triggerData;

    /**
     * 数据来源 (设备监测、手动录入、系统分析等)
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 预警状态 (0-未处理, 1-已查看, 2-处理中, 3-已处理, 4-已忽略)
     */
    @TableField("status")
    private Integer status;

    /**
     * 处理人ID（医生或护理人员）
     */
    @TableField("handler_id")
    private Long handlerId;

    /**
     * 处理人姓名
     */
    @TableField("handler_name")
    private String handlerName;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理备注
     */
    @TableField("handle_remark")
    private String handleRemark;

    /**
     * 是否已通知家属 (0-否, 1-是)
     */
    @TableField("is_notified_family")
    private Integer isNotifiedFamily;

    /**
     * 是否已通知医生 (0-否, 1-是)
     */
    @TableField("is_notified_doctor")
    private Integer isNotifiedDoctor;

    /**
     * 预警触发时间
     */
    @TableField("trigger_time")
    private LocalDateTime triggerTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTriggerData() {
        return triggerData;
    }

    public void setTriggerData(String triggerData) {
        this.triggerData = triggerData;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public Integer getIsNotifiedFamily() {
        return isNotifiedFamily;
    }

    public void setIsNotifiedFamily(Integer isNotifiedFamily) {
        this.isNotifiedFamily = isNotifiedFamily;
    }

    public Integer getIsNotifiedDoctor() {
        return isNotifiedDoctor;
    }

    public void setIsNotifiedDoctor(Integer isNotifiedDoctor) {
        this.isNotifiedDoctor = isNotifiedDoctor;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
