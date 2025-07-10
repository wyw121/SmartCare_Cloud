-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: smartcare_cloud
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_health_warning`
--

DROP TABLE IF EXISTS `t_health_warning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_health_warning` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `elderly_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '老人姓名',
  `warning_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预警类型',
  `warning_level` int NOT NULL COMMENT '预警级别 (1-低风险, 2-中风险, 3-高风险, 4-紧急)',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预警标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预警内容',
  `trigger_data` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '触发数据',
  `data_source` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `trigger_time` datetime NOT NULL COMMENT '触发时间',
  `status` int NOT NULL DEFAULT '0' COMMENT '处理状态 (0-未处理, 1-已查看, 2-处理中, 3-已处理, 4-已忽略)',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_remark` text COLLATE utf8mb4_unicode_ci COMMENT '处理备注',
  `is_notified_family` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已通知家属 (0-否, 1-是)',
  `is_notified_doctor` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已通知医生 (0-否, 1-是)',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 (1-已删除, 0-未删除)',
  PRIMARY KEY (`id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_warning_level` (`warning_level`),
  KEY `idx_trigger_time` (`trigger_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康预警表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_health_warning`
--

LOCK TABLES `t_health_warning` WRITE;
/*!40000 ALTER TABLE `t_health_warning` DISABLE KEYS */;
INSERT INTO `t_health_warning` VALUES (1,1001,'张志明','血压异常',4,'血压严重偏高','血压测量值达到180/110mmHg，远高于正常范围，存在高血压危象风险，需立即干预！','180/110mmHg（正常范围：90-140/60-90mmHg）','智能血压计','2025-07-03 08:12:32',3,2001,'李医生','2025-07-03 08:30:00','已安排急诊就医，血压得到控制',1,1,'高血压急症','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(2,1002,'李秀英','心率异常',4,'心率过速伴心律不齐','心率持续超过120次/分钟，并伴有心律不齐，疑似心脏问题，请立即就医！','心率128次/分钟，不规则（正常范围：60-100次/分钟）','智能手环','2025-07-03 09:35:17',2,2002,'陈医生','2025-07-03 09:45:00','已进行心电图检查，正在观察',1,1,'心律不齐','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(3,1003,'王建国','体温异常',4,'高热不退','体温持续39.8℃，已超过24小时，有脱水风险，需立即医疗介入！','体温39.8℃（正常范围：36-37.2℃）','智能体温计','2025-07-02 21:45:08',3,2001,'李医生','2025-07-03 07:05:23','已送医院治疗，体温下降',1,1,'高热处理','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(4,1004,'陈淑华','血糖异常',4,'低血糖昏迷风险','血糖值降至2.1mmol/L，存在昏迷风险，需紧急补充葡萄糖！','血糖2.1mmol/L（正常范围：3.9-6.1mmol/L）','血糖仪','2025-07-03 10:15:45',3,2003,'刘医生','2025-07-03 10:20:00','已注射葡萄糖，血糖恢复正常',1,1,'低血糖急救','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(5,1005,'赵老爷','跌倒检测',4,'严重跌倒事件','检测到老人在卫生间严重跌倒，冲击力达8.5G，可能有骨折风险！','冲击力8.5G，位置：卫生间','跌倒检测器','2025-07-03 11:25:12',3,2004,'急救队','2025-07-03 11:30:00','已送医院检查，右髋骨折',1,1,'跌倒骨折','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(6,1006,'孙大妈','血压异常',3,'血压持续偏高','血压连续3天超过160/95mmHg，需要调整用药方案','收缩压162mmHg，舒张压98mmHg','智能血压计','2025-07-03 14:10:30',2,2002,'陈医生','2025-07-03 14:15:00','已调整降压药物剂量',1,1,'血压管理','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(7,1007,'周奶奶','血糖异常',3,'餐后血糖超标','餐后2小时血糖达到15.8mmol/L，存在糖尿病并发症风险','餐后血糖15.8mmol/L（正常<11.1mmol/L）','血糖仪','2025-07-03 15:25:18',1,2001,'李医生','2025-07-03 15:30:00','已安排内分泌科会诊',1,1,'血糖超标','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(8,1008,'吴老头','心率异常',3,'心率过缓','心率仅42次/分钟，可能存在心脏传导阻滞','心率42次/分钟，规整','心电监护','2025-07-03 16:40:25',2,2003,'刘医生','2025-07-03 16:45:00','已做24小时心电监护',1,1,'心动过缓','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(9,1009,'马婆婆','药物未按时服用',3,'重要药物漏服','降压药和抗凝药连续2天未服用，存在血栓风险','漏服药物：氨氯地平、华法林','智能药盒','2025-07-03 17:10:44',1,2002,'陈医生','2025-07-03 17:15:00','已督促服药，调整用药时间',1,1,'药物管理','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(10,1010,'胡大爷','长期不活动',3,'72小时无活动','连续72小时几乎无活动记录，可能存在健康问题','活动时间<2小时/天，连续3天','活动监测器','2025-07-03 17:55:33',2,2004,'护士长','2025-07-03 18:00:00','已上门检查，发现感冒卧床',1,0,'活动不足','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(11,1011,'曾奶奶','血压异常',2,'血压轻度偏高','血压值148/88mmHg，略高于正常范围，建议观察','收缩压148mmHg，舒张压88mmHg','智能血压计','2025-07-03 19:25:15',1,2001,'李医生','2025-07-03 19:30:00','建议低盐饮食，继续观察',1,1,'血压监测','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(12,1012,'田大爷','血糖异常',2,'空腹血糖偏高','空腹血糖7.2mmol/L，需要饮食控制','空腹血糖7.2mmol/L（正常<6.1mmol/L）','血糖仪','2025-07-03 08:00:00',0,NULL,NULL,NULL,NULL,0,1,'血糖管理','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(13,1013,'邓老太','睡眠异常',2,'睡眠质量差','连续一周睡眠时间不足5小时，可能影响身体健康','平均睡眠时间4.2小时/天','睡眠监测器','2025-07-03 20:40:22',1,2003,'刘医生','2025-07-03 20:45:00','已开具助眠药物',1,1,'睡眠障碍','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(14,1014,'黄大妈','体温异常',2,'体温轻度升高','体温37.8℃，可能有轻微感染','体温37.8℃','智能体温计','2025-07-03 14:30:00',0,NULL,NULL,NULL,NULL,0,0,'体温监测','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(15,1015,'郑爷爷','药物未按时服用',2,'维生素漏服','复合维生素连续3天未服用','漏服药物：复合维生素B','智能药盒','2025-07-03 21:00:00',0,NULL,NULL,NULL,NULL,1,0,'营养补充','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(16,1016,'韩奶奶','血压异常',1,'血压略有波动','血压值在正常范围边缘，建议定期监测','收缩压135mmHg，舒张压85mmHg','智能血压计','2025-07-03 16:15:00',0,NULL,NULL,NULL,NULL,0,0,'血压观察','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(17,1017,'冯大爷','心率异常',1,'心率轻度过快','心率105次/分钟，略高于正常范围','心率105次/分钟','智能手环','2025-07-03 13:45:00',0,NULL,NULL,NULL,NULL,0,0,'心率监测','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(18,1018,'蒋老太','活动量不足',1,'今日活动量偏少','今日活动时间仅2小时，建议增加运动','活动时间2小时（建议>4小时）','活动监测器','2025-07-03 22:00:00',0,NULL,NULL,NULL,NULL,0,0,'运动建议','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(19,1019,'沈奶奶','血糖异常',1,'餐后血糖略高','餐后血糖8.5mmol/L，在正常范围内偏高','餐后血糖8.5mmol/L','血糖仪','2025-07-03 12:30:00',0,NULL,NULL,NULL,NULL,0,0,'血糖观察','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(20,1020,'贾大爷','体重异常',1,'体重轻度下降','一周内体重下降1.5kg，需要关注营养状况','体重变化：-1.5kg/周','智能体重秤','2025-07-03 07:30:00',0,NULL,NULL,NULL,NULL,0,0,'体重监测','2025-07-10 17:18:19','2025-07-10 17:18:19',NULL,NULL,0),(21,1,'李志强','血压异常',3,'高血压风险预警','监测发现李志强老人血压持续偏高，收缩压150mmHg，舒张压95mmHg，建议及时就医调整用药。','收缩压:150mmHg;舒张压:95mmHg','血压监测设备','2025-07-10 15:19:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(22,1,'李志强','心率异常',2,'心率不齐预警','检测到李志强老人心率不规则，平均心率105次/分钟，存在心律不齐现象，建议关注。','心率:105次/分钟;心律:不规则','心率监测设备','2025-07-10 12:19:28',1,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(23,1,'李志强','血糖异常',2,'血糖偏高预警','李志强老人餐后2小时血糖值为13.2mmol/L，超出正常范围，需要调整饮食和用药。','餐后2h血糖:13.2mmol/L','血糖监测设备','2025-07-09 17:19:28',2,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(24,1,'李志强','用药提醒',1,'降压药服用提醒','李志强老人今日早晨降压药（硝苯地平）尚未服用，请及时提醒。','药物:硝苯地平;剂量:10mg;时间:08:00','智能药盒','2025-07-10 16:49:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(25,1,'李志强','活动量监测',1,'日常活动量不足','李志强老人今日步行仅320步，活动量明显不足，建议增加适度运动。','步数:320步;活动时间:15分钟','智能手环','2025-07-10 14:19:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(26,2,'王淑华','跌倒风险',4,'紧急跌倒预警','检测到王淑华老人在卫生间发生跌倒，已自动报警，请立即前往查看！','位置:卫生间;加速度异常:8.5G','跌倒检测器','2025-07-10 17:04:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(27,2,'王淑华','血压异常',2,'低血压预警','王淑华老人血压偏低，收缩压85mmHg，舒张压55mmHg，有头晕风险，请注意观察。','收缩压:85mmHg;舒张压:55mmHg','血压监测设备','2025-07-10 16:19:28',1,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(28,2,'王淑华','睡眠监测',2,'睡眠质量差预警','王淑华老人昨夜睡眠质量较差，深睡眠仅2.1小时，夜间醒来6次，建议关注睡眠情况。','总睡眠:6.2小时;深睡眠:2.1小时;醒来次数:6次','睡眠监测设备','2025-07-10 09:19:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(29,2,'王淑华','体温异常',3,'发热预警','王淑华老人体温升高至38.2°C，持续1小时，可能存在感染风险，建议及时就医。','体温:38.2°C;持续时间:1小时','体温监测设备','2025-07-10 15:19:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(30,2,'王淑华','定位监测',2,'活动范围异常','王淑华老人离开正常活动区域超过30分钟，当前位置：养老院后花园，请确认安全。','位置:后花园;离开时间:30分钟','GPS定位器','2025-07-10 16:34:28',1,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(31,3,'李建民','血糖异常',2,'糖尿病血糖控制不佳','李建民老人空腹血糖8.5mmol/L，控制不理想，建议调整饮食和运动方案。','空腹血糖:8.5mmol/L','血糖监测设备','2025-07-10 13:19:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0),(32,3,'李建民','用药提醒',1,'胰岛素注射提醒','李建民老人晚餐前胰岛素尚未注射，请及时提醒注射。','药物:胰岛素;剂量:8单位;时间:17:30','智能药盒','2025-07-10 16:59:28',0,NULL,NULL,NULL,NULL,0,0,NULL,'2025-07-10 17:19:28','2025-07-10 17:19:28','system',NULL,0);
/*!40000 ALTER TABLE `t_health_warning` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 17:21:31
