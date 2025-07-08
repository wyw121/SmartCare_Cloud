package com.smartcare.cloud.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.dto.FamilyElderlyAccessDTO;
import com.smartcare.cloud.service.FamilyPermissionService;
import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 家属权限控制器 实现符合医疗信息隐私保护标准的家属权限管理
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "家属权限管理", description = "家属权限相关接口")
@RestController
@RequestMapping("/api/family")
@CrossOrigin
public class FamilyPermissionController {

    @Autowired
    private FamilyPermissionService familyPermissionService;

    /**
     * 检查家属是否可以访问指定老人信息
     */
    @Operation(summary = "检查家属老人访问权限", description = "检查家属是否可以访问指定老人的信息")
    @GetMapping("/check-elderly-access/{elderlyId}/{familyId}")
    public ResponseResult<Map<String, Object>> checkElderlyAccess(
            @PathVariable Long elderlyId,
            @PathVariable Long familyId,
            HttpServletRequest request) {
        try {
            boolean hasAccess = familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId);

            // 记录访问日志
            familyPermissionService.logFamilyAction(familyId, "check_elderly_access",
                    Map.of("elderlyId", elderlyId, "result", hasAccess), request);

            return ResponseResult.success(Map.of(
                    "hasAccess", hasAccess,
                    "elderlyId", elderlyId,
                    "familyId", familyId
            ));
        } catch (Exception e) {
            return ResponseResult.error("检查访问权限失败：" + e.getMessage());
        }
    }

    /**
     * 获取家属关联的老人列表
     */
    @Operation(summary = "获取家属关联老人列表", description = "获取指定家属可以查看的老人列表")
    @GetMapping("/elderly-list/{familyId}")
    public ResponseResult<List<FamilyElderlyAccessDTO>> getElderlyList(
            @PathVariable Long familyId,
            HttpServletRequest request) {
        try {
            List<FamilyElderlyAccessDTO> elderlyList = familyPermissionService.getFamilyElderlyList(familyId);

            // 记录访问日志
            familyPermissionService.logFamilyAction(familyId, "get_elderly_list",
                    Map.of("count", elderlyList.size()), request);

            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            return ResponseResult.error("获取老人列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取家属可查看的老人基本信息
     */
    @Operation(summary = "获取老人基本信息", description = "获取家属可查看的老人基本信息（脱敏）")
    @GetMapping("/elderly-info/{elderlyId}")
    public ResponseResult<FamilyElderlyAccessDTO> getElderlyInfo(
            @PathVariable Long elderlyId,
            @RequestParam Long familyId,
            HttpServletRequest request) {
        try {
            // 检查访问权限
            if (!familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId)) {
                return ResponseResult.error("无权访问该老人信息");
            }

            FamilyElderlyAccessDTO elderlyInfo = familyPermissionService.getElderlyInfoForFamily(elderlyId, familyId);

            // 记录访问日志
            familyPermissionService.logFamilyAction(familyId, "view_elderly_info",
                    Map.of("elderlyId", elderlyId), request);

            return ResponseResult.success(elderlyInfo);
        } catch (Exception e) {
            return ResponseResult.error("获取老人信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取家属可查看的健康概要信息
     */
    @Operation(summary = "获取健康概要信息", description = "获取家属可查看的健康概要信息（脱敏）")
    @GetMapping("/health-summary/{elderlyId}")
    public ResponseResult<Map<String, Object>> getHealthSummary(
            @PathVariable Long elderlyId,
            @RequestParam Long familyId,
            HttpServletRequest request) {
        try {
            // 检查访问权限
            if (!familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId)) {
                return ResponseResult.error("无权访问该老人健康信息");
            }

            Map<String, Object> healthSummary = familyPermissionService.getHealthSummaryForFamily(elderlyId, familyId);

            // 记录访问日志
            familyPermissionService.logFamilyAction(familyId, "view_health_summary",
                    Map.of("elderlyId", elderlyId), request);

            return ResponseResult.success(healthSummary);
        } catch (Exception e) {
            return ResponseResult.error("获取健康信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取家属可查看的紧急预警信息
     */
    @Operation(summary = "获取紧急预警信息", description = "获取家属可查看的紧急预警信息")
    @GetMapping("/emergency-alerts/{elderlyId}")
    public ResponseResult<List<Map<String, Object>>> getEmergencyAlerts(
            @PathVariable Long elderlyId,
            @RequestParam Long familyId,
            HttpServletRequest request) {
        try {
            // 检查访问权限
            if (!familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId)) {
                return ResponseResult.error("无权访问该老人预警信息");
            }

            List<Map<String, Object>> alerts = familyPermissionService.getEmergencyAlertsForFamily(elderlyId, familyId);

            // 记录访问日志
            familyPermissionService.logFamilyAction(familyId, "view_emergency_alerts",
                    Map.of("elderlyId", elderlyId, "alertCount", alerts.size()), request);

            return ResponseResult.success(alerts);
        } catch (Exception e) {
            return ResponseResult.error("获取预警信息失败：" + e.getMessage());
        }
    }

    /**
     * 紧急联系功能
     */
    @Operation(summary = "紧急联系", description = "家属紧急联系功能")
    @PostMapping("/emergency-contact")
    public ResponseResult<Void> emergencyContact(
            @RequestBody Map<String, Object> contactInfo,
            HttpServletRequest request) {
        try {
            Long familyId = Long.valueOf(contactInfo.get("familyId").toString());
            Long elderlyId = Long.valueOf(contactInfo.get("elderlyId").toString());
            String message = contactInfo.get("message").toString();

            // 检查访问权限
            if (!familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId)) {
                return ResponseResult.error("无权进行紧急联系");
            }

            familyPermissionService.handleEmergencyContact(familyId, elderlyId, message);

            // 记录操作日志
            familyPermissionService.logFamilyAction(familyId, "emergency_contact",
                    Map.of("elderlyId", elderlyId, "message", message), request);

            return ResponseResult.success("紧急联系已发送，工作人员将尽快回复");
        } catch (Exception e) {
            return ResponseResult.error("紧急联系失败：" + e.getMessage());
        }
    }

    /**
     * 探视预约功能
     */
    @Operation(summary = "探视预约", description = "家属探视预约功能")
    @PostMapping("/visit-schedule")
    public ResponseResult<Map<String, Object>> scheduleVisit(
            @RequestBody Map<String, Object> visitInfo,
            HttpServletRequest request) {
        try {
            Long familyId = Long.valueOf(visitInfo.get("familyId").toString());
            Long elderlyId = Long.valueOf(visitInfo.get("elderlyId").toString());
            String visitDate = visitInfo.get("visitDate").toString();
            String visitTime = visitInfo.get("visitTime").toString();

            // 检查访问权限
            if (!familyPermissionService.checkFamilyElderlyAccess(elderlyId, familyId)) {
                return ResponseResult.error("无权预约探视");
            }

            Map<String, Object> result = familyPermissionService.scheduleVisit(familyId, elderlyId, visitDate, visitTime);

            // 记录操作日志
            familyPermissionService.logFamilyAction(familyId, "schedule_visit",
                    Map.of("elderlyId", elderlyId, "visitDate", visitDate, "visitTime", visitTime), request);

            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.error("探视预约失败：" + e.getMessage());
        }
    }

    /**
     * 记录家属操作日志
     */
    @Operation(summary = "记录操作日志", description = "记录家属操作日志")
    @PostMapping("/log-action")
    public ResponseResult<Void> logAction(
            @RequestBody Map<String, Object> logData,
            HttpServletRequest request) {
        try {
            Long familyId = Long.valueOf(logData.get("familyId").toString());
            String action = logData.get("action").toString();
            Map<String, Object> details = (Map<String, Object>) logData.get("details");

            familyPermissionService.logFamilyAction(familyId, action, details, request);

            return ResponseResult.success("操作日志记录成功");
        } catch (Exception e) {
            return ResponseResult.error("记录操作日志失败：" + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    @Operation(summary = "获取客户端IP", description = "获取客户端IP地址")
    @GetMapping("/get-client-ip")
    public ResponseResult<Map<String, String>> getClientIP(HttpServletRequest request) {
        try {
            String ip = getClientIPAddress(request);
            return ResponseResult.success(Map.of("ip", ip));
        } catch (Exception e) {
            return ResponseResult.error("获取IP地址失败：" + e.getMessage());
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIPAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
