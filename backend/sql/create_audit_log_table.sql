-- 操作审计日志表
CREATE TABLE IF NOT EXISTS `audit_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型: LOGIN-登录, LOGOUT-登出, CREATE-新增, UPDATE-修改, DELETE-删除, EXPORT-导出, IMPORT-导入',
  `operation_module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块: ELDERLY-老人管理, DOCTOR-医生管理, HEALTH_WARNING-健康预警等',
  `operation_desc` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法: GET, POST, PUT, DELETE',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `request_params` TEXT DEFAULT NULL COMMENT '请求参数(JSON格式)',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理(浏览器信息)',
  `operation_status` TINYINT DEFAULT 1 COMMENT '操作状态: 1-成功, 0-失败',
  `error_message` TEXT DEFAULT NULL COMMENT '错误信息(失败时记录)',
  `execution_time` INT DEFAULT NULL COMMENT '执行时间(毫秒)',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_operation_type` (`operation_type`),
  INDEX `idx_operation_module` (`operation_module`),
  INDEX `idx_created_time` (`created_time`),
  INDEX `idx_ip_address` (`ip_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作审计日志表';

-- 插入示例数据
INSERT INTO `audit_log` (`user_id`, `username`, `operation_type`, `operation_module`, `operation_desc`, `request_method`, `request_url`, `ip_address`, `operation_status`, `execution_time`) 
VALUES 
(1, 'admin', 'LOGIN', 'SYSTEM', '用户登录系统', 'POST', '/api/auth/login', '127.0.0.1', 1, 250),
(1, 'admin', 'CREATE', 'ELDERLY', '创建老人档案：张明', 'POST', '/api/elderly', '127.0.0.1', 1, 150),
(1, 'admin', 'UPDATE', 'ELDERLY', '更新老人档案：李华', 'PUT', '/api/elderly/2', '127.0.0.1', 1, 120),
(2, 'doctor01', 'CREATE', 'HEALTH_WARNING', '创建健康预警：血压异常', 'POST', '/api/health-warning', '192.168.1.100', 1, 180);
