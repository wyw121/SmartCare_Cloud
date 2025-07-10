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
-- Table structure for table `health_record`
--

DROP TABLE IF EXISTS `health_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `doctor_id` bigint DEFAULT NULL COMMENT '医生ID',
  `record_type` int NOT NULL DEFAULT '1' COMMENT '记录类型',
  `record_date` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '记录日期',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '记录标题',
  `doctor_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生姓名',
  `blood_pressure_high` int DEFAULT NULL COMMENT '收缩压',
  `blood_pressure_low` int DEFAULT NULL COMMENT '舒张压',
  `heart_rate` int DEFAULT NULL COMMENT '心率',
  `temperature` decimal(4,1) DEFAULT NULL COMMENT '体温',
  `blood_sugar` decimal(5,2) DEFAULT NULL COMMENT '血糖',
  `weight` decimal(5,2) DEFAULT NULL COMMENT '体重',
  `symptoms` text COLLATE utf8mb4_unicode_ci COMMENT '症状描述',
  `diagnosis` text COLLATE utf8mb4_unicode_ci COMMENT '诊断结果',
  `treatment` text COLLATE utf8mb4_unicode_ci COMMENT '治疗方案',
  `medication` text COLLATE utf8mb4_unicode_ci COMMENT '用药信息',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '详细描述',
  `risk_level` int NOT NULL DEFAULT '0' COMMENT '风险等级',
  `is_abnormal` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否异常',
  `remarks` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_record`
--

LOCK TABLES `health_record` WRITE;
/*!40000 ALTER TABLE `health_record` DISABLE KEYS */;
INSERT INTO `health_record` VALUES (10,14,NULL,1,'2025-06-28',NULL,'周医生',125,80,74,36.7,6.00,68.00,NULL,'血脂偏高',NULL,'他汀类药物',NULL,1,1,NULL,'2025-06-29 23:20:42','2025-06-29 23:20:42',NULL,NULL,0),(11,17,NULL,2,'2025-06-28',NULL,'吴医生',145,92,82,37.2,8.20,45.20,NULL,'糖尿病，骨质疏松',NULL,'胰岛素，钙剂',NULL,2,1,NULL,'2025-06-29 23:20:42','2025-06-29 23:20:42',NULL,NULL,0),(12,18,NULL,2,'2025-06-28',NULL,'郑医生',155,98,88,37.5,9.10,70.80,NULL,'高血压，心脏病',NULL,'降压药，强心剂',NULL,3,1,NULL,'2025-06-29 23:20:42','2025-06-29 23:20:42',NULL,NULL,0),(13,21,NULL,2,'2025-06-28',NULL,'马医生',160,100,95,38.2,10.50,42.00,NULL,'严重高血压，糖尿病并发症',NULL,'胰岛素，多种降压药',NULL,3,1,NULL,'2025-06-29 23:20:42','2025-06-29 23:20:42',NULL,NULL,0),(14,22,NULL,2,'2025-06-28',NULL,'冯医生',165,105,100,38.5,11.20,38.50,NULL,'心衰，肾功能不全',NULL,'利尿剂，强心剂',NULL,3,1,NULL,'2025-06-29 23:20:42','2025-06-29 23:20:42',NULL,NULL,0),(15,8,NULL,1,'2025-06-28',NULL,'李主任',115,75,70,36.4,5.50,72.00,'无明显不适症状','身体状况良好，各项指标正常','继续保持良好生活习惯','维生素D，钙片','定期体检，健康老人标准',0,0,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(16,8,NULL,1,'2025-06-25',NULL,'李主任',118,78,72,36.5,5.40,71.80,'偶有轻微疲劳','血压血糖稳定，心率正常','适量运动，注意休息','维生素B族','健康状况稳定',0,0,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(17,9,NULL,1,'2025-06-28',NULL,'王医生',118,78,68,36.6,5.60,58.50,'精神状态良好','整体健康状况良好','保持规律作息','钙片，维生素C','健康老人，继续维护',0,0,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(18,9,NULL,1,'2025-06-25',NULL,'王医生',120,80,70,36.4,5.70,58.20,'睡眠质量良好','各项生理指标正常','户外活动适量','叶酸','健康维护良好',0,0,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(19,13,NULL,1,'2025-06-28',NULL,'张医生',130,82,76,36.8,6.50,52.30,'偶有头晕，轻微胸闷','血压轻度升高，需要关注','低盐饮食，控制体重','降压药（轻剂量）','亚健康状态，需要生活方式调整',1,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(20,13,NULL,1,'2025-06-25',NULL,'张医生',132,84,78,36.9,6.30,52.00,'头晕症状加重','血压波动，血糖轻微升高','增加运动量，调整饮食','降压药','需要密切监测血压变化',1,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(21,14,NULL,1,'2025-06-28',NULL,'赵医生',125,80,74,36.7,6.00,68.00,'易疲劳，食欲不振','血脂偏高，肝功能轻度异常','低脂饮食，戒酒','他汀类药物，护肝片','亚健康，需要肝功能保护',1,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(22,4,NULL,2,'2025-06-28',NULL,'陈主任',150,95,85,37.8,8.50,65.00,'头痛，视物模糊，乏力','高血压合并糖尿病','胰岛素治疗，血压监测','降压药，胰岛素','中度风险，需要药物控制',2,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(23,4,NULL,2,'2025-06-25',NULL,'陈主任',155,98,88,38.0,9.20,64.80,'多饮多尿，头晕','血糖控制不佳，血压偏高','加强血糖监测','调整胰岛素剂量','糖尿病并发症风险增加',2,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(24,3,NULL,1,'2025-06-28',NULL,'孙医生',140,90,68,37.1,7.80,48.80,'口渴，疲劳感明显','糖尿病前期，血压升高','控制饮食，增加运动','二甲双胍，降压药','中度风险，预防糖尿病进展',2,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(25,1,NULL,1,'2025-06-28',NULL,'李主任',120,80,72,36.5,5.80,70.50,'无特殊不适','身体状况稳定，定期随访','保持现状','维生素类','更新后健康状况良好',0,0,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0),(26,2,NULL,1,'2025-06-28',NULL,'周医生',135,85,75,36.7,6.20,55.20,'轻微头痛','血压偏高，需要监测','生活方式调整','降压药（小剂量）','亚健康状态监测',1,1,NULL,'2025-06-29 23:51:12','2025-06-29 23:51:12',NULL,NULL,0);
/*!40000 ALTER TABLE `health_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 17:16:49
