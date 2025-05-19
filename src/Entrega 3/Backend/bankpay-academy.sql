CREATE DATABASE  IF NOT EXISTS `bankpay_academy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bankpay_academy`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: neonpayacademy.mysql.database.azure.com    Database: bankpay_academy
-- ------------------------------------------------------
-- Server version	8.0.40-azure

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
-- Table structure for table `asa_servicos`
--

DROP TABLE IF EXISTS `asa_servicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asa_servicos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asa_servicos`
--

LOCK TABLES `asa_servicos` WRITE;
/*!40000 ALTER TABLE `asa_servicos` DISABLE KEYS */;
INSERT INTO `asa_servicos` VALUES (1,'2º Via Carteirinha',28.00,'img_carteirinha'),(2,'Atestado Personalizado',5.00,'img_atestado'),(3,'Exame',60.00,'img_exame'),(4,'2º Via Histórico Escolar',18.00,'img_historico_escolar'),(5,'2º Via Diploma',240.00,'img_diploma'),(6,'Conteúdo Programático',10.00,'img_programatico');
/*!40000 ALTER TABLE `asa_servicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cantina`
--

DROP TABLE IF EXISTS `cantina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cantina` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cantina`
--

LOCK TABLES `cantina` WRITE;
/*!40000 ALTER TABLE `cantina` DISABLE KEYS */;
INSERT INTO `cantina` VALUES (1,'Croissant Salgado','Unidade',8.00,'img_croissant_salgado'),(2,'Croissant Chocolate','Unidade',11.00,'img_croissant_chocolate'),(3,'Coca Cola Zero','220ml',5.90,'img_coca_zero'),(4,'Coxinha','Unidade',8.00,'img_coxinha'),(5,'Café coado','100ml',5.90,'img_cafe'),(6,'Suco de Açai','500ml',13.00,'img_suco_acai');
/*!40000 ALTER TABLE `cantina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historico_pontos`
--

DROP TABLE IF EXISTS `historico_pontos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historico_pontos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `mes` date NOT NULL,
  `pontos_usados` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_pontos`
--

LOCK TABLES `historico_pontos` WRITE;
/*!40000 ALTER TABLE `historico_pontos` DISABLE KEYS */;
INSERT INTO `historico_pontos` VALUES (1,1,'2025-05-18',1),(2,1,'2025-05-18',3),(3,1,'2025-05-18',10),(4,1,'2025-05-18',1),(5,1,'2025-05-18',10),(6,1,'2025-05-18',3),(7,6,'2025-05-18',200),(8,1,'2025-05-18',48),(9,7,'2025-05-18',10),(10,7,'2025-05-18',12),(11,7,'2025-05-18',12),(12,1,'2025-05-18',200),(13,7,'2025-05-18',0),(14,7,'2025-05-18',0),(15,1,'2025-05-18',200000),(16,9,'2025-05-18',2),(17,9,'2025-05-18',2),(18,9,'2025-05-18',2),(19,9,'2025-05-18',2),(20,9,'2025-05-18',3),(21,9,'2025-05-18',48),(22,1,'2025-05-18',1),(23,1,'2025-05-18',1),(24,1,'2025-05-18',1);
/*!40000 ALTER TABLE `historico_pontos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `pontos` int NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'Bandana','Tamanho único',30,'img_bandana'),(2,'Shoulder Bag','Tamanho único',70,'img_shoulder'),(3,'Meia Cano Alto','Tamanho único',50,'img_meia'),(4,'Corta Vento','Tamanho único',180,'img_corta_vento'),(5,'Mala de Viagem','Tamanho único',200,'img_mala'),(6,'Moletom','Tamanho único',250,'img_moletom');
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transacoes`
--

DROP TABLE IF EXISTS `transacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transacoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int DEFAULT NULL,
  `tipo` enum('entrada','saida') DEFAULT NULL,
  `valor` decimal(10,2) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `chave_pix` varchar(100) DEFAULT NULL,
  `status` enum('pendente','confirmado') DEFAULT 'pendente',
  `data` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `senha_pedido` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transacoes`
--

LOCK TABLES `transacoes` WRITE;
/*!40000 ALTER TABLE `transacoes` DISABLE KEYS */;
INSERT INTO `transacoes` VALUES (1,1,'entrada',10.00,'Depósito via Pix','pix@neonpay.com','confirmado','2025-05-18 20:51:35',NULL),(2,1,'entrada',100.00,'Depósito via Pix','02a0e907-66e2-4328-8ac4-195fdd461994','confirmado','2025-05-18 20:57:41',NULL),(3,1,'saida',8.00,'Pagamento na Cantina: Coxinha','cantina@edu.fecap.br','confirmado','2025-05-18 21:08:18',1),(4,1,'entrada',200.00,'Depósito via Pix','627ef9ae-cf14-4398-b5f4-62ea0cafd9ac','confirmado','2025-05-18 21:38:59',NULL),(5,1,'saida',19.00,'Pagamento na Cantina: Croissant Salgado, Croissant Chocolate','cantina@edu.fecap.br','confirmado','2025-05-18 21:51:17',2),(6,1,'saida',51.80,'Pagamento na Cantina: Croissant Salgado, Croissant Chocolate, Coca Cola Zero, Coxinha, Café coado, Suco de Açai','cantina@edu.fecap.br','confirmado','2025-05-18 22:02:33',3),(7,1,'saida',5.00,'Pagamento de serviços ASA: Atestado Personalizado','asa@edu.fecap.br','confirmado','2025-05-18 22:03:10',NULL),(8,1,'saida',51.80,'Pagamento na Cantina: Croissant Salgado, Croissant Chocolate, Coca Cola Zero, Coxinha, Café coado, Suco de Açai','cantina@edu.fecap.br','confirmado','2025-05-18 22:05:30',4),(9,1,'saida',18.00,'Pagamento de serviços ASA: 2º Via Histórico Escolar','asa@edu.fecap.br','confirmado','2025-05-18 22:07:14',NULL),(10,6,'entrada',5000.00,'Depósito via Pix','e734d712-96bc-4e94-8eb1-25e6410bdb94','confirmado','2025-05-18 22:17:49',NULL),(11,6,'saida',1000.00,'Envio Pix','47877929897','confirmado','2025-05-18 22:18:22',NULL),(12,1,'entrada',1000.00,'Recebimento Pix','11970382953','confirmado','2025-05-18 22:18:22',NULL),(13,1,'saida',240.00,'Pagamento de serviços ASA: 2º Via Diploma','asa@edu.fecap.br','confirmado','2025-05-18 22:19:31',NULL),(14,1,'saida',0.00,'Troca por Pontos: Bandana','atleticafecap@edu.fecap.br','confirmado','2025-05-18 22:19:49',5),(15,7,'entrada',1111111.11,'Depósito via Pix','4081ac37-62e7-4ad0-9784-e92fee432065','confirmado','2025-05-18 22:23:25',NULL),(16,7,'saida',51.80,'Pagamento na Cantina: Croissant Salgado, Croissant Chocolate, Coca Cola Zero, Coxinha, Café coado, Suco de Açai','cantina@edu.fecap.br','confirmado','2025-05-18 22:24:54',6),(17,7,'saida',60.00,'Pagamento de serviços ASA: Exame','asa@edu.fecap.br','confirmado','2025-05-18 22:25:51',NULL),(18,7,'saida',60.00,'Pagamento de serviços ASA: Exame','asa@edu.fecap.br','confirmado','2025-05-18 22:25:53',NULL),(19,7,'saida',0.00,'Troca por Pontos: Bandana','atleticafecap@edu.fecap.br','confirmado','2025-05-18 22:26:16',7),(20,1,'entrada',1000.00,'Depósito via Pix','2eddfde3-039b-45e3-b3ea-4fc0c6bbb3aa','confirmado','2025-05-18 22:29:48',NULL),(21,1,'entrada',10000.00,'Depósito via Pix','87950ea3-cd61-41e4-86ad-733183c77a8d','confirmado','2025-05-18 22:30:28',NULL),(22,1,'saida',1000.00,'Envio Pix','11949326461','confirmado','2025-05-18 22:31:03',NULL),(23,7,'entrada',1000.00,'Recebimento Pix','47877929897','confirmado','2025-05-18 22:31:03',NULL),(24,1,'entrada',10000000.00,'Depósito via Pix','d07ede46-e246-4bf1-9af2-a0a4c08bcfd9','confirmado','2025-05-18 22:34:22',NULL),(25,7,'saida',0.08,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 22:38:55',NULL),(26,1,'entrada',0.08,'Recebimento Pix','11949326461','confirmado','2025-05-18 22:38:55',NULL),(27,7,'saida',0.08,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 22:38:56',NULL),(28,1,'entrada',0.08,'Recebimento Pix','11949326461','confirmado','2025-05-18 22:38:57',NULL),(29,1,'entrada',10000000.00,'Depósito via Pix','19204824-3a24-424c-99e6-4bb25c92a7f3','confirmado','2025-05-18 23:13:21',NULL),(30,1,'entrada',99999999.99,'Depósito via Pix','9a94cd6e-4e5b-48ac-8683-63de9916bfd5','pendente','2025-05-18 23:14:10',NULL),(31,1,'entrada',99999999.99,'Depósito via Pix','164c3f46-ee92-48b2-8268-9e66d8bbad72','confirmado','2025-05-18 23:20:05',NULL),(32,1,'saida',1000000.00,'Envio Pix','Jose.bento@gmail.com','confirmado','2025-05-18 23:23:38',NULL),(33,9,'entrada',1000000.00,'Recebimento Pix','hebert@gmail.com','confirmado','2025-05-18 23:23:38',NULL),(34,1,'entrada',99999.99,'Depósito via Pix','1f5186b1-aa40-4295-b833-9ae1957387ce','confirmado','2025-05-18 23:24:13',NULL),(35,1,'entrada',99999999.99,'Depósito via Pix','700e8b76-2e99-4198-acb1-4facb583f532','pendente','2025-05-18 23:24:47',NULL),(36,1,'entrada',99999999.99,'Depósito via Pix','6e29e5c9-15fe-4537-afbf-0163254b5dd7','confirmado','2025-05-18 23:24:47',NULL),(37,9,'entrada',1000.00,'Depósito via Pix','7ddd2ee0-8fed-4236-855f-292e70b8050e','confirmado','2025-05-18 23:25:03',NULL),(38,9,'saida',10.00,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 23:28:11',NULL),(39,1,'entrada',10.00,'Recebimento Pix','jose.bento@gmail.com','confirmado','2025-05-18 23:28:12',NULL),(40,9,'saida',10.00,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 23:28:13',NULL),(41,9,'saida',10.00,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 23:28:13',NULL),(42,1,'entrada',10.00,'Recebimento Pix','jose.bento@gmail.com','confirmado','2025-05-18 23:28:13',NULL),(43,1,'entrada',10.00,'Recebimento Pix','jose.bento@gmail.com','confirmado','2025-05-18 23:28:13',NULL),(44,9,'saida',10.00,'Envio Pix','hebert@gmail.com','confirmado','2025-05-18 23:28:15',NULL),(45,1,'entrada',10.00,'Recebimento Pix','jose.bento@gmail.com','confirmado','2025-05-18 23:28:15',NULL),(46,1,'entrada',90000000.00,'Depósito via Pix','64b434d1-2139-447b-b6c8-2e66209185f5','confirmado','2025-05-18 23:28:27',NULL),(47,1,'entrada',90000000.00,'Depósito via Pix','1665c994-f9b3-4cd0-904b-d5e68ff0ce2d','confirmado','2025-05-18 23:29:05',NULL),(48,9,'saida',19.00,'Pagamento na Cantina: Croissant Salgado, Croissant Chocolate','cantina@edu.fecap.br','confirmado','2025-05-18 23:29:27',8),(49,1,'entrada',50000000.00,'Depósito via Pix','1c63c410-9638-4228-89ca-6c635bd31f24','confirmado','2025-05-18 23:30:12',NULL),(50,9,'saida',240.00,'Pagamento de serviços ASA: 2º Via Diploma','asa@edu.fecap.br','confirmado','2025-05-18 23:30:26',NULL),(51,9,'saida',0.00,'Troca por Pontos: Meia Cano Alto','atleticafecap@edu.fecap.br','confirmado','2025-05-18 23:31:18',9),(52,1,'entrada',50000000.00,'Depósito via Pix','d57ac42b-0d2a-4bc2-b818-a831e61fd0c1','pendente','2025-05-18 23:31:42',NULL),(53,1,'entrada',50000000.00,'Depósito via Pix','126cc58b-bbfb-4f44-8c60-bccfb2c4b3e0','confirmado','2025-05-18 23:31:51',NULL),(54,1,'entrada',49000000.00,'Depósito via Pix','59e4452f-a3ff-468a-a6e1-40acbc0f83bc','confirmado','2025-05-18 23:33:01',NULL),(55,1,'entrada',0.01,'Depósito via Pix','370a4636-5243-4a74-90f5-8011d32c8c5c','pendente','2025-05-18 23:35:50',NULL),(56,1,'entrada',0.01,'Depósito via Pix','f4cbb42e-3432-4b19-be4b-4fecc56fa4f7','confirmado','2025-05-18 23:35:56',NULL),(57,1,'saida',0.00,'Troca por Pontos: Shoulder Bag','atleticafecap@edu.fecap.br','confirmado','2025-05-18 23:36:29',10),(58,1,'saida',8.00,'Pagamento na Cantina: Coxinha','cantina@edu.fecap.br','confirmado','2025-05-18 23:37:08',11),(59,1,'saida',8.00,'Pagamento na Cantina: Coxinha','cantina@edu.fecap.br','confirmado','2025-05-18 23:37:10',12),(60,1,'saida',8.00,'Pagamento na Cantina: Coxinha','cantina@edu.fecap.br','confirmado','2025-05-18 23:37:11',13),(61,1,'saida',0.00,'Troca por Pontos: Moletom','atleticafecap@edu.fecap.br','confirmado','2025-05-19 01:54:35',14);
/*!40000 ALTER TABLE `transacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `data_nasc` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `senha` varchar(255) NOT NULL,
  `saldo` decimal(10,2) DEFAULT '0.00',
  `pontos` int DEFAULT '0',
  `chave_pix` varchar(255) DEFAULT NULL,
  `tipo_chave_pix` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `chave_pix` (`chave_pix`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Hebertin dos Reis Esteves','47877929897','2006-01-08','hebert@email.com','11987654321','$2b$10$HihIZ7M4p5xx4iAlq6QSyOQSErr61FVM82eQtqELGQwZxwlX.oc8y',69110932.56,199929,'hebert@gmail.com','email'),(6,'Hubert','12517628831','1998-02-26','hubert@edu.fecap.br','11948812392','$2b$10$tyyt3yeeordiRDhgDxEtb.qCdhjKp1HlNHjB2zPVBuV0o9DN1Cvs2',4000.00,200,'11970382953','celular'),(7,'Alexandra Christine','50219876827','2005-08-27','Alexandra.raimundo@edu.fecap.br','11949326461','$2b$10$ATSjWBYUb7nIrnUiPhSP6.jtXIHpfFWKVieiMK0RlHvlVM1gt78E.',1111939.15,4,'11949326461','celular'),(9,'José Bento','03703251840','2005-08-27','jose.bento@edu.fecap.br','11949186099','$2b$10$TJZ9Zteruq4lC4wf1QKUK.B6yc5QCvayrvWhPOr1qtLa2MV2gaWbC',1000701.00,9,'jose.bento@gmail.com','email');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 23:05:09
