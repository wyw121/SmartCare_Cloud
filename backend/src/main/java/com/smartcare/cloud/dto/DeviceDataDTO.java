package com.smartcare.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 设备数据上传DTO
 * 
 * 用于医疗监测设备上传健康数据,支持多种生理指标。
 * 数据将被解析并存储到健康记录表中。
 * 
 * @author SmartCare Team
 * @date 2024-01-27
 */
public class DeviceDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID(必填)
     */
    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    /**
     * 老人ID(必填)
     */
    @NotNull(message = "老人ID不能为空")
    private Long elderlyId;

    /**
     * 数据采集时间(必填)
     */
    @NotNull(message = "采集时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;

    /**
     * 收缩压(mmHg)
     */
    private Integer systolicPressure;

    /**
     * 舒张压(mmHg)
     */
    private Integer diastolicPressure;

    /**
     * 心率(次/分钟)
     */
    private Integer heartRate;

    /**
     * 体温(°C)
     */
    private BigDecimal bodyTemperature;

    /**
     * 血糖(mmol/L)
     */
    private BigDecimal bloodGlucose;

    /**
     * 血氧饱和度(%)
     */
    private Integer spo2;

    /**
     * 体重(kg)
     */
    private BigDecimal weight;

    /**
     * 数据类型(blood_pressure/heart_rate/temperature/glucose/spo2/weight)
     */
    private String dataType;

    /**
     * 原始数据(JSON格式,用于存储设备特定数据)
     */
    private String rawData;

    /**
     * 备注
     */
    private String remark;

    // Getters and Setters
    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(Integer systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(Integer diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public BigDecimal getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(BigDecimal bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public BigDecimal getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(BigDecimal bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public Integer getSpo2() {
        return spo2;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DeviceDataDTO{" +
                "equipmentId=" + equipmentId +
                ", elderlyId=" + elderlyId +
                ", collectTime=" + collectTime +
                ", systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", heartRate=" + heartRate +
                ", bodyTemperature=" + bodyTemperature +
                ", bloodGlucose=" + bloodGlucose +
                ", spo2=" + spo2 +
                ", weight=" + weight +
                ", dataType='" + dataType + '\'' +
                ", rawData='" + rawData + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
