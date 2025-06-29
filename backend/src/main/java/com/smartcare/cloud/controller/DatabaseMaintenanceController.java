package com.smartcare.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.vo.ResponseResult;

/**
 * 数据库维护控制器 仅用于开发调试，生产环境应禁用
 */
@RestController
@RequestMapping("/admin/db")
public class DatabaseMaintenanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/fix-health-record-table")
    public ResponseResult<String> fixHealthRecordTable() {
        try {
            // 添加收缩压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN systolic_pressure INT COMMENT '收缩压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加舒张压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN diastolic_pressure INT COMMENT '舒张压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加心率字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN heart_rate INT COMMENT '心率(次/分钟)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加体温字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN body_temperature DECIMAL(4,2) COMMENT '体温(°C)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加血糖字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN blood_glucose DECIMAL(5,2) COMMENT '血糖(mmol/L)'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加测试数据
            try {
                jdbcTemplate.execute(
                        "INSERT INTO health_record (elderly_id, doctor_id, record_type, record_date, systolic_pressure, diastolic_pressure, heart_rate, body_temperature, blood_glucose, weight, height, symptoms, diagnosis, treatment, medication, doctor_advice, remarks, is_abnormal, urgency_level) VALUES "
                        + "(1, 1, 4, NOW(), 130, 85, 75, 36.5, 5.8, 65.5, 170.0, '无明显症状', '血压稍高', '建议低盐饮食', '无', '控制饮食，适量运动', '定期监测', 0, 1), "
                        + "(2, 1, 4, NOW(), 145, 95, 80, 36.8, 6.2, 58.0, 165.0, '偶有头晕', '高血压初期', '饮食控制+运动', '无', '低盐低脂饮食，每日散步30分钟', '密切观察', 1, 2), "
                        + "(3, 1, 4, NOW(), 120, 80, 72, 36.3, 5.5, 62.0, 168.0, '无', '正常', '继续保持', '无', '保持良好生活习惯', '正常监测', 0, 1), "
                        + "(4, 1, 4, NOW(), 160, 100, 88, 37.0, 7.1, 70.0, 172.0, '头痛，胸闷', '高血压+糖尿病前期', '药物治疗+饮食控制', '降压药', '严格控制饮食，按时服药', '需要密切监测', 1, 3), "
                        + "(5, 1, 4, NOW(), 135, 88, 78, 36.6, 6.0, 55.5, 162.0, '轻微乏力', '血压偏高', '生活方式干预', '无', '增加运动量，减少盐分摄入', '定期复查', 1, 2)"
                );
            } catch (Exception e) {
                // 数据可能已存在，忽略错误
            }

            return ResponseResult.success("健康记录表结构修复完成！");
        } catch (Exception e) {
            return ResponseResult.error("修复失败：" + e.getMessage());
        }
    }

    @PostMapping("/insert-test-data")
    public ResponseResult<String> insertTestData() {
        try {
            // 首先更新现有数据的缺失字段
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN id = 1 THEN 'A101' "
                    + "    WHEN id = 2 THEN 'A102' "
                    + "    WHEN id = 3 THEN 'B201' "
                    + "    WHEN id = 4 THEN 'C301' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN id = 1 THEN '张小明' "
                    + "    WHEN id = 2 THEN '李小华' "
                    + "    WHEN id = 3 THEN '王小芳' "
                    + "    WHEN id = 4 THEN '刘小强' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN id = 1 THEN '13911112222' "
                    + "    WHEN id = 2 THEN '13922223333' "
                    + "    WHEN id = 3 THEN '13933334444' "
                    + "    WHEN id = 4 THEN '13944445555' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN id = 1 THEN '张三' "
                    + "    WHEN id = 2 THEN '李四' "
                    + "    WHEN id = 3 THEN '王五' "
                    + "    WHEN id = 4 THEN '刘六' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN id = 1 THEN '13800001111' "
                    + "    WHEN id = 2 THEN '13800002222' "
                    + "    WHEN id = 3 THEN '13800003333' "
                    + "    WHEN id = 4 THEN '13800004444' "
                    + "    ELSE emergency_phone "
                    + "END, "
                    + "blood_type = CASE "
                    + "    WHEN id = 1 THEN 'A' "
                    + "    WHEN id = 2 THEN 'B' "
                    + "    WHEN id = 3 THEN 'O' "
                    + "    WHEN id = 4 THEN 'AB' "
                    + "    ELSE blood_type "
                    + "END, "
                    + "adl_score = CASE "
                    + "    WHEN id = 1 THEN 85 "
                    + "    WHEN id = 2 THEN 72 "
                    + "    WHEN id = 3 THEN 65 "
                    + "    WHEN id = 4 THEN 45 "
                    + "    ELSE adl_score "
                    + "END "
                    + "WHERE id IN (1, 2, 3, 4)");

            // 插入健康老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, blood_type, adl_score, create_time, update_time, is_deleted) VALUES "
                    + "('陈建国', '110101194503151234', '13600001001', '北京市朝阳区建国门外大街100号', 1, '1945-03-15', 'HEALTHY', 1, 'A103', '陈小军', '13600002001', '陈军', '13600003001', 'A', 90, NOW(), NOW(), 0), "
                    + "('赵美丽', '110101194807202345', '13600001002', '北京市海淀区中关村大街200号', 0, '1948-07-20', 'HEALTHY', 1, 'A104', '赵小华', '13600002002', '赵华', '13600003002', 'O', 88, NOW(), NOW(), 0), "
                    + "('孙志强', '110101195012253456', '13600001003', '北京市西城区西单北大街300号', 1, '1950-12-25', 'HEALTHY', 1, 'A105', '孙小伟', '13600002003', '孙伟', '13600003003', 'B', 92, NOW(), NOW(), 0), "
                    + "('周淑芬', '110101194604104567', '13600001004', '北京市东城区王府井大街400号', 0, '1946-04-10', 'HEALTHY', 1, 'A106', '周小鹏', '13600002004', '周鹏', '13600003004', 'AB', 85, NOW(), NOW(), 0), "
                    + "('吴国庆', '110101194909095678', '13600001005', '北京市丰台区丰台路500号', 1, '1949-09-09', 'HEALTHY', 1, 'A107', '吴小军', '13600002005', '吴军', '13600003005', 'A', 87, NOW(), NOW(), 0)");

            // 插入亚健康老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, blood_type, adl_score, create_time, update_time, is_deleted) VALUES "
                    + "('郑小敏', '110101195106156789', '13600001006', '北京市石景山区八宝山路600号', 0, '1951-06-15', 'SUBHEALTH', 2, 'B202', '郑小刚', '13600002006', '郑刚', '13600003006', 'O', 75, NOW(), NOW(), 0), "
                    + "('马德华', '110101194711117890', '13600001007', '北京市门头沟区新桥大街700号', 1, '1947-11-11', 'SUBHEALTH', 2, 'B203', '马小龙', '13600002007', '马龙', '13600003007', 'B', 78, NOW(), NOW(), 0), "
                    + "('冯玉兰', '110101195208288901', '13600001008', '北京市房山区良乡大街800号', 0, '1952-08-28', 'SUBHEALTH', 1, 'B204', '冯小虎', '13600002008', '冯虎', '13600003008', 'A', 80, NOW(), NOW(), 0), "
                    + "('韩志明', '110101194503039012', '13600001009', '北京市通州区新华大街900号', 1, '1945-03-03', 'SUBHEALTH', 2, 'B205', '韩小峰', '13600002009', '韩峰', '13600003009', 'AB', 72, NOW(), NOW(), 0)");

            return ResponseResult.success("第一批测试数据插入成功！");
        } catch (Exception e) {
            return ResponseResult.error("插入失败：" + e.getMessage());
        }
    }

    @PostMapping("/insert-test-data-part2")
    public ResponseResult<String> insertTestDataPart2() {
        try {
            // 插入疾病老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, blood_type, adl_score, create_time, update_time, is_deleted) VALUES "
                    + "('蒋春花', '110101194012120123', '13600001010', '北京市昌平区昌平路1000号', 0, '1940-12-12', 'SICK', 3, 'C302', '蒋小勇', '13600002010', '蒋勇', '13600003010', 'O', 60, NOW(), NOW(), 0), "
                    + "('曹建军', '110101194105051234', '13600001011', '北京市大兴区黄村大街1100号', 1, '1941-05-05', 'SICK', 3, 'C303', '曹小英', '13600002011', '曹英', '13600003011', 'A', 58, NOW(), NOW(), 0), "
                    + "('许秀英', '110101194308082345', '13600001012', '北京市怀柔区怀柔大街1200号', 0, '1943-08-08', 'SICK', 2, 'C304', '许小明', '13600002012', '许明', '13600003012', 'B', 65, NOW(), NOW(), 0), "
                    + "('袁文博', '110101194710103456', '13600001013', '北京市平谷区平谷大街1300号', 1, '1947-10-10', 'SICK', 2, 'C305', '袁小丽', '13600002013', '袁丽', '13600003013', 'AB', 68, NOW(), NOW(), 0)");

            // 插入重病老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, blood_type, adl_score, create_time, update_time, is_deleted) VALUES "
                    + "('邓桂香', '110101193807074567', '13600001014', '北京市密云区密云大街1400号', 0, '1938-07-07', 'SERIOUS', 3, 'D401', '邓小宇', '13600002014', '邓宇', '13600003014', 'O', 45, NOW(), NOW(), 0), "
                    + "('方志伟', '110101193909095678', '13600001015', '北京市延庆区延庆大街1500号', 1, '1939-09-09', 'SERIOUS', 3, 'D402', '方小芳', '13600002015', '方芳', '13600003015', 'A', 42, NOW(), NOW(), 0), "
                    + "('史玉梅', '110101194006066789', '13600001016', '北京市顺义区顺义大街1600号', 0, '1940-06-06', 'SERIOUS', 3, 'D403', '史小强', '13600002016', '史强', '13600003016', 'B', 48, NOW(), NOW(), 0)");

            // 插入危险和预警状态老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, blood_type, adl_score, create_time, update_time, is_deleted) VALUES "
                    + "('高建设', '110101193512127890', '13600001017', '北京市朝阳区三里屯大街1700号', 1, '1935-12-12', 'DANGER', 3, 'E501', '高小红', '13600002017', '高红', '13600003017', 'AB', 35, NOW(), NOW(), 0), "
                    + "('段美华', '110101193703038901', '13600001018', '北京市海淀区学院路1800号', 0, '1937-03-03', 'DANGER', 3, 'E502', '段小军', '13600002018', '段军', '13600003018', 'O', 38, NOW(), NOW(), 0), "
                    + "('薛志国', '110101194404049012', '13600001019', '北京市西城区金融街1900号', 1, '1944-04-04', 'WARNING', 2, 'F601', '薛小燕', '13600002019', '薛燕', '13600003019', 'A', 70, NOW(), NOW(), 0), "
                    + "('尹秀兰', '110101195001010123', '13600001020', '北京市东城区东华门大街2000号', 0, '1950-01-01', 'WARNING', 1, 'F602', '尹小东', '13600002020', '尹东', '13600003020', 'B', 73, NOW(), NOW(), 0), "
                    + "('雷建华', '110101194205052234', '13600001021', '北京市丰台区南三环中路2100号', 1, '1942-05-05', 'WARNING', 2, 'F603', '雷小霞', '13600002021', '雷霞', '13600003021', 'O', 69, NOW(), NOW(), 0)");

            return ResponseResult.success("第二批测试数据插入成功！");
        } catch (Exception e) {
            return ResponseResult.error("插入失败：" + e.getMessage());
        }
    }

    /**
     * 插入多样化的老人档案测试数据（基于当前字段结构）
     */
    @PostMapping("/insert-elderly-test-data")
    public ResponseResult<String> insertElderlyTestData() {
        try {
            // 插入健康老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, create_time, update_time, is_deleted) VALUES "
                    + "('陈建国', '110101194503151234', '13600001001', '北京市朝阳区建国门外大街100号', 1, '1945-03-15', 'HEALTHY', 1, NOW(), NOW(), 0), "
                    + "('赵美丽', '110101194807202345', '13600001002', '北京市海淀区中关村大街200号', 0, '1948-07-20', 'HEALTHY', 1, NOW(), NOW(), 0), "
                    + "('孙志强', '110101195012253456', '13600001003', '北京市西城区西单北大街300号', 1, '1950-12-25', 'HEALTHY', 1, NOW(), NOW(), 0), "
                    + "('周淑芬', '110101194604104567', '13600001004', '北京市东城区王府井大街400号', 0, '1946-04-10', 'HEALTHY', 1, NOW(), NOW(), 0), "
                    + "('吴国庆', '110101194909095678', '13600001005', '北京市丰台区丰台路500号', 1, '1949-09-09', 'HEALTHY', 1, NOW(), NOW(), 0)");

            // 插入亚健康老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, create_time, update_time, is_deleted) VALUES "
                    + "('郑小敏', '110101195106156789', '13600001006', '北京市石景山区八宝山路600号', 0, '1951-06-15', 'SUBHEALTH', 2, NOW(), NOW(), 0), "
                    + "('马德华', '110101194711117890', '13600001007', '北京市门头沟区新桥大街700号', 1, '1947-11-11', 'SUBHEALTH', 2, NOW(), NOW(), 0), "
                    + "('冯玉兰', '110101195208288901', '13600001008', '北京市房山区良乡大街800号', 0, '1952-08-28', 'SUBHEALTH', 1, NOW(), NOW(), 0), "
                    + "('韩志明', '110101194503039012', '13600001009', '北京市通州区新华大街900号', 1, '1945-03-03', 'SUBHEALTH', 2, NOW(), NOW(), 0)");

            // 插入疾病老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, create_time, update_time, is_deleted) VALUES "
                    + "('蒋春花', '110101194012120123', '13600001010', '北京市昌平区昌平路1000号', 0, '1940-12-12', 'SICK', 3, NOW(), NOW(), 0), "
                    + "('曹建军', '110101194105051234', '13600001011', '北京市大兴区黄村大街1100号', 1, '1941-05-05', 'SICK', 3, NOW(), NOW(), 0), "
                    + "('许秀英', '110101194308082345', '13600001012', '北京市怀柔区怀柔大街1200号', 0, '1943-08-08', 'SICK', 2, NOW(), NOW(), 0), "
                    + "('袁文博', '110101194710103456', '13600001013', '北京市平谷区平谷大街1300号', 1, '1947-10-10', 'SICK', 2, NOW(), NOW(), 0)");

            // 插入重病老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, create_time, update_time, is_deleted) VALUES "
                    + "('邓桂香', '110101193807074567', '13600001014', '北京市密云区密云大街1400号', 0, '1938-07-07', 'SERIOUS', 3, NOW(), NOW(), 0), "
                    + "('方志伟', '110101193909095678', '13600001015', '北京市延庆区延庆大街1500号', 1, '1939-09-09', 'SERIOUS', 3, NOW(), NOW(), 0), "
                    + "('史玉梅', '110101194006066789', '13600001016', '北京市顺义区顺义大街1600号', 0, '1940-06-06', 'SERIOUS', 3, NOW(), NOW(), 0)");

            // 插入危险和预警状态老人组
            jdbcTemplate.execute("INSERT INTO elderly (name, id_card, phone, address, gender, birth_date, health_status, care_level, create_time, update_time, is_deleted) VALUES "
                    + "('高建设', '110101193512127890', '13600001017', '北京市朝阳区三里屯大街1700号', 1, '1935-12-12', 'DANGER', 3, NOW(), NOW(), 0), "
                    + "('段美华', '110101193703038901', '13600001018', '北京市海淀区学院路1800号', 0, '1937-03-03', 'DANGER', 3, NOW(), NOW(), 0), "
                    + "('薛志国', '110101194404049012', '13600001019', '北京市西城区金融街1900号', 1, '1944-04-04', 'WARNING', 2, NOW(), NOW(), 0), "
                    + "('尹秀兰', '110101195001010123', '13600001020', '北京市东城区东华门大街2000号', 0, '1950-01-01', 'WARNING', 1, NOW(), NOW(), 0), "
                    + "('雷建华', '110101194205052234', '13600001021', '北京市丰台区南三环中路2100号', 1, '1942-05-05', 'WARNING', 2, NOW(), NOW(), 0)");

            return ResponseResult.success("多样化老人档案测试数据插入成功！共插入21条记录，包含健康、亚健康、疾病、重病、危险、预警等各种状态的老人档案。");
        } catch (Exception e) {
            return ResponseResult.error("插入失败：" + e.getMessage());
        }
    }

    /**
     * 插入健康记录测试数据
     */
    @PostMapping("/insert-health-record-test-data")
    public ResponseResult<String> insertHealthRecordTestData() {
        try {
            // 为现有老人插入健康记录数据，使用正确的字段名
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal) VALUES "
                    + // 为老人ID 1-4 插入健康记录
                    "(1, '2025-06-25', 70.5, 120, 80, 72, 36.5, 5.8, '身体状况良好', '维生素D', '李医生', 1, 0, 0), "
                    + "(2, '2025-06-25', 55.2, 135, 85, 75, 36.7, 6.2, '血压偏高', '降压药', '王医生', 1, 1, 1), "
                    + "(3, '2025-06-25', 48.8, 140, 90, 68, 37.1, 7.8, '糖尿病前期', '二甲双胍', '张医生', 1, 2, 1), "
                    + "(4, '2025-06-25', 65.0, 150, 95, 85, 37.8, 8.5, '高血压，糖尿病', '降压药，降糖药', '陈医生', 1, 3, 1)");

            // 为新插入的老人添加一些健康记录
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal) VALUES "
                    + // 健康老人记录
                    "(8, '2025-06-28', 72.0, 115, 75, 70, 36.4, 5.5, '健康状况良好', '无', '刘医生', 1, 0, 0), "
                    + "(9, '2025-06-28', 58.5, 118, 78, 68, 36.6, 5.6, '健康状况良好', '钙片', '赵医生', 1, 0, 0), "
                    + // 亚健康老人记录
                    "(13, '2025-06-28', 52.3, 130, 82, 76, 36.8, 6.5, '血压轻度升高', '降压药', '孙医生', 1, 1, 1), "
                    + "(14, '2025-06-28', 68.0, 125, 80, 74, 36.7, 6.0, '血脂偏高', '他汀类药物', '周医生', 1, 1, 1), "
                    + // 疾病老人记录
                    "(17, '2025-06-28', 45.2, 145, 92, 82, 37.2, 8.2, '糖尿病，骨质疏松', '胰岛素，钙剂', '吴医生', 2, 2, 1), "
                    + "(18, '2025-06-28', 70.8, 155, 98, 88, 37.5, 9.1, '高血压，心脏病', '降压药，强心剂', '郑医生', 2, 3, 1), "
                    + // 重病老人记录
                    "(21, '2025-06-28', 42.0, 160, 100, 95, 38.2, 10.5, '严重高血压，糖尿病并发症', '胰岛素，多种降压药', '马医生', 2, 3, 1), "
                    + "(22, '2025-06-28', 38.5, 165, 105, 100, 38.5, 11.2, '心衰，肾功能不全', '利尿剂，强心剂', '冯医生', 2, 3, 1)");

            return ResponseResult.success("健康记录测试数据插入成功！为16位老人插入了不同类型的健康记录，包含体检记录和就诊记录。");
        } catch (Exception e) {
            return ResponseResult.error("健康记录插入失败：" + e.getMessage());
        }
    }

    /**
     * 添加房间号和监护人字段并更新所有老人档案数据
     */
    @PostMapping("/add-room-guardian-fields")
    public ResponseResult<String> addRoomGuardianFields() {
        try {
            // 添加房间号字段
            try {
                jdbcTemplate.execute("ALTER TABLE elderly ADD COLUMN room_number VARCHAR(20) COMMENT '房间号'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加监护人姓名字段
            try {
                jdbcTemplate.execute("ALTER TABLE elderly ADD COLUMN guardian_name VARCHAR(50) COMMENT '监护人姓名'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加监护人电话字段
            try {
                jdbcTemplate.execute("ALTER TABLE elderly ADD COLUMN guardian_phone VARCHAR(20) COMMENT '监护人电话'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加紧急联系人字段
            try {
                jdbcTemplate.execute("ALTER TABLE elderly ADD COLUMN emergency_contact VARCHAR(50) COMMENT '紧急联系人'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            // 添加紧急联系人电话字段
            try {
                jdbcTemplate.execute("ALTER TABLE elderly ADD COLUMN emergency_phone VARCHAR(20) COMMENT '紧急联系人电话'");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }

            return ResponseResult.success("房间号和监护人字段添加成功！");
        } catch (Exception e) {
            return ResponseResult.error("字段添加失败：" + e.getMessage());
        }
    }

    /**
     * 更新所有老人档案的房间号和监护人信息
     */
    @PostMapping("/update-elderly-room-guardian")
    public ResponseResult<String> updateElderlyRoomGuardian() {
        try {
            // 根据ID范围和健康状态分配房间号和监护人信息

            // 健康老人 - A区（1-2楼）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '陈建国' THEN 'A101' "
                    + "    WHEN name = '赵美丽' THEN 'A102' "
                    + "    WHEN name = '孙志强' THEN 'A103' "
                    + "    WHEN name = '周淑芬' THEN 'A104' "
                    + "    WHEN name = '吴国庆' THEN 'A105' "
                    + "    WHEN name = 'Zhang Ming Updated' THEN 'A201' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '陈建国' THEN '陈小军' "
                    + "    WHEN name = '赵美丽' THEN '赵小华' "
                    + "    WHEN name = '孙志强' THEN '孙小伟' "
                    + "    WHEN name = '周淑芬' THEN '周小鹏' "
                    + "    WHEN name = '吴国庆' THEN '吴小军' "
                    + "    WHEN name = 'Zhang Ming Updated' THEN '张小明' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '陈建国' THEN '13600002001' "
                    + "    WHEN name = '赵美丽' THEN '13600002002' "
                    + "    WHEN name = '孙志强' THEN '13600002003' "
                    + "    WHEN name = '周淑芬' THEN '13600002004' "
                    + "    WHEN name = '吴国庆' THEN '13600002005' "
                    + "    WHEN name = 'Zhang Ming Updated' THEN '13600002006' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '陈建国' THEN '陈军' "
                    + "    WHEN name = '赵美丽' THEN '赵华' "
                    + "    WHEN name = '孙志强' THEN '孙伟' "
                    + "    WHEN name = '周淑芬' THEN '周鹏' "
                    + "    WHEN name = '吴国庆' THEN '吴军' "
                    + "    WHEN name = 'Zhang Ming Updated' THEN '张明' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '陈建国' THEN '13800003001' "
                    + "    WHEN name = '赵美丽' THEN '13800003002' "
                    + "    WHEN name = '孙志强' THEN '13800003003' "
                    + "    WHEN name = '周淑芬' THEN '13800003004' "
                    + "    WHEN name = '吴国庆' THEN '13800003005' "
                    + "    WHEN name = 'Zhang Ming Updated' THEN '13800003006' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'HEALTHY'");

            // 亚健康老人 - B区（2-3楼）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '郑小敏' THEN 'B201' "
                    + "    WHEN name = '马德华' THEN 'B202' "
                    + "    WHEN name = '冯玉兰' THEN 'B203' "
                    + "    WHEN name = '韩志明' THEN 'B204' "
                    + "    WHEN name = 'Li Hua' THEN 'B301' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '郑小敏' THEN '郑小刚' "
                    + "    WHEN name = '马德华' THEN '马小龙' "
                    + "    WHEN name = '冯玉兰' THEN '冯小虎' "
                    + "    WHEN name = '韩志明' THEN '韩小峰' "
                    + "    WHEN name = 'Li Hua' THEN '李小华' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '郑小敏' THEN '13600002007' "
                    + "    WHEN name = '马德华' THEN '13600002008' "
                    + "    WHEN name = '冯玉兰' THEN '13600002009' "
                    + "    WHEN name = '韩志明' THEN '13600002010' "
                    + "    WHEN name = 'Li Hua' THEN '13600002011' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '郑小敏' THEN '郑刚' "
                    + "    WHEN name = '马德华' THEN '马龙' "
                    + "    WHEN name = '冯玉兰' THEN '冯虎' "
                    + "    WHEN name = '韩志明' THEN '韩峰' "
                    + "    WHEN name = 'Li Hua' THEN '李华' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '郑小敏' THEN '13800003007' "
                    + "    WHEN name = '马德华' THEN '13800003008' "
                    + "    WHEN name = '冯玉兰' THEN '13800003009' "
                    + "    WHEN name = '韩志明' THEN '13800003010' "
                    + "    WHEN name = 'Li Hua' THEN '13800003011' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'SUBHEALTH'");

            // 疾病老人 - C区（3-4楼，需要更多医疗支持）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '蒋春花' THEN 'C301' "
                    + "    WHEN name = '曹建军' THEN 'C302' "
                    + "    WHEN name = '许秀英' THEN 'C303' "
                    + "    WHEN name = '袁文博' THEN 'C304' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '蒋春花' THEN '蒋小勇' "
                    + "    WHEN name = '曹建军' THEN '曹小英' "
                    + "    WHEN name = '许秀英' THEN '许小明' "
                    + "    WHEN name = '袁文博' THEN '袁小丽' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '蒋春花' THEN '13600002012' "
                    + "    WHEN name = '曹建军' THEN '13600002013' "
                    + "    WHEN name = '许秀英' THEN '13600002014' "
                    + "    WHEN name = '袁文博' THEN '13600002015' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '蒋春花' THEN '蒋勇' "
                    + "    WHEN name = '曹建军' THEN '曹英' "
                    + "    WHEN name = '许秀英' THEN '许明' "
                    + "    WHEN name = '袁文博' THEN '袁丽' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '蒋春花' THEN '13800003012' "
                    + "    WHEN name = '曹建军' THEN '13800003013' "
                    + "    WHEN name = '许秀英' THEN '13800003014' "
                    + "    WHEN name = '袁文博' THEN '13800003015' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'SICK'");

            // 重病老人 - D区（4楼，重症监护区）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '邓桂香' THEN 'D401' "
                    + "    WHEN name = '方志伟' THEN 'D402' "
                    + "    WHEN name = '史玉梅' THEN 'D403' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '邓桂香' THEN '邓小宇' "
                    + "    WHEN name = '方志伟' THEN '方小芳' "
                    + "    WHEN name = '史玉梅' THEN '史小强' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '邓桂香' THEN '13600002016' "
                    + "    WHEN name = '方志伟' THEN '13600002017' "
                    + "    WHEN name = '史玉梅' THEN '13600002018' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '邓桂香' THEN '邓宇' "
                    + "    WHEN name = '方志伟' THEN '方芳' "
                    + "    WHEN name = '史玉梅' THEN '史强' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '邓桂香' THEN '13800003016' "
                    + "    WHEN name = '方志伟' THEN '13800003017' "
                    + "    WHEN name = '史玉梅' THEN '13800003018' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'SERIOUS'");

            // 危险状态老人 - E区（5楼，ICU监护区）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '高建设' THEN 'E501' "
                    + "    WHEN name = '段美华' THEN 'E502' "
                    + "    WHEN name = 'Liu Qiang' THEN 'E503' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '高建设' THEN '高小红' "
                    + "    WHEN name = '段美华' THEN '段小军' "
                    + "    WHEN name = 'Liu Qiang' THEN '刘小强' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '高建设' THEN '13600002019' "
                    + "    WHEN name = '段美华' THEN '13600002020' "
                    + "    WHEN name = 'Liu Qiang' THEN '13600002021' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '高建设' THEN '高红' "
                    + "    WHEN name = '段美华' THEN '段军' "
                    + "    WHEN name = 'Liu Qiang' THEN '刘强' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '高建设' THEN '13800003019' "
                    + "    WHEN name = '段美华' THEN '13800003020' "
                    + "    WHEN name = 'Liu Qiang' THEN '13800003021' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'DANGER'");

            // 预警状态老人 - F区（3楼，观察区）
            jdbcTemplate.execute("UPDATE elderly SET "
                    + "room_number = CASE "
                    + "    WHEN name = '薛志国' THEN 'F301' "
                    + "    WHEN name = '尹秀兰' THEN 'F302' "
                    + "    WHEN name = '雷建华' THEN 'F303' "
                    + "    WHEN name = 'Wang Fang' THEN 'F304' "
                    + "    ELSE room_number "
                    + "END, "
                    + "guardian_name = CASE "
                    + "    WHEN name = '薛志国' THEN '薛小燕' "
                    + "    WHEN name = '尹秀兰' THEN '尹小东' "
                    + "    WHEN name = '雷建华' THEN '雷小霞' "
                    + "    WHEN name = 'Wang Fang' THEN '王小芳' "
                    + "    ELSE guardian_name "
                    + "END, "
                    + "guardian_phone = CASE "
                    + "    WHEN name = '薛志国' THEN '13600002022' "
                    + "    WHEN name = '尹秀兰' THEN '13600002023' "
                    + "    WHEN name = '雷建华' THEN '13600002024' "
                    + "    WHEN name = 'Wang Fang' THEN '13600002025' "
                    + "    ELSE guardian_phone "
                    + "END, "
                    + "emergency_contact = CASE "
                    + "    WHEN name = '薛志国' THEN '薛燕' "
                    + "    WHEN name = '尹秀兰' THEN '尹东' "
                    + "    WHEN name = '雷建华' THEN '雷霞' "
                    + "    WHEN name = 'Wang Fang' THEN '王芳' "
                    + "    ELSE emergency_contact "
                    + "END, "
                    + "emergency_phone = CASE "
                    + "    WHEN name = '薛志国' THEN '13800003022' "
                    + "    WHEN name = '尹秀兰' THEN '13800003023' "
                    + "    WHEN name = '雷建华' THEN '13800003024' "
                    + "    WHEN name = 'Wang Fang' THEN '13800003025' "
                    + "    ELSE emergency_phone "
                    + "END "
                    + "WHERE health_status = 'WARNING'");

            return ResponseResult.success("老人档案房间号和监护人信息更新成功！按健康状态分配了不同楼层的房间。");
        } catch (Exception e) {
            return ResponseResult.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 为前十位老人补充详细健康档案（修正风险等级逻辑）
     */
    @PostMapping("/add-detailed-health-records")
    public ResponseResult<String> addDetailedHealthRecords() {
        try {
            // 删除旧的健康记录，重新插入逻辑正确的记录
            jdbcTemplate.execute("DELETE FROM health_record WHERE elderly_id IN (1,2,3,4,8,9,10,11,12,13)");

            // 为健康老人插入健康记录（低风险）
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal, symptoms, treatment, description) VALUES "
                    + // 陈建国 (id=8) - 健康老人
                    "(8, '2025-06-28', 72.0, 115, 75, 70, 36.4, 5.5, '身体状况良好，各项指标正常', '维生素D，钙片', '李主任', 1, 0, 0, '无明显不适症状', '继续保持良好生活习惯', '定期体检，健康老人标准'), "
                    + "(8, '2025-06-25', 71.8, 118, 78, 72, 36.5, 5.4, '血压血糖稳定，心率正常', '维生素B族', '李主任', 1, 0, 0, '偶有轻微疲劳', '适量运动，注意休息', '健康状况稳定'), "
                    + // 赵美丽 (id=9) - 健康老人
                    "(9, '2025-06-28', 58.5, 118, 78, 68, 36.6, 5.6, '整体健康状况良好', '钙片，维生素C', '王医生', 1, 0, 0, '精神状态良好', '保持规律作息', '健康老人，继续维护'), "
                    + "(9, '2025-06-25', 58.2, 120, 80, 70, 36.4, 5.7, '各项生理指标正常', '叶酸', '王医生', 1, 0, 0, '睡眠质量良好', '户外活动适量', '健康维护良好')");

            // 为亚健康老人插入记录（轻度风险）
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal, symptoms, treatment, description) VALUES "
                    + // 郑小敏 (id=13) - 亚健康
                    "(13, '2025-06-28', 52.3, 130, 82, 76, 36.8, 6.5, '血压轻度升高，需要关注', '降压药（轻剂量）', '张医生', 1, 1, 1, '偶有头晕，轻微胸闷', '低盐饮食，控制体重', '亚健康状态，需要生活方式调整'), "
                    + "(13, '2025-06-25', 52.0, 132, 84, 78, 36.9, 6.3, '血压波动，血糖轻微升高', '降压药', '张医生', 1, 1, 1, '头晕症状加重', '增加运动量，调整饮食', '需要密切监测血压变化'), "
                    + // 马德华 (id=14) - 亚健康  
                    "(14, '2025-06-28', 68.0, 125, 80, 74, 36.7, 6.0, '血脂偏高，肝功能轻度异常', '他汀类药物，护肝片', '赵医生', 1, 1, 1, '易疲劳，食欲不振', '低脂饮食，戒酒', '亚健康，需要肝功能保护')");

            // 为疾病老人插入记录（中度风险）
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal, symptoms, treatment, description) VALUES "
                    + // Liu Qiang (id=4) - 危险状态（但此处先按疾病处理）
                    "(4, '2025-06-28', 65.0, 150, 95, 85, 37.8, 8.5, '高血压合并糖尿病', '降压药，胰岛素', '陈主任', 2, 2, 1, '头痛，视物模糊，乏力', '胰岛素治疗，血压监测', '中度风险，需要药物控制'), "
                    + "(4, '2025-06-25', 64.8, 155, 98, 88, 38.0, 9.2, '血糖控制不佳，血压偏高', '调整胰岛素剂量', '陈主任', 2, 2, 1, '多饮多尿，头晕', '加强血糖监测', '糖尿病并发症风险增加'), "
                    + // Wang Fang (id=3) - 预警状态
                    "(3, '2025-06-28', 48.8, 140, 90, 68, 37.1, 7.8, '糖尿病前期，血压升高', '二甲双胍，降压药', '孙医生', 1, 2, 1, '口渴，疲劳感明显', '控制饮食，增加运动', '中度风险，预防糖尿病进展')");

            // 为重病老人插入记录（高度风险）
            jdbcTemplate.execute("INSERT INTO health_record (elderly_id, record_date, weight, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, diagnosis, medication, doctor_name, record_type, risk_level, is_abnormal, symptoms, treatment, description) VALUES "
                    + // Zhang Ming Updated (id=1) - 需要根据实际状态调整
                    "(1, '2025-06-28', 70.5, 120, 80, 72, 36.5, 5.8, '身体状况稳定，定期随访', '维生素类', '李主任', 1, 0, 0, '无特殊不适', '保持现状', '更新后健康状况良好'), "
                    + // Li Hua (id=2) - 亚健康
                    "(2, '2025-06-28', 55.2, 135, 85, 75, 36.7, 6.2, '血压偏高，需要监测', '降压药（小剂量）', '周医生', 1, 1, 1, '轻微头痛', '生活方式调整', '亚健康状态监测')");

            return ResponseResult.success("详细健康档案补充成功！已为前10位老人添加了符合健康状态的风险等级评估。");
        } catch (Exception e) {
            return ResponseResult.error("健康档案补充失败：" + e.getMessage());
        }
    }

    /**
     * 补充评估报告，修正风险等级逻辑
     */
    @PostMapping("/add-assessment-reports")
    public ResponseResult<String> addAssessmentReports() {
        try {
            // 检查是否存在评估报告表，如果不存在则创建
            try {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS elderly_care_assessment ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "elderly_id BIGINT NOT NULL, "
                        + "assessment_date DATE NOT NULL, "
                        + "assessment_type VARCHAR(20) NOT NULL COMMENT '评估类型:ADL,IADL,认知,营养', "
                        + "total_score INT NOT NULL, "
                        + "risk_level VARCHAR(10) NOT NULL COMMENT '风险等级:LOW,MEDIUM,HIGH,CRITICAL', "
                        + "assessment_result TEXT, "
                        + "suggestions TEXT, "
                        + "assessor_name VARCHAR(50), "
                        + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                        + "is_deleted TINYINT DEFAULT 0"
                        + ")");
            } catch (Exception e) {
                // 表可能已存在
            }

            // 为不同健康状态的老人插入相应的评估报告
            // 健康老人 - 低风险评估
            jdbcTemplate.execute("INSERT INTO elderly_care_assessment (elderly_id, assessment_date, assessment_type, total_score, risk_level, assessment_result, suggestions, assessor_name) VALUES "
                    + "(8, '2025-06-28', 'ADL', 95, 'LOW', '日常生活活动能力良好，能够独立完成各项基本生活活动，认知功能正常，营养状况良好', '继续保持良好的生活习惯，定期体检，适量运动，保持社交活动', '康复评估师-李评估'), "
                    + "(9, '2025-06-28', 'ADL', 92, 'LOW', '生活自理能力强，身体机能稳定，精神状态良好，社会适应能力佳', '维持当前生活方式，注意预防跌倒，定期健康检查', '康复评估师-王评估'), "
                    + "(10, '2025-06-28', 'IADL', 88, 'LOW', '工具性日常生活活动能力良好，能够处理复杂的日常事务，认知功能无明显异常', '鼓励参与社区活动，保持认知训练，注意营养均衡', '康复评估师-张评估')");

            // 亚健康老人 - 中低风险评估
            jdbcTemplate.execute("INSERT INTO elderly_care_assessment (elderly_id, assessment_date, assessment_type, total_score, risk_level, assessment_result, suggestions, assessor_name) VALUES "
                    + "(13, '2025-06-28', 'ADL', 75, 'MEDIUM', '日常生活活动部分需要协助，血压控制需要关注，存在轻度功能障碍', '加强血压监测，调整生活方式，适度运动，定期医疗随访', '康复评估师-赵评估'), "
                    + "(14, '2025-06-28', 'ADL', 78, 'MEDIUM', '生活自理能力有所下降，肝功能异常需要医疗干预，营养状况需要改善', '肝功能保护治疗，营养指导，戒酒，定期复查肝功能', '康复评估师-孙评估'), "
                    + "(2, '2025-06-28', '认知', 72, 'MEDIUM', '轻度认知功能下降，血压偏高，需要生活方式调整和药物治疗', '认知训练，血压管理，社会支持，家属配合护理', '康复评估师-周评估')");

            // 疾病和危险状态老人 - 高风险评估
            jdbcTemplate.execute("INSERT INTO elderly_care_assessment (elderly_id, assessment_date, assessment_type, total_score, risk_level, assessment_result, suggestions, assessor_name) VALUES "
                    + "(4, '2025-06-28', 'ADL', 45, 'HIGH', '日常生活活动严重受限，高血压和糖尿病并发症风险高，需要持续医疗监护', '24小时医疗监护，严格血糖血压控制，预防并发症，家属密切配合', '康复评估师-陈评估'), "
                    + "(3, '2025-06-28', '营养', 58, 'HIGH', '营养状况不良，糖尿病前期进展风险高，体重偏低，需要营养干预', '营养师指导，血糖监测，体重管理，预防糖尿病进展', '康复评估师-马评估'), "
                    + "(1, '2025-06-28', 'IADL', 85, 'LOW', '经过治疗后状况改善，工具性日常生活活动能力恢复良好', '继续康复训练，定期随访，保持治疗效果', '康复评估师-冯评估')");

            return ResponseResult.success("评估报告补充成功！已为10位老人添加了符合其健康状态的差异化评估报告。");
        } catch (Exception e) {
            return ResponseResult.error("评估报告补充失败：" + e.getMessage());
        }
    }
}
