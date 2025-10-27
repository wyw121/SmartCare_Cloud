package com.smartcare.cloud.controller;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 老人联系人管理控制器
 * 负责联系人相关功能：紧急联系人管理、联系医护请求
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Tag(name = "老人联系人管理", description = "老人联系人相关接口")
@RestController
@RequestMapping("/api/elderly")
@Validated
public class ElderlyContactController {

    private static final Logger log = LoggerFactory.getLogger(ElderlyContactController.class);

    @Autowired
    private ElderlyService elderlyService;

    // ========================================
    // 紧急联系人管理
    // ========================================

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

    // ========================================
    // 联系医护功能（家属权限）
    // ========================================

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
