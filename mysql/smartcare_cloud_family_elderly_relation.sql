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
-- Table structure for table `family_elderly_relation`
--

DROP TABLE IF EXISTS `family_elderly_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `family_elderly_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `family_user_id` bigint NOT NULL COMMENT '瀹跺睘鐢ㄦ埛ID',
  `elderly_id` bigint NOT NULL COMMENT '鑰佷汉ID',
  `relationship_type` varchar(20) NOT NULL COMMENT '鍏崇郴绫诲瀷锛歝hild-瀛愬コ,spouse-閰嶅伓,parent-鐖舵瘝,sibling-鍏勫紵濮愬?,grandchild-瀛欒緢,other-鍏朵粬',
  `relationship_name` varchar(20) NOT NULL COMMENT '鍏崇郴鍚嶇О锛氬効瀛?濂冲効,涓堝か,濡诲瓙绛',
  `is_primary` tinyint(1) DEFAULT '0' COMMENT '鏄?惁涓昏?鑱旂郴浜猴細1-鏄?紝0-鍚',
  `is_emergency` tinyint(1) DEFAULT '0' COMMENT '鏄?惁绱ф?鑱旂郴浜猴細1-鏄?紝0-鍚',
  `contact_priority` int DEFAULT '5' COMMENT '鑱旂郴浼樺厛绾э細1-鏈?珮锛?-鏈?綆',
  `visit_frequency` varchar(20) DEFAULT NULL COMMENT '鎺㈣?棰戠巼锛歞aily-姣忔棩,weekly-姣忓懆,monthly-姣忔湀,rarely-寰堝皯',
  `authorized_operations` json DEFAULT NULL COMMENT '鎺堟潈鎿嶄綔鍒楄〃',
  `start_date` date DEFAULT NULL COMMENT '鍏崇郴寮??鏃ユ湡',
  `end_date` date DEFAULT NULL COMMENT '鍏崇郴缁撴潫鏃ユ湡',
  `status` tinyint(1) DEFAULT '1' COMMENT '鐘舵?锛?-鏈夋晥锛?-鏃犳晥',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '鏄?惁鍒犻櫎锛?-鏈?垹闄わ紝1-宸插垹闄',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  `create_by` bigint DEFAULT NULL COMMENT '鍒涘缓浜篒D',
  `update_by` bigint DEFAULT NULL COMMENT '鏇存柊浜篒D',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_elderly` (`family_user_id`,`elderly_id`),
  KEY `idx_family_user` (`family_user_id`),
  KEY `idx_elderly` (`elderly_id`),
  KEY `idx_relationship` (`relationship_type`),
  KEY `idx_status` (`status`),
  KEY `idx_is_primary` (`is_primary`),
  KEY `idx_is_emergency` (`is_emergency`),
  KEY `idx_contact_priority` (`contact_priority`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='瀹跺睘鑰佷汉鍏崇郴琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `family_elderly_relation`
--

LOCK TABLES `family_elderly_relation` WRITE;
/*!40000 ALTER TABLE `family_elderly_relation` DISABLE KEYS */;
INSERT INTO `family_elderly_relation` VALUES (1,1,1,'child','鍎垮瓙',1,1,1,NULL,NULL,'2025-07-10',NULL,1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL),(2,1,2,'child','濂冲効',0,1,2,NULL,NULL,'2025-07-10',NULL,1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL),(3,1,3,'child','鍎垮瓙',0,0,3,NULL,NULL,'2025-07-10',NULL,1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL),(4,1,4,'grandchild','瀛欏瓙',0,0,4,NULL,NULL,'2025-07-10',NULL,1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL),(5,1,5,'grandchild','瀛欏コ',0,0,5,NULL,NULL,'2025-07-10',NULL,1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL);
/*!40000 ALTER TABLE `family_elderly_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 17:16:47
