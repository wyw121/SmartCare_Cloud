package com.smartcare.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.util.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统角色管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "系统角色管理", description = "系统角色管理相关接口")
@RestController
@RequestMapping("/api/system/roles")
@CrossOrigin
public class SystemRoleController {

    /**
     * 获取角色列表
     */
    @Operation(summary = "获取角色列表", description = "获取系统所有角色列表")
    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getRoleList() {
        try {
            List<Map<String, Object>> roles = new ArrayList<>();

            Map<String, Object> adminRole = new HashMap<>();
            adminRole.put("id", 1L);
            adminRole.put("name", "系统管理员");
            adminRole.put("code", "admin");
            adminRole.put("description", "系统管理员，拥有所有权限");
            roles.add(adminRole);

            Map<String, Object> doctorRole = new HashMap<>();
            doctorRole.put("id", 2L);
            doctorRole.put("name", "医生");
            doctorRole.put("code", "doctor");
            doctorRole.put("description", "医生用户，可管理老人健康信息");
            roles.add(doctorRole);

            Map<String, Object> familyRole = new HashMap<>();
            familyRole.put("id", 3L);
            familyRole.put("name", "家属");
            familyRole.put("code", "family");
            familyRole.put("description", "家属用户，可查看关联老人信息");
            roles.add(familyRole);

            return ResponseResult.success(roles);
        } catch (Exception e) {
            return ResponseResult.error("获取角色列表失败：" + e.getMessage());
        }
    }
}
