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
@RequestMapping("/elderly")
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
    public ResponseResult<Void> updateElderly(@Valid @RequestBody Elderly elderly) {
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
     * 获取老人健康状态统计
     */
    @Operation(summary = "获取老人健康状态统计")
    @GetMapping("/health-statistics")
    public ResponseResult<Object> getElderlyHealthStatistics() {
        log.info("获取老人健康状态统计");
        return elderlyService.getElderlyHealthStatistics();
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
        log.info("批量导出老人档案，IDs：{}", ids);
        return elderlyService.exportElderlyData(ids);
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
}
