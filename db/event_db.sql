-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: event_db
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_date` date DEFAULT NULL,
  `quantity` int NOT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `tourism_package_id` bigint DEFAULT NULL,
  `payment_reference` varchar(255) DEFAULT NULL,
  `payment_status` enum('FAILED','PAID','PENDING') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKstqpl16k0httl7pdl18vm75bh` (`tourism_package_id`),
  CONSTRAINT `FKstqpl16k0httl7pdl18vm75bh` FOREIGN KEY (`tourism_package_id`) REFERENCES `tourism_package` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (16,'2025-09-12',5,'ertyh@gmail.com','Nita Dangol',6,NULL,NULL),(17,'2025-09-12',5,'erhfjkh@gmail.com','Pyaari Dangol',6,NULL,NULL),(19,'2025-09-14',2,'wrtt@gmail.com','kabya',6,'TESTREF123','PAID'),(20,'2025-09-14',4,'mittali@gmail.com','Mittali',7,'TESTREF123','PAID'),(21,'2025-09-14',4,'mittali@gmail.com','Mittali',7,NULL,'FAILED');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tourism_package`
--

DROP TABLE IF EXISTS `tourism_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tourism_package` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `available_quantity` int DEFAULT NULL,
  `total_quantity` int NOT NULL,
  `booking_deadline` date NOT NULL DEFAULT '2025-12-31',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tourism_package`
--

LOCK TABLES `tourism_package` WRITE;
/*!40000 ALTER TABLE `tourism_package` DISABLE KEYS */;
INSERT INTO `tourism_package` VALUES (5,' This is one of the cheapest package we ever have. This package includes accommodation only. We will head toward sagarmatha national park on 1st october.','Sagarmatha National Park',15000,0,20,'2025-09-10'),(6,' This is the most sold package we ever have. This package includes accommodation, guide and food only. We will head toward kedarnath on 15th october.','Kedarnath',15000,6,18,'2025-10-15'),(7,'This package involves all the expenses including transportation, accomodation, guide and food.','Tilicho lake Package',25000,12,20,'2025-10-14'),(8,'With this package we will provide you trekkings stuff','Annapurna Base Camp',50000,30,30,'2025-09-11'),(11,'With this package we will provide you trekkings stuff','Fewa Lake package',10,15,15,'2025-09-13');
/*!40000 ALTER TABLE `tourism_package` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-14 18:10:17
