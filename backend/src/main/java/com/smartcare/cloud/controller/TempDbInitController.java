package com.smartcare.cloud.controller;

import com.smartcare.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 数据库初始化控制器（临时使用）
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/temp/db-init")
public class TempDbInitController {

    private static final Logger log = LoggerFactory.getLogger(TempDbInitController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 初始化健康预警表和测试数据
     */
    @PostMapping("/health-warning")
    public ResponseResult<String> initHealthWarningData() {
        log.info("开始初始化健康预警表和测试数据");
        try {
            // 创建健康预警表
            String createTableSql = "CREATE TABLE IF NOT EXISTS `t_health_warning` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                    "  `elderly_id` bigint NOT NULL COMMENT '老人ID'," +
                    "  `elderly_name` varchar(50) NOT NULL COMMENT '老人姓名'," +
                    "  `warning_type` varchar(50) NOT NULL COMMENT '预警类型'," +
                    "  `warning_level` int NOT NULL COMMENT '预警级别(1-低风险,2-中风险,3-高风险,4-紧急)'," +
                    "  `title` varchar(200) NOT NULL COMMENT '预警标题'," +
                    "  `content` text COMMENT '预警内容描述'," +
                    "  `trigger_data` varchar(500) COMMENT '触发数据'," +
                    "  `data_source` varchar(100) COMMENT '数据来源'," +
                    "  `status` int NOT NULL DEFAULT '0' COMMENT '预警状态(0-未处理,1-已查看,2-处理中,3-已处理,4-已忽略)'," +
                    "  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID'," +
                    "  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名'," +
                    "  `handle_time` datetime DEFAULT NULL COMMENT '处理时间'," +
                    "  `handle_remark` text COMMENT '处理备注'," +
                    "  `is_notified_family` tinyint DEFAULT '0' COMMENT '是否已通知家属(0-否,1-是)'," +
                    "  `is_notified_doctor` tinyint DEFAULT '0' COMMENT '是否已通知医生(0-否,1-是)'," +
                    "  `trigger_time` datetime NOT NULL COMMENT '预警触发时间'," +
                    "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                    "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                    "  `remark` varchar(500) DEFAULT NULL COMMENT '备注'," +
                    "  PRIMARY KEY (`id`)," +
                    "  KEY `idx_elderly_id` (`elderly_id`)," +
                    "  KEY `idx_warning_type` (`warning_type`)," +
                    "  KEY `idx_warning_level` (`warning_level`)," +
                    "  KEY `idx_status` (`status`)," +
                    "  KEY `idx_trigger_time` (`trigger_time`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康预警表'";

            jdbcTemplate.execute(createTableSql);
            log.info("健康预警表创建成功");

            // 清理现有数据
            jdbcTemplate.execute("DELETE FROM t_health_warning WHERE id > 0");
            log.info("清理现有数据完成");

            // 重置自增ID
            jdbcTemplate.execute("ALTER TABLE t_health_warning AUTO_INCREMENT = 1");
            log.info("重置自增ID完成");

            // 插入测试数据
            String[] insertSqls = {
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES " +
                "(1, '张老太', '血压异常', 3, '高血压紧急预警', '检测到张老太血压持续偏高，收缩压186mmHg，舒张压102mmHg，已超过安全范围', '收缩压:186mmHg,舒张压:102mmHg', '智能血压计', 0, '2024-01-27 09:15:32')",
                
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES " +
                "(2, '李大爷', '心率异常', 2, '心率偏快预警', '李大爷心率持续偏快，当前心率105次/分钟，建议关注', '心率:105次/分钟', '智能手环', 1, '2024-01-27 08:30:15')",
                
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES " +
                "(3, '王奶奶', '血糖异常', 4, '血糖危险预警', '王奶奶空腹血糖严重偏高，当前值12.8mmol/L，需要立即就医', '空腹血糖:12.8mmol/L', '血糖仪', 0, '2024-01-27 07:45:20')",
                
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, handler_id, handler_name, handle_time, trigger_time) VALUES " +
                "(4, '赵老伯', '长期不活动', 2, '活动量不足预警', '赵老伯今日活动量明显不足，步数仅88步，建议增加活动', '步数:88步,活动时长:15分钟', '智能手环', 3, 101, '护士小李', '2024-01-27 10:20:00', '2024-01-27 06:00:00')",
                
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES " +
                "(5, '陈老师', '体温异常', 3, '发热预警', '陈老师体温偏高，当前体温38.2°C，持续监测中', '体温:38.2°C', '智能体温计', 2, '2024-01-27 05:30:45')"
            };

            for (String sql : insertSqls) {
                jdbcTemplate.execute(sql);
            }
            
            log.info("测试数据插入完成，共插入5条记录");
            
            return ResponseResult.success("健康预警表和测试数据初始化成功！");
        } catch (Exception e) {
            log.error("初始化健康预警数据失败", e);
            return ResponseResult.error("初始化失败：" + e.getMessage());
        }
    }
}
