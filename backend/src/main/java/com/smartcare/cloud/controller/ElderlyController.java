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
    public ResponseResult<Page<Elderly>> getElderlyPage(@Valid @RequestBody ElderlyPageDTO pageDTO) {
        log.info("分页查询老人档案，参数：{}", pageDTO);
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
}
