-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: tmdt3
-- ------------------------------------------------------
-- Server version	8.0.42

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

create DATABASE TMDT3;
USE TMDT3;
--
-- Table structure for table `cartitem`
--

DROP TABLE IF EXISTS `cartitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cartitem` (
                            `idCartItem` int NOT NULL AUTO_INCREMENT,
                            `idUser` varchar(36) NOT NULL,
                            `idProduct` varchar(36) NOT NULL,
                            `quantity` int NOT NULL,
                            `addedAt` datetime DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`idCartItem`),
                            KEY `idUser` (`idUser`),
                            KEY `idProduct` (`idProduct`),
                            CONSTRAINT `cartitem_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE,
                            CONSTRAINT `cartitem_ibfk_2` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cartitem`
--

LOCK TABLES `cartitem` WRITE;
/*!40000 ALTER TABLE `cartitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `cartitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderitem` (
                             `idOrderItem` int NOT NULL AUTO_INCREMENT,
                             `idOrder` int NOT NULL,
                             `idProduct` varchar(36) NOT NULL,
                             `productName` varchar(255) NOT NULL,
                             `productImageLink` varchar(1000) DEFAULT NULL,
                             `quantity` int NOT NULL,
                             `totalPrice` int NOT NULL,
                             `idReview` varchar(36) ,
                             PRIMARY KEY (`idOrderItem`),
                             KEY `idOrder` (`idOrder`),
                             CONSTRAINT `fk_reviews_idReview` FOREIGN KEY (`idReview`) REFERENCES `reviews` (`idReview`),
                             CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orders` (`idOrder`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
                          `idOrder` int NOT NULL AUTO_INCREMENT,
                          `idUser` varchar(36) NOT NULL,
                          `totalAmount` int NOT NULL,
                          `shippingAddress` text NOT NULL,
                          `paymentMethod` varchar(50) DEFAULT NULL,
                          `currentStatus` varchar(50) DEFAULT 'PLACED',
                          `customerName` varchar(100) NOT NULL,
                          `phoneNumber` varchar(45) NOT NULL,
                          `cancelledBy` varchar(20) DEFAULT NULL,
                          `cancelReason` varchar(50) DEFAULT NULL,
                          PRIMARY KEY (`idOrder`),
                          KEY `idUser` (`idUser`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderstatus`
--

DROP TABLE IF EXISTS `orderstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderstatus` (
                               `idStatus` int NOT NULL AUTO_INCREMENT,
                               `idOrder` int NOT NULL,
                               `status` varchar(50) NOT NULL,
                               `statusTime` datetime DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`idStatus`),
                               KEY `idOrder` (`idOrder`),
                               CONSTRAINT `orderstatus_ibfk_1` FOREIGN KEY (`idOrder`) REFERENCES `orders` (`idOrder`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderstatus`
--

LOCK TABLES `orderstatus` WRITE;
/*!40000 ALTER TABLE `orderstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
                           `idProduct` varchar(36) NOT NULL DEFAULT 'uuid()',
                           `productName` varchar(255) NOT NULL,
                           `cogs` int NOT NULL,
                           `description` text,
                           `imageLink` varchar(1000) DEFAULT NULL,
                           `artist` varchar(45) DEFAULT NULL,
                           `category` varchar(45) DEFAULT NULL,
                           `createdAt` datetime DEFAULT NULL,
                           PRIMARY KEY (`idProduct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productdetails`
--

DROP TABLE IF EXISTS `productdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productdetails` (
                                  `idProduct` varchar(36) NOT NULL DEFAULT 'uuid()',
                                  `price` int NOT NULL,
                                  `view` int DEFAULT '0',
                                  `soldQuantity` int DEFAULT '0',
                                  `availableQuantity` int NOT NULL,
                                  `averageRating` decimal(2,1) DEFAULT '0.0',
                                  `ratingCount` int DEFAULT '0',
                                  PRIMARY KEY (`idProduct`),
                                  KEY `idProduct` (`idProduct`),
                                  CONSTRAINT `fk_productdetails` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productdetails`
--

LOCK TABLES `productdetails` WRITE;
/*!40000 ALTER TABLE `productdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `productdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
                           `idReview` varchar(36) NOT NULL DEFAULT 'uuid()',
                           `idProduct` varchar(36),
                           `idUser` varchar(36),
                           `rating` int NOT NULL,
                           `comment` text,
                           `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`idReview`),
                           KEY `fk_reviews_idProduct` (`idProduct`),
                           KEY `fk_reviews_idUser` (`idUser`),
                           CONSTRAINT `fk_reviews_idProduct` FOREIGN KEY (`idProduct`) REFERENCES `product` (`idProduct`),
                           CONSTRAINT `fk_reviews_idUser` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`),
                           CONSTRAINT `reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `idUser` varchar(36) NOT NULL DEFAULT 'uuid()',
                        `userName` varchar(255) NOT NULL,
                        `idProvided` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `phoneNumber` varchar(10) DEFAULT NULL,
                        `email` varchar(45) DEFAULT NULL,
                        `address` varchar(255) DEFAULT NULL,
                        `badCancelCount` int DEFAULT 0,
                        `purchaseRestricted` boolean default false,
                        `role` varchar(20) DEFAULT NULL,
                        PRIMARY KEY (`idUser`),
                        UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('292c4c3a-0bb3-42d5-9d0e-54b3b6cfafbf','hello',NULL,'$2a$10$.wf02ligF5Mi4LmK/vyKkuOcpwpIzGvhaOTC6mkM.Ej/NCy0UsQvu',NULL,'hello@gmail.com',NULL,0,false,'CUSTOMER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-31 21:23:32
