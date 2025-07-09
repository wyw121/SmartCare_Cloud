package com.smartcare.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.FamilyService;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 家属专用控制器 为家属用户提供专门的API接口
 *
 * @author SmartCare Team
 * @date 2025-07-09
 */
@Tag(name = "家属专用接口", description = "家属用户专用的API接口")
@RestController
@RequestMapping("/api/family")
@Validated
public class FamilyController {

    private static final Logger log = LoggerFactory.getLogger(FamilyController.class);

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private HealthWarningService healthWarningService;

    /**
     * 根据老人ID列表批量获取老人信息（家属专用）
     */
    @Operation(summary = "家属批量获取老人信息", description = "家属根据关联的老人ID列表批量获取老人信息")
    @PostMapping("/elderly/batch")
    public ResponseResult<List<Elderly>> getElderlyByIds(@RequestBody FamilyElderlyRequest request) {
        log.info("家属批量获取老人信息，老人IDs：{}", request.getElderlyIds());
        try {
            // 暂时使用固定的家属用户ID进行权限验证
            // TODO: 从JWT token或session中获取当前登录用户的ID
            Long currentFamilyUserId = 3L;

            // 验证家属是否有权限查看这些老人
            if (!familyService.hasPermissionToViewElderlyList(currentFamilyUserId, request.getElderlyIds())) {
                log.warn("家属用户{}无权限查看老人列表：{}", currentFamilyUserId, request.getElderlyIds());
                return ResponseResult.error("无权限查看指定老人信息");
            }

            return elderlyService.getElderlyByIds(request.getElderlyIds());
        } catch (Exception e) {
            log.error("家属批量获取老人信息失败", e);
            return ResponseResult.error("获取老人信息失败");
        }
    }

    /**
     * 获取老人最新体征数据（家属权限）
     */
    @Operation(summary = "获取老人最新体征数据", description = "家属查看关联老人的最新体征数据")
    @GetMapping("/elderly/{elderlyId}/vitals/latest")
    public ResponseResult<Object> getLatestVitals(
            @Parameter(description = "老人ID") @PathVariable Long elderlyId) {
        log.info("获取老人最新体征数据，老人ID：{}", elderlyId);
        try {
            // 权限验证
            Long currentFamilyUserId = 3L;
            if (!familyService.hasPermissionToViewElderly(currentFamilyUserId, elderlyId)) {
                log.warn("家属用户{}无权限查看老人{}的体征数据", currentFamilyUserId, elderlyId);
                return ResponseResult.error("无权限查看该老人信息");
            }

            // TODO: 从健康记录服务获取最新体征数据
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
    @GetMapping("/elderly/{elderlyId}/warnings")
    public ResponseResult<List<Object>> getWarnings(
            @Parameter(description = "老人ID") @PathVariable Long elderlyId) {
        log.info("获取老人预警信息，老人ID：{}", elderlyId);
        try {
            // 权限验证
            Long currentFamilyUserId = 3L;
            if (!familyService.hasPermissionToViewElderly(currentFamilyUserId, elderlyId)) {
                log.warn("家属用户{}无权限查看老人{}的预警信息", currentFamilyUserId, elderlyId);
                return ResponseResult.error("无权限查看该老人信息");
            }

            // TODO: 从预警服务获取预警信息
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
    public ResponseResult<Void> markWarningsAsRead(@RequestBody FamilyWarningRequest request) {
        log.info("标记预警为已读，预警IDs：{}", request.getWarningIds());
        try {
            // TODO: 验证家属是否有权限操作这些预警
            // TODO: 调用预警服务标记为已读

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
    public ResponseResult<Void> sendContactRequest(@RequestBody FamilyContactRequest request) {
        log.info("发送联系医护请求，数据：{}", request);
        try {
            // TODO: 验证家属身份和权限
            // TODO: 调用通知服务发送联系请求
            // TODO: 记录联系日志

            log.info("联系请求详情 - 老人：{}，紧急程度：{}，消息：{}",
                    request.getElderlyName(), request.getUrgency(), request.getMessage());

            // 模拟发送成功
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("发送联系医护请求失败", e);
            return ResponseResult.error("发送请求失败");
        }
    }

    /**
     * 家属专用 - 分页查询关联老人的健康预警列表
     */
    @Operation(summary = "家属查询关联老人健康预警", description = "家属分页查询关联老人的健康预警信息")
    @PostMapping("/warnings/page")
    public ResponseResult<Map<String, Object>> getFamilyWarningsPage(@RequestBody FamilyWarningPageRequest request) {
        log.info("家属分页查询健康预警，参数：{}", request);
        try {
            // 获取当前家属用户ID（实际应从JWT token获取）
            Long currentFamilyUserId = 3L;

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(currentFamilyUserId);
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.error("当前家属用户没有关联的老人");
            }

            // 构建查询条件，限制为家属关联的老人
            HealthWarningPageDTO dto = new HealthWarningPageDTO();
            dto.setPageNum(request.getPageNum());
            dto.setPageSize(request.getPageSize());

            // 设置查询条件
            if (request.getElderlyName() != null && !request.getElderlyName().trim().isEmpty()) {
                dto.setElderlyName(request.getElderlyName());
            }
            if (request.getWarningType() != null && !request.getWarningType().trim().isEmpty()) {
                dto.setWarningType(request.getWarningType());
            }
            if (request.getWarningLevel() != null && !request.getWarningLevel().trim().isEmpty()) {
                dto.setWarningLevel(request.getWarningLevel());
            }
            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                dto.setStatus(request.getStatus());
            }
            if (request.getStartTime() != null) {
                dto.setStartTime(request.getStartTime());
            }
            if (request.getEndTime() != null) {
                dto.setEndTime(request.getEndTime());
            }

            // 调用健康预警服务，传入家属关联的老人ID列表
            PageInfo<HealthWarning> pageInfo = healthWarningService.getPageListByElderlyIds(dto, elderlyIds);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("家属分页查询健康预警失败", e);
            return ResponseResult.error("查询健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 家属专用 - 获取关联老人健康预警统计数据
     */
    @Operation(summary = "家属获取健康预警统计", description = "家属获取关联老人的健康预警统计数据")
    @GetMapping("/warnings/statistics")
    public ResponseResult<Map<String, Object>> getFamilyWarningStatistics() {
        log.info("家属获取健康预警统计");
        try {
            // 获取当前家属用户ID（实际应从JWT token获取）
            Long currentFamilyUserId = 3L;

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(currentFamilyUserId);
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.error("当前家属用户没有关联的老人");
            }

            // 获取统计数据（按老人ID列表过滤）
            Map<String, Object> statistics = healthWarningService.getWarningStatisticsByElderlyIds(elderlyIds);

            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("家属获取健康预警统计失败", e);
            return ResponseResult.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 开发环境 - 初始化测试数据
     */
    @Operation(summary = "初始化测试数据", description = "开发环境专用，创建家属关联的健康预警测试数据")
    @PostMapping("/init-test-data")
    public ResponseResult<String> initTestData() {
        log.info("初始化家属端测试数据");
        try {
            // 创建测试健康预警数据
            List<HealthWarning> testWarnings = createTestWarnings();

            // 这里应该调用service保存数据
            // 但由于是测试，我们只返回成功信息
            log.info("已创建 {} 条测试预警数据", testWarnings.size());

            return ResponseResult.success("测试数据初始化成功，创建了" + testWarnings.size() + "条预警数据");
        } catch (Exception e) {
            log.error("初始化测试数据失败", e);
            return ResponseResult.error("初始化失败：" + e.getMessage());
        }
    }

    /**
     * 创建测试健康预警数据
     */
    private List<HealthWarning> createTestWarnings() {
        List<HealthWarning> warnings = new ArrayList<>();

        // 为老人ID=1创建预警
        HealthWarning warning1 = new HealthWarning();
        warning1.setElderlyId(1L);
        warning1.setElderlyName("张大爷");
        warning1.setWarningType("血压异常");
        warning1.setWarningLevel(3);
        warning1.setTitle("高血压预警");
        warning1.setContent("血压监测显示收缩压180mmHg，超出正常范围，请及时就医");
        warning1.setTriggerData("收缩压:180mmHg, 舒张压:110mmHg");
        warning1.setDataSource("血压监测设备");
        warning1.setStatus(0);
        warning1.setTriggerTime(java.time.LocalDateTime.now().minusHours(2));
        warnings.add(warning1);

        HealthWarning warning2 = new HealthWarning();
        warning2.setElderlyId(2L);
        warning2.setElderlyName("李奶奶");
        warning2.setWarningType("心率异常");
        warning2.setWarningLevel(2);
        warning2.setTitle("心率过快预警");
        warning2.setContent("心率监测显示心率105次/分，略高于正常范围");
        warning2.setTriggerData("心率:105次/分");
        warning2.setDataSource("心率监测设备");
        warning2.setStatus(1);
        warning2.setTriggerTime(java.time.LocalDateTime.now().minusHours(4));
        warnings.add(warning2);

        HealthWarning warning3 = new HealthWarning();
        warning3.setElderlyId(1L);
        warning3.setElderlyName("张大爷");
        warning3.setWarningType("用药提醒");
        warning3.setWarningLevel(1);
        warning3.setTitle("用药时间提醒");
        warning3.setContent("降压药服用时间已到，请按时服药");
        warning3.setTriggerData("药物:降压药, 剂量:1片");
        warning3.setDataSource("智能药盒");
        warning3.setStatus(0);
        warning3.setTriggerTime(java.time.LocalDateTime.now().minusMinutes(30));
        warnings.add(warning3);

        return warnings;
    }

    /**
     * 开发环境 - 检查数据库数据
     */
    @Operation(summary = "检查数据库数据", description = "开发环境专用，检查数据库中的健康预警数据")
    @GetMapping("/check-data")
    public ResponseResult<Map<String, Object>> checkData() {
        log.info("检查数据库健康预警数据");
        try {
            Map<String, Object> result = new HashMap<>();

            // 检查总数据量
            long totalCount = healthWarningService.count();
            result.put("totalWarnings", totalCount);

            // 检查老人ID=1的数据
            long elderly1Count = healthWarningService.count(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                            .eq(HealthWarning::getElderlyId, 1L)
            );
            result.put("elderly1Warnings", elderly1Count);

            // 检查老人ID=2的数据
            long elderly2Count = healthWarningService.count(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                            .eq(HealthWarning::getElderlyId, 2L)
            );
            result.put("elderly2Warnings", elderly2Count);

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(3L);
            result.put("familyRelatedElderlyIds", elderlyIds);

            // 检查家属相关数据
            if (elderlyIds != null && !elderlyIds.isEmpty()) {
                long familyRelatedCount = healthWarningService.count(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                                .in(HealthWarning::getElderlyId, elderlyIds)
                );
                result.put("familyRelatedWarnings", familyRelatedCount);
            } else {
                result.put("familyRelatedWarnings", 0);
            }

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("检查数据库数据失败", e);
            return ResponseResult.error("检查失败：" + e.getMessage());
        }
    }

    // ========================================
    // 内部DTO类
    // ========================================
    /**
     * 家属获取老人信息请求
     */
    public static class FamilyElderlyRequest {

        private List<Long> elderlyIds;

        public List<Long> getElderlyIds() {
            return elderlyIds;
        }

        public void setElderlyIds(List<Long> elderlyIds) {
            this.elderlyIds = elderlyIds;
        }
    }

    /**
     * 家属预警操作请求
     */
    public static class FamilyWarningRequest {

        private List<Long> warningIds;

        public List<Long> getWarningIds() {
            return warningIds;
        }

        public void setWarningIds(List<Long> warningIds) {
            this.warningIds = warningIds;
        }
    }

    /**
     * 家属联系请求
     */
    public static class FamilyContactRequest {

        private String elderlyName;
        private String urgency;
        private String message;

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
        }

        public String getUrgency() {
            return urgency;
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "FamilyContactRequest{"
                    + "elderlyName='" + elderlyName + '\''
                    + ", urgency='" + urgency + '\''
                    + ", message='" + message + '\''
                    + '}';
        }
    }

    /**
     * 家属健康预警分页查询请求
     */
    public static class HealthWarningPageRequest {

        private Long elderlyId;
        private int pageNum;
        private int pageSize;

        public Long getElderlyId() {
            return elderlyId;
        }

        public void setElderlyId(Long elderlyId) {
            this.elderlyId = elderlyId;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    /**
     * 家属健康预警分页查询请求
     */
    public static class FamilyWarningPageRequest {

        private Integer pageNum = 1;
        private Integer pageSize = 10;
        private String elderlyName;
        private String warningType;
        private String warningLevel;
        private String status;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;

        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
        }

        public String getWarningType() {
            return warningType;
        }

        public void setWarningType(String warningType) {
            this.warningType = warningType;
        }

        public String getWarningLevel() {
            return warningLevel;
        }

        public void setWarningLevel(String warningLevel) {
            this.warningLevel = warningLevel;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.time.LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "FamilyWarningPageRequest{"
                    + "pageNum=" + pageNum
                    + ", pageSize=" + pageSize
                    + ", elderlyName='" + elderlyName + '\''
                    + ", warningType='" + warningType + '\''
                    + ", warningLevel='" + warningLevel + '\''
                    + ", status='" + status + '\''
                    + ", startTime=" + startTime
                    + ", endTime=" + endTime
                    + '}';
        }
    }
}
