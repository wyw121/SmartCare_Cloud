package com.smartcare.cloud.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.dto.DeviceDataDTO;
import com.smartcare.cloud.entity.Equipment;

/**
 * 设备管理服务接口
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
public interface EquipmentService extends IService<Equipment> {

    /**
     * 分页查询设备列表
     */
    Page<Equipment> getEquipmentPage(int current, int size, String deviceType, String status, String keyword);

    /**
     * 根据ID获取设备详情
     */
    Equipment getEquipmentDetail(Long id);

    /**
     * 创建设备
     */
    boolean createEquipment(Equipment equipment);

    /**
     * 更新设备信息
     */
    boolean updateEquipment(Equipment equipment);

    /**
     * 删除设备
     */
    boolean deleteEquipment(Long id);

    /**
     * 获取设备类型统计
     */
    List<Map<String, Object>> getDeviceTypeStatistics();

    /**
     * 获取设备状态统计
     */
    List<Map<String, Object>> getDeviceStatusStatistics();

    /**
     * 获取设备在线率统计
     */
    Map<String, Object> getDeviceOnlineStatistics();

    /**
     * 获取设备接入参数信息
     */
    Map<String, Object> getDeviceIntegrationInfo(String deviceType);

    /**
     * 更新设备状态
     */
    boolean updateDeviceStatus(Long id, String status);

    /**
     * 绑定设备到老人
     */
    boolean bindDeviceToElderly(Long deviceId, Long elderlyId);

    /**
     * 处理设备上传的健康数据
     * 
     * 解析设备数据,存储到健康记录表,
     * 如果数据异常则自动创建健康预警
     * 
     * @param deviceData 设备数据DTO
     * @return 处理结果
     */
    boolean processDeviceData(DeviceDataDTO deviceData);
}
