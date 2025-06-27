package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 健康预警管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "健康预警管理", description = "健康预警相关接口")
@RestController
@RequestMapping("/api/health-warning")
@Validated
@CrossOrigin
public class HealthWarningController {

    private static final Logger log = LoggerFactory.getLogger(HealthWarningController.class);

    @Autowired
    private HealthWarningService healthWarningService;

    /**
     * 分页查询健康预警列表
     */
    @Operation(summary = "分页查询健康预警列表")
    @PostMapping("/page")
    public ResponseResult<Map<String, Object>> getPageList(@RequestBody @Valid HealthWarningPageDTO dto) {
        log.info("分页查询健康预警列表，参数：{}", dto);
        try {
            PageInfo<HealthWarning> pageInfo = healthWarningService.getPageList(dto);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("分页查询健康预警列表失败", e);
            return ResponseResult.error("分页查询健康预警列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询健康预警详情
     */
    @Operation(summary = "根据ID查询健康预警详情")
    @GetMapping("/{id}")
    public ResponseResult<HealthWarning> getById(
            @Parameter(description = "预警ID") @PathVariable @NotNull Long id) {
        log.info("查询健康预警详情，ID：{}", id);
        try {
            HealthWarning healthWarning = healthWarningService.getById(id);
            if (healthWarning == null) {
                return ResponseResult.error("健康预警信息不存在");
            }
            return ResponseResult.success(healthWarning);
        } catch (Exception e) {
            log.error("查询健康预警详情失败", e);
            return ResponseResult.error("查询健康预警详情失败：" + e.getMessage());
        }
    }

    /**
     * 新增健康预警
     */
    @Operation(summary = "新增健康预警")
    @PostMapping("/add")
    public ResponseResult<String> add(@RequestBody @Valid HealthWarning healthWarning) {
        log.info("新增健康预警，参数：{}", healthWarning);
        try {
            boolean result = healthWarningService.save(healthWarning);
            if (result) {
                return ResponseResult.success("新增健康预警成功");
            } else {
                return ResponseResult.error("新增健康预警失败");
            }
        } catch (Exception e) {
            log.error("新增健康预警失败", e);
            return ResponseResult.error("新增健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 更新健康预警
     */
    @Operation(summary = "更新健康预警")
    @PutMapping("/update")
    public ResponseResult<String> update(@RequestBody @Valid HealthWarning healthWarning) {
        log.info("更新健康预警，参数：{}", healthWarning);
        try {
            boolean result = healthWarningService.updateById(healthWarning);
            if (result) {
                return ResponseResult.success("更新健康预警成功");
            } else {
                return ResponseResult.error("更新健康预警失败");
            }
        } catch (Exception e) {
            log.error("更新健康预警失败", e);
            return ResponseResult.error("更新健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 删除健康预警
     */
    @Operation(summary = "删除健康预警")
    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(
            @Parameter(description = "预警ID") @PathVariable @NotNull Long id) {
        log.info("删除健康预警，ID：{}", id);
        try {
            boolean result = healthWarningService.removeById(id);
            if (result) {
                return ResponseResult.success("删除健康预警成功");
            } else {
                return ResponseResult.error("删除健康预警失败");
            }
        } catch (Exception e) {
            log.error("删除健康预警失败", e);
            return ResponseResult.error("删除健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除健康预警
     */
    @Operation(summary = "批量删除健康预警")
    @DeleteMapping("/batch")
    public ResponseResult<String> deleteBatch(@RequestBody List<Long> ids) {
        log.info("批量删除健康预警，IDs：{}", ids);
        try {
            boolean result = healthWarningService.deleteBatch(ids);
            if (result) {
                return ResponseResult.success("批量删除健康预警成功");
            } else {
                return ResponseResult.error("批量删除健康预警失败");
            }
        } catch (Exception e) {
            log.error("批量删除健康预警失败", e);
            return ResponseResult.error("批量删除健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 处理健康预警
     */
    @Operation(summary = "处理健康预警")
    @PutMapping("/{id}/handle")
    public ResponseResult<String> handleWarning(
            @Parameter(description = "预警ID") @PathVariable @NotNull Long id,
            @RequestBody Map<String, Object> data) {
        log.info("处理健康预警，ID：{}，处理数据：{}", id, data);
        try {
            String handleRemark = (String) data.get("handleRemark");
            Integer status = (Integer) data.get("status");

            // 模拟处理人信息，实际应从当前登录用户获取
            Long handlerId = 1L;
            String handlerName = "系统管理员";

            boolean result = healthWarningService.handleWarning(id, handlerId, handlerName, handleRemark);
            if (result) {
                return ResponseResult.success("处理健康预警成功");
            } else {
                return ResponseResult.error("处理健康预警失败");
            }
        } catch (Exception e) {
            log.error("处理健康预警失败", e);
            return ResponseResult.error("处理健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 更新预警状态
     */
    @Operation(summary = "更新预警状态")
    @PutMapping("/{id}/status/{status}")
    public ResponseResult<String> updateStatus(
            @Parameter(description = "预警ID") @PathVariable @NotNull Long id,
            @Parameter(description = "状态") @PathVariable @NotNull Integer status) {
        log.info("更新预警状态，ID：{}，状态：{}", id, status);
        try {
            boolean result = healthWarningService.updateStatus(id, status);
            if (result) {
                return ResponseResult.success("更新预警状态成功");
            } else {
                return ResponseResult.error("更新预警状态失败");
            }
        } catch (Exception e) {
            log.error("更新预警状态失败", e);
            return ResponseResult.error("更新预警状态失败：" + e.getMessage());
        }
    }

    /**
     * 根据老人ID查询预警列表
     */
    @Operation(summary = "根据老人ID查询预警列表")
    @GetMapping("/elderly/{elderlyId}")
    public ResponseResult<List<HealthWarning>> getByElderlyId(
            @Parameter(description = "老人ID") @PathVariable @NotNull Long elderlyId) {
        log.info("根据老人ID查询预警列表，老人ID：{}", elderlyId);
        try {
            List<HealthWarning> warningList = healthWarningService.getByElderlyId(elderlyId);
            return ResponseResult.success(warningList);
        } catch (Exception e) {
            log.error("根据老人ID查询预警列表失败", e);
            return ResponseResult.error("根据老人ID查询预警列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取未处理预警数量
     */
    @Operation(summary = "获取未处理预警数量")
    @GetMapping("/count/unhandled")
    public ResponseResult<Long> getUnhandledCount() {
        log.info("获取未处理预警数量");
        try {
            long count = healthWarningService.getUnhandledCount();
            return ResponseResult.success(count);
        } catch (Exception e) {
            log.error("获取未处理预警数量失败", e);
            return ResponseResult.error("获取未处理预警数量失败：" + e.getMessage());
        }
    }

    /**
     * 获取各级别预警统计
     */
    @Operation(summary = "获取各级别预警统计")
    @GetMapping("/statistics/level")
    public ResponseResult<List<Object>> getWarningLevelStatistics() {
        log.info("获取各级别预警统计");
        try {
            List<Object> statistics = healthWarningService.getWarningLevelStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取各级别预警统计失败", e);
            return ResponseResult.error("获取各级别预警统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警类型统计
     */
    @Operation(summary = "获取预警类型统计")
    @GetMapping("/statistics/type")
    public ResponseResult<List<Object>> getWarningTypeStatistics() {
        log.info("获取预警类型统计");
        try {
            List<Object> statistics = healthWarningService.getWarningTypeStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("获取预警类型统计失败", e);
            return ResponseResult.error("获取预警类型统计失败：" + e.getMessage());
        }
    }
}
