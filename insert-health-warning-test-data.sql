-- 智慧医养健康预警测试数据
-- 首先创建健康预警表（如果不存在）
CREATE TABLE IF NOT EXISTS `t_health_warning` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `elderly_name` varchar(50) NOT NULL COMMENT '老人姓名',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型',
  `warning_level` int NOT NULL COMMENT '预警级别(1-低风险,2-中风险,3-高风险,4-紧急)',
  `title` varchar(200) NOT NULL COMMENT '预警标题',
  `content` text COMMENT '预警内容描述',
  `trigger_data` varchar(500) COMMENT '触发数据',
  `data_source` varchar(100) COMMENT '数据来源',
  `status` int NOT NULL DEFAULT '0' COMMENT '预警状态(0-未处理,1-已查看,2-处理中,3-已处理,4-已忽略)',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_remark` text COMMENT '处理备注',
  `is_notified_family` tinyint DEFAULT '0' COMMENT '是否已通知家属(0-否,1-是)',
  `is_notified_doctor` tinyint DEFAULT '0' COMMENT '是否已通知医生(0-否,1-是)',
  `trigger_time` datetime NOT NULL COMMENT '预警触发时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_warning_level` (`warning_level`),
  KEY `idx_status` (`status`),
  KEY `idx_trigger_time` (`trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康预警表';

-- 清理现有数据
DELETE FROM t_health_warning WHERE id > 0;

-- 重置自增ID
ALTER TABLE t_health_warning AUTO_INCREMENT = 1;

-- 插入测试健康预警数据
INSERT INTO t_health_warning (
    elderly_id,
    elderly_name,
    warning_type,
    warning_level,
    title,
    content,
    trigger_data,
    data_source,
    status,
    handler_id,
    handler_name,
    handle_time,
    handle_remark,
    is_notified_family,
    is_notified_doctor,
    trigger_time
) VALUES 
-- 1. 张国强 - 血压异常 - 高风险未处理
(1, '张国强', '血压异常', 3, '张国强血压监测异常', '根据血压监测设备监测数据，发现张国强的血压异常情况，需要及时关注和处理。', '收缩压: 165mmHg, 舒张压: 98mmHg', '血压监测设备', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 08:30:00'),

-- 2. 李秀英 - 血糖异常 - 中风险已查看
(2, '李秀英', '血糖异常', 2, '李秀英血糖水平异常', '根据血糖仪监测数据，发现李秀英的血糖异常情况，需要及时关注和处理。', '血糖值: 8.2mmol/L', '血糖仪', 1, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 09:15:00'),

-- 3. 王建国 - 心率异常 - 紧急已处理
(3, '王建国', '心率异常', 4, '王建国心率不规律', '根据心率监测器监测数据，发现王建国的心率异常情况，需要及时关注和处理。', '心率: 120次/分', '心率监测器', 3, 1, '张医生', '2024-06-27 10:30:00', '已联系家属，王建国的心率异常情况已得到及时处理，目前状况稳定。', 1, 1, '2024-06-27 10:00:00'),

-- 4. 赵淑芳 - 体温异常 - 中风险处理中
(4, '赵淑芳', '体温异常', 2, '赵淑芳体温异常', '根据体温计监测数据，发现赵淑芳的体温异常情况，需要及时关注和处理。', '体温: 38.2°C', '体温计', 2, 1, '李护士', NULL, NULL, 1, 1, '2024-06-27 11:00:00'),

-- 5. 刘德华 - 长期不活动 - 低风险已忽略
(5, '刘德华', '长期不活动', 1, '刘德华长时间无活动迹象', '根据活动监测器监测数据，发现刘德华的长期不活动情况，需要及时关注和处理。', '连续10小时无活动记录', '活动监测器', 4, 1, '王医生', '2024-06-27 12:00:00', '老人正在休息，无需特殊处理', 0, 0, '2024-06-27 07:00:00'),

-- 6. 陈美丽 - 药物未按时服用 - 中风险未处理
(6, '陈美丽', '药物未按时服用', 2, '陈美丽未按时服药提醒', '根据药物提醒器监测数据，发现陈美丽的药物未按时服用情况，需要及时关注和处理。', '降压药延迟2小时服用', '药物提醒器', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 14:00:00'),

-- 7. 孙文华 - 跌倒风险 - 高风险已查看
(7, '孙文华', '跌倒风险', 3, '孙文华跌倒风险预警', '根据跌倒检测器监测数据，发现孙文华的跌倒风险情况，需要及时关注和处理。', '行走不稳，步态异常评分: 85', '跌倒检测器', 1, NULL, NULL, NULL, NULL, 0, 1, '2024-06-27 15:30:00'),

-- 8. 周桂花 - 血压异常 - 紧急未处理
(8, '周桂花', '血压异常', 4, '周桂花血压监测异常', '根据血压监测设备监测数据，发现周桂花的血压异常情况，需要及时关注和处理。', '收缩压: 180mmHg, 舒张压: 110mmHg', '血压监测设备', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 16:00:00'),

-- 9. 吴国庆 - 心率异常 - 高风险处理中
(9, '吴国庆', '心率异常', 3, '吴国庆心率不规律', '根据心率监测器监测数据，发现吴国庆的心率异常情况，需要及时关注和处理。', '心率: 45次/分', '心率监测器', 2, 1, '张医生', NULL, NULL, 1, 1, '2024-06-27 17:00:00'),

-- 10. 郑丽华 - 血糖异常 - 紧急已处理
(10, '郑丽华', '血糖异常', 4, '郑丽华血糖水平异常', '根据血糖仪监测数据，发现郑丽华的血糖异常情况，需要及时关注和处理。', '血糖值: 12.5mmol/L', '血糖仪', 3, 1, '李医生', '2024-06-27 18:30:00', '已紧急送医，血糖已控制稳定', 1, 1, '2024-06-27 18:00:00'),

-- 11. 马志强 - 体温异常 - 低风险已查看
(11, '马志强', '体温异常', 1, '马志强体温异常', '根据体温计监测数据，发现马志强的体温异常情况，需要及时关注和处理。', '体温: 37.2°C', '体温计', 1, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 19:00:00'),

-- 12. 黄春梅 - 长期不活动 - 中风险未处理
(12, '黄春梅', '长期不活动', 2, '黄春梅长时间无活动迹象', '根据活动监测器监测数据，发现黄春梅的长期不活动情况，需要及时关注和处理。', '连续8小时无活动记录', '活动监测器', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 13:00:00'),

-- 13. 林国栋 - 药物未按时服用 - 高风险已处理
(13, '林国栋', '药物未按时服用', 3, '林国栋未按时服药提醒', '根据药物提醒器监测数据，发现林国栋的药物未按时服用情况，需要及时关注和处理。', '心脏药延迟3小时服用', '药物提醒器', 3, 1, '王护士', '2024-06-27 20:00:00', '已提醒老人按时服药，并联系家属监督', 1, 0, '2024-06-27 16:00:00'),

-- 14. 杨秀珍 - 跌倒风险 - 低风险已忽略
(14, '杨秀珍', '跌倒风险', 1, '杨秀珍跌倒风险预警', '根据跌倒检测器监测数据，发现杨秀珍的跌倒风险情况，需要及时关注和处理。', '步态异常评分: 75', '跌倒检测器', 4, 1, '张护士', '2024-06-27 21:00:00', '老人行走正常，无跌倒风险', 0, 0, '2024-06-27 20:30:00'),

-- 15. 何建军 - 其他异常 - 中风险处理中
(15, '何建军', '其他异常', 2, '何建军健康状况异常', '根据手动录入监测数据，发现何建军的其他异常情况，需要及时关注和处理。', '综合健康评分异常: 65', '手动录入', 2, 1, '李医生', NULL, NULL, 0, 1, '2024-06-27 22:00:00'),

-- 16. 张国强 - 心率异常 - 低风险未处理（同一个老人的另一个预警）
(1, '张国强', '心率异常', 1, '张国强心率监测异常', '根据心率监测器监测数据，发现张国强的心率异常情况，需要及时关注和处理。', '心率: 95次/分', '心率监测器', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-27 23:00:00'),

-- 17. 李秀英 - 体温异常 - 紧急已查看
(2, '李秀英', '体温异常', 4, '李秀英体温异常', '根据体温计监测数据，发现李秀英的体温异常情况，需要及时关注和处理。', '体温: 39.5°C', '体温计', 1, NULL, NULL, NULL, NULL, 1, 1, '2024-06-27 23:30:00'),

-- 18. 王建国 - 血压异常 - 中风险未处理
(3, '王建国', '血压异常', 2, '王建国血压监测异常', '根据血压监测设备监测数据，发现王建国的血压异常情况，需要及时关注和处理。', '收缩压: 150mmHg, 舒张压: 95mmHg', '血压监测设备', 0, NULL, NULL, NULL, NULL, 0, 0, '2024-06-28 00:00:00'),

-- 19. 赵淑芳 - 药物未按时服用 - 低风险已处理
(4, '赵淑芳', '药物未按时服用', 1, '赵淑芳未按时服药提醒', '根据药物提醒器监测数据，发现赵淑芳的药物未按时服用情况，需要及时关注和处理。', '降糖药延迟1小时服用', '药物提醒器', 3, 1, '陈护士', '2024-06-28 01:00:00', '已提醒按时服药', 0, 0, '2024-06-28 00:30:00'),

-- 20. 刘德华 - 血糖异常 - 高风险处理中
(5, '刘德华', '血糖异常', 3, '刘德华血糖水平异常', '根据血糖仪监测数据，发现刘德华的血糖异常情况，需要及时关注和处理。', '血糖值: 10.8mmol/L', '血糖仪', 2, 1, '王医生', NULL, NULL, 1, 1, '2024-06-28 01:00:00');

-- 查询结果验证
SELECT 
    id,
    elderly_name,
    warning_type,
    warning_level,
    CASE warning_level 
        WHEN 1 THEN '低风险'
        WHEN 2 THEN '中风险'
        WHEN 3 THEN '高风险'
        WHEN 4 THEN '紧急'
        ELSE '未知'
    END as level_text,
    title,
    CASE status 
        WHEN 0 THEN '未处理'
        WHEN 1 THEN '已查看'
        WHEN 2 THEN '处理中'
        WHEN 3 THEN '已处理'
        WHEN 4 THEN '已忽略'
        ELSE '未知'
    END as status_text,
    handler_name,
    trigger_time,
    create_time
FROM t_health_warning
ORDER BY trigger_time DESC
LIMIT 10;

-- 统计各级别预警数量
SELECT 
    warning_level as level,
    CASE warning_level 
        WHEN 1 THEN '低风险'
        WHEN 2 THEN '中风险'
        WHEN 3 THEN '高风险'
        WHEN 4 THEN '紧急'
        ELSE '未知'
    END as level_text,
    COUNT(*) as count
FROM t_health_warning
GROUP BY warning_level
ORDER BY warning_level;

-- 统计各状态预警数量
SELECT 
    status,
    CASE status 
        WHEN 0 THEN '未处理'
        WHEN 1 THEN '已查看'
        WHEN 2 THEN '处理中'
        WHEN 3 THEN '已处理'
        WHEN 4 THEN '已忽略'
        ELSE '未知'
    END as status_text,
    COUNT(*) as count
FROM t_health_warning
GROUP BY status
ORDER BY status;
