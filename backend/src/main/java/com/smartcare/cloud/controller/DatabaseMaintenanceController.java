package com.smartcare.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 数据库维护控制器
 * 仅用于开发调试，生产环境应禁用
 */
@RestController
@RequestMapping("/admin/db")
public class DatabaseMaintenanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/fix-health-record-table")
    public ResponseResult<String> fixHealthRecordTable() {
        try {
            // 添加收缩压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN systolic_pressure INT COMMENT '收缩压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加舒张压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN diastolic_pressure INT COMMENT '舒张压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加心率字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN heart_rate INT COMMENT '心率(次/分钟)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加体温字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN body_temperature DECIMAL(4,2) COMMENT '体温(°C)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加血糖字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN blood_glucose DECIMAL(5,2) COMMENT '血糖(mmol/L)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加测试数据
            try {
                jdbcTemplate.execute(
                    "INSERT INTO health_record (elderly_id, doctor_id, record_type, record_date, systolic_pressure, diastolic_pressure, heart_rate, body_temperature, blood_glucose, weight, height, symptoms, diagnosis, treatment, medication, doctor_advice, remarks, is_abnormal, urgency_level) VALUES " +
                    "(1, 1, 4, NOW(), 130, 85, 75, 36.5, 5.8, 65.5, 170.0, '无明显症状', '血压稍高', '建议低盐饮食', '无', '控制饮食，适量运动', '定期监测', 0, 1), " +
                    "(2, 1, 4, NOW(), 145, 95, 80, 36.8, 6.2, 58.0, 165.0, '偶有头晕', '高血压初期', '饮食控制+运动', '无', '低盐低脂饮食，每日散步30分钟', '密切观察', 1, 2), " +
                    "(3, 1, 4, NOW(), 120, 80, 72, 36.3, 5.5, 62.0, 168.0, '无', '正常', '继续保持', '无', '保持良好生活习惯', '正常监测', 0, 1), " +
                    "(4, 1, 4, NOW(), 160, 100, 88, 37.0, 7.1, 70.0, 172.0, '头痛，胸闷', '高血压+糖尿病前期', '药物治疗+饮食控制', '降压药', '严格控制饮食，按时服药', '需要密切监测', 1, 3), " +
                    "(5, 1, 4, NOW(), 135, 88, 78, 36.6, 6.0, 55.5, 162.0, '轻微乏力', '血压偏高', '生活方式干预', '无', '增加运动量，减少盐分摄入', '定期复查', 1, 2)"
                );
            } catch (Exception e) {
                // 数据可能已存在，忽略错误
            }

            return ResponseResult.success("健康记录表结构修复完成！");
        } catch (Exception e) {
            return ResponseResult.error("修复失败：" + e.getMessage());
        }
    }
}
