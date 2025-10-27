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
import org.springframework.web.bind.annotation.RequestParam;
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
 * 老人档案管理控制器（核心CRUD）
 * 
 * 职责范围：
 * - 老人档案基本CRUD操作
 * - 档案查询与搜索
 * - 批量操作（导入导出）
 * - 统计数据
 * 
 * 已拆分功能：
 * - 健康相关 → ElderlyHealthController
 * - 联系人相关 → ElderlyContactController
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Tag(name = "老人档案管理", description = "老人档案核心CRUD接口")
@RestController
@RequestMapping("/api/elderly")
@Validated
public class ElderlyController {

    private static final Logger log = LoggerFactory.getLogger(ElderlyController.class);

    @Autowired
    private ElderlyService elderlyService;

    // ========================================
    // 核心CRUD操作
    // ========================================

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

    // ========================================
    // 查询与搜索
    // ========================================

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
     * 搜索老人档案（支持模糊搜索）
     */
    @Operation(summary = "搜索老人档案")
    @GetMapping("/search")
    public ResponseResult<Object> searchElderly(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer pageSize) {
        log.info("搜索老人档案，关键词：{}", keyword);
        return elderlyService.searchElderly(keyword, pageNum, pageSize);
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

    // ========================================
    // 批量操作（导入导出）
    // ========================================

    /**
     * 批量导出老人档案
     */
    @Operation(summary = "批量导出老人档案")
    @PostMapping("/export")
    public ResponseResult<Object> exportElderlyData(@RequestBody(required = false) List<Long> ids) {
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

    // ========================================
    // 统计数据
    // ========================================

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

    // ========================================
    // 家属专用接口
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

    // ========================================
    // 系统健康检查
    // ========================================

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseResult<String> healthCheck() {
        return ResponseResult.success("服务正常运行");
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
}
