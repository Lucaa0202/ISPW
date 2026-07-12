-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ispw
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `remaining` int DEFAULT NULL,
  `duration` varchar(45) DEFAULT NULL,
  `level` varchar(45) DEFAULT NULL,
  `trainer` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pt_idx` (`trainer`),
  CONSTRAINT `pt` FOREIGN KEY (`trainer`) REFERENCES `trainer` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'pilates',30,'60 min','basso','mattia.verdi@gmail.com'),(2,'yoga',29,'60 min','medio','mattia.verdi@gmail.com'),(3,'full body',29,'50 min','alto','mattia.verdi@gmail.com');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `mail` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `online` tinyint DEFAULT NULL,
  `subscription` int DEFAULT NULL,
  `injury` varchar(45) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  PRIMARY KEY (`mail`),
  KEY `subscription_idx` (`subscription`,`mail`),
  CONSTRAINT `customermail` FOREIGN KEY (`mail`) REFERENCES `users` (`email`),
  CONSTRAINT `subscription` FOREIGN KEY (`subscription`) REFERENCES `subscription` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('luca.belforte@gmail.com','Luca','Belforte','Maschio',1,NULL,'','2025-07-07',NULL,'2002-04-01'),('mario.rossi@gmail.com','Mario','Rossi','Maschio',1,NULL,'','2025-07-03',NULL,'2025-02-04');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercise`
--

DROP TABLE IF EXISTS `exercise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise` (
  `id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `numberSeries` int DEFAULT NULL,
  `numberReps` int DEFAULT NULL,
  `restTime` enum('30','60','90','120') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise`
--

LOCK TABLES `exercise` WRITE;
/*!40000 ALTER TABLE `exercise` DISABLE KEYS */;
INSERT INTO `exercise` VALUES (10,'Push ups','Basic push up',4,10,'60'),(20,'Squat','Basic squat',4,10,'60'),(30,'Cruch','Basic crunch',3,10,'60'),(40,'Lat machine','Presa larga',4,10,'60');
/*!40000 ALTER TABLE `exercise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expertieses`
--

DROP TABLE IF EXISTS `expertieses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expertieses` (
  `specialization` int NOT NULL,
  `trainer` varchar(45) NOT NULL,
  PRIMARY KEY (`specialization`,`trainer`),
  KEY `trainer_idx` (`trainer`),
  CONSTRAINT `specialization` FOREIGN KEY (`specialization`) REFERENCES `specialization` (`id`),
  CONSTRAINT `train` FOREIGN KEY (`trainer`) REFERENCES `trainer` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expertieses`
--

LOCK TABLES `expertieses` WRITE;
/*!40000 ALTER TABLE `expertieses` DISABLE KEYS */;
INSERT INTO `expertieses` VALUES (2,'mattia.verdi@gmail.com'),(3,'mattia.verdi@gmail.com');
/*!40000 ALTER TABLE `expertieses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participation`
--

DROP TABLE IF EXISTS `participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participation` (
  `schedule` int NOT NULL,
  `exercise` int NOT NULL,
  PRIMARY KEY (`schedule`,`exercise`),
  KEY `exercise_idx` (`exercise`),
  CONSTRAINT `exercise` FOREIGN KEY (`exercise`) REFERENCES `exercise` (`id`),
  CONSTRAINT `scheda` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participation`
--

LOCK TABLES `participation` WRITE;
/*!40000 ALTER TABLE `participation` DISABLE KEYS */;
INSERT INTO `participation` VALUES (1,10),(2,10),(1,20),(2,20),(3,20),(1,30),(2,30),(3,30),(1,40),(2,40);
/*!40000 ALTER TABLE `participation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pending`
--

DROP TABLE IF EXISTS `pending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pending` (
  `customer` varchar(45) NOT NULL,
  `course` int NOT NULL,
  `date` varchar(45) NOT NULL,
  `hour` varchar(45) NOT NULL,
  PRIMARY KEY (`customer`,`course`,`hour`,`date`),
  KEY `coursep_idx` (`course`),
  CONSTRAINT `coursep` FOREIGN KEY (`course`) REFERENCES `course` (`id`),
  CONSTRAINT `customerp` FOREIGN KEY (`customer`) REFERENCES `customer` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pending`
--

LOCK TABLES `pending` WRITE;
/*!40000 ALTER TABLE `pending` DISABLE KEYS */;
INSERT INTO `pending` VALUES ('mario.rossi@gmail.com',1,'giovedì','14'),('mario.rossi@gmail.com',2,'venerdì','16');
/*!40000 ALTER TABLE `pending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `schedule` int DEFAULT NULL,
  `exercise` int DEFAULT NULL,
  `reason` varchar(45) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `richiestaesercizio_idx` (`exercise`),
  KEY `richiestascheda_idx` (`schedule`),
  CONSTRAINT `richiestaesercizio` FOREIGN KEY (`exercise`) REFERENCES `exercise` (`id`),
  CONSTRAINT `richiestascheda` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `customer` varchar(45) NOT NULL,
  `course` int NOT NULL,
  `date` varchar(45) NOT NULL,
  `hour` varchar(45) NOT NULL,
  PRIMARY KEY (`customer`,`course`,`date`,`hour`),
  KEY `corso_idx` (`course`),
  CONSTRAINT `cliente` FOREIGN KEY (`customer`) REFERENCES `customer` (`mail`),
  CONSTRAINT `corso` FOREIGN KEY (`course`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES ('mario.rossi@gmail.com',2,'mercoledì','16'),('mario.rossi@gmail.com',3,'lunedì','18'),('mario.rossi@gmail.com',3,'venerdì','18');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `customer` varchar(45) DEFAULT NULL,
  `trainer` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_idx` (`customer`),
  KEY `trainer_idx` (`trainer`),
  CONSTRAINT `customer` FOREIGN KEY (`customer`) REFERENCES `customer` (`mail`),
  CONSTRAINT `trainer` FOREIGN KEY (`trainer`) REFERENCES `trainer` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,'full body','mario.rossi@gmail.com','mattia.verdi@gmail.com'),(2,'upper','mario.rossi@gmail.com','mattia.verdi@gmail.com'),(3,'gambe','mario.rossi@gmail.com','mattia.verdi@gmail.com');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session` (
  `day` varchar(45) NOT NULL,
  `hour` varchar(45) NOT NULL,
  `course` int DEFAULT NULL,
  PRIMARY KEY (`day`,`hour`),
  KEY `session_idx` (`course`),
  CONSTRAINT `session` FOREIGN KEY (`course`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` VALUES ('giovedì','14',1),('martedì','14',1),('mercoledì','16',2),('venerdì','16',2),('lunedì','18',3),('mercoledì','18',3),('venerdì','18',3);
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialization`
--

DROP TABLE IF EXISTS `specialization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialization` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialization`
--

LOCK TABLES `specialization` WRITE;
/*!40000 ALTER TABLE `specialization` DISABLE KEYS */;
INSERT INTO `specialization` VALUES (1,'powerlifting'),(2,'posturale'),(3,'fisioterapia'),(4,'bodybuilding');
/*!40000 ALTER TABLE `specialization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `type` enum('ANNUALE','MENSILE','TRIMESTRALE','QUADRIMESTRALE') DEFAULT NULL,
  `price` float DEFAULT NULL,
  `discount` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer`
--

DROP TABLE IF EXISTS `trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer` (
  `email` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `online` tinyint DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `mail` FOREIGN KEY (`email`) REFERENCES `users` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer`
--

LOCK TABLES `trainer` WRITE;
/*!40000 ALTER TABLE `trainer` DISABLE KEYS */;
INSERT INTO `trainer` VALUES ('mattia.verdi@gmail.com','mattia','verdi','Maschio',NULL,'2000-07-24');
/*!40000 ALTER TABLE `trainer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` enum('CLIENT','TRAINER') NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('luca.belforte@gmail.com','luca','CLIENT'),('mario.rossi@gmail.com','mariorossi','CLIENT'),('mattia.verdi@gmail.com','mattiaverdi','TRAINER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-09  9:47:38
