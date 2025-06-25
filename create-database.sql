-- 智慧医养平台数据库初始化脚本
-- 请在 MySQL 中执行此脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 使用数据库
USE smartcare_cloud;
-- 创建老人信息表
CREATE TABLE IF NOT EXISTS elderly (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  name VARCHAR(100) NOT NULL COMMENT '姓名',
  id_card VARCHAR(18) COMMENT '身份证号',
  phone VARCHAR(20) COMMENT '联系电话',
  address TEXT COMMENT '住址',
  gender ENUM('MALE', 'FEMALE') COMMENT '性别',
  birth_date DATE COMMENT '出生日期',
  emergency_contact VARCHAR(100) COMMENT '紧急联系人',
  emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
  health_status VARCHAR(50) DEFAULT 'NORMAL' COMMENT '健康状态',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '老人信息表';
-- 创建医生信息表
CREATE TABLE IF NOT EXISTS doctor (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  name VARCHAR(100) NOT NULL COMMENT '姓名',
  department VARCHAR(100) COMMENT '科室',
  title VARCHAR(50) COMMENT '职称',
  phone VARCHAR(20) COMMENT '联系电话',
  email VARCHAR(100) COMMENT '邮箱',
  speciality TEXT COMMENT '专业特长',
  status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '状态',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表';
-- 插入测试数据
INSERT INTO elderly (
    name,
    id_card,
    phone,
    address,
    gender,
    birth_date,
    emergency_contact,
    emergency_phone,
    health_status
  )
VALUES (
    '张明',
    '110101195001011234',
    '13800138001',
    '北京市朝阳区幸福路123号',
    'MALE',
    '1950-01-01',
    '张小明',
    '13900139001',
    'NORMAL'
  ),
  (
    '李芳',
    '110101195505051234',
    '13800138002',
    '北京市海淀区康乐街456号',
    'FEMALE',
    '1955-05-05',
    '李大明',
    '13900139002',
    'ATTENTION'
  ),
  (
    '王德',
    '110101194801011234',
    '13800138003',
    '北京市西城区安康路789号',
    'MALE',
    '1948-01-01',
    '王小德',
    '13900139003',
    'WARNING'
  );
INSERT INTO doctor (
    name,
    department,
    title,
    phone,
    email,
    speciality,
    status
  )
VALUES (
    '赵医生',
    '内科',
    '主任医师',
    '13700137001',
    'zhao@hospital.com',
    '心血管疾病诊治',
    'ACTIVE'
  ),
  (
    '钱医生',
    '外科',
    '副主任医师',
    '13700137002',
    'qian@hospital.com',
    '骨科手术',
    'ACTIVE'
  ),
  (
    '孙医生',
    '神经科',
    '主治医师',
    '13700137003',
    'sun@hospital.com',
    '老年痴呆症诊断',
    'ACTIVE'
  );
SELECT 'Database smartcare_cloud created successfully!' as message;