-- =====================================================
-- 智慧医养平台 - 基础测试数据
-- 生成时间: 2025-10-29T00:28:22.089Z
-- =====================================================

USE smartcare_cloud;

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. 机构信息数据
-- =====================================================
TRUNCATE TABLE organization_info;
INSERT INTO organization_info (id, org_code, org_name, org_type, parent_org_id, contact_person, contact_phone, address, capacity, current_occupancy, status) VALUES
(1, 'ORG001', '智慧医养综合服务中心', 'nursing_home', NULL, '李经理', '13800138000', '北京市朝阳区XX路XX号', 200, 126, 1),
(2, 'ORG002', '附属康复医院', 'hospital', NULL, '李经理', '13800138000', '北京市朝阳区XX路XX号', 100, 79, 1),
(3, 'ORG003', '阳光养老院', 'nursing_home', NULL, '李经理', '13800138000', '北京市朝阳区XX路XX号', 150, 97, 1),
(4, 'ORG004', '社区健康服务中心', 'community', NULL, '李经理', '13800138000', '北京市朝阳区XX路XX号', 50, 32, 1);

-- =====================================================
-- 2. 系统用户数据
-- =====================================================
-- 2.1 系统管理员
TRUNCATE TABLE sys_user;
INSERT INTO sys_user (id, username, password, real_name, role_code, organization_id, status) VALUES
-- 2.2 业务管理员
-- 2.3 医生用户
-- 2.4 护工用户
-- 2.5 社工用户
-- 2.6 家属用户
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 'system_admin', 1, 1)
(2, 'admin2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员2', 'system_admin', 4, 1)
(3, 'admin3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员3', 'system_admin', 1, 1)
(4, 'business_admin1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '业务管理员1', 'business_admin', 1, 1)
(5, 'business_admin2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '业务管理员2', 'business_admin', 2, 1)
(6, 'business_admin3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '业务管理员3', 'business_admin', 3, 1)
(7, 'doctor1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张伟医生', 'doctor', 4, 1)
(8, 'doctor2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李娜医生', 'doctor', 3, 1)
(9, 'doctor3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王芳医生', 'doctor', 4, 1)
(10, 'doctor4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '刘强医生', 'doctor', 2, 1)
(11, 'doctor5', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '陈敏医生', 'doctor', 3, 1)
(12, 'doctor6', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '杨洋医生', 'doctor', 3, 1)
(13, 'doctor7', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '赵丽医生', 'doctor', 1, 1)
(14, 'doctor8', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '黄刚医生', 'doctor', 2, 1)
(15, 'doctor9', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '周静医生', 'doctor', 3, 1)
(16, 'doctor10', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '吴军医生', 'doctor', 4, 1)
(17, 'nurse1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工1', 'nurse', 3, 1)
(18, 'nurse2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工2', 'nurse', 4, 1)
(19, 'nurse3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工3', 'nurse', 4, 1)
(20, 'nurse4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工4', 'nurse', 2, 1)
(21, 'nurse5', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工5', 'nurse', 3, 1)
(22, 'nurse6', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工6', 'nurse', 3, 1)
(23, 'nurse7', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工7', 'nurse', 4, 1)
(24, 'nurse8', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工8', 'nurse', 2, 1)
(25, 'nurse9', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工9', 'nurse', 2, 1)
(26, 'nurse10', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工10', 'nurse', 1, 1)
(27, 'nurse11', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工11', 'nurse', 1, 1)
(28, 'nurse12', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工12', 'nurse', 4, 1)
(29, 'nurse13', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工13', 'nurse', 2, 1)
(30, 'nurse14', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工14', 'nurse', 2, 1)
(31, 'nurse15', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工15', 'nurse', 2, 1)
(32, 'nurse16', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工16', 'nurse', 3, 1)
(33, 'nurse17', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工17', 'nurse', 3, 1)
(34, 'nurse18', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工18', 'nurse', 2, 1)
(35, 'nurse19', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工19', 'nurse', 3, 1)
(36, 'nurse20', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '护工20', 'nurse', 2, 1)
(37, 'social_worker1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '社工1', 'social_worker', 3, 1)
(38, 'social_worker2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '社工2', 'social_worker', 3, 1)
(39, 'social_worker3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '社工3', 'social_worker', 2, 1)
(40, 'social_worker4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '社工4', 'social_worker', 3, 1)
(41, 'social_worker5', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '社工5', 'social_worker', 1, 1)
(42, 'family1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属1', 'family', NULL, 1)
(43, 'family2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属2', 'family', NULL, 1)
(44, 'family3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属3', 'family', NULL, 1)
(45, 'family4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属4', 'family', NULL, 1)
(46, 'family5', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属5', 'family', NULL, 1)
(47, 'family6', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属6', 'family', NULL, 1)
(48, 'family7', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属7', 'family', NULL, 1)
(49, 'family8', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属8', 'family', NULL, 1)
(50, 'family9', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属9', 'family', NULL, 1)
(51, 'family10', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属10', 'family', NULL, 1)
(52, 'family11', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属11', 'family', NULL, 1)
(53, 'family12', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属12', 'family', NULL, 1)
(54, 'family13', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属13', 'family', NULL, 1)
(55, 'family14', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属14', 'family', NULL, 1)
(56, 'family15', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属15', 'family', NULL, 1)
(57, 'family16', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属16', 'family', NULL, 1)
(58, 'family17', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属17', 'family', NULL, 1)
(59, 'family18', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属18', 'family', NULL, 1)
(60, 'family19', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属19', 'family', NULL, 1)
(61, 'family20', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属20', 'family', NULL, 1)
(62, 'family21', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属21', 'family', NULL, 1)
(63, 'family22', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属22', 'family', NULL, 1)
(64, 'family23', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属23', 'family', NULL, 1)
(65, 'family24', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属24', 'family', NULL, 1)
(66, 'family25', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属25', 'family', NULL, 1)
(67, 'family26', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属26', 'family', NULL, 1)
(68, 'family27', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属27', 'family', NULL, 1)
(69, 'family28', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属28', 'family', NULL, 1)
(70, 'family29', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属29', 'family', NULL, 1)
(71, 'family30', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属30', 'family', NULL, 1)
(72, 'family31', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属31', 'family', NULL, 1)
(73, 'family32', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属32', 'family', NULL, 1)
(74, 'family33', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属33', 'family', NULL, 1)
(75, 'family34', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属34', 'family', NULL, 1)
(76, 'family35', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属35', 'family', NULL, 1)
(77, 'family36', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属36', 'family', NULL, 1)
(78, 'family37', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属37', 'family', NULL, 1)
(79, 'family38', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属38', 'family', NULL, 1)
(80, 'family39', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属39', 'family', NULL, 1)
(81, 'family40', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属40', 'family', NULL, 1)
(82, 'family41', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属41', 'family', NULL, 1)
(83, 'family42', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属42', 'family', NULL, 1)
(84, 'family43', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属43', 'family', NULL, 1)
(85, 'family44', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属44', 'family', NULL, 1)
(86, 'family45', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属45', 'family', NULL, 1)
(87, 'family46', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属46', 'family', NULL, 1)
(88, 'family47', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属47', 'family', NULL, 1)
(89, 'family48', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属48', 'family', NULL, 1)
(90, 'family49', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属49', 'family', NULL, 1)
(91, 'family50', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '家属50', 'family', NULL, 1)

-- =====================================================
-- 3. 医生信息数据
-- =====================================================
TRUNCATE TABLE t_doctor;
INSERT INTO t_doctor (id, user_id, organization_id, employee_number, name, gender, phone, email, department, title, specialization, experience_years, max_patients, status) VALUES
(1, 7, 4, 'DOC0001', '张伟医生', '男', '138001380200', 'doctor1@smartcare.com', '康复科', '主治医师', '老年慢性病管理', 11, 60, 1),
(2, 8, 3, 'DOC0002', '李娜医生', '女', '138001380201', 'doctor2@smartcare.com', '外科', '主任医师', '老年慢性病管理', 10, 55, 1),
(3, 9, 4, 'DOC0003', '王芳医生', '男', '138001380202', 'doctor3@smartcare.com', '内科', '主任医师', '老年慢性病管理', 24, 43, 1),
(4, 10, 2, 'DOC0004', '刘强医生', '男', '138001380203', 'doctor4@smartcare.com', '中医科', '医师', '老年慢性病管理', 9, 51, 1),
(5, 11, 3, 'DOC0005', '陈敏医生', '女', '138001380204', 'doctor5@smartcare.com', '外科', '医师', '老年慢性病管理', 20, 46, 1),
(6, 12, 3, 'DOC0006', '杨洋医生', '男', '138001380205', 'doctor6@smartcare.com', '老年科', '副主任医师', '老年慢性病管理', 19, 42, 1),
(7, 13, 1, 'DOC0007', '赵丽医生', '女', '138001380206', 'doctor7@smartcare.com', '老年科', '医师', '老年慢性病管理', 14, 59, 1),
(8, 14, 2, 'DOC0008', '黄刚医生', '女', '138001380207', 'doctor8@smartcare.com', '康复科', '主任医师', '老年慢性病管理', 10, 52, 1),
(9, 15, 3, 'DOC0009', '周静医生', '女', '138001380208', 'doctor9@smartcare.com', '老年科', '主治医师', '老年慢性病管理', 15, 48, 1),
(10, 16, 4, 'DOC0010', '吴军医生', '女', '138001380209', 'doctor10@smartcare.com', '外科', '主任医师', '老年慢性病管理', 10, 40, 1);

-- =====================================================
-- 4. 护工信息数据
-- =====================================================
TRUNCATE TABLE nurse_info;
INSERT INTO nurse_info (id, user_id, organization_id, employee_number, name, gender, phone, work_area, max_care_count, status) VALUES
(1, 17, 3, 'NUR0001', '护工1', 0, '138001380300', '四病区', 3, 1),
(2, 18, 4, 'NUR0002', '护工2', 0, '138001380301', '四病区', 4, 1),
(3, 19, 4, 'NUR0003', '护工3', 1, '138001380302', '三病区', 3, 1),
(4, 20, 2, 'NUR0004', '护工4', 1, '138001380303', '三病区', 5, 1),
(5, 21, 3, 'NUR0005', '护工5', 0, '138001380304', '四病区', 4, 1),
(6, 22, 3, 'NUR0006', '护工6', 0, '138001380305', '四病区', 3, 1),
(7, 23, 4, 'NUR0007', '护工7', 0, '138001380306', '二病区', 5, 1),
(8, 24, 2, 'NUR0008', '护工8', 1, '138001380307', '四病区', 4, 1),
(9, 25, 2, 'NUR0009', '护工9', 1, '138001380308', '二病区', 6, 1),
(10, 26, 1, 'NUR0010', '护工10', 1, '138001380309', '四病区', 6, 1),
(11, 27, 1, 'NUR0011', '护工11', 0, '138001380310', '二病区', 6, 1),
(12, 28, 4, 'NUR0012', '护工12', 0, '138001380311', '三病区', 3, 1),
(13, 29, 2, 'NUR0013', '护工13', 1, '138001380312', '四病区', 3, 1),
(14, 30, 2, 'NUR0014', '护工14', 0, '138001380313', '一病区', 6, 1),
(15, 31, 2, 'NUR0015', '护工15', 1, '138001380314', '四病区', 6, 1),
(16, 32, 3, 'NUR0016', '护工16', 1, '138001380315', '二病区', 5, 1),
(17, 33, 3, 'NUR0017', '护工17', 1, '138001380316', '三病区', 6, 1),
(18, 34, 2, 'NUR0018', '护工18', 0, '138001380317', '一病区', 3, 1),
(19, 35, 3, 'NUR0019', '护工19', 1, '138001380318', '四病区', 3, 1),
(20, 36, 2, 'NUR0020', '护工20', 1, '138001380319', '三病区', 4, 1);

-- =====================================================
-- 5. 社工信息数据
-- =====================================================
TRUNCATE TABLE social_worker_info;
INSERT INTO social_worker_info (id, user_id, organization_id, employee_number, name, gender, phone, specialization, status) VALUES
(1, 37, 3, 'SW0001', '社工1', 1, '138001380400', '心理咨询与康复辅导', 1),
(2, 38, 3, 'SW0002', '社工2', 0, '138001380401', '心理咨询与康复辅导', 1),
(3, 39, 2, 'SW0003', '社工3', 0, '138001380402', '心理咨询与康复辅导', 1),
(4, 40, 3, 'SW0004', '社工4', 1, '138001380403', '心理咨询与康复辅导', 1),
(5, 41, 1, 'SW0005', '社工5', 0, '138001380404', '心理咨询与康复辅导', 1);

-- =====================================================
-- 6. 老人档案数据
-- =====================================================
TRUNCATE TABLE elderly;
INSERT INTO elderly (id, organization_id, name, gender, birth_date, phone, address, room_number, admission_date, admission_type, health_status, care_level, disability_level, payment_method, responsible_doctor_id, responsible_nurse_id, status) VALUES
(1, 1, '徐刚', 1, '1944-12-03', '13800138000', '北京市朝阳区XX街道XX号', '314', '2022-07-30', 'long_term', 'poor', 1, 3, 'insurance', 5, 3, 1),
(2, 1, '周娟', 0, '1936-08-21', '13800138001', '北京市朝阳区XX街道XX号', '520', '2023-03-31', 'long_term', 'poor', 3, 1, 'insurance', 9, 14, 1),
(3, 2, '陈兰', 0, '1932-11-03', '13800138002', '北京市朝阳区XX街道XX号', '111', '2024-06-19', 'long_term', 'good', 2, 3, 'self', 10, 6, 1),
(4, 3, '赵文', 1, '1957-07-16', '13800138003', '北京市朝阳区XX街道XX号', '414', '2023-10-06', 'long_term', 'fair', 2, 3, 'self', 3, 8, 1),
(5, 3, '赵刚', 1, '1944-07-21', '13800138004', '北京市朝阳区XX街道XX号', '105', '2022-08-05', 'long_term', 'poor', 3, 1, 'government', 2, 7, 1),
(6, 1, '胡国', 1, '1942-02-12', '13800138005', '北京市朝阳区XX街道XX号', '309', '2023-12-18', 'long_term', 'fair', 2, 2, 'insurance', 7, 15, 1),
(7, 4, '徐兰', 0, '1956-01-25', '13800138006', '北京市朝阳区XX街道XX号', '407', '2023-04-07', 'long_term', 'fair', 1, 3, 'insurance', 4, 18, 1),
(8, 1, '孙强', 1, '1956-05-12', '13800138007', '北京市朝阳区XX街道XX号', '517', '2023-12-24', 'long_term', 'poor', 2, 1, 'government', 4, 20, 1),
(9, 3, '徐华', 1, '1941-02-13', '13800138008', '北京市朝阳区XX街道XX号', '420', '2024-08-01', 'long_term', 'fair', 3, 1, 'government', 9, 15, 1),
(10, 1, '孙兰', 0, '1941-02-25', '13800138009', '北京市朝阳区XX街道XX号', '413', '2022-05-15', 'long_term', 'poor', 3, 1, 'insurance', 10, 18, 1),
(11, 2, '陈伟', 1, '1941-10-15', '13800138010', '北京市朝阳区XX街道XX号', '206', '2022-11-04', 'long_term', 'good', 2, 1, 'self', 5, 2, 1),
(12, 1, '陈刚', 1, '1944-05-31', '13800138011', '北京市朝阳区XX街道XX号', '218', '2023-09-15', 'long_term', 'fair', 2, 2, 'insurance', 8, 5, 1),
(13, 2, '张红', 0, '1945-11-01', '13800138012', '北京市朝阳区XX街道XX号', '403', '2023-04-11', 'long_term', 'fair', 3, 3, 'insurance', 7, 16, 1),
(14, 1, '何娟', 0, '1953-11-21', '13800138013', '北京市朝阳区XX街道XX号', '111', '2024-09-27', 'long_term', 'poor', 2, 2, 'insurance', 2, 18, 1),
(15, 1, '赵秀', 0, '1940-11-17', '13800138014', '北京市朝阳区XX街道XX号', '402', '2023-11-27', 'long_term', 'fair', 3, 2, 'insurance', 5, 15, 1),
(16, 3, '王梅', 0, '1930-06-27', '13800138015', '北京市朝阳区XX街道XX号', '113', '2022-04-03', 'long_term', 'good', 1, 3, 'insurance', 2, 12, 1),
(17, 3, '郭兰', 0, '1933-11-04', '13800138016', '北京市朝阳区XX街道XX号', '102', '2022-07-13', 'long_term', 'poor', 1, 2, 'insurance', 7, 20, 1),
(18, 4, '吴秀', 0, '1954-09-09', '13800138017', '北京市朝阳区XX街道XX号', '517', '2022-08-29', 'long_term', 'good', 2, 3, 'government', 5, 7, 1),
(19, 3, '吴强', 1, '1954-05-16', '13800138018', '北京市朝阳区XX街道XX号', '419', '2022-06-30', 'long_term', 'poor', 3, 3, 'government', 2, 10, 1),
(20, 4, '孙文', 1, '1947-01-29', '13800138019', '北京市朝阳区XX街道XX号', '308', '2023-12-04', 'long_term', 'fair', 2, 3, 'insurance', 7, 5, 1),
(21, 1, '李梅', 0, '1950-09-01', '13800138020', '北京市朝阳区XX街道XX号', '308', '2022-10-31', 'long_term', 'poor', 1, 1, 'insurance', 10, 8, 1),
(22, 3, '马华', 1, '1946-03-22', '13800138021', '北京市朝阳区XX街道XX号', '211', '2024-09-16', 'long_term', 'fair', 2, 1, 'self', 10, 4, 1),
(23, 4, '黄建', 1, '1955-12-27', '13800138022', '北京市朝阳区XX街道XX号', '410', '2024-04-01', 'long_term', 'fair', 2, 2, 'government', 4, 4, 1),
(24, 1, '林明', 1, '1957-10-23', '13800138023', '北京市朝阳区XX街道XX号', '413', '2023-02-24', 'long_term', 'poor', 2, 2, 'government', 5, 4, 1),
(25, 2, '陈建', 1, '1958-08-25', '13800138024', '北京市朝阳区XX街道XX号', '411', '2024-07-19', 'long_term', 'poor', 2, 2, 'self', 9, 13, 1),
(26, 1, '徐军', 1, '1952-08-20', '13800138025', '北京市朝阳区XX街道XX号', '406', '2024-01-09', 'long_term', 'good', 2, 3, 'insurance', 2, 3, 1),
(27, 1, '李强', 1, '1945-02-28', '13800138026', '北京市朝阳区XX街道XX号', '108', '2022-12-14', 'long_term', 'fair', 1, 1, 'insurance', 3, 16, 1),
(28, 4, '郭娟', 0, '1930-08-04', '13800138027', '北京市朝阳区XX街道XX号', '406', '2024-01-28', 'long_term', 'poor', 2, 1, 'insurance', 5, 18, 1),
(29, 1, '马梅', 0, '1930-02-24', '13800138028', '北京市朝阳区XX街道XX号', '319', '2024-09-16', 'long_term', 'fair', 3, 2, 'insurance', 4, 9, 1),
(30, 2, '李静', 0, '1952-11-26', '13800138029', '北京市朝阳区XX街道XX号', '114', '2022-05-23', 'long_term', 'good', 1, 3, 'government', 7, 12, 1),
(31, 2, '何芳', 0, '1959-11-21', '13800138030', '北京市朝阳区XX街道XX号', '520', '2022-03-04', 'long_term', 'fair', 2, 1, 'government', 4, 1, 1),
(32, 1, '吴勇', 1, '1934-03-24', '13800138031', '北京市朝阳区XX街道XX号', '102', '2023-11-10', 'long_term', 'fair', 3, 2, 'government', 4, 7, 1),
(33, 3, '吴文', 1, '1944-06-18', '13800138032', '北京市朝阳区XX街道XX号', '419', '2024-09-06', 'long_term', 'good', 1, 2, 'government', 6, 5, 1),
(34, 2, '高军', 1, '1939-08-25', '13800138033', '北京市朝阳区XX街道XX号', '414', '2024-08-16', 'long_term', 'good', 3, 1, 'self', 10, 6, 1),
(35, 3, '赵军', 1, '1930-09-08', '13800138034', '北京市朝阳区XX街道XX号', '413', '2022-10-15', 'long_term', 'fair', 2, 1, 'government', 8, 20, 1),
(36, 4, '高兰', 0, '1930-05-19', '13800138035', '北京市朝阳区XX街道XX号', '102', '2024-08-10', 'long_term', 'fair', 3, 1, 'self', 1, 16, 1),
(37, 4, '黄芳', 0, '1943-02-22', '13800138036', '北京市朝阳区XX街道XX号', '215', '2023-11-20', 'long_term', 'poor', 1, 3, 'government', 1, 17, 1),
(38, 2, '徐伟', 1, '1945-11-06', '13800138037', '北京市朝阳区XX街道XX号', '417', '2022-12-29', 'long_term', 'poor', 2, 3, 'self', 3, 10, 1),
(39, 2, '马勇', 1, '1960-02-10', '13800138038', '北京市朝阳区XX街道XX号', '304', '2022-10-29', 'long_term', 'fair', 1, 2, 'government', 8, 9, 1),
(40, 3, '朱军', 1, '1947-01-15', '13800138039', '北京市朝阳区XX街道XX号', '409', '2023-08-12', 'long_term', 'good', 2, 3, 'government', 3, 8, 1),
(41, 1, '罗强', 1, '1959-09-20', '13800138040', '北京市朝阳区XX街道XX号', '405', '2022-09-23', 'long_term', 'poor', 1, 2, 'government', 9, 9, 1),
(42, 1, '刘国', 1, '1937-11-05', '13800138041', '北京市朝阳区XX街道XX号', '401', '2023-09-20', 'long_term', 'good', 1, 2, 'insurance', 10, 7, 1),
(43, 2, '高敏', 0, '1945-08-16', '13800138042', '北京市朝阳区XX街道XX号', '316', '2022-10-31', 'long_term', 'fair', 2, 1, 'insurance', 7, 6, 1),
(44, 3, '杨娟', 0, '1957-03-18', '13800138043', '北京市朝阳区XX街道XX号', '316', '2024-05-28', 'long_term', 'fair', 3, 1, 'government', 9, 7, 1),
(45, 1, '李建', 1, '1956-06-10', '13800138044', '北京市朝阳区XX街道XX号', '317', '2023-04-08', 'long_term', 'poor', 1, 1, 'government', 10, 16, 1),
(46, 3, '陈华', 1, '1934-07-29', '13800138045', '北京市朝阳区XX街道XX号', '214', '2022-10-20', 'long_term', 'poor', 2, 1, 'government', 6, 17, 1),
(47, 4, '林梅', 0, '1957-09-12', '13800138046', '北京市朝阳区XX街道XX号', '310', '2022-11-29', 'long_term', 'good', 2, 2, 'insurance', 3, 16, 1),
(48, 4, '杨娟', 0, '1939-02-25', '13800138047', '北京市朝阳区XX街道XX号', '102', '2023-10-07', 'long_term', 'poor', 2, 3, 'government', 9, 11, 1),
(49, 3, '赵伟', 1, '1943-11-18', '13800138048', '北京市朝阳区XX街道XX号', '516', '2024-07-19', 'long_term', 'good', 1, 2, 'self', 4, 7, 1),
(50, 4, '周静', 0, '1941-07-02', '13800138049', '北京市朝阳区XX街道XX号', '511', '2022-01-08', 'long_term', 'good', 2, 3, 'insurance', 1, 2, 1),
(51, 2, '赵霞', 0, '1936-02-28', '13800138050', '北京市朝阳区XX街道XX号', '108', '2023-09-02', 'long_term', 'good', 1, 2, 'self', 1, 7, 1),
(52, 1, '李秀', 0, '1939-04-14', '13800138051', '北京市朝阳区XX街道XX号', '502', '2022-04-23', 'long_term', 'good', 2, 3, 'government', 9, 8, 1),
(53, 2, '高红', 0, '1945-10-10', '13800138052', '北京市朝阳区XX街道XX号', '404', '2022-09-16', 'long_term', 'poor', 1, 2, 'insurance', 9, 9, 1),
(54, 1, '何梅', 0, '1959-04-09', '13800138053', '北京市朝阳区XX街道XX号', '402', '2022-08-26', 'long_term', 'fair', 3, 2, 'insurance', 7, 14, 1),
(55, 1, '赵明', 1, '1959-10-29', '13800138054', '北京市朝阳区XX街道XX号', '416', '2023-05-26', 'long_term', 'poor', 1, 2, 'self', 2, 5, 1),
(56, 2, '黄强', 1, '1958-04-01', '13800138055', '北京市朝阳区XX街道XX号', '318', '2022-09-29', 'long_term', 'poor', 1, 1, 'self', 2, 12, 1),
(57, 4, '胡军', 1, '1949-11-24', '13800138056', '北京市朝阳区XX街道XX号', '403', '2022-09-10', 'long_term', 'fair', 3, 3, 'insurance', 5, 19, 1),
(58, 3, '徐兰', 0, '1946-11-26', '13800138057', '北京市朝阳区XX街道XX号', '204', '2022-05-01', 'long_term', 'poor', 2, 3, 'government', 1, 8, 1),
(59, 3, '吴敏', 0, '1932-04-30', '13800138058', '北京市朝阳区XX街道XX号', '502', '2023-04-20', 'long_term', 'good', 2, 1, 'government', 8, 12, 1),
(60, 2, '高军', 1, '1957-08-04', '13800138059', '北京市朝阳区XX街道XX号', '119', '2024-09-14', 'long_term', 'fair', 3, 3, 'government', 4, 20, 1),
(61, 2, '郭军', 1, '1954-07-02', '13800138060', '北京市朝阳区XX街道XX号', '316', '2023-02-06', 'long_term', 'poor', 2, 2, 'insurance', 4, 4, 1),
(62, 3, '林勇', 1, '1935-10-04', '13800138061', '北京市朝阳区XX街道XX号', '518', '2023-08-24', 'long_term', 'poor', 1, 2, 'government', 10, 20, 1),
(63, 2, '黄建', 1, '1951-10-30', '13800138062', '北京市朝阳区XX街道XX号', '410', '2023-07-29', 'long_term', 'poor', 2, 2, 'insurance', 7, 19, 1),
(64, 4, '周娟', 0, '1942-05-30', '13800138063', '北京市朝阳区XX街道XX号', '414', '2022-01-20', 'long_term', 'poor', 1, 1, 'government', 7, 10, 1),
(65, 2, '马梅', 0, '1936-12-15', '13800138064', '北京市朝阳区XX街道XX号', '517', '2022-10-29', 'long_term', 'fair', 1, 2, 'self', 8, 15, 1),
(66, 4, '周梅', 0, '1946-12-08', '13800138065', '北京市朝阳区XX街道XX号', '410', '2023-11-10', 'long_term', 'fair', 3, 3, 'self', 7, 11, 1),
(67, 2, '张丽', 0, '1957-06-30', '13800138066', '北京市朝阳区XX街道XX号', '406', '2022-11-14', 'long_term', 'poor', 3, 1, 'government', 6, 5, 1),
(68, 1, '赵霞', 0, '1944-12-18', '13800138067', '北京市朝阳区XX街道XX号', '406', '2023-02-18', 'long_term', 'poor', 1, 1, 'government', 5, 6, 1),
(69, 1, '李静', 0, '1959-12-17', '13800138068', '北京市朝阳区XX街道XX号', '303', '2023-04-18', 'long_term', 'good', 2, 3, 'insurance', 9, 1, 1),
(70, 3, '孙刚', 1, '1935-02-13', '13800138069', '北京市朝阳区XX街道XX号', '411', '2024-09-14', 'long_term', 'good', 3, 2, 'self', 1, 18, 1),
(71, 1, '杨丽', 0, '1946-06-19', '13800138070', '北京市朝阳区XX街道XX号', '517', '2024-06-01', 'long_term', 'good', 1, 1, 'insurance', 8, 8, 1),
(72, 2, '罗强', 1, '1945-11-24', '13800138071', '北京市朝阳区XX街道XX号', '309', '2023-12-26', 'long_term', 'good', 1, 3, 'insurance', 10, 9, 1),
(73, 2, '胡兰', 0, '1953-03-10', '13800138072', '北京市朝阳区XX街道XX号', '208', '2023-03-03', 'long_term', 'poor', 3, 1, 'insurance', 1, 2, 1),
(74, 4, '徐建', 1, '1938-08-24', '13800138073', '北京市朝阳区XX街道XX号', '320', '2024-02-08', 'long_term', 'good', 2, 1, 'self', 3, 3, 1),
(75, 3, '徐勇', 1, '1936-02-12', '13800138074', '北京市朝阳区XX街道XX号', '405', '2022-05-27', 'long_term', 'good', 3, 1, 'insurance', 4, 4, 1),
(76, 4, '孙丽', 0, '1955-12-20', '13800138075', '北京市朝阳区XX街道XX号', '301', '2022-06-26', 'long_term', 'fair', 3, 2, 'government', 10, 16, 1),
(77, 3, '郭建', 1, '1942-05-04', '13800138076', '北京市朝阳区XX街道XX号', '109', '2023-11-09', 'long_term', 'fair', 2, 2, 'government', 9, 16, 1),
(78, 2, '马建', 1, '1945-02-02', '13800138077', '北京市朝阳区XX街道XX号', '109', '2024-07-06', 'long_term', 'poor', 1, 1, 'self', 7, 7, 1),
(79, 2, '徐明', 1, '1944-03-29', '13800138078', '北京市朝阳区XX街道XX号', '215', '2022-07-03', 'long_term', 'good', 2, 3, 'government', 4, 3, 1),
(80, 4, '黄梅', 0, '1930-04-07', '13800138079', '北京市朝阳区XX街道XX号', '104', '2022-03-30', 'long_term', 'good', 2, 1, 'self', 2, 6, 1),
(81, 4, '林伟', 1, '1934-10-08', '13800138080', '北京市朝阳区XX街道XX号', '407', '2024-02-07', 'long_term', 'poor', 1, 1, 'insurance', 9, 17, 1),
(82, 4, '王建', 1, '1946-07-09', '13800138081', '北京市朝阳区XX街道XX号', '120', '2024-06-11', 'long_term', 'poor', 3, 1, 'insurance', 10, 1, 1),
(83, 2, '徐刚', 1, '1949-11-15', '13800138082', '北京市朝阳区XX街道XX号', '316', '2023-01-18', 'long_term', 'poor', 1, 1, 'self', 7, 15, 1),
(84, 4, '刘军', 1, '1942-06-21', '13800138083', '北京市朝阳区XX街道XX号', '406', '2022-03-12', 'long_term', 'fair', 2, 1, 'insurance', 6, 11, 1),
(85, 3, '陈霞', 0, '1950-08-04', '13800138084', '北京市朝阳区XX街道XX号', '405', '2024-04-26', 'long_term', 'good', 3, 1, 'insurance', 7, 12, 1),
(86, 2, '张刚', 1, '1934-12-03', '13800138085', '北京市朝阳区XX街道XX号', '502', '2022-05-01', 'long_term', 'poor', 3, 1, 'government', 4, 19, 1),
(87, 4, '陈娟', 0, '1938-03-12', '13800138086', '北京市朝阳区XX街道XX号', '306', '2022-09-08', 'long_term', 'good', 2, 1, 'self', 9, 15, 1),
(88, 1, '周霞', 0, '1943-07-07', '13800138087', '北京市朝阳区XX街道XX号', '403', '2022-07-28', 'long_term', 'fair', 3, 2, 'insurance', 7, 20, 1),
(89, 4, '郭芳', 0, '1943-10-13', '13800138088', '北京市朝阳区XX街道XX号', '406', '2024-04-24', 'long_term', 'good', 2, 1, 'government', 10, 1, 1),
(90, 1, '王国', 1, '1932-01-13', '13800138089', '北京市朝阳区XX街道XX号', '519', '2023-08-04', 'long_term', 'poor', 1, 2, 'government', 8, 3, 1),
(91, 4, '马军', 1, '1944-07-05', '13800138090', '北京市朝阳区XX街道XX号', '517', '2022-05-11', 'long_term', 'poor', 2, 2, 'government', 3, 4, 1),
(92, 3, '吴军', 1, '1934-02-09', '13800138091', '北京市朝阳区XX街道XX号', '105', '2022-01-15', 'long_term', 'good', 3, 2, 'self', 4, 12, 1),
(93, 3, '张强', 1, '1946-12-15', '13800138092', '北京市朝阳区XX街道XX号', '312', '2022-03-03', 'long_term', 'poor', 1, 2, 'self', 4, 15, 1),
(94, 2, '孙芳', 0, '1934-02-04', '13800138093', '北京市朝阳区XX街道XX号', '208', '2024-06-10', 'long_term', 'poor', 2, 1, 'government', 7, 8, 1),
(95, 1, '王芳', 0, '1943-10-18', '13800138094', '北京市朝阳区XX街道XX号', '409', '2022-05-19', 'long_term', 'fair', 3, 3, 'self', 9, 2, 1),
(96, 2, '朱梅', 0, '1938-04-04', '13800138095', '北京市朝阳区XX街道XX号', '219', '2023-07-03', 'long_term', 'good', 3, 3, 'self', 1, 20, 1),
(97, 3, '黄兰', 0, '1949-03-27', '13800138096', '北京市朝阳区XX街道XX号', '310', '2023-02-19', 'long_term', 'fair', 2, 1, 'government', 1, 8, 1),
(98, 3, '马刚', 1, '1931-10-16', '13800138097', '北京市朝阳区XX街道XX号', '120', '2024-05-19', 'long_term', 'poor', 3, 3, 'government', 3, 16, 1),
(99, 4, '朱强', 1, '1940-01-26', '13800138098', '北京市朝阳区XX街道XX号', '313', '2024-07-04', 'long_term', 'poor', 3, 1, 'government', 8, 19, 1),
(100, 1, '朱建', 1, '1944-09-02', '13800138099', '北京市朝阳区XX街道XX号', '104', '2023-05-12', 'long_term', 'good', 1, 3, 'self', 8, 12, 1);

-- =====================================================
-- 7. 医生-老人关联数据
-- =====================================================
TRUNCATE TABLE doctor_elderly_relation;
INSERT INTO doctor_elderly_relation (doctor_id, elderly_id, relationship_type, start_date, status) VALUES
(1, 1, 'primary', '2023-10-30', 1),
(2, 2, 'primary', '2024-01-17', 1),
(3, 3, 'primary', '2023-11-01', 1),
(4, 4, 'primary', '2024-03-02', 1),
(5, 5, 'primary', '2023-04-21', 1),
(6, 6, 'primary', '2023-10-03', 1),
(7, 7, 'primary', '2023-04-05', 1),
(8, 8, 'primary', '2024-07-18', 1),
(9, 9, 'primary', '2023-08-23', 1),
(10, 10, 'primary', '2024-03-06', 1),
(1, 11, 'primary', '2023-05-03', 1),
(2, 12, 'primary', '2024-07-08', 1),
(3, 13, 'primary', '2023-05-17', 1),
(4, 14, 'primary', '2024-06-17', 1),
(5, 15, 'primary', '2023-12-30', 1),
(6, 16, 'primary', '2024-06-04', 1),
(7, 17, 'primary', '2023-05-11', 1),
(8, 18, 'primary', '2024-06-24', 1),
(9, 19, 'primary', '2023-03-08', 1),
(10, 20, 'primary', '2024-04-08', 1),
(1, 21, 'primary', '2024-01-07', 1),
(2, 22, 'primary', '2024-07-03', 1),
(3, 23, 'primary', '2024-07-02', 1),
(4, 24, 'primary', '2023-12-17', 1),
(5, 25, 'primary', '2023-03-03', 1),
(6, 26, 'primary', '2023-10-23', 1),
(7, 27, 'primary', '2023-04-25', 1),
(8, 28, 'primary', '2024-01-13', 1),
(9, 29, 'primary', '2023-09-24', 1),
(10, 30, 'primary', '2023-03-28', 1),
(1, 31, 'primary', '2024-02-16', 1),
(2, 32, 'primary', '2023-02-06', 1),
(3, 33, 'primary', '2023-01-14', 1),
(4, 34, 'primary', '2024-01-22', 1),
(5, 35, 'primary', '2024-09-04', 1),
(6, 36, 'primary', '2024-03-19', 1),
(7, 37, 'primary', '2024-03-28', 1),
(8, 38, 'primary', '2023-05-04', 1),
(9, 39, 'primary', '2024-09-14', 1),
(10, 40, 'primary', '2023-02-09', 1),
(1, 41, 'primary', '2023-07-17', 1),
(2, 42, 'primary', '2023-09-23', 1),
(3, 43, 'primary', '2024-09-24', 1),
(4, 44, 'primary', '2024-05-17', 1),
(5, 45, 'primary', '2024-06-21', 1),
(6, 46, 'primary', '2023-01-08', 1),
(7, 47, 'primary', '2024-04-10', 1),
(8, 48, 'primary', '2023-11-20', 1),
(9, 49, 'primary', '2024-09-22', 1),
(10, 50, 'primary', '2023-03-03', 1),
(1, 51, 'primary', '2023-03-14', 1),
(2, 52, 'primary', '2023-01-14', 1),
(3, 53, 'primary', '2024-03-11', 1),
(4, 54, 'primary', '2023-06-05', 1),
(5, 55, 'primary', '2023-05-17', 1),
(6, 56, 'primary', '2024-03-03', 1),
(7, 57, 'primary', '2024-05-30', 1),
(8, 58, 'primary', '2023-08-29', 1),
(9, 59, 'primary', '2023-02-28', 1),
(10, 60, 'primary', '2023-09-30', 1),
(1, 61, 'primary', '2023-01-25', 1),
(2, 62, 'primary', '2023-12-29', 1),
(3, 63, 'primary', '2023-01-21', 1),
(4, 64, 'primary', '2023-12-24', 1),
(5, 65, 'primary', '2023-06-27', 1),
(6, 66, 'primary', '2023-12-08', 1),
(7, 67, 'primary', '2024-06-11', 1),
(8, 68, 'primary', '2023-10-19', 1),
(9, 69, 'primary', '2023-02-16', 1),
(10, 70, 'primary', '2024-05-19', 1),
(1, 71, 'primary', '2023-10-23', 1),
(2, 72, 'primary', '2024-03-16', 1),
(3, 73, 'primary', '2023-07-30', 1),
(4, 74, 'primary', '2023-07-23', 1),
(5, 75, 'primary', '2023-04-22', 1),
(6, 76, 'primary', '2023-10-27', 1),
(7, 77, 'primary', '2023-03-15', 1),
(8, 78, 'primary', '2024-03-31', 1),
(9, 79, 'primary', '2024-08-16', 1),
(10, 80, 'primary', '2024-08-24', 1),
(1, 81, 'primary', '2024-03-24', 1),
(2, 82, 'primary', '2023-11-23', 1),
(3, 83, 'primary', '2023-02-09', 1),
(4, 84, 'primary', '2024-02-14', 1),
(5, 85, 'primary', '2023-12-28', 1),
(6, 86, 'primary', '2023-01-10', 1),
(7, 87, 'primary', '2024-09-24', 1),
(8, 88, 'primary', '2024-03-06', 1),
(9, 89, 'primary', '2023-06-10', 1),
(10, 90, 'primary', '2023-12-27', 1),
(1, 91, 'primary', '2024-09-23', 1),
(2, 92, 'primary', '2023-11-17', 1),
(3, 93, 'primary', '2023-03-16', 1),
(4, 94, 'primary', '2024-08-15', 1),
(5, 95, 'primary', '2024-07-10', 1),
(6, 96, 'primary', '2023-12-03', 1),
(7, 97, 'primary', '2024-06-05', 1),
(8, 98, 'primary', '2024-08-30', 1),
(9, 99, 'primary', '2024-07-03', 1),
(10, 100, 'primary', '2024-06-19', 1);

-- =====================================================
-- 8. 护工-老人关联数据
-- =====================================================
TRUNCATE TABLE nurse_elderly_relation;
INSERT INTO nurse_elderly_relation (nurse_id, elderly_id, care_level, start_date, status) VALUES
(1, 1, 1, '2023-08-21', 1),
(2, 2, 1, '2023-03-22', 1),
(3, 3, 3, '2023-06-29', 1),
(4, 4, 3, '2023-04-06', 1),
(5, 5, 2, '2023-03-16', 1),
(6, 6, 1, '2024-01-11', 1),
(7, 7, 3, '2023-07-01', 1),
(8, 8, 1, '2023-01-31', 1),
(9, 9, 3, '2024-05-20', 1),
(10, 10, 3, '2024-09-13', 1),
(11, 11, 3, '2023-06-08', 1),
(12, 12, 2, '2023-07-28', 1),
(13, 13, 2, '2024-02-03', 1),
(14, 14, 3, '2024-09-19', 1),
(15, 15, 1, '2024-04-13', 1),
(16, 16, 3, '2023-06-20', 1),
(17, 17, 3, '2023-08-18', 1),
(18, 18, 2, '2023-01-25', 1),
(19, 19, 1, '2023-12-14', 1),
(20, 20, 3, '2023-07-12', 1),
(1, 21, 2, '2023-08-17', 1),
(2, 22, 3, '2024-03-12', 1),
(3, 23, 1, '2024-06-08', 1),
(4, 24, 2, '2023-09-06', 1),
(5, 25, 2, '2024-03-05', 1),
(6, 26, 3, '2024-08-14', 1),
(7, 27, 3, '2023-01-02', 1),
(8, 28, 2, '2024-09-07', 1),
(9, 29, 3, '2023-05-04', 1),
(10, 30, 2, '2023-01-30', 1),
(11, 31, 3, '2023-11-27', 1),
(12, 32, 3, '2023-06-05', 1),
(13, 33, 2, '2024-05-07', 1),
(14, 34, 1, '2024-04-23', 1),
(15, 35, 3, '2023-03-24', 1),
(16, 36, 2, '2024-08-12', 1),
(17, 37, 3, '2024-02-19', 1),
(18, 38, 1, '2023-11-17', 1),
(19, 39, 3, '2023-01-30', 1),
(20, 40, 3, '2023-12-27', 1),
(1, 41, 2, '2023-06-23', 1),
(2, 42, 2, '2024-03-01', 1),
(3, 43, 2, '2023-08-13', 1),
(4, 44, 1, '2023-06-18', 1),
(5, 45, 1, '2023-07-17', 1),
(6, 46, 2, '2023-02-23', 1),
(7, 47, 2, '2023-11-10', 1),
(8, 48, 3, '2024-06-18', 1),
(9, 49, 2, '2023-03-01', 1),
(10, 50, 3, '2023-08-21', 1),
(11, 51, 1, '2023-03-19', 1),
(12, 52, 3, '2023-05-22', 1),
(13, 53, 2, '2024-08-13', 1),
(14, 54, 2, '2023-06-22', 1),
(15, 55, 2, '2023-06-03', 1),
(16, 56, 1, '2024-02-27', 1),
(17, 57, 1, '2023-04-09', 1),
(18, 58, 3, '2024-06-24', 1),
(19, 59, 2, '2024-05-04', 1),
(20, 60, 1, '2024-07-03', 1),
(1, 61, 1, '2023-04-25', 1),
(2, 62, 2, '2024-06-02', 1),
(3, 63, 3, '2023-02-15', 1),
(4, 64, 2, '2024-04-01', 1),
(5, 65, 3, '2023-10-24', 1),
(6, 66, 1, '2023-05-02', 1),
(7, 67, 1, '2023-07-10', 1),
(8, 68, 1, '2024-09-15', 1),
(9, 69, 3, '2023-09-08', 1),
(10, 70, 3, '2024-05-06', 1),
(11, 71, 3, '2024-06-07', 1),
(12, 72, 1, '2024-09-24', 1),
(13, 73, 3, '2024-06-15', 1),
(14, 74, 3, '2023-01-29', 1),
(15, 75, 3, '2024-04-28', 1),
(16, 76, 2, '2023-02-27', 1),
(17, 77, 2, '2023-05-18', 1),
(18, 78, 2, '2024-02-29', 1),
(19, 79, 2, '2023-01-26', 1),
(20, 80, 3, '2023-02-08', 1),
(1, 81, 2, '2024-04-22', 1),
(2, 82, 1, '2024-01-18', 1),
(3, 83, 2, '2023-09-11', 1),
(4, 84, 2, '2023-12-15', 1),
(5, 85, 1, '2024-08-04', 1),
(6, 86, 1, '2024-04-06', 1),
(7, 87, 1, '2024-01-15', 1),
(8, 88, 2, '2023-04-22', 1),
(9, 89, 2, '2023-11-29', 1),
(10, 90, 1, '2024-03-19', 1),
(11, 91, 2, '2024-07-26', 1),
(12, 92, 1, '2023-01-27', 1),
(13, 93, 3, '2024-01-04', 1),
(14, 94, 2, '2023-10-07', 1),
(15, 95, 2, '2023-07-13', 1),
(16, 96, 3, '2023-11-27', 1),
(17, 97, 3, '2023-08-17', 1),
(18, 98, 3, '2023-02-10', 1),
(19, 99, 3, '2023-10-09', 1),
(20, 100, 1, '2023-06-01', 1);

-- =====================================================
-- 9. 家属-老人关联数据
-- =====================================================
TRUNCATE TABLE family_elderly_relation;
INSERT INTO family_elderly_relation (family_id, elderly_id, relationship, access_level, is_primary_contact, status) VALUES
(42, 1, '女婿', 'health', 1, 1),
(43, 2, '女婿', 'full', 1, 1),
(44, 3, '女儿', 'basic', 1, 1),
(45, 4, '孙子', 'basic', 1, 1),
(46, 5, '孙女', 'full', 1, 1),
(47, 6, '孙女', 'full', 1, 1),
(48, 7, '儿媳', 'health', 1, 1),
(49, 8, '儿子', 'basic', 1, 1),
(50, 9, '孙女', 'full', 1, 1),
(51, 10, '女婿', 'health', 1, 1),
(52, 11, '孙子', 'health', 1, 1),
(53, 12, '孙女', 'full', 1, 1),
(54, 13, '孙女', 'health', 1, 1),
(55, 14, '女儿', 'health', 1, 1),
(56, 15, '儿媳', 'health', 1, 1),
(57, 16, '女儿', 'health', 1, 1),
(58, 17, '孙女', 'basic', 1, 1),
(59, 18, '女儿', 'health', 1, 1),
(60, 19, '女婿', 'health', 1, 1),
(61, 20, '儿媳', 'health', 1, 1),
(62, 21, '儿媳', 'basic', 1, 1),
(63, 22, '儿子', 'basic', 1, 1),
(64, 23, '女婿', 'health', 1, 1),
(65, 24, '孙女', 'full', 1, 1),
(66, 25, '女儿', 'health', 1, 1),
(67, 26, '儿子', 'full', 1, 1),
(68, 27, '孙女', 'full', 1, 1),
(69, 28, '女儿', 'health', 1, 1),
(70, 29, '女儿', 'health', 1, 1),
(71, 30, '女婿', 'health', 1, 1),
(72, 31, '孙女', 'health', 1, 1),
(73, 32, '孙子', 'health', 1, 1),
(74, 33, '孙女', 'basic', 1, 1),
(75, 34, '女婿', 'full', 1, 1),
(76, 35, '孙女', 'basic', 1, 1),
(77, 36, '儿媳', 'health', 1, 1),
(78, 37, '儿子', 'full', 1, 1),
(79, 38, '女儿', 'health', 1, 1),
(80, 39, '儿子', 'basic', 1, 1),
(81, 40, '孙子', 'full', 1, 1),
(82, 41, '孙女', 'full', 1, 1),
(83, 42, '孙子', 'full', 1, 1),
(84, 43, '孙女', 'basic', 1, 1),
(85, 44, '儿媳', 'basic', 1, 1),
(86, 45, '孙女', 'full', 1, 1),
(87, 46, '孙女', 'health', 1, 1),
(88, 47, '孙子', 'basic', 1, 1),
(89, 48, '女婿', 'health', 1, 1),
(90, 49, '儿子', 'basic', 1, 1),
(91, 50, '孙女', 'health', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 基础数据生成完成!
-- 总计:
--   机构: 4
--   用户: 91
--   医生: 10
--   护工: 20
--   社工: 5
--   老人: 100
--   家属: 50
-- =====================================================
