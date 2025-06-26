-- 医生排班表
CREATE TABLE IF NOT EXISTS `t_doctor_schedule` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `doctor_id` BIGINT(20) NOT NULL COMMENT '医生ID',
    `doctor_name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
    `department` VARCHAR(100) NOT NULL COMMENT '科室',
    `work_date` DATE NOT NULL COMMENT '工作日期',
    `work_time_start` TIME NOT NULL COMMENT '工作开始时间',
    `work_time_end` TIME NOT NULL COMMENT '工作结束时间',
    `schedule_type` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '排班类型(NORMAL-正常班,NIGHT-夜班,EMERGENCY-急诊)',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态(1-正常,0-取消)',
    `max_appointments` INT(3) DEFAULT 30 COMMENT '最大预约数量',
    `current_appointments` INT(3) DEFAULT 0 COMMENT '当前预约数量',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_work_date` (`work_date`),
    KEY `idx_department` (`department`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生排班表';

-- 医生患者关系表
CREATE TABLE IF NOT EXISTS `t_doctor_elderly_relation` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `doctor_id` BIGINT(20) NOT NULL COMMENT '医生ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `relation_type` VARCHAR(20) NOT NULL DEFAULT 'PRIMARY' COMMENT '关系类型(PRIMARY-主管医生,CONSULT-会诊医生,TEMPORARY-临时医生)',
    `start_date` DATE NOT NULL COMMENT '关系开始日期',
    `end_date` DATE NULL COMMENT '关系结束日期',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态(1-有效,0-无效)',
    `assign_reason` VARCHAR(500) NULL COMMENT '分配原因',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_doctor_elderly` (`doctor_id`, `elderly_id`, `relation_type`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_elderly_id` (`elderly_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生患者关系表';

-- 医生绩效统计表
CREATE TABLE IF NOT EXISTS `t_doctor_performance` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `doctor_id` BIGINT(20) NOT NULL COMMENT '医生ID',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `stat_type` VARCHAR(20) NOT NULL DEFAULT 'DAILY' COMMENT '统计类型(DAILY-日统计,MONTHLY-月统计,YEARLY-年统计)',
    `patient_count` INT(6) DEFAULT 0 COMMENT '服务患者数量',
    `consultation_count` INT(6) DEFAULT 0 COMMENT '诊疗次数',
    `health_record_count` INT(6) DEFAULT 0 COMMENT '健康记录数量',
    `warning_handled_count` INT(6) DEFAULT 0 COMMENT '处理预警数量',
    `satisfaction_score` DECIMAL(3,2) DEFAULT 0.00 COMMENT '满意度评分(0-5)',
    `working_hours` DECIMAL(5,2) DEFAULT 0.00 COMMENT '工作时长(小时)',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_doctor_date_type` (`doctor_id`, `stat_date`, `stat_type`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生绩效统计表';
