-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Tempo de geração: 19/05/2025 às 00:21
-- Versão do servidor: 9.1.0
-- Versão do PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `bankpay_academy`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `asa_servicos`
--

DROP TABLE IF EXISTS `asa_servicos`;
CREATE TABLE IF NOT EXISTS `asa_servicos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `asa_servicos`
--

INSERT INTO `asa_servicos` (`id`, `titulo`, `preco`, `imagem`) VALUES
(1, '2º Via Carteirinha', 28.00, 'img_carteirinha'),
(2, 'Atestado Personalizado', 5.00, 'img_atestado'),
(3, 'Exame', 60.00, 'img_exame'),
(4, '2º Via Histórico Escolar', 18.00, 'img_historico_escolar'),
(5, '2º Via Diploma', 240.00, 'img_diploma'),
(6, 'Conteúdo Programático', 10.00, 'img_programatico');

-- --------------------------------------------------------

--
-- Estrutura para tabela `cantina`
--

DROP TABLE IF EXISTS `cantina`;
CREATE TABLE IF NOT EXISTS `cantina` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `cantina`
--

INSERT INTO `cantina` (`id`, `titulo`, `descricao`, `preco`, `imagem`) VALUES
(1, 'Croissant Salgado', 'Unidade', 8.00, 'img_croissant_salgado'),
(2, 'Croissant Chocolate', 'Unidade', 11.00, 'img_croissant_chocolate'),
(3, 'Coca Cola Zero', '220ml', 5.90, 'img_coca_zero'),
(4, 'Coxinha', 'Unidade', 8.00, 'img_coxinha'),
(5, 'Café coado', '100ml', 5.90, 'img_cafe'),
(6, 'Suco de Açai', '500ml', 13.00, 'img_suco_acai');

-- --------------------------------------------------------

--
-- Estrutura para tabela `historico_pontos`
--

DROP TABLE IF EXISTS `historico_pontos`;
CREATE TABLE IF NOT EXISTS `historico_pontos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `mes` date NOT NULL,
  `pontos_usados` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `historico_trocas`
--

DROP TABLE IF EXISTS `historico_trocas`;
CREATE TABLE IF NOT EXISTS `historico_trocas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `data` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `produto_id` (`produto_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `produtos`
--

DROP TABLE IF EXISTS `produtos`;
CREATE TABLE IF NOT EXISTS `produtos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `pontos` int NOT NULL,
  `imagem` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `produtos`
--

INSERT INTO `produtos` (`id`, `titulo`, `descricao`, `pontos`, `imagem`) VALUES
(1, 'Bandana', 'Tamanho único', 30, 'img_bandana'),
(2, 'Shoulder Bag', 'Tamanho único', 70, 'img_shoulder'),
(3, 'Meia Cano Alto', 'Tamanho único', 50, 'img_meia'),
(4, 'Corta Vento', 'Tamanho único', 180, 'img_corta_vento'),
(5, 'Mala de Viagem', 'Tamanho único', 200, 'img_mala'),
(6, 'Moletom', 'Tamanho único', 250, 'img_moletom');

-- --------------------------------------------------------

--
-- Estrutura para tabela `transacoes`
--

DROP TABLE IF EXISTS `transacoes`;
CREATE TABLE IF NOT EXISTS `transacoes` (
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
