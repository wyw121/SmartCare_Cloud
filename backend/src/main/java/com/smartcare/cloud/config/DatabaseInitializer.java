package com.smartcare.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.smartcare.cloud.util.DatabaseScriptExecutor;

/**
 * 数据库初始化组件 在应用启动时自动执行数据库结构优化
 *
 * @author SmartCare
 */
@Component
@Order(1)
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private DatabaseScriptExecutor scriptExecutor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== 开始数据库结构检查和优化 ===");

        try {
            // 检查elderly表是否存在
            boolean elderlyExists = scriptExecutor.tableExists("elderly");
            System.out.println("elderly表存在: " + elderlyExists);

            if (elderlyExists) {
                // 检查是否需要添加扩展字段
                boolean emergencyContactExists = scriptExecutor.columnExists("elderly", "emergency_contact");
                boolean careLevelExists = scriptExecutor.columnExists("elderly", "care_level");
                boolean bloodTypeExists = scriptExecutor.columnExists("elderly", "blood_type");
                boolean adlScoreExists = scriptExecutor.columnExists("elderly", "adl_score");

                System.out.println("emergency_contact字段存在: " + emergencyContactExists);
                System.out.println("care_level字段存在: " + careLevelExists);
                System.out.println("blood_type字段存在: " + bloodTypeExists);
                System.out.println("adl_score字段存在: " + adlScoreExists);

                if (!emergencyContactExists || !careLevelExists || !bloodTypeExists || !adlScoreExists) {
                    System.out.println("检测到需要优化数据库结构，开始执行...");
                    scriptExecutor.executeScript("sql/optimize-elderly-database.sql");
                    System.out.println("数据库结构优化完成！");
                } else {
                    System.out.println("数据库结构已是最新，无需优化。");
                }
            } else {
                System.out.println("elderly表不存在，请先运行初始化脚本创建基础表结构。");
            }

            // 检查其他表是否存在
            boolean healthRecordExists = scriptExecutor.tableExists("elderly_health_record");
            boolean careAssessmentExists = scriptExecutor.tableExists("elderly_care_assessment");
            boolean serviceRecordExists = scriptExecutor.tableExists("elderly_service_record");

            System.out.println("elderly_health_record表存在: " + healthRecordExists);
            System.out.println("elderly_care_assessment表存在: " + careAssessmentExists);
            System.out.println("elderly_service_record表存在: " + serviceRecordExists);

            if (!healthRecordExists || !careAssessmentExists || !serviceRecordExists) {
                System.out.println("检测到缺少相关表，开始创建...");
                scriptExecutor.executeScript("sql/optimize-elderly-database.sql");
                System.out.println("相关表创建完成！");
            }

        } catch (Exception e) {
            System.err.println("数据库结构检查失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 数据库结构检查和优化完成 ===");
    }
}
