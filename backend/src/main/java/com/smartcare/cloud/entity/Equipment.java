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
 * 设备管理实体类
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
@TableName("equipment")
@Schema(description = "设备管理实体")
public class Equipment {

    /**
     * 设备ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "设备ID")
    private Long id;

    /**
     * 设备编号
     */
    @Schema(description = "设备编号")
    private String deviceId;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 设备类型
     */
    @Schema(description = "设备类型：BLOOD_PRESSURE,BLOOD_GLUCOSE,SMART_WATCH,THERMOMETER,ECG_MONITOR,WEIGHT_SCALE,LOCATOR,EMERGENCY_BUTTON,ENVIRONMENT_SENSOR")
    private String deviceType;

    /**
     * 设备品牌
     */
    @Schema(description = "设备品牌")
    private String brand;

    /**
     * 设备型号
     */
    @Schema(description = "设备型号")
    private String model;

    /**
     * 制造商
     */
    @Schema(description = "制造商")
    private String manufacturer;

    /**
     * 设备状态
     */
    @Schema(description = "设备状态：ONLINE,OFFLINE,FAULT,MAINTENANCE")
    private String status;

    /**
     * 设备位置
     */
    @Schema(description = "设备位置")
    private String location;

    /**
     * 绑定老人ID
     */
    @Schema(description = "绑定老人ID")
    private Long elderlyId;

    /**
     * 设备序列号
     */
    @Schema(description = "设备序列号")
    private String serialNumber;

    /**
     * MAC地址
     */
    @Schema(description = "MAC地址")
    private String macAddress;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ipAddress;

    /**
     * 通信协议
     */
    @Schema(description = "通信协议：WIFI,BLUETOOTH,ZIGBEE,LORA,4G,5G")
    private String protocol;

    /**
     * API接入地址
     */
    @Schema(description = "API接入地址")
    private String apiEndpoint;

    /**
     * 数据推送频率（秒）
     */
    @Schema(description = "数据推送频率（秒）")
    private Integer dataFrequency;

    /**
     * 电池电量百分比
     */
    @Schema(description = "电池电量百分比")
    private Integer batteryLevel;

    /**
     * 固件版本
     */
    @Schema(description = "固件版本")
    private String firmwareVersion;

    /**
     * 最后在线时间
     */
    @Schema(description = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    /**
     * 最后数据上报时间
     */
    @Schema(description = "最后数据上报时间")
    private LocalDateTime lastDataTime;

    /**
     * 安装日期
     */
    @Schema(description = "安装日期")
    private LocalDateTime installDate;

    /**
     * 保修到期日期
     */
    @Schema(description = "保修到期日期")
    private LocalDateTime warrantyExpiry;

    /**
     * 设备描述
     */
    @Schema(description = "设备描述")
    private String description;

    /**
     * 配置参数（JSON格式）
     */
    @Schema(description = "配置参数")
    private String configParams;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除")
    private Integer isDeleted;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public Integer getDataFrequency() {
        return dataFrequency;
    }

    public void setDataFrequency(Integer dataFrequency) {
        this.dataFrequency = dataFrequency;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public LocalDateTime getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(LocalDateTime lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public LocalDateTime getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(LocalDateTime lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public LocalDateTime getInstallDate() {
        return installDate;
    }

    public void setInstallDate(LocalDateTime installDate) {
        this.installDate = installDate;
    }

    public LocalDateTime getWarrantyExpiry() {
        return warrantyExpiry;
    }

    public void setWarrantyExpiry(LocalDateTime warrantyExpiry) {
        this.warrantyExpiry = warrantyExpiry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigParams() {
        return configParams;
    }

    public void setConfigParams(String configParams) {
        this.configParams = configParams;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
