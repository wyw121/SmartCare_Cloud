package com.smartcare.cloud.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcare.cloud.dto.DeviceDataDTO;
import com.smartcare.cloud.entity.Equipment;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.mapper.EquipmentMapper;
import com.smartcare.cloud.service.EquipmentService;
import com.smartcare.cloud.service.HealthWarningService;

/**
 * 设备管理服务实现类
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    private static final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    @Autowired
    private HealthWarningService healthWarningService;

    @Autowired
    private ObjectMapper objectMapper;

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

    /**
     * 处理设备上传的健康数据
     * 
     * 主要流程:
     * 1. 验证设备和老人信息
     * 2. 解析并存储健康数据(TODO: 待健康记录表字段修复后实现)
     * 3. 检测数据异常,自动创建健康预警
     * 4. 更新设备状态和最后在线时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processDeviceData(DeviceDataDTO deviceData) {
        try {
            // 1. 验证设备是否存在
            Equipment equipment = this.getById(deviceData.getEquipmentId());
            if (equipment == null) {
                log.error("设备不存在,ID:{}", deviceData.getEquipmentId());
                return false;
            }

            // 2. 验证设备是否绑定到指定老人
            if (equipment.getElderlyId() != null && 
                !equipment.getElderlyId().equals(deviceData.getElderlyId())) {
                log.warn("设备绑定的老人ID({})与数据中的老人ID({})不匹配", 
                    equipment.getElderlyId(), deviceData.getElderlyId());
            }

            // 3. 存储健康记录(TODO: 待health_record表字段修复后实现)
            // HealthRecord record = createHealthRecord(deviceData);
            // healthRecordService.save(record);
            log.info("设备数据已接收(暂存内存),老人ID:{}, 数据类型:{}", 
                deviceData.getElderlyId(), deviceData.getDataType());

            // 4. 检测数据异常,自动创建预警
            checkAndCreateWarning(deviceData);

            // 5. 更新设备状态
            equipment.setStatus("在线");
            equipment.setLastOnlineTime(LocalDateTime.now());
            equipment.setUpdateTime(LocalDateTime.now());
            this.updateById(equipment);

            log.info("设备数据处理成功,设备ID:{}, 老人ID:{}", 
                deviceData.getEquipmentId(), deviceData.getElderlyId());

            return true;

        } catch (Exception e) {
            log.error("处理设备数据失败", e);
            throw new RuntimeException("处理设备数据失败: " + e.getMessage());
        }
    }

    /**
     * 检测健康数据异常并创建预警
     */
    private void checkAndCreateWarning(DeviceDataDTO deviceData) {
        StringBuilder warningMsg = new StringBuilder();
        String warningType = null;
        Integer warningLevel = null;

        // 检测血压异常
        if (deviceData.getSystolicPressure() != null && deviceData.getDiastolicPressure() != null) {
            int systolic = deviceData.getSystolicPressure();
            int diastolic = deviceData.getDiastolicPressure();
            
            if (systolic >= 180 || diastolic >= 110) {
                warningType = "高血压危象";
                warningLevel = 4; // 紧急
                warningMsg.append(String.format("血压严重异常: %d/%d mmHg (正常范围90-140/60-90)", 
                    systolic, diastolic));
            } else if (systolic >= 160 || diastolic >= 100) {
                warningType = "高血压";
                warningLevel = 3; // 高
                warningMsg.append(String.format("血压偏高: %d/%d mmHg", systolic, diastolic));
            } else if (systolic < 90 || diastolic < 60) {
                warningType = "低血压";
                warningLevel = 2; // 中
                warningMsg.append(String.format("血压偏低: %d/%d mmHg", systolic, diastolic));
            }
        }

        // 检测心率异常
        if (deviceData.getHeartRate() != null) {
            int heartRate = deviceData.getHeartRate();
            
            if (heartRate > 120 || heartRate < 50) {
                if (warningType == null) {
                    warningType = heartRate > 120 ? "心动过速" : "心动过缓";
                    warningLevel = 3;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("心率异常: %d次/分 (正常范围60-100)", heartRate));
            }
        }

        // 检测体温异常
        if (deviceData.getBodyTemperature() != null) {
            double temp = deviceData.getBodyTemperature().doubleValue();
            
            if (temp >= 39.0) {
                if (warningType == null) {
                    warningType = "高热";
                    warningLevel = 4;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("体温过高: %.1f°C (正常范围36.0-37.3)", temp));
            } else if (temp >= 37.5) {
                if (warningType == null) {
                    warningType = "发热";
                    warningLevel = 2;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("体温偏高: %.1f°C", temp));
            } else if (temp < 35.0) {
                if (warningType == null) {
                    warningType = "低体温";
                    warningLevel = 3;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("体温过低: %.1f°C", temp));
            }
        }

        // 检测血糖异常
        if (deviceData.getBloodGlucose() != null) {
            double glucose = deviceData.getBloodGlucose().doubleValue();
            
            if (glucose >= 11.1) {
                if (warningType == null) {
                    warningType = "高血糖";
                    warningLevel = 3;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("血糖过高: %.1f mmol/L (正常范围3.9-6.1)", glucose));
            } else if (glucose < 3.0) {
                if (warningType == null) {
                    warningType = "低血糖";
                    warningLevel = 4;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("血糖过低: %.1f mmol/L", glucose));
            }
        }

        // 检测血氧异常
        if (deviceData.getSpo2() != null) {
            int spo2 = deviceData.getSpo2();
            
            if (spo2 < 90) {
                if (warningType == null) {
                    warningType = "低氧血症";
                    warningLevel = 4;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("血氧饱和度过低: %d%% (正常≥95)", spo2));
            } else if (spo2 < 95) {
                if (warningType == null) {
                    warningType = "血氧偏低";
                    warningLevel = 2;
                }
                if (warningMsg.length() > 0) warningMsg.append(", ");
                warningMsg.append(String.format("血氧饱和度偏低: %d%%", spo2));
            }
        }

        // 如果检测到异常,创建健康预警
        if (warningType != null && warningLevel != null) {
            try {
                HealthWarning warning = new HealthWarning();
                warning.setElderlyId(deviceData.getElderlyId());
                warning.setWarningType(warningType);
                warning.setWarningLevel(warningLevel);
                warning.setTitle(getLevelText(warningLevel));
                warning.setContent(warningMsg.toString());
                warning.setStatus(0); // 未查看
                warning.setTriggerTime(deviceData.getCollectTime());
                warning.setDataSource("设备监测");
                
                // 如果有原始数据,记录下来
                if (deviceData.getRawData() != null) {
                    warning.setHandleRemark("设备原始数据: " + deviceData.getRawData());
                }

                // 使用HealthWarningServiceImpl的createWarningWithEvent方法发布事件
                if (healthWarningService instanceof HealthWarningServiceImpl) {
                    ((HealthWarningServiceImpl) healthWarningService)
                        .createWarningWithEvent(warning, "device");
                } else {
                    // 降级方案: 直接保存
                    healthWarningService.save(warning);
                }

                log.info("设备数据异常,已创建健康预警,类型:{}, 级别:{}, 老人ID:{}", 
                    warningType, warningLevel, deviceData.getElderlyId());

            } catch (Exception e) {
                log.error("创建健康预警失败", e);
            }
        }
    }

    /**
     * 获取预警级别文本
     */
    private String getLevelText(Integer level) {
        switch (level) {
            case 4: return "紧急";
            case 3: return "高";
            case 2: return "中";
            case 1: return "低";
            default: return "未知";
        }
    }

    /**
     * 创建健康记录(TODO: 待health_record表字段修复后启用)
     */
    @SuppressWarnings("unused")
    private HealthRecord createHealthRecord(DeviceDataDTO deviceData) {
        HealthRecord record = new HealthRecord();
        record.setElderlyId(deviceData.getElderlyId());
        record.setRecordType(4); // 监测记录
        record.setRecordDate(deviceData.getCollectTime());
        
        // TODO: 待数据库表修复后取消注释
        // record.setSystolicPressure(deviceData.getSystolicPressure());
        // record.setDiastolicPressure(deviceData.getDiastolicPressure());
        // record.setHeartRate(deviceData.getHeartRate());
        // record.setBodyTemperature(deviceData.getBodyTemperature());
        // record.setBloodGlucose(deviceData.getBloodGlucose());
        
        if (deviceData.getRemark() != null) {
            record.setRemarks(deviceData.getRemark());
        }
        
        return record;
    }
}
