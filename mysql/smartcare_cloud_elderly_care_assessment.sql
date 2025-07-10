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
-- Table structure for table `elderly_care_assessment`
--

DROP TABLE IF EXISTS `elderly_care_assessment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elderly_care_assessment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `elderly_id` bigint NOT NULL,
  `assessment_date` date NOT NULL,
  `assessment_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评估类型:ADL,IADL,认知,营养',
  `total_score` int NOT NULL,
  `risk_level` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '风险等级:LOW,MEDIUM,HIGH,CRITICAL',
  `assessment_result` text COLLATE utf8mb4_unicode_ci,
  `suggestions` text COLLATE utf8mb4_unicode_ci,
  `assessor_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elderly_care_assessment`
--

LOCK TABLES `elderly_care_assessment` WRITE;
/*!40000 ALTER TABLE `elderly_care_assessment` DISABLE KEYS */;
INSERT INTO `elderly_care_assessment` VALUES (1,8,'2025-06-28','ADL',95,'LOW','日常生活活动能力良好，能够独立完成各项基本生活活动，认知功能正常，营养状况良好','继续保持良好的生活习惯，定期体检，适量运动，保持社交活动','康复评估师-李评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(2,9,'2025-06-28','ADL',92,'LOW','生活自理能力强，身体机能稳定，精神状态良好，社会适应能力佳','维持当前生活方式，注意预防跌倒，定期健康检查','康复评估师-王评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(3,10,'2025-06-28','IADL',88,'LOW','工具性日常生活活动能力良好，能够处理复杂的日常事务，认知功能无明显异常','鼓励参与社区活动，保持认知训练，注意营养均衡','康复评估师-张评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(4,13,'2025-06-28','ADL',75,'MEDIUM','日常生活活动部分需要协助，血压控制需要关注，存在轻度功能障碍','加强血压监测，调整生活方式，适度运动，定期医疗随访','康复评估师-赵评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(5,14,'2025-06-28','ADL',78,'MEDIUM','生活自理能力有所下降，肝功能异常需要医疗干预，营养状况需要改善','肝功能保护治疗，营养指导，戒酒，定期复查肝功能','康复评估师-孙评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(6,2,'2025-06-28','认知',72,'MEDIUM','轻度认知功能下降，血压偏高，需要生活方式调整和药物治疗','认知训练，血压管理，社会支持，家属配合护理','康复评估师-周评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(7,4,'2025-06-28','ADL',45,'HIGH','日常生活活动严重受限，高血压和糖尿病并发症风险高，需要持续医疗监护','24小时医疗监护，严格血糖血压控制，预防并发症，家属密切配合','康复评估师-陈评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(8,3,'2025-06-28','营养',58,'HIGH','营养状况不良，糖尿病前期进展风险高，体重偏低，需要营养干预','营养师指导，血糖监测，体重管理，预防糖尿病进展','康复评估师-马评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0),(9,1,'2025-06-28','IADL',85,'LOW','经过治疗后状况改善，工具性日常生活活动能力恢复良好','继续康复训练，定期随访，保持治疗效果','康复评估师-冯评估','2025-06-29 23:51:17','2025-06-29 23:51:17',0);
/*!40000 ALTER TABLE `elderly_care_assessment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 17:16:50
