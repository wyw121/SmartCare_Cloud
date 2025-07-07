package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.Equipment;
import com.smartcare.cloud.mapper.EquipmentMapper;
import com.smartcare.cloud.service.EquipmentService;

/**
 * 设备管理服务实现类
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Override
    public Page<Equipment> getEquipmentPage(int current, int size, String deviceType, String status, String keyword) {
        Page<Equipment> page = new Page<>(current, size);
        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();

        if (deviceType != null && !deviceType.isEmpty()) {
            queryWrapper.eq("device_type", deviceType);
        }

        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                    .like("device_name", keyword)
                    .or().like("device_id", keyword)
                    .or().like("brand", keyword)
                    .or().like("model", keyword));
        }

        queryWrapper.orderByDesc("create_time");

        return this.page(page, queryWrapper);
    }

    @Override
    public Equipment getEquipmentDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean createEquipment(Equipment equipment) {
        equipment.setCreateTime(LocalDateTime.now());
        equipment.setUpdateTime(LocalDateTime.now());
        return this.save(equipment);
    }

    @Override
    public boolean updateEquipment(Equipment equipment) {
        equipment.setUpdateTime(LocalDateTime.now());
        return this.updateById(equipment);
    }

    @Override
    public boolean deleteEquipment(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<Map<String, Object>> getDeviceTypeStatistics() {
        return baseMapper.getDeviceTypeStatistics();
    }

    @Override
    public List<Map<String, Object>> getDeviceStatusStatistics() {
        return baseMapper.getDeviceStatusStatistics();
    }

    @Override
    public Map<String, Object> getDeviceOnlineStatistics() {
        return baseMapper.getDeviceOnlineStatistics();
    }

    @Override
    public Map<String, Object> getDeviceIntegrationInfo(String deviceType) {
        Map<String, Object> info = new HashMap<>();

        switch (deviceType.toUpperCase()) {
            case "BLOOD_PRESSURE":
                info.put("deviceType", "智能血压计");
                info.put("description", "用于监测老人血压的智能设备");
                info.put("apiEndpoint", "/api/devices/blood-pressure/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"systolic", "diastolic", "heartRate", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'systolic': 120, 'diastolic': 80, 'heartRate': 72, 'timestamp': '2025-07-08T10:30:00', 'deviceId': 'BP001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "实时上报或定时上报（可配置）");
                break;

            case "BLOOD_GLUCOSE":
                info.put("deviceType", "血糖仪");
                info.put("description", "用于监测老人血糖水平的设备");
                info.put("apiEndpoint", "/api/devices/blood-glucose/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"glucose", "measureType", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'glucose': 6.5, 'measureType': 'fasting', 'timestamp': '2025-07-08T08:00:00', 'deviceId': 'BG001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "按需测量时上报");
                break;

            case "SMART_WATCH":
                info.put("deviceType", "智能手环");
                info.put("description", "监测老人心率、步数、睡眠等数据");
                info.put("apiEndpoint", "/api/devices/smart-watch/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"heartRate", "steps", "sleepDuration", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'heartRate': 75, 'steps': 5000, 'sleepDuration': 480, 'timestamp': '2025-07-08T23:59:59', 'deviceId': 'SW001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "每分钟或实时同步");
                break;

            case "THERMOMETER":
                info.put("deviceType", "体温计");
                info.put("description", "用于测量老人体温的智能设备");
                info.put("apiEndpoint", "/api/devices/thermometer/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"temperature", "measureSite", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'temperature': 36.5, 'measureSite': 'forehead', 'timestamp': '2025-07-08T09:00:00', 'deviceId': 'TH001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "测量时实时上报");
                break;

            case "ECG_MONITOR":
                info.put("deviceType", "心电监护仪");
                info.put("description", "监测老人心电图和生命体征");
                info.put("apiEndpoint", "/api/devices/ecg-monitor/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"ecgData", "heartRate", "heartRhythm", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'ecgData': 'base64encodedECGData', 'heartRate': 72, 'heartRhythm': 'normal', 'timestamp': '2025-07-08T10:00:00', 'deviceId': 'ECG001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "实时连续监测");
                break;

            case "WEIGHT_SCALE":
                info.put("deviceType", "智能体重秤");
                info.put("description", "测量老人体重、BMI等身体指标");
                info.put("apiEndpoint", "/api/devices/weight-scale/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"weight", "bmi", "bodyFat", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'weight': 65.5, 'bmi': 23.2, 'bodyFat': 18.5, 'timestamp': '2025-07-08T07:00:00', 'deviceId': 'WS001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "测量时实时上报");
                break;

            case "LOCATOR":
                info.put("deviceType", "定位器");
                info.put("description", "追踪老人位置，防止走失");
                info.put("apiEndpoint", "/api/devices/locator/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"latitude", "longitude", "accuracy", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'latitude': 39.9042, 'longitude': 116.4074, 'accuracy': 5, 'timestamp': '2025-07-08T12:00:00', 'deviceId': 'LOC001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "每5分钟或实时定位");
                break;

            case "EMERGENCY_BUTTON":
                info.put("deviceType", "紧急呼叫器");
                info.put("description", "老人紧急求助设备");
                info.put("apiEndpoint", "/api/devices/emergency-button/alert");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"alertType", "location", "timestamp", "deviceId", "elderlyId"});
                info.put("sampleData", "{'alertType': 'emergency', 'location': {'latitude': 39.9042, 'longitude': 116.4074}, 'timestamp': '2025-07-08T15:30:00', 'deviceId': 'EB001', 'elderlyId': 1}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "紧急情况时立即触发");
                break;

            case "ENVIRONMENT_SENSOR":
                info.put("deviceType", "环境监测传感器");
                info.put("description", "监测室内温湿度、空气质量等环境数据");
                info.put("apiEndpoint", "/api/devices/environment-sensor/data");
                info.put("protocol", "HTTP/HTTPS");
                info.put("dataFormat", "JSON");
                info.put("requiredFields", new String[]{"temperature", "humidity", "airQuality", "timestamp", "deviceId", "location"});
                info.put("sampleData", "{'temperature': 22.5, 'humidity': 45, 'airQuality': 'good', 'timestamp': '2025-07-08T14:00:00', 'deviceId': 'ENV001', 'location': 'room_A101'}");
                info.put("authentication", "Bearer Token");
                info.put("frequency", "每10分钟上报一次");
                break;

            default:
                info.put("deviceType", "未知设备类型");
                info.put("description", "请选择正确的设备类型");
                break;
        }

        // 通用接入信息
        info.put("baseUrl", "https://api.smartcare.com");
        info.put("authenticationMethod", "Bearer Token");
        info.put("contentType", "application/json");
        info.put("timeout", "30秒");
        info.put("retryPolicy", "失败时重试3次，间隔5秒");
        info.put("httpMethod", "POST");

        return info;
    }

    @Override
    public boolean updateDeviceStatus(Long id, String status) {
        Equipment equipment = this.getById(id);
        if (equipment != null) {
            equipment.setStatus(status);
            equipment.setLastOnlineTime(LocalDateTime.now());
            equipment.setUpdateTime(LocalDateTime.now());
            return this.updateById(equipment);
        }
        return false;
    }

    @Override
    public boolean bindDeviceToElderly(Long deviceId, Long elderlyId) {
        Equipment equipment = this.getById(deviceId);
        if (equipment != null) {
            equipment.setElderlyId(elderlyId);
            equipment.setUpdateTime(LocalDateTime.now());
            return this.updateById(equipment);
        }
        return false;
    }
}
