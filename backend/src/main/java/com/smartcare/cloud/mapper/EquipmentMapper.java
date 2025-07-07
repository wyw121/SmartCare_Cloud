package com.smartcare.cloud.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.Equipment;

/**
 * 设备管理Mapper接口
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    /**
     * 获取设备类型统计
     */
    @Select("SELECT device_type, COUNT(*) as count FROM equipment WHERE is_deleted = 0 GROUP BY device_type")
    List<Map<String, Object>> getDeviceTypeStatistics();

    /**
     * 获取设备状态统计
     */
    @Select("SELECT status, COUNT(*) as count FROM equipment WHERE is_deleted = 0 GROUP BY status")
    List<Map<String, Object>> getDeviceStatusStatistics();

    /**
     * 获取设备在线率统计
     */
    @Select("SELECT "
            + "COUNT(*) as total_count, "
            + "SUM(CASE WHEN status = 'ONLINE' THEN 1 ELSE 0 END) as online_count, "
            + "SUM(CASE WHEN status = 'OFFLINE' THEN 1 ELSE 0 END) as offline_count, "
            + "SUM(CASE WHEN status = 'FAULT' THEN 1 ELSE 0 END) as fault_count "
            + "FROM equipment WHERE is_deleted = 0")
    Map<String, Object> getDeviceOnlineStatistics();
}
