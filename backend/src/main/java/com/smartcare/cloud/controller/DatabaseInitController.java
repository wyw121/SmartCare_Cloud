package com.smartcare.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数据库初始化控制器 用于创建数据库表和初始化数据
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
            String createTableSql = "CREATE TABLE IF NOT EXISTS `t_doctor` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',"
                    + "`employee_number` VARCHAR(50) NOT NULL COMMENT '医生工号',"
                    + "`name` VARCHAR(50) NOT NULL COMMENT '医生姓名',"
                    + "`gender` VARCHAR(10) NOT NULL COMMENT '性别',"
                    + "`age` INT(3) NULL COMMENT '年龄',"
                    + "`phone` VARCHAR(20) NOT NULL COMMENT '联系电话',"
                    + "`email` VARCHAR(100) NULL COMMENT '邮箱地址',"
                    + "`department` VARCHAR(100) NOT NULL COMMENT '科室',"
                    + "`title` VARCHAR(50) NOT NULL COMMENT '职称',"
                    + "`specialization` TEXT NULL COMMENT '专业特长',"
                    + "`experience_years` INT(2) NULL COMMENT '从业年限',"
                    + "`hospital_id` BIGINT(20) NULL COMMENT '所属医院ID',"
                    + "`hospital_name` VARCHAR(100) NULL COMMENT '所属医院名称',"
                    + "`license_number` VARCHAR(50) NULL COMMENT '执业证书号',"
                    + "`status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-在职, 0-离职)',"
                    + "`avatar` VARCHAR(255) NULL COMMENT '头像URL',"
                    + "`remark` TEXT NULL COMMENT '备注',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_employee_number` (`employee_number`),"
                    + "KEY `idx_name` (`name`),"
                    + "KEY `idx_department` (`department`),"
                    + "KEY `idx_title` (`title`),"
                    + "KEY `idx_phone` (`phone`)"
                    + ") ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表'";

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
            String insertSql = "INSERT IGNORE INTO `t_doctor` ("
                    + "`employee_number`,"
                    + "`name`,"
                    + "`gender`,"
                    + "`age`,"
                    + "`phone`,"
                    + "`email`,"
                    + "`department`,"
                    + "`title`,"
                    + "`specialization`,"
                    + "`experience_years`,"
                    + "`hospital_name`"
                    + ") VALUES "
                    + "('D001', '刘医生', '男', 45, '13700137001', 'dr.liu@hospital.com', '内科', '主任医师', '心血管疾病诊疗', 20, '北京市第一人民医院'),"
                    + "('D002', '张医生', '女', 38, '13700137002', 'dr.zhang@hospital.com', '妇科', '副主任医师', '妇科常见病诊疗', 15, '北京市第一人民医院'),"
                    + "('D003', '王医生', '男', 42, '13700137003', 'dr.wang@hospital.com', '外科', '主治医师', '普外科手术', 18, '北京市第二人民医院'),"
                    + "('D004', '李医生', '女', 35, '13700137004', 'dr.li@hospital.com', '儿科', '主治医师', '儿科常见病', 12, '北京市儿童医院'),"
                    + "('D005', '陈医生', '男', 50, '13700137005', 'dr.chen@hospital.com', '骨科', '主任医师', '骨科创伤与修复', 25, '北京市骨科医院')";

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

    /**
     * 创建角色权限系统表
     */
    @Operation(summary = "创建角色权限系统表")
    @PostMapping("/create-role-system")
    public ResponseResult<String> createRoleSystem() {
        try {
            // 创建角色表
            String createRoleTableSql = "CREATE TABLE IF NOT EXISTS `sys_role` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',"
                    + "`role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',"
                    + "`role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',"
                    + "`description` TEXT NULL COMMENT '角色描述',"
                    + "`status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',"
                    + "`sort_order` INT(11) DEFAULT 0 COMMENT '排序',"
                    + "`remark` TEXT NULL COMMENT '备注',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "`create_by` BIGINT(20) NULL COMMENT '创建者ID',"
                    + "`update_by` BIGINT(20) NULL COMMENT '更新者ID',"
                    + "`is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_role_code` (`role_code`),"
                    + "KEY `idx_status` (`status`),"
                    + "KEY `idx_sort_order` (`sort_order`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表'";

            jdbcTemplate.execute(createRoleTableSql);

            // 创建权限表
            String createPermissionTableSql = "CREATE TABLE IF NOT EXISTS `sys_permission` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',"
                    + "`permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',"
                    + "`permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',"
                    + "`permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型：menu-菜单，button-按钮，api-接口',"
                    + "`parent_id` BIGINT(20) DEFAULT 0 COMMENT '父权限ID',"
                    + "`permission_path` VARCHAR(255) NULL COMMENT '权限路径',"
                    + "`icon` VARCHAR(50) NULL COMMENT '图标',"
                    + "`description` TEXT NULL COMMENT '权限描述',"
                    + "`status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',"
                    + "`sort_order` INT(11) DEFAULT 0 COMMENT '排序',"
                    + "`remark` TEXT NULL COMMENT '备注',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "`create_by` BIGINT(20) NULL COMMENT '创建者ID',"
                    + "`update_by` BIGINT(20) NULL COMMENT '更新者ID',"
                    + "`is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_permission_code` (`permission_code`),"
                    + "KEY `idx_parent_id` (`parent_id`),"
                    + "KEY `idx_permission_type` (`permission_type`),"
                    + "KEY `idx_status` (`status`),"
                    + "KEY `idx_sort_order` (`sort_order`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表'";

            jdbcTemplate.execute(createPermissionTableSql);

            // 创建角色权限关联表
            String createRolePermissionTableSql = "CREATE TABLE IF NOT EXISTS `sys_role_permission` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',"
                    + "`role_id` BIGINT(20) NOT NULL COMMENT '角色ID',"
                    + "`permission_id` BIGINT(20) NOT NULL COMMENT '权限ID',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`create_by` BIGINT(20) NULL COMMENT '创建者ID',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),"
                    + "KEY `idx_role_id` (`role_id`),"
                    + "KEY `idx_permission_id` (`permission_id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表'";

            jdbcTemplate.execute(createRolePermissionTableSql);

            // 创建用户角色关联表
            String createUserRoleTableSql = "CREATE TABLE IF NOT EXISTS `sys_user_role` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',"
                    + "`user_id` BIGINT(20) NOT NULL COMMENT '用户ID',"
                    + "`role_id` BIGINT(20) NOT NULL COMMENT '角色ID',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`create_by` BIGINT(20) NULL COMMENT '创建者ID',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),"
                    + "KEY `idx_user_id` (`user_id`),"
                    + "KEY `idx_role_id` (`role_id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表'";

            jdbcTemplate.execute(createUserRoleTableSql);

            return ResponseResult.success("角色权限系统表创建成功");
        } catch (Exception e) {
            return ResponseResult.error("创建角色权限系统表失败: " + e.getMessage());
        }
    }

    /**
     * 初始化角色权限数据
     */
    @Operation(summary = "初始化角色权限数据")
    @PostMapping("/init-role-data")
    public ResponseResult<String> initRoleData() {
        try {
            // 插入角色数据
            String insertRoleSql = "INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `sort_order`, `remark`) VALUES "
                    + "(1, '系统管理员', 'admin', '拥有系统全部管理权限，可以进行用户管理、系统配置、数据分析等操作', 1, 1, '超级管理员角色'),"
                    + "(2, '医生', 'doctor', '拥有医疗管理相关权限，可以进行健康管理、预警处理、评估报告等操作', 1, 2, '医生角色'),"
                    + "(3, '家属', 'family', '拥有查看权限，可以查看关联老人的健康信息、报告等', 1, 3, '家属角色')";

            int roleRows = jdbcTemplate.update(insertRoleSql);

            // 插入权限数据
            String insertPermissionSql = "INSERT IGNORE INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `permission_path`, `icon`, `description`, `status`, `sort_order`) VALUES "
                    + "(1, '首页仪表板', 'dashboard', 'menu', 0, '/dashboard', 'el-icon-monitor', '系统首页仪表板', 1, 1),"
                    + "(2, '老人档案', 'elderly', 'menu', 0, '/elderly', 'el-icon-user', '老人档案管理', 1, 2),"
                    + "(3, '健康预警', 'health-warning', 'menu', 0, '/health-warning', 'el-icon-warning', '健康预警管理', 1, 3),"
                    + "(4, '用户管理', 'user-management', 'menu', 0, '/user-management', 'el-icon-user', '用户管理', 1, 4),"
                    + "(5, '个人中心', 'profile', 'menu', 0, '/profile', 'el-icon-user', '个人中心', 1, 5)";

            int permissionRows = jdbcTemplate.update(insertPermissionSql);

            // 插入角色权限关联数据
            String insertRolePermissionSql = "INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES "
                    + "(1, 1), (1, 2), (1, 3), (1, 4), (1, 5)," // 管理员拥有所有权限
                    + "(2, 1), (2, 2), (2, 3), (2, 5)," // 医生拥有仪表板、档案、预警、个人中心
                    + "(3, 1), (3, 2), (3, 5)";                   // 家属拥有仪表板、档案、个人中心

            int rolePermissionRows = jdbcTemplate.update(insertRolePermissionSql);

            return ResponseResult.success("角色权限数据初始化成功，角色: " + roleRows + " 条，权限: " + permissionRows + " 条，关联: " + rolePermissionRows + " 条");
        } catch (Exception e) {
            return ResponseResult.error("初始化角色权限数据失败: " + e.getMessage());
        }
    }

    /**
     * 检查角色权限系统表是否存在
     */
    @Operation(summary = "检查角色权限系统表")
    @GetMapping("/check-role-system")
    public ResponseResult<String> checkRoleSystem() {
        try {
            String checkRoleSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sys_role'";
            Integer roleCount = jdbcTemplate.queryForObject(checkRoleSql, Integer.class);

            String checkPermissionSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sys_permission'";
            Integer permissionCount = jdbcTemplate.queryForObject(checkPermissionSql, Integer.class);

            if (roleCount != null && roleCount > 0 && permissionCount != null && permissionCount > 0) {
                // 查询数据数量
                String roleDataSql = "SELECT COUNT(*) FROM sys_role";
                Integer roleDataCount = jdbcTemplate.queryForObject(roleDataSql, Integer.class);

                String permissionDataSql = "SELECT COUNT(*) FROM sys_permission";
                Integer permissionDataCount = jdbcTemplate.queryForObject(permissionDataSql, Integer.class);

                return ResponseResult.success("角色权限系统表已存在，角色: " + roleDataCount + " 条，权限: " + permissionDataCount + " 条");
            } else {
                return ResponseResult.success("角色权限系统表不完整，需要创建");
            }
        } catch (Exception e) {
            return ResponseResult.error("检查角色权限系统表失败: " + e.getMessage());
        }
    }
}
