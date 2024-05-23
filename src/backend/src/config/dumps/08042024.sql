-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: testgetaulasmobile
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `type` enum('Online','Presencial') NOT NULL,
  `category` enum('Informática','Esportes','Música','Diversos') NOT NULL,
  `img` varchar(255) NOT NULL,
  `description` longtext,
  `address` varchar(255) DEFAULT NULL,
  `zone` enum('Norte','Sul','Leste','Oeste','Aula Online') NOT NULL,
  `occupiedSlots` int NOT NULL,
  `maxCapacity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,'Word Básico','Online','Informática','/images/informatica.png','Aprenda os fundamentos essenciais do processador de texto Microsoft Word, incluindo formatação básica, edição de documentos e utilização de ferramentas de formatação de texto.','Rua seila, São Paulo','Aula Online',0,10),(2,'Javascript Básico','Online','Informática','/images/informatica.png','Explore os princípios introdutórios da linguagem de programação JavaScript, cobrindo conceitos como variáveis, operadores, estruturas de controle e funções básicas.','Rua não sei, São Paulo','Aula Online',5,10),(3,'Basquete da ONG','Presencial','Esportes','/images/esporte.png','Participe de aulas de basquete oferecidas pela organização não governamental (ONG), com foco no desenvolvimento de habilidades fundamentais do esporte, como dribles, arremessos e trabalho em equipe.','Rua do esporte, São Paulo','Sul',1,10),(4,'Matemática Básica','Presencial','Diversos','/images/diversos.png','Este curso oferece uma introdução abrangente aos conceitos básicos da matemática, destinado a fortalecer habilidades essenciais em aritmética, álgebra, geometria e resolução de problemas.','Rua da matemática, 234','Sul',1,10),(5,'Aula de violão Básica','Online','Música','/images/musica.png','Inicie sua jornada musical com aulas básicas de violão, onde você aprenderá os fundamentos, incluindo acordes básicos, técnicas de dedilhado e leitura de tablaturas simples.','Google meets','Aula Online',6,10),(6,'Redação','Online','Diversos','/images/diversos.png','Aula de radação das brabas','Google meets','Aula Online',2,10),(7,'Aula de Baixo','Online','Música','/images/musica.png','Inicie sua jornada musical com aulas básicas de baixo, onde você aprenderá os fundamentos, incluindo acordes básicos, leitura de tablaturas simples.','Google meets','Aula Online',6,10),(8,'Aula de Saxofone','Online','Música','/images/musica.png','Inicie sua jornada musical com aulas básicas de saxofone, onde você aprenderá os fundamentos básicos.','Google meets','Aula Online',6,10),(9,'Piano Básico','Presencial','Música','/images/musica.png','Participe de aulas de piano oferecidas pela organização não governamental (ONG), com foco no desenvolvimento de habilidades fundamentais','Rua da música, São Paulo','Leste',0,10),(10,'Tênis testeando o tamanho que  o title pode ter','Presencial','Esportes','/images/esporte.png','Participe de aulas de tênis oferecidas pela organização não governamental (ONG), com foco no desenvolvimento de habilidades fundamentais do esporte','Rua do esporte, São Paulo','Oeste',1,10),(11,'Macramê','Presencial','Diversos','/images/diversos.png','O macramê é uma forma de artesanato bem antiga. Nela, fios são trançados e atados por nós e, para isso, não é utilizada nenhuma ferramenta ou máquina, apenas as mãos e os fios.','Rua do esporte, São Paulo','Norte',1,10);
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-08  3:21:23
