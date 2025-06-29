-- 插入老人测试数据
USE smartcare_cloud;

-- 删除现有测试数据
DELETE FROM elderly WHERE id IN (1, 2, 3);

-- 插入测试数据
INSERT INTO elderly (
    id, name, id_card, phone, address, gender, birth_date, 
    emergency_contact, emergency_phone, health_status,
    care_level, status, blood_type, height, weight,
    medical_history, allergy_history, medication_history,
    room_number, guardian_name, guardian_phone, guardian_relation,
    remark, created_time, updated_time
) VALUES 
(1, '张明', '110101194501010001', '13800138001', '北京市朝阳区长安街1号', 1, '1945-01-01',
 '张小红', '13800138002', 'HEALTHY', 1, 1, 'A', 175.0, 68.5,
 '高血压病史5年', '对青霉素过敏', '降压药：氨氯地平片 每日一次',
 'A101', '张小红', '13800138002', '女儿',
 '身体状况良好，定期体检', NOW(), NOW()),

(2, '李华', '110101195002020002', '13800138003', '北京市海淀区中关村大街2号', 2, '1950-02-02',
 '李强', '13800138004', 'SUBHEALTH', 2, 1, 'B', 162.0, 55.0,
 '糖尿病、关节炎', '无已知过敏', '降糖药：二甲双胍 每日两次',
 'B205', '李强', '13800138004', '儿子',
 '需要协助行走，血糖控制良好', NOW(), NOW()),

(3, '王芳', '110101194803030003', '13800138005', '北京市西城区西单大街3号', 2, '1948-03-03',
 '王军', '13800138006', 'WARNING', 3, 1, 'O', 158.0, 50.0,
 '心脏病、高血压、糖尿病', '对磺胺类药物过敏', '多种药物，详见用药清单',
 'C301', '王军', '13800138006', '儿子',
 '需要特别照护，定期监测生命体征', NOW(), NOW());

-- 查询验证数据
SELECT * FROM elderly;
