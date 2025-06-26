package com.smartcare.cloud.controller;

import com.smartcare.cloud.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

/**
 * 数据库初始化控制器
 * 用于创建数据库表和初始化数据
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "数据库初始化", description = "数据库初始化相关接口")
@RestController
@RequestMapping("/api/init")
@CrossOrigin
public class DatabaseInitController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建医生表
     */
    @Operation(summary = "创建医生表")
    @PostMapping("/create-doctor-table")
    public ResponseResult<String> createDoctorTable() {
        try {
            String createTableSql = "CREATE TABLE IF NOT EXISTS `t_doctor` (" +
                    "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                    "`employee_number` VARCHAR(50) NOT NULL COMMENT '医生工号'," +
                    "`name` VARCHAR(50) NOT NULL COMMENT '医生姓名'," +
                    "`gender` VARCHAR(10) NOT NULL COMMENT '性别'," +
                    "`age` INT(3) NULL COMMENT '年龄'," +
                    "`phone` VARCHAR(20) NOT NULL COMMENT '联系电话'," +
                    "`email` VARCHAR(100) NULL COMMENT '邮箱地址'," +
                    "`department` VARCHAR(100) NOT NULL COMMENT '科室'," +
                    "`title` VARCHAR(50) NOT NULL COMMENT '职称'," +
                    "`specialization` TEXT NULL COMMENT '专业特长'," +
                    "`experience_years` INT(2) NULL COMMENT '从业年限'," +
                    "`hospital_id` BIGINT(20) NULL COMMENT '所属医院ID'," +
                    "`hospital_name` VARCHAR(100) NULL COMMENT '所属医院名称'," +
                    "`license_number` VARCHAR(50) NULL COMMENT '执业证书号'," +
                    "`status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-在职, 0-离职)'," +
                    "`avatar` VARCHAR(255) NULL COMMENT '头像URL'," +
                    "`remark` TEXT NULL COMMENT '备注'," +
                    "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                    "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE KEY `uk_employee_number` (`employee_number`)," +
                    "KEY `idx_name` (`name`)," +
                    "KEY `idx_department` (`department`)," +
                    "KEY `idx_title` (`title`)," +
                    "KEY `idx_phone` (`phone`)" +
                    ") ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表'";

            jdbcTemplate.execute(createTableSql);
            return ResponseResult.success("医生表创建成功");
        } catch (Exception e) {
            return ResponseResult.error("医生表创建失败: " + e.getMessage());
        }
    }

    /**
     * 插入医生测试数据
     */
    @Operation(summary = "插入医生测试数据")
    @PostMapping("/insert-doctor-data")
    public ResponseResult<String> insertDoctorData() {
        try {
            String insertSql = "INSERT IGNORE INTO `t_doctor` (" +
                    "`employee_number`," +
                    "`name`," +
                    "`gender`," +
                    "`age`," +
                    "`phone`," +
                    "`email`," +
                    "`department`," +
                    "`title`," +
                    "`specialization`," +
                    "`experience_years`," +
                    "`hospital_name`" +
                ") VALUES " +
                "('D001', '刘医生', '男', 45, '13700137001', 'dr.liu@hospital.com', '内科', '主任医师', '心血管疾病诊疗', 20, '北京市第一人民医院')," +
                "('D002', '张医生', '女', 38, '13700137002', 'dr.zhang@hospital.com', '妇科', '副主任医师', '妇科常见病诊疗', 15, '北京市第一人民医院')," +
                "('D003', '王医生', '男', 42, '13700137003', 'dr.wang@hospital.com', '外科', '主治医师', '普外科手术', 18, '北京市第二人民医院')," +
                "('D004', '李医生', '女', 35, '13700137004', 'dr.li@hospital.com', '儿科', '主治医师', '儿科常见病', 12, '北京市儿童医院')," +
                "('D005', '陈医生', '男', 50, '13700137005', 'dr.chen@hospital.com', '骨科', '主任医师', '骨科创伤与修复', 25, '北京市骨科医院')";

            int rows = jdbcTemplate.update(insertSql);
            return ResponseResult.success("成功插入 " + rows + " 条医生数据");
        } catch (Exception e) {
            return ResponseResult.error("插入医生数据失败: " + e.getMessage());
        }
    }

    /**
     * 检查表是否存在
     */
    @Operation(summary = "检查医生表是否存在")
    @GetMapping("/check-doctor-table")
    public ResponseResult<String> checkDoctorTable() {
        try {
            String checkSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 't_doctor'";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);
            
            if (count != null && count > 0) {
                // 查询表中数据数量
                String dataSql = "SELECT COUNT(*) FROM t_doctor";
                Integer dataCount = jdbcTemplate.queryForObject(dataSql, Integer.class);
                return ResponseResult.success("医生表已存在，当前有 " + dataCount + " 条数据");
            } else {
                return ResponseResult.success("医生表不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("检查医生表失败: " + e.getMessage());
        }
    }
}
