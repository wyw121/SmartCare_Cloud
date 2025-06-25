-- 智慧医养大数据公共服务平台数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartcare_cloud;
-- 老人档案表
CREATE TABLE IF NOT EXISTS `elderly` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT(1) NOT NULL COMMENT '性别 (1-男, 0-女)',
    `id_card` VARCHAR(18) NOT NULL COMMENT '身份证号',
    `birth_date` DATE NULL COMMENT '出生日期',
    `age` INT(3) NULL COMMENT '年龄',
    `phone` VARCHAR(20) NULL COMMENT '联系电话',
    `address` VARCHAR(255) NULL COMMENT '家庭地址',
    `emergency_contact_name` VARCHAR(50) NULL COMMENT '紧急联系人姓名',
    `emergency_contact_phone` VARCHAR(20) NULL COMMENT '紧急联系人电话',
    `health_status` VARCHAR(20) NOT NULL DEFAULT 'HEALTHY' COMMENT '健康状况 (HEALTHY-健康, SUBHEALTH-亚健康, SICK-疾病, SERIOUS-重病, DANGER-危险, WARNING-预警)',
    `care_level` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '照护等级 (1-自理, 2-半自理, 3-不能自理)',
    `insurance_type` VARCHAR(50) NULL COMMENT '医保类型',
    `medical_history` TEXT NULL COMMENT '既往病史',
    `allergy_history` TEXT NULL COMMENT '过敏史',
    `family_doctor_id` BIGINT(20) NULL COMMENT '家庭医生ID',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '档案状态 (1-正常, 0-停用)',
    `remarks` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_id_card` (`id_card`),
    KEY `idx_name` (`name`),
    KEY `idx_phone` (`phone`),
    KEY `idx_health_status` (`health_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '老人档案表';
-- 医生信息表
CREATE TABLE IF NOT EXISTS `doctor` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
    `gender` TINYINT(1) NOT NULL COMMENT '性别 (1-男, 0-女)',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `email` VARCHAR(100) NULL COMMENT '邮箱地址',
    `department` VARCHAR(100) NOT NULL COMMENT '科室',
    `title` VARCHAR(50) NOT NULL COMMENT '职称',
    `speciality` VARCHAR(200) NULL COMMENT '专业特长',
    `experience_years` INT(2) NULL COMMENT '从业年限',
    `license_number` VARCHAR(50) NOT NULL COMMENT '执业证书号',
    `hospital_id` BIGINT(20) NULL COMMENT '所属医院ID',
    `introduction` TEXT NULL COMMENT '个人简介',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-在职, 0-离职)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_license_number` (`license_number`),
    KEY `idx_name` (`name`),
    KEY `idx_phone` (`phone`),
    KEY `idx_department` (`department`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表';
-- 健康记录表
CREATE TABLE IF NOT EXISTS `health_record` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `doctor_id` BIGINT(20) NULL COMMENT '医生ID',
    `record_type` VARCHAR(20) NOT NULL COMMENT '记录类型 (CHECKUP-体检, DIAGNOSIS-诊断, TREATMENT-治疗, MEDICATION-用药)',
    `record_date` DATETIME NOT NULL COMMENT '记录时间',
    `symptoms` TEXT NULL COMMENT '症状描述',
    `diagnosis` TEXT NULL COMMENT '诊断结果',
    `treatment_plan` TEXT NULL COMMENT '治疗方案',
    `medication` TEXT NULL COMMENT '用药信息',
    `vital_signs` JSON NULL COMMENT '生命体征 (血压、心率、体温等)',
    `lab_results` JSON NULL COMMENT '检验结果',
    `images` JSON NULL COMMENT '影像资料',
    `notes` TEXT NULL COMMENT '备注',
    `next_visit_date` DATETIME NULL COMMENT '下次复诊时间',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-正常, 0-作废)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_record_date` (`record_date`),
    KEY `idx_record_type` (`record_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康记录表';
-- 健康预警表
CREATE TABLE IF NOT EXISTS `health_warning` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `warning_type` VARCHAR(20) NOT NULL COMMENT '预警类型 (VITAL_SIGNS-生命体征, MEDICATION-用药, ACTIVITY-活动, EMERGENCY-紧急)',
    `warning_level` VARCHAR(10) NOT NULL COMMENT '预警级别 (LOW-低, MEDIUM-中, HIGH-高, URGENT-紧急)',
    `title` VARCHAR(100) NOT NULL COMMENT '预警标题',
    `content` TEXT NOT NULL COMMENT '预警内容',
    `trigger_value` VARCHAR(100) NULL COMMENT '触发值',
    `normal_range` VARCHAR(100) NULL COMMENT '正常范围',
    `warning_time` DATETIME NOT NULL COMMENT '预警时间',
    `handle_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '处理状态 (0-未处理, 1-处理中, 2-已处理)',
    `handle_user` VARCHAR(50) NULL COMMENT '处理人',
    `handle_time` DATETIME NULL COMMENT '处理时间',
    `handle_result` TEXT NULL COMMENT '处理结果',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_warning_type` (`warning_type`),
    KEY `idx_warning_level` (`warning_level`),
    KEY `idx_warning_time` (`warning_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康预警表';
-- 设备信息表
CREATE TABLE IF NOT EXISTS `equipment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `equipment_code` VARCHAR(50) NOT NULL COMMENT '设备编号',
    `equipment_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `equipment_type` VARCHAR(50) NOT NULL COMMENT '设备类型',
    `brand` VARCHAR(50) NULL COMMENT '品牌',
    `model` VARCHAR(50) NULL COMMENT '型号',
    `serial_number` VARCHAR(100) NULL COMMENT '序列号',
    `location` VARCHAR(200) NULL COMMENT '安装位置',
    `elderly_id` BIGINT(20) NULL COMMENT '绑定老人ID',
    `install_date` DATE NULL COMMENT '安装日期',
    `last_maintenance_date` DATE NULL COMMENT '上次维护日期',
    `next_maintenance_date` DATE NULL COMMENT '下次维护日期',
    `warranty_expire_date` DATE NULL COMMENT '保修到期日期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '设备状态 (NORMAL-正常, WARNING-预警, ERROR-故障, OFFLINE-离线)',
    `remarks` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_equipment_code` (`equipment_code`),
    KEY `idx_equipment_name` (`equipment_name`),
    KEY `idx_equipment_type` (`equipment_type`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '设备信息表';
-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `avatar` VARCHAR(500) NULL COMMENT '头像URL',
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色 (ADMIN-管理员, DOCTOR-医生, NURSE-护士, USER-普通用户)',
    `department` VARCHAR(100) NULL COMMENT '部门',
    `last_login_time` DATETIME NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) NULL COMMENT '最后登录IP',
    `login_count` INT(10) NOT NULL DEFAULT 0 COMMENT '登录次数',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-正常, 0-禁用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除 (1-已删除, 0-未删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`),
    KEY `idx_role` (`role`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表';
-- 插入初始数据
-- 插入管理员用户 (密码: admin123, 实际项目中应该加密)
INSERT INTO `user` (
        `username`,
        `password`,
        `real_name`,
        `role`,
        `department`
    )
VALUES (
        'admin',
        '$2a$10$7JB720yubVSOfvVaMWye2.bP7gR/zHQXSn0A2h.Rh5C/Z8bpNkb.G',
        '系统管理员',
        'ADMIN',
        '信息技术部'
    ) ON DUPLICATE KEY
UPDATE `username` = `username`;
-- 插入测试医生数据
INSERT INTO `doctor` (
        `name`,
        `gender`,
        `phone`,
        `email`,
        `department`,
        `title`,
        `speciality`,
        `experience_years`,
        `license_number`
    )
VALUES (
        '张医生',
        1,
        '13800138001',
        'zhang@hospital.com',
        '内科',
        '主任医师',
        '心血管疾病',
        15,
        'DOC001'
    ),
    (
        '李医生',
        0,
        '13800138002',
        'li@hospital.com',
        '老年科',
        '副主任医师',
        '老年慢性病',
        12,
        'DOC002'
    ),
    (
        '王医生',
        1,
        '13800138003',
        'wang@hospital.com',
        '康复科',
        '主治医师',
        '康复治疗',
        8,
        'DOC003'
    ) ON DUPLICATE KEY
UPDATE `name` = `name`;
-- 插入测试老人数据
INSERT INTO `elderly` (
        `name`,
        `gender`,
        `id_card`,
        `age`,
        `phone`,
        `address`,
        `health_status`,
        `family_doctor_id`
    )
VALUES (
        '张老伯',
        1,
        '320102195001011234',
        74,
        '13900139001',
        '南京市玄武区梅园新村1号',
        'HEALTHY',
        1
    ),
    (
        '李奶奶',
        0,
        '320102194801015678',
        76,
        '13900139002',
        '南京市秦淮区夫子庙2号',
        'SUBHEALTH',
        2
    ),
    (
        '王大爷',
        1,
        '320102194501019012',
        79,
        '13900139003',
        '南京市鼓楼区湖南路3号',
        'WARNING',
        3
    ) ON DUPLICATE KEY
UPDATE `name` = `name`;
-- 插入测试设备数据
INSERT INTO `equipment` (
        `equipment_code`,
        `equipment_name`,
        `equipment_type`,
        `brand`,
        `model`,
        `elderly_id`,
        `status`
    )
VALUES (
        'EQ001',
        '血压监测仪',
        '血压设备',
        '欧姆龙',
        'HEM-7136',
        1,
        'NORMAL'
    ),
    (
        'EQ002',
        '血糖仪',
        '血糖设备',
        '强生',
        'OneTouch',
        2,
        'NORMAL'
    ),
    (
        'EQ003',
        '心率监测器',
        '心率设备',
        '飞利浦',
        'HeartStart',
        3,
        'WARNING'
    ) ON DUPLICATE KEY
UPDATE `equipment_code` = `equipment_code`;
-- 医生信息表
CREATE TABLE IF NOT EXISTS `t_doctor` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `employee_number` VARCHAR(50) NOT NULL COMMENT '医生工号',
    `name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
    `gender` VARCHAR(10) NOT NULL COMMENT '性别',
    `age` INT(3) NULL COMMENT '年龄',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `email` VARCHAR(100) NULL COMMENT '邮箱地址',
    `department` VARCHAR(100) NOT NULL COMMENT '科室',
    `title` VARCHAR(50) NOT NULL COMMENT '职称',
    `specialization` TEXT NULL COMMENT '专业特长',
    `experience_years` INT(2) NULL COMMENT '从业年限',
    `hospital_id` BIGINT(20) NULL COMMENT '所属医院ID',
    `hospital_name` VARCHAR(100) NULL COMMENT '所属医院名称',
    `license_number` VARCHAR(50) NULL COMMENT '执业证书号',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-在职, 0-离职)',
    `avatar` VARCHAR(255) NULL COMMENT '头像URL',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_employee_number` (`employee_number`),
    KEY `idx_name` (`name`),
    KEY `idx_department` (`department`),
    KEY `idx_title` (`title`),
    KEY `idx_phone` (`phone`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表';
-- 健康预警表
CREATE TABLE IF NOT EXISTS `t_health_warning` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `elderly_name` VARCHAR(50) NOT NULL COMMENT '老人姓名',
    `warning_type` VARCHAR(50) NOT NULL COMMENT '预警类型',
    `warning_level` TINYINT(1) NOT NULL COMMENT '预警级别 (1-低风险, 2-中风险, 3-高风险, 4-紧急)',
    `title` VARCHAR(200) NOT NULL COMMENT '预警标题',
    `content` TEXT NOT NULL COMMENT '预警内容描述',
    `trigger_data` TEXT NULL COMMENT '触发数据',
    `data_source` VARCHAR(50) NULL COMMENT '数据来源',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '预警状态 (0-未处理, 1-已查看, 2-处理中, 3-已处理, 4-已忽略)',
    `handler_id` BIGINT(20) NULL COMMENT '处理人ID',
    `handler_name` VARCHAR(50) NULL COMMENT '处理人姓名',
    `handle_time` DATETIME NULL COMMENT '处理时间',
    `handle_remark` TEXT NULL COMMENT '处理备注',
    `is_notified_family` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已通知家属',
    `is_notified_doctor` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已通知医生',
    `trigger_time` DATETIME NOT NULL COMMENT '预警触发时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` TEXT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_warning_type` (`warning_type`),
    KEY `idx_warning_level` (`warning_level`),
    KEY `idx_status` (`status`),
    KEY `idx_trigger_time` (`trigger_time`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康预警表';
-- 健康记录表
CREATE TABLE IF NOT EXISTS `t_health_record` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `elderly_name` VARCHAR(50) NOT NULL COMMENT '老人姓名',
    `record_type` VARCHAR(50) NOT NULL COMMENT '记录类型 (体检、住院、门诊、药物、康复等)',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `doctor_id` BIGINT(20) NULL COMMENT '医生ID',
    `doctor_name` VARCHAR(50) NULL COMMENT '医生姓名',
    `hospital_name` VARCHAR(100) NULL COMMENT '医院名称',
    `department` VARCHAR(100) NULL COMMENT '科室',
    `diagnosis` TEXT NULL COMMENT '诊断结果',
    `symptoms` TEXT NULL COMMENT '症状描述',
    `treatment` TEXT NULL COMMENT '治疗方案',
    `prescription` TEXT NULL COMMENT '处方用药',
    `vital_signs` JSON NULL COMMENT '生命体征数据 (血压、心率、体温、血糖等)',
    `examination_results` JSON NULL COMMENT '检查结果数据',
    `abnormal_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '异常状态 (0-正常, 1-异常)',
    `follow_up_date` DATE NULL COMMENT '随访日期',
    `attachment_urls` JSON NULL COMMENT '附件URL列表',
    `record_source` VARCHAR(50) NULL COMMENT '记录来源 (医院上传、设备监测、手动录入等)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` TEXT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_record_type` (`record_type`),
    KEY `idx_record_date` (`record_date`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_abnormal_status` (`abnormal_status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康记录表';
-- 设备管理表
CREATE TABLE IF NOT EXISTS `t_equipment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `device_id` VARCHAR(100) NOT NULL COMMENT '设备编号',
    `device_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `device_type` VARCHAR(50) NOT NULL COMMENT '设备类型 (血压计、血糖仪、心率监测器、体温计、智能手环等)',
    `manufacturer` VARCHAR(100) NULL COMMENT '生产厂商',
    `model` VARCHAR(100) NULL COMMENT '设备型号',
    `elderly_id` BIGINT(20) NULL COMMENT '绑定老人ID',
    `elderly_name` VARCHAR(50) NULL COMMENT '绑定老人姓名',
    `installation_location` VARCHAR(200) NULL COMMENT '安装位置',
    `installation_date` DATE NULL COMMENT '安装日期',
    `last_maintenance_date` DATE NULL COMMENT '最后维护日期',
    `next_maintenance_date` DATE NULL COMMENT '下次维护日期',
    `warranty_expiry_date` DATE NULL COMMENT '保修到期日期',
    `device_status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '设备状态 (1-正常, 2-异常, 3-离线, 4-维修中, 0-已停用)',
    `battery_level` INT(3) NULL COMMENT '电池电量百分比',
    `signal_strength` INT(3) NULL COMMENT '信号强度',
    `firmware_version` VARCHAR(50) NULL COMMENT '固件版本',
    `last_data_time` DATETIME NULL COMMENT '最后数据上报时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` TEXT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_id` (`device_id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_device_type` (`device_type`),
    KEY `idx_device_status` (`device_status`),
    KEY `idx_last_data_time` (`last_data_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '设备管理表';
-- 用户管理表
CREATE TABLE IF NOT EXISTS `t_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `avatar` VARCHAR(255) NULL COMMENT '头像URL',
    `role` VARCHAR(50) NOT NULL DEFAULT 'USER' COMMENT '角色 (ADMIN-管理员, DOCTOR-医生, NURSE-护士, USER-普通用户)',
    `department` VARCHAR(100) NULL COMMENT '所属部门',
    `position` VARCHAR(100) NULL COMMENT '职位',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-正常, 0-禁用)',
    `last_login_time` DATETIME NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) NULL COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`),
    KEY `idx_role` (`role`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户管理表';
-- 插入测试数据
-- 老人档案测试数据
INSERT IGNORE INTO `t_elderly` (
        `name`,
        `gender`,
        `id_card`,
        `birth_date`,
        `age`,
        `phone`,
        `address`,
        `emergency_contact_name`,
        `emergency_contact_phone`,
        `health_status`,
        `care_level`,
        `medical_history`
    )
VALUES (
        '张三',
        1,
        '110101195001011234',
        '1950-01-01',
        74,
        '13800139001',
        '北京市朝阳区某某街道1号',
        '张小明',
        '13900139001',
        'HEALTHY',
        1,
        '高血压病史10年'
    ),
    (
        '李四',
        0,
        '110101195502022345',
        '1955-02-02',
        69,
        '13800139002',
        '北京市海淀区某某路2号',
        '李小红',
        '13900139002',
        'SUBHEALTH',
        2,
        '糖尿病病史8年'
    ),
    (
        '王五',
        1,
        '110101194803033456',
        '1948-03-03',
        76,
        '13800139003',
        '北京市西城区某某胡同3号',
        '王小华',
        '13900139003',
        'WARNING',
        3,
        '心脏病病史15年，高血压并发症'
    );
-- 医生信息测试数据
INSERT IGNORE INTO `t_doctor` (
        `employee_number`,
        `name`,
        `gender`,
        `age`,
        `phone`,
        `email`,
        `department`,
        `title`,
        `specialization`,
        `experience_years`,
        `hospital_name`
    )
VALUES (
        'D001',
        '刘医生',
        '男',
        45,
        '13700137001',
        'dr.liu@hospital.com',
        '内科',
        '主任医师',
        '心血管疾病诊疗',
        20,
        '北京市第一人民医院'
    ),
    (
        'D002',
        '陈医生',
        '女',
        38,
        '13700137002',
        'dr.chen@hospital.com',
        '内分泌科',
        '副主任医师',
        '糖尿病、甲状腺疾病',
        15,
        '北京市第一人民医院'
    ),
    (
        'D003',
        '王医生',
        '男',
        42,
        '13700137003',
        'dr.wang@hospital.com',
        '神经内科',
        '主治医师',
        '老年痴呆、帕金森病',
        18,
        '北京市第一人民医院'
    );
-- 健康预警测试数据
INSERT IGNORE INTO `t_health_warning` (
        `elderly_id`,
        `elderly_name`,
        `warning_type`,
        `warning_level`,
        `title`,
        `content`,
        `trigger_data`,
        `data_source`,
        `trigger_time`
    )
VALUES (
        1,
        '张三',
        '血压异常',
        3,
        '血压异常偏高预警',
        '监测到血压值持续偏高，建议及时就医检查',
        '收缩压180mmHg，舒张压110mmHg',
        '血压监测设备',
        NOW() - INTERVAL 2 HOUR
    ),
    (
        2,
        '李四',
        '血糖异常',
        2,
        '血糖波动预警',
        '血糖值出现较大波动，建议调整饮食和用药',
        '空腹血糖12.5mmol/L',
        '血糖监测设备',
        NOW() - INTERVAL 1 HOUR
    ),
    (
        3,
        '王五',
        '心率异常',
        4,
        '心率异常紧急预警',
        '心率异常，疑似心律不齐，建议立即就医',
        '心率35次/分钟，心律不规整',
        '心率监测设备',
        NOW() - INTERVAL 30 MINUTE
    );
-- 设备管理测试数据
INSERT IGNORE INTO `t_equipment` (
        `device_id`,
        `device_name`,
        `device_type`,
        `manufacturer`,
        `model`,
        `elderly_id`,
        `elderly_name`,
        `installation_location`,
        `installation_date`,
        `device_status`,
        `battery_level`
    )
VALUES (
        'BP001',
        '智能血压计1号',
        '血压计',
        '欧姆龙',
        'HEM-7136',
        1,
        '张三',
        '张三家客厅',
        '2024-01-01',
        1,
        85
    ),
    (
        'BG001',
        '智能血糖仪1号',
        '血糖仪',
        '强生',
        'OneTouch',
        2,
        '李四',
        '李四家卧室',
        '2024-01-02',
        1,
        72
    ),
    (
        'HR001',
        '心率监测器1号',
        '心率监测器',
        '华为',
        'WATCH GT3',
        3,
        '王五',
        '王五随身佩戴',
        '2024-01-03',
        2,
        45
    );
-- 用户管理测试数据
INSERT IGNORE INTO `t_user` (
        `username`,
        `password`,
        `real_name`,
        `email`,
        `phone`,
        `role`,
        `department`
    )
VALUES (
        'admin',
        '$2a$10$7JB720yubVSP5XEH.UJNKe8I8.TZCpGPq1FtCm1oEhGPwGr/dLd2C',
        '系统管理员',
        'admin@smartcare.com',
        '13500135000',
        'ADMIN',
        '系统管理部'
    ),
    (
        'doctor01',
        '$2a$10$7JB720yubVSP5XEH.UJNKe8I8.TZCpGPq1FtCm1oEhGPwGr/dLd2C',
        '刘医生',
        'dr.liu@hospital.com',
        '13700137001',
        'DOCTOR',
        '内科'
    ),
    (
        'nurse01',
        '$2a$10$7JB720yubVSP5XEH.UJNKe8I8.TZCpGPq1FtCm1oEhGPwGr/dLd2C',
        '护士小张',
        'nurse.zhang@hospital.com',
        '13600136001',
        'NURSE',
        '护理部'
    );
COMMIT;