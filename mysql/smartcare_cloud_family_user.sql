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
-- Table structure for table `family_user`
--

DROP TABLE IF EXISTS `family_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `family_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint NOT NULL COMMENT '绯荤粺鐢ㄦ埛ID锛堝叧鑱攕ys_user琛?級',
  `real_name` varchar(50) NOT NULL COMMENT '鐪熷疄濮撳悕',
  `gender` tinyint(1) DEFAULT '1' COMMENT '鎬у埆锛?-鐢凤紝2-濂',
  `phone` varchar(20) DEFAULT NULL COMMENT '鎵嬫満鍙风爜',
  `email` varchar(100) DEFAULT NULL COMMENT '閭??鍦板潃',
  `status` tinyint(1) DEFAULT '1' COMMENT '鐘舵?锛?-姝ｅ父锛?-绂佺敤',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '鏄?惁鍒犻櫎锛?-鏈?垹闄わ紝1-宸插垹闄',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  `create_by` bigint DEFAULT NULL COMMENT '鍒涘缓浜篒D',
  `update_by` bigint DEFAULT NULL COMMENT '鏇存柊浜篒D',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_id` (`sys_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_phone` (`phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='瀹跺睘鐢ㄦ埛琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `family_user`
--

LOCK TABLES `family_user` WRITE;
/*!40000 ALTER TABLE `family_user` DISABLE KEYS */;
INSERT INTO `family_user` VALUES (1,3,'鏉庡?灞',1,'13800138002','family@smartcare.com',1,0,'2025-07-10 01:50:14','2025-07-10 01:50:14',1,NULL);
/*!40000 ALTER TABLE `family_user` ENABLE KEYS */;
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
