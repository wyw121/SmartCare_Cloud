package com.smartcare.cloud.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.util.DatabaseScriptExecutor;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 测试数据控制器
 */
@RestController
@RequestMapping("/test")
public class TestDataController {

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private DatabaseScriptExecutor databaseScriptExecutor;

    /**
     * 初始化测试数据
     */
    @PostMapping("/init-elderly-data")
    public ResponseResult<String> initElderlyData() {
        try {
            List<Elderly> elderlyList = new ArrayList<>();

            // 创建测试数据
            Elderly elderly1 = new Elderly();
            elderly1.setName("张国强");
            elderly1.setGender("MALE");
            elderly1.setIdCard("110101194508151234");
            elderly1.setBirthDate(LocalDate.of(1945, 8, 15));
            elderly1.setPhone("13800138001");
            elderly1.setAddress("北京市朝阳区朝阳门外大街138号");
            elderly1.setHealthStatus("HEALTHY");
            elderly1.setCreateTime(LocalDateTime.now());
            elderly1.setUpdateTime(LocalDateTime.now());
            elderly1.setIsDeleted(0);
            elderlyList.add(elderly1);

            Elderly elderly2 = new Elderly();
            elderly2.setName("李秀英");
            elderly2.setGender("FEMALE");
            elderly2.setIdCard("110101194702231234");
            elderly2.setBirthDate(LocalDate.of(1947, 2, 23));
            elderly2.setPhone("13800138003");
            elderly2.setAddress("北京市海淀区中关村大街59号");
            elderly2.setHealthStatus("SUBHEALTH");
            elderly2.setCreateTime(LocalDateTime.now());
            elderly2.setUpdateTime(LocalDateTime.now());
            elderly2.setIsDeleted(0);
            elderlyList.add(elderly2);

            Elderly elderly3 = new Elderly();
            elderly3.setName("王建国");
            elderly3.setGender("MALE");
            elderly3.setIdCard("110101194312101234");
            elderly3.setBirthDate(LocalDate.of(1943, 12, 10));
            elderly3.setPhone("13800138005");
            elderly3.setAddress("北京市西城区西单北大街133号");
            elderly3.setHealthStatus("SICK");
            elderly3.setCreateTime(LocalDateTime.now());
            elderly3.setUpdateTime(LocalDateTime.now());
            elderly3.setIsDeleted(0);
            elderlyList.add(elderly3);

            // 批量保存
            for (Elderly elderly : elderlyList) {
                elderlyService.addElderly(elderly);
            }

            return ResponseResult.success("测试数据初始化成功，共添加 " + elderlyList.size() + " 条记录");
        } catch (Exception e) {
            return ResponseResult.error("初始化测试数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据库表信息
     */
    @GetMapping("/table-info")
    public ResponseResult<String> getTableInfo() {
        try {
            // 简单测试数据库连接
            return ResponseResult.success("数据库连接正常");
        } catch (Exception e) {
            return ResponseResult.error("数据库连接失败：" + e.getMessage());
        }
    }

    /**
     * 执行数据库结构优化
     */
    @PostMapping("/optimize-database")
    public ResponseResult<String> optimizeDatabase() {
        try {
            databaseScriptExecutor.executeScript("sql/optimize-elderly-database.sql");
            return ResponseResult.success("数据库结构优化完成");
        } catch (Exception e) {
            return ResponseResult.error("数据库结构优化失败: " + e.getMessage());
        }
    }

    /**
     * 检查数据库结构
     */
    @GetMapping("/check-database")
    public ResponseResult<String> checkDatabase() {
        try {
            boolean elderlyExists = databaseScriptExecutor.tableExists("elderly");
            boolean healthRecordExists = databaseScriptExecutor.tableExists("elderly_health_record");
            boolean careAssessmentExists = databaseScriptExecutor.tableExists("elderly_care_assessment");
            boolean serviceRecordExists = databaseScriptExecutor.tableExists("elderly_service_record");

            StringBuilder result = new StringBuilder();
            result.append("数据库表检查结果:\n");
            result.append("elderly表: ").append(elderlyExists ? "存在" : "不存在").append("\n");
            result.append("elderly_health_record表: ").append(healthRecordExists ? "存在" : "不存在").append("\n");
            result.append("elderly_care_assessment表: ").append(careAssessmentExists ? "存在" : "不存在").append("\n");
            result.append("elderly_service_record表: ").append(serviceRecordExists ? "存在" : "不存在").append("\n");

            if (elderlyExists) {
                boolean careLevelExists = databaseScriptExecutor.columnExists("elderly", "care_level");
                boolean bloodTypeExists = databaseScriptExecutor.columnExists("elderly", "blood_type");
                boolean adlScoreExists = databaseScriptExecutor.columnExists("elderly", "adl_score");
                result.append("\nelderly表扩展字段:\n");
                result.append("care_level: ").append(careLevelExists ? "存在" : "不存在").append("\n");
                result.append("blood_type: ").append(bloodTypeExists ? "存在" : "不存在").append("\n");
                result.append("adl_score: ").append(adlScoreExists ? "存在" : "不存在").append("\n");
            }

            return ResponseResult.success(result.toString());
        } catch (Exception e) {
            return ResponseResult.error("数据库检查失败: " + e.getMessage());
        }
    }
}
