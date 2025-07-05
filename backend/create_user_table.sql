-- 直接创建用户表的SQL脚本
USE smartcare_cloud;

-- 删除现有表（如果存在）
DROP TABLE IF EXISTS sys_user;

-- 创建用户表
CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender TINYINT(1) NULL,
    phone VARCHAR(20) NULL UNIQUE,
    email VARCHAR(100) NULL UNIQUE,
    role_code VARCHAR(50) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    doctor_title VARCHAR(50) NULL,
    doctor_speciality VARCHAR(200) NULL,
    family_relationship VARCHAR(50) NULL,
    status TINYINT(1) NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0
);

-- 插入测试数据
INSERT INTO sys_user (username, password, name, gender, phone, email, role_code, role_name) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '系统管理员', 1, '13800000000', 'admin@smartcare.com', 'admin', '系统管理员'),
('doctor01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '张医生', 1, '13800000001', 'doctor@smartcare.com', 'doctor', '医生'),
('family01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '家属用户', 0, '13800000002', 'family@smartcare.com', 'family', '家属');

-- 验证表创建成功
SELECT '用户表创建成功！' as message;
SELECT COUNT(*) as user_count FROM sys_user;
