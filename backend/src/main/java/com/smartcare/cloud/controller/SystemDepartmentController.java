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
 * 系统部门管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "系统部门管理", description = "系统部门管理相关接口")
@RestController
@RequestMapping("/api/system/departments")
@CrossOrigin
public class SystemDepartmentController {

    /**
     * 获取部门列表
     */
    @Operation(summary = "获取部门列表", description = "获取系统所有部门列表")
    @GetMapping
    public ResponseResult<List<Map<String, Object>>> getDepartmentList() {
        try {
            List<Map<String, Object>> departments = new ArrayList<>();

            Map<String, Object> managementDept = new HashMap<>();
            managementDept.put("id", 1L);
            managementDept.put("name", "管理部");
            managementDept.put("code", "management");
            managementDept.put("description", "系统管理部门");
            departments.add(managementDept);

            Map<String, Object> medicalDept = new HashMap<>();
            medicalDept.put("id", 2L);
            medicalDept.put("name", "医疗部");
            medicalDept.put("code", "medical");
            medicalDept.put("description", "医疗服务部门");
            departments.add(medicalDept);

            Map<String, Object> careDept = new HashMap<>();
            careDept.put("id", 3L);
            careDept.put("name", "养老服务部");
            careDept.put("code", "care");
            careDept.put("description", "养老服务部门");
            departments.add(careDept);

            return ResponseResult.success(departments);
        } catch (Exception e) {
            return ResponseResult.error("获取部门列表失败：" + e.getMessage());
        }
    }
}
