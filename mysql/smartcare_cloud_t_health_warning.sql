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
  `elderly_name` varchar(50) NOT NULL COMMENT '老人姓名',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型',
  `warning_level` int NOT NULL COMMENT '预警级别(1-低风险,2-中风险,3-高风险,4-紧急)',
  `title` varchar(200) NOT NULL COMMENT '预警标题',
  `content` text COMMENT '预警内容描述',
  `trigger_data` varchar(500) DEFAULT NULL COMMENT '触发数据',
  `data_source` varchar(100) DEFAULT NULL COMMENT '数据来源',
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康预警表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_health_warning`
--

LOCK TABLES `t_health_warning` WRITE;
/*!40000 ALTER TABLE `t_health_warning` DISABLE KEYS */;
INSERT INTO `t_health_warning` VALUES (1,1,'张老太','血压异常',3,'高血压紧急预警','检测到张老太血压持续偏高，收缩压186mmHg，舒张压102mmHg，已超过安全范围','收缩压:186mmHg,舒张压:102mmHg','智能血压计',3,1,'系统管理员','2025-06-27 10:13:35','Contacted family, blood pressure is now stable, regular monitoring recommended',0,0,'2024-01-27 09:15:32','2025-06-27 10:11:28','2025-06-27 10:11:28',NULL),(2,2,'李大爷','心率异常',2,'心率偏快预警','李大爷心率持续偏快，当前心率105次/分钟，建议关注','心率:105次/分钟','智能手环',3,1,'系统管理员','2025-06-27 10:35:24','啊啊啊啊啊啊啊啊啊啊啊啊啊',0,0,'2024-01-27 08:30:15','2025-06-27 10:11:28','2025-06-27 10:11:28',NULL),(3,3,'王奶奶','血糖异常',4,'血糖危险预警','王奶奶空腹血糖严重偏高，当前值12.8mmol/L，需要立即就医','空腹血糖:12.8mmol/L','血糖仪',3,1,'系统管理员','2025-06-27 12:01:26','啊啊啊啊啊啊啊啊啊啊啊啊啊',0,0,'2024-01-27 07:45:20','2025-06-27 10:11:28','2025-06-27 10:11:28',NULL),(4,4,'赵老伯','长期不活动',2,'活动量不足预警','赵老伯今日活动量明显不足，步数仅88步，建议增加活动','步数:88步,活动时长:15分钟','智能手环',3,101,'护士小李','2024-01-27 10:20:00',NULL,0,0,'2024-01-27 06:00:00','2025-06-27 10:11:28','2025-06-27 10:11:28',NULL),(5,5,'陈老师','体温异常',3,'发热预警','陈老师体温偏高，当前体温38.2°C，持续监测中','体温:38.2°C','智能体温计',2,NULL,NULL,NULL,NULL,0,0,'2024-01-27 05:30:45','2025-06-27 10:11:28','2025-06-27 10:11:28',NULL);
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

-- Dump completed on 2025-06-30  0:00:15
