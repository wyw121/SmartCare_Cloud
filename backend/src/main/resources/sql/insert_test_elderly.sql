-- 为家属测试添加老人数据
-- 确保elderly表中有ID为1和2的老人记录

-- 插入测试老人数据（如果不存在）
INSERT IGNORE INTO `elderly` (
    `id`, `name`, `gender`, `id_card`, `birth_date`, `age`, `phone`, 
    `address`, `emergency_contact_name`, `emergency_contact_phone`, 
    `health_status`, `care_level`, `insurance_type`, `medical_history`, 
    `room_number`, `guardian_name`, `status`, `create_by`, `create_time`
) VALUES 
(1, '李爷爷', 1, '110101193001011234', '1930-01-01', 95, '13800138101', 
 '北京市朝阳区建国路88号', '李明华', '13800138001', 
 'HEALTHY', 1, '医保', '高血压病史10年，控制良好', 
 'A101', '李明华', 1, 1, NOW()),
(2, '王奶奶', 0, '110101193503152345', '1935-03-15', 90, '13800138102', 
 '北京市海淀区中关村大街99号', '李明华', '13800138001', 
 'WARNING', 2, '医保', '糖尿病、关节炎', 
 'B205', '李明华', 1, 1, NOW());

-- 更新现有记录（如果已存在）
UPDATE `elderly` 
SET 
    `name` = '李爷爷',
    `gender` = 1,
    `birth_date` = '1930-01-01',
    `age` = 95,
    `phone` = '13800138101',
    `health_status` = 'HEALTHY',
    `care_level` = 1,
    `room_number` = 'A101',
    `guardian_name` = '李明华',
    `status` = 1,
    `update_time` = NOW()
WHERE `id` = 1;

UPDATE `elderly` 
SET 
    `name` = '王奶奶',
    `gender` = 0,
    `birth_date` = '1935-03-15',
    `age` = 90,
    `phone` = '13800138102',
    `health_status` = 'WARNING',
    `care_level` = 2,
    `room_number` = 'B205',
    `guardian_name` = '李明华',
    `status` = 1,
    `update_time` = NOW()
WHERE `id` = 2;

-- 确认数据
SELECT 'elderly test data inserted/updated successfully' as message;
SELECT id, name, gender, age, health_status, room_number FROM elderly WHERE id IN (1, 2);
