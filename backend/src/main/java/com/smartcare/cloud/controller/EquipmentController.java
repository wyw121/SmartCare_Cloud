package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.dto.DeviceDataDTO;
import com.smartcare.cloud.entity.Equipment;
import com.smartcare.cloud.service.EquipmentService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 设备管理控制器
 *
 * @author SmartCare Team
 * @since 2025-07-08
 */
@Tag(name = "设备管理", description = "设备管理相关接口")
@RestController
@RequestMapping("/api/equipment")
@Validated
@CrossOrigin
public class EquipmentController {

    private static final Logger log = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 分页查询设备列表
     */
    @Operation(summary = "分页查询设备列表", description = "支持按设备类型、状态、关键词等条件分页查询设备信息")
    @GetMapping("/list")
    public ResponseResult<Page<Equipment>> getEquipmentList(
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页记录数", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "设备类型，如：血压计、心率监测仪等") 
            @RequestParam(required = false) String deviceType,
            @Parameter(description = "设备状态，如：在用、维修、报废") 
            @RequestParam(required = false) String status,
            @Parameter(description = "搜索关键词，可模糊匹配设备名称、编号") 
            @RequestParam(required = false) String keyword) {

        log.info("分页查询设备列表 - 当前页:{}, 每页大小:{}, 设备类型:{}, 状态:{}, 关键词:{}",
                current, size, deviceType, status, keyword);

        try {
            Page<Equipment> page = equipmentService.getEquipmentPage(current, size, deviceType, status, keyword);
            return ResponseResult.success(page);
        } catch (Exception e) {
            log.error("查询设备列表失败", e);
            return ResponseResult.error("查询设备列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备详情
     */
    @Operation(summary = "获取设备详情", description = "根据设备ID查询设备的完整信息")
    @GetMapping("/{id}")
    public ResponseResult<Equipment> getEquipmentDetail(
            @Parameter(description = "设备ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("获取设备详情 - ID:{}", id);

        try {
            Equipment equipment = equipmentService.getEquipmentDetail(id);
            if (equipment != null) {
                return ResponseResult.success(equipment);
            } else {
                return ResponseResult.error("设备不存在");
            }
        } catch (Exception e) {
            log.error("获取设备详情失败", e);
            return ResponseResult.error("获取设备详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建设备
     */
    @Operation(summary = "创建设备")
    @PostMapping
    public ResponseResult<String> createEquipment(@RequestBody Equipment equipment) {
        log.info("创建设备 - 设备名称:{}", equipment.getDeviceName());

        try {
            boolean success = equipmentService.createEquipment(equipment);
            if (success) {
                return ResponseResult.success("设备创建成功");
            } else {
                return ResponseResult.error("设备创建失败");
            }
        } catch (Exception e) {
            log.error("创建设备失败", e);
            return ResponseResult.error("创建设备失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备信息
     */
    @Operation(summary = "更新设备信息")
    @PutMapping("/{id}")
    public ResponseResult<String> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        log.info("更新设备信息 - ID:{}", id);

        try {
            equipment.setId(id);
            boolean success = equipmentService.updateEquipment(equipment);
            if (success) {
                return ResponseResult.success("设备更新成功");
            } else {
                return ResponseResult.error("设备更新失败");
            }
        } catch (Exception e) {
            log.error("更新设备信息失败", e);
            return ResponseResult.error("更新设备信息失败：" + e.getMessage());
        }
    }

    /**
     * 删除设备
     */
    @Operation(summary = "删除设备")
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteEquipment(@PathVariable Long id) {
        log.info("删除设备 - ID:{}", id);

        try {
            boolean success = equipmentService.deleteEquipment(id);
            if (success) {
                return ResponseResult.success("设备删除成功");
            } else {
                return ResponseResult.error("设备删除失败");
            }
        } catch (Exception e) {
            log.error("删除设备失败", e);
            return ResponseResult.error("删除设备失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备类型统计
     */
    @Operation(summary = "获取设备类型统计")
    @GetMapping("/statistics/type")
    public ResponseResult<List<Map<String, Object>>> getDeviceTypeStatistics() {
        log.info("获取设备类型统计");

        try {
            List<Map<String, Object>> statistics = equipmentService.getDeviceTypeStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取设备类型统计失败", e);
            return ResponseResult.error("获取设备类型统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备状态统计
     */
    @Operation(summary = "获取设备状态统计")
    @GetMapping("/statistics/status")
    public ResponseResult<List<Map<String, Object>>> getDeviceStatusStatistics() {
        log.info("获取设备状态统计");

        try {
            List<Map<String, Object>> statistics = equipmentService.getDeviceStatusStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取设备状态统计失败", e);
            return ResponseResult.error("获取设备状态统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备在线率统计
     */
    @Operation(summary = "获取设备在线率统计")
    @GetMapping("/statistics/online")
    public ResponseResult<Map<String, Object>> getDeviceOnlineStatistics() {
        log.info("获取设备在线率统计");

        try {
            Map<String, Object> statistics = equipmentService.getDeviceOnlineStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取设备在线率统计失败", e);
            return ResponseResult.error("获取设备在线率统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备接入参数信息
     */
    @Operation(summary = "获取设备接入参数信息")
    @GetMapping("/integration-info/{deviceType}")
    public ResponseResult<Map<String, Object>> getDeviceIntegrationInfo(@PathVariable String deviceType) {
        log.info("获取设备接入参数信息 - 设备类型:{}", deviceType);

        try {
            Map<String, Object> info = equipmentService.getDeviceIntegrationInfo(deviceType);
            return ResponseResult.success(info);
        } catch (Exception e) {
            log.error("获取设备接入参数信息失败", e);
            return ResponseResult.error("获取设备接入参数信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有设备类型的接入信息
     */
    @Operation(summary = "获取所有设备类型的接入信息")
    @GetMapping("/integration-info")
    public ResponseResult<Map<String, Object>> getAllDeviceIntegrationInfo() {
        log.info("获取所有设备类型的接入信息");

        try {
            Map<String, Object> allInfo = new HashMap<>();
            String[] deviceTypes = {
                "BLOOD_PRESSURE", "BLOOD_GLUCOSE", "SMART_WATCH", "THERMOMETER",
                "ECG_MONITOR", "WEIGHT_SCALE", "LOCATOR", "EMERGENCY_BUTTON", "ENVIRONMENT_SENSOR"
            };

            for (String deviceType : deviceTypes) {
                allInfo.put(deviceType, equipmentService.getDeviceIntegrationInfo(deviceType));
            }

            return ResponseResult.success(allInfo);
        } catch (Exception e) {
            log.error("获取所有设备类型的接入信息失败", e);
            return ResponseResult.error("获取所有设备类型的接入信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备状态
     */
    @Operation(summary = "更新设备状态")
    @PutMapping("/{id}/status")
    public ResponseResult<String> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("更新设备状态 - ID:{}, 状态:{}", id, status);

        try {
            boolean success = equipmentService.updateDeviceStatus(id, status);
            if (success) {
                return ResponseResult.success("设备状态更新成功");
            } else {
                return ResponseResult.error("设备状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新设备状态失败", e);
            return ResponseResult.error("更新设备状态失败：" + e.getMessage());
        }
    }

    /**
     * 绑定设备到老人
     */
    @Operation(summary = "绑定设备到老人")
    @PutMapping("/{deviceId}/bind/{elderlyId}")
    public ResponseResult<String> bindDeviceToElderly(@PathVariable Long deviceId, @PathVariable Long elderlyId) {
        log.info("绑定设备到老人 - 设备ID:{}, 老人ID:{}", deviceId, elderlyId);

        try {
            boolean success = equipmentService.bindDeviceToElderly(deviceId, elderlyId);
            if (success) {
                return ResponseResult.success("设备绑定成功");
            } else {
                return ResponseResult.error("设备绑定失败");
            }
        } catch (Exception e) {
            log.error("绑定设备到老人失败", e);
            return ResponseResult.error("绑定设备到老人失败：" + e.getMessage());
        }
    }

    /**
     * 上传设备采集数据
     * 
     * 医疗监测设备通过此接口上传健康数据,
     * 系统会解析数据并存储到健康记录表,
     * 如果数据异常会自动创建健康预警。
     * 
     * @param id 设备ID
     * @param deviceData 设备数据
     * @return 处理结果
     */
    @Operation(summary = "上传设备数据", description = "设备上传健康监测数据")
    @PostMapping("/{id}/data")
    public ResponseResult<String> uploadDeviceData(
            @Parameter(description = "设备ID") @PathVariable Long id,
            @Valid @RequestBody DeviceDataDTO deviceData) {
        
        log.info("设备上传数据 - 设备ID:{}, 老人ID:{}, 数据类型:{}", 
            id, deviceData.getElderlyId(), deviceData.getDataType());

        try {
            // 验证设备ID一致性
            if (!id.equals(deviceData.getEquipmentId())) {
                return ResponseResult.error("设备ID不匹配");
            }

            // 调用服务处理设备数据
            boolean success = equipmentService.processDeviceData(deviceData);
            
            if (success) {
                return ResponseResult.success("设备数据上传成功");
            } else {
                return ResponseResult.error("设备数据上传失败");
            }
        } catch (Exception e) {
            log.error("处理设备数据失败", e);
            return ResponseResult.error("处理设备数据失败：" + e.getMessage());
        }
    }
}
