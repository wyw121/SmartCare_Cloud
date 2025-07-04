package com.smartcare.cloud.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 健康预警数据初始化工具 用于自动插入健康预警中文测试数据
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Component("healthWarningDataInitializer")
public class DatabaseInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    // 常量定义
    private static final String SMART_BLOOD_PRESSURE_MONITOR = "智能血压计";
    private static final String SMART_GLUCOMETER = "血糖仪";
    private static final String SMART_THERMOMETER = "智能体温计";
    private static final String SMART_WEARABLE = "智能手环";
    private static final String FALL_DETECTOR = "跌倒检测器";
    private static final String SMART_PILL_BOX = "智能药盒";
    private static final String ACTIVITY_MONITOR = "活动监测器";
    private static final String SLEEP_MONITOR = "睡眠监测器";
    private static final String SMART_SCALE = "智能体重秤";
    private static final String ECG_MONITOR = "心电监护";

    private static final String COUNT_FIELD = "count";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("===========================================");
        logger.info("🏥 智慧医养平台 - 健康预警数据自动初始化");
        logger.info("===========================================");

        try {
            // 首先检查并添加缺失的字段
            checkAndAddMissingFields();

            // 每次启动都重新初始化数据
            logger.info("⚡ 正在清空现有数据并重新插入多样化中文健康预警数据...");
            initializeHealthWarningData();

            // 验证数据插入
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_health_warning", Integer.class);
            logger.info("✅ 健康预警中文数据插入完成！共插入 {} 条记录", count);

            // 显示数据分布
            showDataDistribution();
        } catch (Exception e) {
            logger.error("❌ 数据初始化失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 检查并添加缺失的字段
     */
    private void checkAndAddMissingFields() {
        try {
            // 检查 is_notified_family 字段
            String checkFamily = "SELECT COUNT(*) FROM information_schema.columns "
                    + "WHERE table_schema = DATABASE() AND table_name = 't_health_warning' "
                    + "AND column_name = 'is_notified_family'";

            Integer familyExists = jdbcTemplate.queryForObject(checkFamily, Integer.class);
            if (familyExists == 0) {
                logger.info("📋 添加 is_notified_family 字段...");
                jdbcTemplate.execute("ALTER TABLE t_health_warning ADD COLUMN is_notified_family TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已通知家属 (0-否, 1-是)' AFTER handle_remark");
            }

            // 检查 is_notified_doctor 字段
            String checkDoctor = "SELECT COUNT(*) FROM information_schema.columns "
                    + "WHERE table_schema = DATABASE() AND table_name = 't_health_warning' "
                    + "AND column_name = 'is_notified_doctor'";

            Integer doctorExists = jdbcTemplate.queryForObject(checkDoctor, Integer.class);
            if (doctorExists == 0) {
                logger.info("📋 添加 is_notified_doctor 字段...");
                jdbcTemplate.execute("ALTER TABLE t_health_warning ADD COLUMN is_notified_doctor TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已通知医生 (0-否, 1-是)' AFTER is_notified_family");
            }

            logger.info("✅ 数据表字段检查完成！");
        } catch (Exception e) {
            logger.error("⚠️ 字段检查失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 初始化健康预警数据
     */
    private void initializeHealthWarningData() {
        // 清空现有数据
        jdbcTemplate.execute("TRUNCATE TABLE t_health_warning");

        // 插入测试数据
        String sql = "INSERT INTO t_health_warning ("
                + "elderly_id, elderly_name, warning_type, warning_level, title, content, "
                + "trigger_data, data_source, status, handler_id, handler_name, "
                + "handle_time, handle_remark, is_notified_family, is_notified_doctor, "
                + "trigger_time, create_time, update_time, remark"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // 多样化健康预警数据
        List<Object[]> dataList = Arrays.asList(
                // 血压异常预警
                new Object[]{1001L, "张志明", "血压异常", 4, "血压严重偏高", "血压测量值达到180/110mmHg，远高于正常范围，存在高血压危象风险，需立即干预！",
                    "180/110mmHg（正常范围：90-140/60-90mmHg）", SMART_BLOOD_PRESSURE_MONITOR, 3, 2001L, "李医生",
                    LocalDateTime.of(2025, 7, 3, 8, 30, 0), "已安排急诊就医，血压得到控制", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 8, 12, 32), LocalDateTime.now(), LocalDateTime.now(), "高血压急症"},
                new Object[]{1002L, "李秀英", "心率异常", 4, "心率过速伴心律不齐", "心率持续超过120次/分钟，并伴有心律不齐，疑似心脏问题，请立即就医！",
                    "心率128次/分钟，不规则（正常范围：60-100次/分钟）", SMART_WEARABLE, 2, 2002L, "陈医生",
                    LocalDateTime.of(2025, 7, 3, 9, 45, 0), "已进行心电图检查，正在观察", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 9, 35, 17), LocalDateTime.now(), LocalDateTime.now(), "心律不齐"},
                new Object[]{1003L, "王建国", "体温异常", 4, "高热不退", "体温持续39.8℃，已超过24小时，有脱水风险，需立即医疗介入！",
                    "体温39.8℃（正常范围：36-37.2℃）", SMART_THERMOMETER, 3, 2001L, "李医生",
                    LocalDateTime.of(2025, 7, 3, 7, 5, 23), "已送医院治疗，体温下降", 1, 1,
                    LocalDateTime.of(2025, 7, 2, 21, 45, 8), LocalDateTime.now(), LocalDateTime.now(), "高热处理"},
                new Object[]{1004L, "陈淑华", "血糖异常", 4, "低血糖昏迷风险", "血糖值降至2.1mmol/L，存在昏迷风险，需紧急补充葡萄糖！",
                    "血糖2.1mmol/L（正常范围：3.9-6.1mmol/L）", SMART_GLUCOMETER, 3, 2003L, "刘医生",
                    LocalDateTime.of(2025, 7, 3, 10, 20, 0), "已注射葡萄糖，血糖恢复正常", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 10, 15, 45), LocalDateTime.now(), LocalDateTime.now(), "低血糖急救"},
                new Object[]{1005L, "赵老爷", "跌倒检测", 4, "严重跌倒事件", "检测到老人在卫生间严重跌倒，冲击力达8.5G，可能有骨折风险！",
                    "冲击力8.5G，位置：卫生间", FALL_DETECTOR, 3, 2004L, "急救队",
                    LocalDateTime.of(2025, 7, 3, 11, 30, 0), "已送医院检查，右髋骨折", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 11, 25, 12), LocalDateTime.now(), LocalDateTime.now(), "跌倒骨折"},
                // 中高风险预警
                new Object[]{1006L, "孙大妈", "血压异常", 3, "血压持续偏高", "血压连续3天超过160/95mmHg，需要调整用药方案",
                    "收缩压162mmHg，舒张压98mmHg", SMART_BLOOD_PRESSURE_MONITOR, 2, 2002L, "陈医生",
                    LocalDateTime.of(2025, 7, 3, 14, 15, 0), "已调整降压药物剂量", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 14, 10, 30), LocalDateTime.now(), LocalDateTime.now(), "血压管理"},
                new Object[]{1007L, "周奶奶", "血糖异常", 3, "餐后血糖超标", "餐后2小时血糖达到15.8mmol/L，存在糖尿病并发症风险",
                    "餐后血糖15.8mmol/L（正常<11.1mmol/L）", SMART_GLUCOMETER, 1, 2001L, "李医生",
                    LocalDateTime.of(2025, 7, 3, 15, 30, 0), "已安排内分泌科会诊", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 15, 25, 18), LocalDateTime.now(), LocalDateTime.now(), "血糖超标"},
                new Object[]{1008L, "吴老头", "心率异常", 3, "心率过缓", "心率仅42次/分钟，可能存在心脏传导阻滞",
                    "心率42次/分钟，规整", ECG_MONITOR, 2, 2003L, "刘医生",
                    LocalDateTime.of(2025, 7, 3, 16, 45, 0), "已做24小时心电监护", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 16, 40, 25), LocalDateTime.now(), LocalDateTime.now(), "心动过缓"},
                new Object[]{1009L, "马婆婆", "药物未按时服用", 3, "重要药物漏服", "降压药和抗凝药连续2天未服用，存在血栓风险",
                    "漏服药物：氨氯地平、华法林", SMART_PILL_BOX, 1, 2002L, "陈医生",
                    LocalDateTime.of(2025, 7, 3, 17, 15, 0), "已督促服药，调整用药时间", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 17, 10, 44), LocalDateTime.now(), LocalDateTime.now(), "药物管理"},
                new Object[]{1010L, "胡大爷", "长期不活动", 3, "72小时无活动", "连续72小时几乎无活动记录，可能存在健康问题",
                    "活动时间<2小时/天，连续3天", ACTIVITY_MONITOR, 2, 2004L, "护士长",
                    LocalDateTime.of(2025, 7, 3, 18, 0, 0), "已上门检查，发现感冒卧床", 1, 0,
                    LocalDateTime.of(2025, 7, 3, 17, 55, 33), LocalDateTime.now(), LocalDateTime.now(), "活动不足"},
                // 中低风险预警
                new Object[]{1011L, "曾奶奶", "血压异常", 2, "血压轻度偏高", "血压值148/88mmHg，略高于正常范围，建议观察",
                    "收缩压148mmHg，舒张压88mmHg", SMART_BLOOD_PRESSURE_MONITOR, 1, 2001L, "李医生",
                    LocalDateTime.of(2025, 7, 3, 19, 30, 0), "建议低盐饮食，继续观察", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 19, 25, 15), LocalDateTime.now(), LocalDateTime.now(), "血压监测"},
                new Object[]{1012L, "田大爷", "血糖异常", 2, "空腹血糖偏高", "空腹血糖7.2mmol/L，需要饮食控制",
                    "空腹血糖7.2mmol/L（正常<6.1mmol/L）", SMART_GLUCOMETER, 0, null, null,
                    null, null, 0, 1,
                    LocalDateTime.of(2025, 7, 3, 8, 0, 0), LocalDateTime.now(), LocalDateTime.now(), "血糖管理"},
                new Object[]{1013L, "邓老太", "睡眠异常", 2, "睡眠质量差", "连续一周睡眠时间不足5小时，可能影响身体健康",
                    "平均睡眠时间4.2小时/天", SLEEP_MONITOR, 1, 2003L, "刘医生",
                    LocalDateTime.of(2025, 7, 3, 20, 45, 0), "已开具助眠药物", 1, 1,
                    LocalDateTime.of(2025, 7, 3, 20, 40, 22), LocalDateTime.now(), LocalDateTime.now(), "睡眠障碍"},
                new Object[]{1014L, "黄大妈", "体温异常", 2, "体温轻度升高", "体温37.8℃，可能有轻微感染",
                    "体温37.8℃", SMART_THERMOMETER, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 14, 30, 0), LocalDateTime.now(), LocalDateTime.now(), "体温监测"},
                new Object[]{1015L, "郑爷爷", "药物未按时服用", 2, "维生素漏服", "复合维生素连续3天未服用",
                    "漏服药物：复合维生素B", SMART_PILL_BOX, 0, null, null,
                    null, null, 1, 0,
                    LocalDateTime.of(2025, 7, 3, 21, 0, 0), LocalDateTime.now(), LocalDateTime.now(), "营养补充"},
                // 低风险预警
                new Object[]{1016L, "韩奶奶", "血压异常", 1, "血压略有波动", "血压值在正常范围边缘，建议定期监测",
                    "收缩压135mmHg，舒张压85mmHg", SMART_BLOOD_PRESSURE_MONITOR, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 16, 15, 0), LocalDateTime.now(), LocalDateTime.now(), "血压观察"},
                new Object[]{1017L, "冯大爷", "心率异常", 1, "心率轻度过快", "心率105次/分钟，略高于正常范围",
                    "心率105次/分钟", SMART_WEARABLE, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 13, 45, 0), LocalDateTime.now(), LocalDateTime.now(), "心率监测"},
                new Object[]{1018L, "蒋老太", "活动量不足", 1, "今日活动量偏少", "今日活动时间仅2小时，建议增加运动",
                    "活动时间2小时（建议>4小时）", ACTIVITY_MONITOR, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 22, 0, 0), LocalDateTime.now(), LocalDateTime.now(), "运动建议"},
                new Object[]{1019L, "沈奶奶", "血糖异常", 1, "餐后血糖略高", "餐后血糖8.5mmol/L，在正常范围内偏高",
                    "餐后血糖8.5mmol/L", SMART_GLUCOMETER, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 12, 30, 0), LocalDateTime.now(), LocalDateTime.now(), "血糖观察"},
                new Object[]{1020L, "贾大爷", "体重异常", 1, "体重轻度下降", "一周内体重下降1.5kg，需要关注营养状况",
                    "体重变化：-1.5kg/周", SMART_SCALE, 0, null, null,
                    null, null, 0, 0,
                    LocalDateTime.of(2025, 7, 3, 7, 30, 0), LocalDateTime.now(), LocalDateTime.now(), "体重监测"}
        );

        // 批量插入数据
        jdbcTemplate.batchUpdate(sql, dataList);
    }

    /**
     * 显示数据分布统计
     */
    private void showDataDistribution() {
        logger.info("\n📊 健康预警数据分布统计：");

        // 按预警类型统计
        List<Map<String, Object>> typeStats = jdbcTemplate.queryForList(
                "SELECT warning_type as type, COUNT(*) as count FROM t_health_warning GROUP BY warning_type ORDER BY count DESC"
        );
        logger.info("📋 预警类型分布：");
        typeStats.forEach(row -> logger.info("   {} : {} 条", row.get("type"), row.get(COUNT_FIELD)));

        // 按预警级别统计
        List<Map<String, Object>> levelStats = jdbcTemplate.queryForList(
                "SELECT warning_level as level, COUNT(*) as count FROM t_health_warning GROUP BY warning_level ORDER BY level"
        );
        logger.info("\n📊 预警级别分布：");
        levelStats.forEach(row -> {
            String levelName = getLevelName((Integer) row.get("level"));
            logger.info("   {} : {} 条", levelName, row.get(COUNT_FIELD));
        });

        // 按处理状态统计
        List<Map<String, Object>> statusStats = jdbcTemplate.queryForList(
                "SELECT status, COUNT(*) as count FROM t_health_warning GROUP BY status ORDER BY status"
        );
        logger.info("\n📈 处理状态分布：");
        statusStats.forEach(row -> {
            String statusName = getStatusName((Integer) row.get("status"));
            logger.info("   {} : {} 条", statusName, row.get(COUNT_FIELD));
        });

        // 按通知状态统计
        List<Map<String, Object>> notificationStats = jdbcTemplate.queryForList(
                "SELECT "
                + "SUM(CASE WHEN is_notified_family = 1 THEN 1 ELSE 0 END) as family_notified, "
                + "SUM(CASE WHEN is_notified_doctor = 1 THEN 1 ELSE 0 END) as doctor_notified, "
                + "COUNT(*) as total FROM t_health_warning"
        );
        logger.info("\n📱 通知状态统计：");
        notificationStats.forEach(row -> {
            logger.info("   已通知家属: {} 条", row.get("family_notified"));
            logger.info("   已通知医生: {} 条", row.get("doctor_notified"));
            logger.info("   总计: {} 条", row.get("total"));
        });

        logger.info("\n🎉 健康预警中文数据已成功初始化！");
    }

    /**
     * 获取预警级别名称
     */
    private String getLevelName(Integer level) {
        switch (level) {
            case 1:
                return "级别1(低风险)";
            case 2:
                return "级别2(中风险)";
            case 3:
                return "级别3(高风险)";
            case 4:
                return "级别4(紧急)";
            default:
                return "未知级别";
        }
    }

    /**
     * 获取处理状态名称
     */
    private String getStatusName(Integer status) {
        switch (status) {
            case 0:
                return "状态0(未处理)";
            case 1:
                return "状态1(已查看)";
            case 2:
                return "状态2(处理中)";
            case 3:
                return "状态3(已处理)";
            case 4:
                return "状态4(已忽略)";
            default:
                return "未知状态";
        }
    }
}
