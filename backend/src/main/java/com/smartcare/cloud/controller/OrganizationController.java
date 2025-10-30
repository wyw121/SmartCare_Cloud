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
import com.smartcare.cloud.entity.Organization;
import com.smartcare.cloud.service.OrganizationService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 机构管理Controller
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Tag(name = "机构管理", description = "养老服务机构信息管理")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Operation(summary = "分页查询机构列表")
    @GetMapping("/page")
    public ResponseResult<Page<Organization>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size) {
        
        Page<Organization> page = new Page<>(current, size);
        Page<Organization> result = organizationService.page(page);
        
        return ResponseResult.success(result);
    }

    @Operation(summary = "根据ID查询机构详情")
    @GetMapping("/{id}")
    public ResponseResult<Organization> getById(
            @Parameter(description = "机构ID") @PathVariable Long id) {
        
        Organization org = organizationService.getById(id);
        
        if (org == null) {
            return ResponseResult.error("机构不存在");
        }
        
        return ResponseResult.success(org);
    }

    @Operation(summary = "新增机构")
    @PostMapping
    public ResponseResult<Void> add(@RequestBody Organization organization) {
        boolean saved = organizationService.save(organization);
        
        if (saved) {
            return ResponseResult.success("新增机构成功");
        }
        
        return ResponseResult.error("新增机构失败");
    }

    @Operation(summary = "更新机构信息")
    @PutMapping
    public ResponseResult<Void> update(@RequestBody Organization organization) {
        boolean updated = organizationService.updateById(organization);
        
        if (updated) {
            return ResponseResult.success("更新机构信息成功");
        }
        
        return ResponseResult.error("更新机构信息失败");
    }

    @Operation(summary = "删除机构(逻辑删除)")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(
            @Parameter(description = "机构ID") @PathVariable Long id) {
        
        Organization org = organizationService.getById(id);
        
        if (org == null) {
            return ResponseResult.error("机构不存在");
        }
        
        org.setIsDeleted(1);
        org.setStatus(0);
        
        boolean deleted = organizationService.updateById(org);
        
        if (deleted) {
            return ResponseResult.success("删除机构成功");
        }
        
        return ResponseResult.error("删除机构失败");
    }

    @Operation(summary = "更新机构床位使用情况")
    @PutMapping("/{id}/occupied-beds")
    public ResponseResult<Void> updateOccupiedBeds(
            @Parameter(description = "机构ID") @PathVariable Long id,
            @Parameter(description = "已使用床位数") @RequestParam Integer occupiedBeds) {
        
        boolean updated = organizationService.updateOccupiedBeds(id, occupiedBeds);
        
        if (updated) {
            return ResponseResult.success("更新床位信息成功");
        }
        
        return ResponseResult.error("更新床位信息失败");
    }

    @Operation(summary = "检查床位是否充足")
    @GetMapping("/{id}/check-beds")
    public ResponseResult<Boolean> checkBedAvailability(
            @Parameter(description = "机构ID") @PathVariable Long id,
            @Parameter(description = "需要的床位数") @RequestParam Integer requiredBeds) {
        
        boolean available = organizationService.checkBedAvailability(id, requiredBeds);
        
        return ResponseResult.success(available);
    }
}
