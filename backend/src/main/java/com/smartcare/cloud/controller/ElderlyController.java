package com.smartcare.cloud.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.dto.ElderlyPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 老人档案管理控制器
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Tag(name = "老人档案管理", description = "老人档案相关接口")
@RestController
@RequestMapping("/api/elderly")
@Validated
public class ElderlyController {

    private static final Logger log = LoggerFactory.getLogger(ElderlyController.class);

    @Autowired
    private ElderlyService elderlyService;

    /**
     * 分页查询老人档案
     */
    @Operation(summary = "分页查询老人档案")
    @PostMapping("/page")
    public ResponseResult<Page<Elderly>> getElderlyPage(@RequestBody ElderlyPageDTO pageDTO) {
        log.info("分页查询老人档案，参数：{}", pageDTO);
        // 设置默认值
        if (pageDTO.getPageNum() == null || pageDTO.getPageNum() < 1) {
            pageDTO.setPageNum(1);
        }
        if (pageDTO.getPageSize() == null || pageDTO.getPageSize() < 1) {
            pageDTO.setPageSize(10);
        }
        return elderlyService.getElderlyPage(pageDTO);
    }

    /**
     * 根据ID获取老人档案详情
     */
    @Operation(summary = "获取老人档案详情")
    @GetMapping("/{id}")
    public ResponseResult<Elderly> getElderlyById(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("获取老人档案详情，ID：{}", id);
        return elderlyService.getElderlyById(id);
    }

    /**
     * 新增老人档案
     */
    @Operation(summary = "新增老人档案")
    @PostMapping
    public ResponseResult<Void> addElderly(@Valid @RequestBody Elderly elderly) {
        log.info("新增老人档案，参数：{}", elderly.getName());
        return elderlyService.addElderly(elderly);
    }

    /**
     * 更新老人档案
     */
    @Operation(summary = "更新老人档案")
    @PutMapping
    public ResponseResult<Void> updateElderly(@RequestBody Elderly elderly) {
        log.info("更新老人档案，ID：{}", elderly.getId());
        return elderlyService.updateElderly(elderly);
    }

    /**
     * 删除老人档案
     */
    @Operation(summary = "删除老人档案")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteElderly(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("删除老人档案，ID：{}", id);
        return elderlyService.deleteElderly(id);
    }

    /**
     * 批量删除老人档案
     */
    @Operation(summary = "批量删除老人档案")
    @DeleteMapping("/batch")
    public ResponseResult<Void> batchDeleteElderly(@RequestBody List<Long> ids) {
        log.info("批量删除老人档案，IDs：{}", ids);
        return elderlyService.batchDeleteElderly(ids);
    }

    /**
     * 获取重点关注老人列表
     */
    @Operation(summary = "获取重点关注老人列表")
    @GetMapping("/key")
    public ResponseResult<List<Elderly>> getKeyElderlyList() {
        log.info("获取重点关注老人列表");
        return elderlyService.getKeyElderlyList();
    }

    /**
     * 获取老人健康档案
     */
    @Operation(summary = "获取老人健康档案")
    @GetMapping("/{id}/health-records")
    public ResponseResult<Object> getElderlyHealthRecords(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("获取老人健康档案，ID：{}", id);
        return elderlyService.getElderlyHealthRecords(id);
    }

    /**
     * 添加健康记录
     */
    @Operation(summary = "添加健康记录")
    @PostMapping("/{id}/health-records")
    public ResponseResult<Void> addHealthRecord(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id,
            @RequestBody Object healthRecord) {
        log.info("添加健康记录，老人ID：{}", id);
        return elderlyService.addHealthRecord(id, healthRecord);
    }

    /**
     * 获取老人紧急联系人
     */
    @Operation(summary = "获取老人紧急联系人")
    @GetMapping("/{id}/emergency-contacts")
    public ResponseResult<Object> getEmergencyContacts(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("获取老人紧急联系人，ID：{}", id);
        return elderlyService.getEmergencyContacts(id);
    }

    /**
     * 更新紧急联系人
     */
    @Operation(summary = "更新紧急联系人")
    @PutMapping("/{id}/emergency-contacts")
    public ResponseResult<Void> updateEmergencyContacts(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id,
            @RequestBody Object emergencyContacts) {
        log.info("更新紧急联系人，老人ID：{}", id);
        return elderlyService.updateEmergencyContacts(id, emergencyContacts);
    }

    /**
     * 获取老人照护计划
     */
    @Operation(summary = "获取老人照护计划")
    @GetMapping("/{id}/care-plan")
    public ResponseResult<Object> getCarePlan(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("获取老人照护计划，ID：{}", id);
        return elderlyService.getCarePlan(id);
    }

    /**
     * 更新照护计划
     */
    @Operation(summary = "更新照护计划")
    @PutMapping("/{id}/care-plan")
    public ResponseResult<Void> updateCarePlan(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id,
            @RequestBody Object carePlan) {
        log.info("更新照护计划，老人ID：{}", id);
        return elderlyService.updateCarePlan(id, carePlan);
    }

    /**
     * 获取健康预警信息
     */
    @Operation(summary = "获取健康预警信息")
    @GetMapping("/{id}/health-warnings")
    public ResponseResult<Object> getHealthWarnings(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("获取健康预警信息，ID：{}", id);
        return elderlyService.getHealthWarnings(id);
    }

    /**
     * 生成健康评估报告
     */
    @Operation(summary = "生成健康评估报告")
    @PostMapping("/{id}/assessment-report")
    public ResponseResult<Object> generateAssessmentReport(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long id) {
        log.info("生成健康评估报告，老人ID：{}", id);
        return elderlyService.generateAssessmentReport(id);
    }

    /**
     * 批量导出老人档案
     */
    @Operation(summary = "批量导出老人档案")
    @PostMapping("/export")
    public ResponseResult<Object> exportElderlyData(@RequestBody List<Long> ids) {
        try {
            log.info("批量导出老人档案，IDs：{}", ids);
            if (ids == null) {
                log.info("导出全部老人档案数据");
            } else {
                log.info("导出指定老人档案数据，共 {} 条", ids.size());
            }
            return elderlyService.exportElderlyData(ids);
        } catch (Exception e) {
            log.error("导出老人档案失败", e);
            return ResponseResult.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 批量导入老人档案
     */
    @Operation(summary = "批量导入老人档案")
    @PostMapping("/import")
    public ResponseResult<Object> importElderlyData(@RequestBody Object importData) {
        log.info("批量导入老人档案");
        return elderlyService.importElderlyData(importData);
    }

    /**
     * 获取照护等级统计
     */
    @Operation(summary = "获取照护等级统计")
    @GetMapping("/care-level-statistics")
    public ResponseResult<Object> getCareLevelStatistics() {
        log.info("获取照护等级统计");
        return elderlyService.getCareLevelStatistics();
    }

    /**
     * 获取年龄段分布统计
     */
    @Operation(summary = "获取年龄段分布统计")
    @GetMapping("/age-distribution")
    public ResponseResult<Object> getAgeDistribution() {
        log.info("获取年龄段分布统计");
        return elderlyService.getAgeDistribution();
    }

    /**
     * 搜索老人档案（支持模糊搜索）
     */
    @Operation(summary = "搜索老人档案")
    @GetMapping("/search")
    public ResponseResult<Object> searchElderly(
            @Parameter(description = "搜索关键词") String keyword,
            @Parameter(description = "页码") Integer pageNum,
            @Parameter(description = "每页大小") Integer pageSize) {
        log.info("搜索老人档案，关键词：{}", keyword);
        return elderlyService.searchElderly(keyword, pageNum, pageSize);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseResult<String> healthCheck() {
        return ResponseResult.success("服务正常运行");
    }

    /**
     * 获取所有老人数据（用于调试）
     */
    @GetMapping("/all")
    public ResponseResult<List<Elderly>> getAllElderly() {
        try {
            List<Elderly> elderlyList = elderlyService.list();
            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            log.error("获取所有老人数据失败", e);
            return ResponseResult.error("查询失败");
        }
    }

    /**
     * 测试数据库连接
     */
    @Operation(summary = "测试数据库连接")
    @GetMapping("/test")
    public ResponseResult<String> testDatabase() {
        try {
            log.info("测试数据库连接");
            // 简单查询数据库
            long count = elderlyService.count();
            return ResponseResult.success("数据库连接正常，共有 " + count + " 条老人记录");
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return ResponseResult.error("数据库连接失败: " + e.getMessage());
        }
    }

    /**
     * 获取健康统计数据
     */
    @Operation(summary = "获取健康统计数据", description = "获取老人健康状态的完整统计信息")
    @GetMapping("/health-statistics")
    public ResponseResult<Object> getHealthStatistics() {
        try {
            Object result = elderlyService.getHealthStatistics();
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取健康统计数据失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    /**
     * 获取健康状态分布
     */
    @Operation(summary = "获取健康状态分布", description = "获取老人健康状态的分布统计")
    @GetMapping("/health-status-distribution")
    public ResponseResult<Object> getHealthStatusDistribution() {
        try {
            Object result = elderlyService.getHealthStatusDistribution();
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取健康状态分布失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    /**
     * 获取年龄段健康分布
     */
    @Operation(summary = "获取年龄段健康分布", description = "获取不同年龄段老人的健康状态分布")
    @GetMapping("/age-health-distribution")
    public ResponseResult<Object> getAgeHealthDistribution() {
        try {
            Object result = elderlyService.getAgeHealthDistribution();
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取年龄段健康分布失败", e);
            return ResponseResult.error("统计失败");
        }
    }

    /**
     * 获取健康风险评估
     */
    @Operation(summary = "获取健康风险评估", description = "获取老人健康风险评估数据")
    @GetMapping("/health-risk-assessment")
    public ResponseResult<Object> getHealthRiskAssessment() {
        try {
            Object result = elderlyService.getHealthRiskAssessment();
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取健康风险评估失败", e);
            return ResponseResult.error("评估失败");
        }
    }

    // ========================================
    // 家属专用API接口
    // ========================================
    /**
     * 根据老人ID列表批量获取老人信息（家属专用）
     */
    @Operation(summary = "家属批量获取老人信息", description = "家属根据关联的老人ID列表批量获取老人信息")
    @PostMapping("/family/batch")
    public ResponseResult<Object> getElderlyByIds(@RequestBody List<Long> elderlyIds) {
        log.info("家属批量获取老人信息，老人IDs：{}", elderlyIds);
        try {
            // 这里可以添加权限验证，确保当前登录的家属用户只能查看自己关联的老人
            Object result = elderlyService.getElderlyByIds(elderlyIds);
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("家属批量获取老人信息失败", e);
            return ResponseResult.error("获取老人信息失败");
        }
    }

    /**
     * 获取老人最新体征数据（家属权限）
     */
    @Operation(summary = "获取老人最新体征数据", description = "家属查看关联老人的最新体征数据")
    @GetMapping("/{elderlyId}/vitals/latest")
    public ResponseResult<Object> getLatestVitals(
            @Parameter(description = "老人ID") @PathVariable Long elderlyId) {
        log.info("获取老人最新体征数据，老人ID：{}", elderlyId);
        try {
            // 这里可以调用健康服务获取最新体征数据
            // 暂时返回模拟数据
            java.util.Map<String, Object> vitals = new java.util.HashMap<>();
            vitals.put("bloodPressure", "120/80");
            vitals.put("heartRate", 72);
            vitals.put("temperature", 36.5);
            vitals.put("updateTime", new java.util.Date());

            return ResponseResult.success(vitals);
        } catch (Exception e) {
            log.error("获取老人体征数据失败", e);
            return ResponseResult.error("获取体征数据失败");
        }
    }

    /**
     * 获取老人预警信息（家属权限）
     */
    @Operation(summary = "获取老人预警信息", description = "家属查看关联老人的健康预警信息")
    @GetMapping("/{elderlyId}/warnings")
    public ResponseResult<List<Object>> getWarnings(
            @Parameter(description = "老人ID") @PathVariable Long elderlyId) {
        log.info("获取老人预警信息，老人ID：{}", elderlyId);
        try {
            // 这里可以调用预警服务获取预警信息
            // 暂时返回模拟数据
            List<Object> warnings = new java.util.ArrayList<>();

            // 模拟一些预警数据
            java.util.Map<String, Object> warning1 = new java.util.HashMap<>();
            warning1.put("id", 1);
            warning1.put("message", "血压偏高，建议注意休息");
            warning1.put("level", "WARNING");
            warning1.put("createTime", new java.util.Date());
            warning1.put("isRead", false);
            warning1.put("suggestion", "定期监测血压，适当运动，保持心情愉快");
            warnings.add(warning1);

            return ResponseResult.success(warnings);
        } catch (Exception e) {
            log.error("获取老人预警信息失败", e);
            return ResponseResult.error("获取预警信息失败");
        }
    }

    /**
     * 标记预警为已读（家属权限）
     */
    @Operation(summary = "标记预警为已读", description = "家属标记预警信息为已读状态")
    @PostMapping("/warnings/mark-read")
    public ResponseResult<Void> markWarningsAsRead(@RequestBody List<Long> warningIds) {
        log.info("标记预警为已读，预警IDs：{}", warningIds);
        try {
            // 这里可以调用预警服务标记为已读
            // 暂时返回成功
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("标记预警为已读失败", e);
            return ResponseResult.error("标记失败");
        }
    }

    /**
     * 发送联系医护请求（家属权限）
     */
    @Operation(summary = "发送联系医护请求", description = "家属发送联系医护人员的请求")
    @PostMapping("/contact/send")
    public ResponseResult<Void> sendContactRequest(@RequestBody java.util.Map<String, Object> contactData) {
        log.info("发送联系医护请求，数据：{}", contactData);
        try {
            // 这里可以调用通知服务发送联系请求
            // 可以发送短信、邮件或系统消息给医护人员

            String elderlyName = (String) contactData.get("elderlyName");
            String urgency = (String) contactData.get("urgency");
            String message = (String) contactData.get("message");

            log.info("联系请求详情 - 老人：{}，紧急程度：{}，消息：{}", elderlyName, urgency, message);

            // 模拟发送成功
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("发送联系医护请求失败", e);
            return ResponseResult.error("发送请求失败");
        }
    }
}
