-- =====================================================
-- 智慧医养平台 - 数据库迁移脚本 V1.0
-- 创建日期: 2025-10-28
-- 说明: 创建额外的索引和外键约束(可选)
-- 注意: 外键约束在开发阶段可选择性添加,生产环境建议谨慎使用
-- =====================================================

-- 使用数据库
USE smartcare_cloud;

DROP PROCEDURE IF EXISTS add_index_if_not_exists;

DELIMITER $$

CREATE PROCEDURE add_index_if_not_exists(
	IN dbName VARCHAR(64),
	IN tblName VARCHAR(64),
	IN idxName VARCHAR(64),
	IN idxDefinition TEXT
)
BEGIN
	IF NOT EXISTS (
		SELECT 1
		FROM INFORMATION_SCHEMA.STATISTICS
		WHERE TABLE_SCHEMA = dbName
		  AND TABLE_NAME = tblName
		  AND INDEX_NAME = idxName
	) THEN
		SET @ddl = CONCAT('ALTER TABLE `', dbName, '`.`', tblName, '` ADD ', idxDefinition);
		PREPARE stmt FROM @ddl;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END$$

DELIMITER ;

SET @current_db = DATABASE();

-- =====================================================
-- 性能优化: 复合索引
-- =====================================================

-- 老人表: 常用查询组合索引
CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_org_status', 'INDEX `idx_elderly_org_status` (`organization_id`, `status`)');
CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_care_status', 'INDEX `idx_elderly_care_status` (`care_level`, `status`)');

-- 医生-老人关联表: 常用查询组合索引
CALL add_index_if_not_exists(@current_db, 'doctor_elderly_relation', 'idx_doc_rel_doctor_status', 'INDEX `idx_doc_rel_doctor_status` (`doctor_id`, `status`)');
CALL add_index_if_not_exists(@current_db, 'doctor_elderly_relation', 'idx_doc_rel_elderly_status', 'INDEX `idx_doc_rel_elderly_status` (`elderly_id`, `status`)');

-- 护工-老人关联表: 常用查询组合索引
CALL add_index_if_not_exists(@current_db, 'nurse_elderly_relation', 'idx_nurse_rel_nurse_status', 'INDEX `idx_nurse_rel_nurse_status` (`nurse_id`, `status`)');
CALL add_index_if_not_exists(@current_db, 'nurse_elderly_relation', 'idx_nurse_rel_elderly_status', 'INDEX `idx_nurse_rel_elderly_status` (`elderly_id`, `status`)');

-- 家属-老人关联表: 常用查询组合索引
CALL add_index_if_not_exists(@current_db, 'family_elderly_relation', 'idx_family_rel_family_status', 'INDEX `idx_family_rel_family_status` (`family_user_id`, `status`)');
CALL add_index_if_not_exists(@current_db, 'family_elderly_relation', 'idx_family_rel_elderly_status', 'INDEX `idx_family_rel_elderly_status` (`elderly_id`, `status`)');

-- 健康预警表: 按状态和时间查询
CALL add_index_if_not_exists(@current_db, 't_health_warning', 'idx_warning_status_time', 'INDEX `idx_warning_status_time` (`status`, `trigger_time`)');
CALL add_index_if_not_exists(@current_db, 't_health_warning', 'idx_warning_elderly_status', 'INDEX `idx_warning_elderly_status` (`elderly_id`, `status`)');
CALL add_index_if_not_exists(@current_db, 't_health_warning', 'idx_warning_trigger_time', 'INDEX `idx_warning_trigger_time` (`trigger_time`)');

-- 护理记录表: 按老人和日期范围查询
CALL add_index_if_not_exists(@current_db, 'nursing_record', 'idx_elderly_date_range', 'INDEX `idx_elderly_date_range` (`elderly_id`, `record_date`, `record_time`)');

-- 用药记录表: 按老人和状态查询
CALL add_index_if_not_exists(@current_db, 'medication_record', 'idx_elderly_medication_status', 'INDEX `idx_elderly_medication_status` (`elderly_id`, `status`)');

-- 巡诊记录表: 按医生和日期范围查询
CALL add_index_if_not_exists(@current_db, 'visit_record', 'idx_doctor_date_range', 'INDEX `idx_doctor_date_range` (`doctor_id`, `visit_date`)');

-- 操作日志表: 按用户和时间范围查询
CALL add_index_if_not_exists(@current_db, 'operation_log', 'idx_user_time_range', 'INDEX `idx_user_time_range` (`user_id`, `operation_time`)');


-- =====================================================
-- 外键约束 (可选 - 仅在需要严格数据完整性时添加)
-- 注意: 外键会影响插入和删除性能,建议在应用层处理数据完整性
-- =====================================================

-- 如需启用外键约束,请取消下方注释

/*
-- 医生-老人关联表外键
ALTER TABLE `doctor_elderly_relation`
ADD CONSTRAINT `fk_doctor_relation_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_doctor_relation_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 护工-老人关联表外键
ALTER TABLE `nurse_elderly_relation`
ADD CONSTRAINT `fk_nurse_relation_nurse` FOREIGN KEY (`nurse_id`) REFERENCES `nurse_info`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_nurse_relation_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 家属-老人关联表外键
ALTER TABLE `family_elderly_relation`
ADD CONSTRAINT `fk_family_relation_family` FOREIGN KEY (`family_user_id`) REFERENCES `family_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_family_relation_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 护理记录表外键
ALTER TABLE `nursing_record`
ADD CONSTRAINT `fk_nursing_record_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_nursing_record_nurse` FOREIGN KEY (`nurse_id`) REFERENCES `nurse_info`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 用药记录表外键
ALTER TABLE `medication_record`
ADD CONSTRAINT `fk_medication_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_medication_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- 巡诊记录表外键
ALTER TABLE `visit_record`
ADD CONSTRAINT `fk_visit_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_visit_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 老人表外键
ALTER TABLE `elderly`
ADD CONSTRAINT `fk_elderly_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `fk_elderly_doctor` FOREIGN KEY (`responsible_doctor_id`) REFERENCES `t_doctor`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `fk_elderly_nurse` FOREIGN KEY (`responsible_nurse_id`) REFERENCES `nurse_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- 医生表外键
ALTER TABLE `t_doctor`
ADD CONSTRAINT `fk_doctor_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `fk_doctor_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 护工表外键
ALTER TABLE `nurse_info`
ADD CONSTRAINT `fk_nurse_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `fk_nurse_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 社工表外键
ALTER TABLE `social_worker_info`
ADD CONSTRAINT `fk_social_worker_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `fk_social_worker_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 系统用户表外键
ALTER TABLE `sys_user`
ADD CONSTRAINT `fk_user_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization_info`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
*/

-- =====================================================
-- 性能优化建议
-- =====================================================

-- 1. 分析表统计信息(在数据导入后执行)
-- ANALYZE TABLE elderly, t_doctor, nurse_info, health_warning, nursing_record, medication_record, visit_record, operation_log;

-- 2. 优化表(在大量数据操作后执行)
-- OPTIMIZE TABLE elderly, t_doctor, nurse_info, health_warning, nursing_record, medication_record, visit_record, operation_log;

-- 3. 查看索引使用情况
-- SELECT * FROM sys.schema_unused_indexes WHERE object_schema = 'smartcare_db';

-- =====================================================
-- 脚本执行完成
-- =====================================================
SELECT '索引创建完成! 外键约束默认未启用(如需启用请取消注释)' AS Message;
