-- 简单的用户表创建脚本
USE smartcare_cloud;

-- 删除现有表
DROP TABLE IF EXISTS `sys_user`;

-- 创建用户表
CREATE TABLE `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `gender` TINYINT(1) NULL COMMENT '性别：1-男，0-女',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码：admin/doctor/family',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `doctor_title` VARCHAR(50) NULL COMMENT '医生职称',
    `doctor_speciality` VARCHAR(200) NULL COMMENT '医生专业特长',
    `family_relationship` VARCHAR(50) NULL COMMENT '与老人关系',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 插入测试数据
INSERT INTO `sys_user` (`username`, `password`, `name`, `gender`, `phone`, `email`, `role_code`, `role_name`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '系统管理员', 1, '13800000000', 'admin@smartcare.com', 'admin', '系统管理员'),
('doctor01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '张医生', 1, '13800000001', 'doctor@smartcare.com', 'doctor', '医生'),
('family01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '家属用户', 0, '13800000002', 'family@smartcare.com', 'family', '家属');
