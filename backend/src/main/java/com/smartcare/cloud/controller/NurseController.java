package com.smartcare.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.smartcare.cloud.entity.NurseInfo;
import com.smartcare.cloud.service.NurseInfoService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 护工管理Controller
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Tag(name = "护工管理", description = "护工信息的增删改查接口")
@RestController
@RequestMapping("/api/nurse")
public class NurseController {

    @Autowired
    private NurseInfoService nurseInfoService;

    @Operation(summary = "分页查询护工列表", description = "支持按姓名、工号、工作区域筛选")
    @GetMapping("/page")
    public ResponseResult<Page<NurseInfo>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "姓名") @RequestParam(required = false) String name,
            @Parameter(description = "工号") @RequestParam(required = false) String employeeNumber,
            @Parameter(description = "工作区域") @RequestParam(required = false) String workArea) {
        
        Page<NurseInfo> page = new Page<>(current, size);
        Page<NurseInfo> result = nurseInfoService.page(page);
        
        return ResponseResult.success(result);
    }

    @Operation(summary = "根据ID查询护工详情")
    @GetMapping("/{id}")
    public ResponseResult<NurseInfo> getById(
            @Parameter(description = "护工ID") @PathVariable Long id) {
        
        NurseInfo nurseInfo = nurseInfoService.getById(id);
        
        if (nurseInfo == null) {
            return ResponseResult.error("护工不存在");
        }
        
        return ResponseResult.success(nurseInfo);
    }

    @Operation(summary = "新增护工")
    @PostMapping
    public ResponseResult<Void> add(@RequestBody NurseInfo nurseInfo) {
        boolean saved = nurseInfoService.save(nurseInfo);
        
        if (saved) {
            return ResponseResult.success("新增护工成功");
        }
        
        return ResponseResult.error("新增护工失败");
    }

    @Operation(summary = "更新护工信息")
    @PutMapping
    public ResponseResult<Void> update(@RequestBody NurseInfo nurseInfo) {
        boolean updated = nurseInfoService.updateById(nurseInfo);
        
        if (updated) {
            return ResponseResult.success("更新护工信息成功");
        }
        
        return ResponseResult.error("更新护工信息失败");
    }

    @Operation(summary = "删除护工(逻辑删除)")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(
            @Parameter(description = "护工ID") @PathVariable Long id) {
        
        NurseInfo nurseInfo = nurseInfoService.getById(id);
        
        if (nurseInfo == null) {
            return ResponseResult.error("护工不存在");
        }
        
        nurseInfo.setIsDeleted(1);
        nurseInfo.setStatus(0);
        
        boolean deleted = nurseInfoService.updateById(nurseInfo);
        
        if (deleted) {
            return ResponseResult.success("删除护工成功");
        }
        
        return ResponseResult.error("删除护工失败");
    }

    @Operation(summary = "查询护工当前照护人数")
    @GetMapping("/{id}/elderly-count")
    public ResponseResult<Integer> getElderlyCount(
            @Parameter(description = "护工ID") @PathVariable Long id) {
        
        int count = nurseInfoService.countCurrentElderly(id);
        
        return ResponseResult.success(count);
    }

    @Operation(summary = "检查护工是否达到最大照护人数")
    @GetMapping("/{id}/is-full")
    public ResponseResult<Boolean> isMaxCapacityReached(
            @Parameter(description = "护工ID") @PathVariable Long id) {
        
        boolean isFull = nurseInfoService.isMaxCapacityReached(id);
        
        return ResponseResult.success(isFull);
    }
}
