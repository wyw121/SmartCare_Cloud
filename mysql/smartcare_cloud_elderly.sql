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
-- Table structure for table `elderly`
--

DROP TABLE IF EXISTS `elderly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elderly` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '濮撳悕',
  `gender` tinyint(1) NOT NULL COMMENT '鎬у埆 (1-鐢? 0-濂?',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '韬?唤璇佸彿',
  `birth_date` date DEFAULT NULL COMMENT '鍑虹敓鏃ユ湡',
  `age` int DEFAULT NULL COMMENT '骞撮緞',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鑱旂郴鐢佃瘽',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '瀹跺涵鍦板潃',
  `emergency_contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绱ф?鑱旂郴浜哄?鍚',
  `emergency_contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绱ф?鑱旂郴浜虹數璇',
  `health_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'HEALTHY' COMMENT '鍋ュ悍鐘跺喌 (HEALTHY-鍋ュ悍, SUBHEALTH-浜氬仴搴? SICK-鐤剧梾, SERIOUS-閲嶇梾, DANGER-鍗遍櫓, WARNING-棰勮?)',
  `care_level` tinyint(1) NOT NULL DEFAULT '1' COMMENT '鐓ф姢绛夌骇 (1-鑷?悊, 2-鍗婅嚜鐞? 3-涓嶈兘鑷?悊)',
  `insurance_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍖讳繚绫诲瀷',
  `medical_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '鏃㈠線鐥呭彶',
  `allergy_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '杩囨晱鍙',
  `family_doctor_id` bigint DEFAULT NULL COMMENT '瀹跺涵鍖荤敓ID',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '妗ｆ?鐘舵? (1-姝ｅ父, 0-鍋滅敤)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '澶囨敞',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍒涘缓鑰',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鏇存柊鑰',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '鏄?惁鍒犻櫎 (1-宸插垹闄? 0-鏈?垹闄?',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id_card` (`id_card`),
  KEY `idx_name` (`name`),
  KEY `idx_phone` (`phone`),
  KEY `idx_health_status` (`health_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鑰佷汉妗ｆ?琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elderly`
--

LOCK TABLES `elderly` WRITE;
/*!40000 ALTER TABLE `elderly` DISABLE KEYS */;
INSERT INTO `elderly` VALUES (1,'Zhang Ming',1,'110101195001011234','1950-01-01',74,'13800138001','Beijing Chaoyang District','Zhang XiaoMing','13900139001','HEALTHY',1,'Employee Insurance','Hypertension for 5 years',NULL,NULL,1,NULL,'2025-06-26 08:23:28','2025-06-26 08:23:28','system',NULL,0),(2,'Li Hua',0,'110101194512152345','1945-12-15',79,'13800138002','Beijing Haidian District','Li XiaoHua','13900139002','SUBHEALTH',2,'Resident Insurance','Diabetes, Heart Disease',NULL,NULL,1,NULL,'2025-06-26 08:23:28','2025-06-26 08:23:28','system',NULL,0),(3,'Wang Fang',0,'110101195203203456','1952-03-20',72,'13800138003','Beijing Xicheng District','Wang XiaoFang','13900139003','WARNING',1,'Employee Insurance','Osteoporosis',NULL,NULL,1,NULL,'2025-06-26 08:23:28','2025-06-26 08:23:28','system',NULL,0),(4,'Liu Qiang',1,'110101194810104567','1948-10-10',76,'13800138004','Beijing Dongcheng District','Liu XiaoQiang','13900139004','DANGER',3,'Resident Insurance','Stroke, Hypertension, Diabetes',NULL,NULL,1,NULL,'2025-06-26 08:23:28','2025-06-26 08:23:28','system',NULL,0),(5,'Chen Jing',0,'110101195506066789','1955-06-06',69,'13800138005','Beijing Fengtai District','Chen XiaoJing','13900139005','SICK',2,'Employee Insurance','Asthma',NULL,NULL,1,NULL,'2025-06-26 08:23:28','2025-06-26 08:23:28','system',NULL,0),(6,'Test User 1',0,'110101199001011111',NULL,NULL,'13800138001','Test Address 1',NULL,NULL,'NORMAL',1,NULL,NULL,NULL,NULL,1,NULL,'2025-06-26 08:26:46','2025-06-26 08:26:46',NULL,NULL,0),(7,'Test User 2',0,'110101199001012222',NULL,NULL,'13800138002','Test Address 2',NULL,NULL,'NORMAL',1,NULL,NULL,NULL,NULL,1,NULL,'2025-06-26 08:26:46','2025-06-26 08:26:46',NULL,NULL,0);
/*!40000 ALTER TABLE `elderly` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-27  9:40:55
