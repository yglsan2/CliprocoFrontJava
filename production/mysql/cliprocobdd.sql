-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 26 mars 2025 à 15:14
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionclients`
--

-- --------------------------------------------------------

--
-- Structure de la table `adresses`
--

DROP TABLE IF EXISTS `adresses`;
CREATE TABLE IF NOT EXISTS `adresses` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `numRue` varchar(10) NOT NULL,
  `nomRue` varchar(30) NOT NULL,
  `codePostal` varchar(5) NOT NULL,
  `ville` varchar(30) NOT NULL,
  PRIMARY KEY (`identifiant`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `adresses`
--

INSERT INTO `adresses` (`identifiant`, `numRue`, `nomRue`, `codePostal`, `ville`) VALUES
-- Adresses pour les clients
(1, '15', 'Rue des Quatre Vents', '54520', 'Laxou'),
(2, '42', 'Avenue de la Quiche', '54000', 'Nancy'),
(3, '7', 'Rue du Schnaps', '57000', 'Metz'),
(4, '23', 'Boulevard des Bretzels', '54000', 'Nancy'),
(5, '11', 'Rue de la Choucroute', '54520', 'Laxou'),
(6, '89', 'Avenue des Vosges', '57000', 'Metz'),
(7, '3', 'Rue du Mirabelle', '54000', 'Nancy'),
(8, '67', 'Boulevard de la Lorraine', '54520', 'Laxou'),
(9, '12', 'Rue des Quiches', '57000', 'Metz'),
(10, '45', 'Avenue de la Tarte', '54000', 'Nancy'),
-- Adresses pour les prospects
(11, '18', 'Rue du Flammekueche', '54520', 'Laxou'),
(12, '33', 'Avenue des Spätzle', '54000', 'Nancy'),
(13, '9', 'Rue de la Bière', '57000', 'Metz'),
(14, '27', 'Boulevard des Bretzels', '54520', 'Laxou'),
(15, '14', 'Rue de la Choucroute', '54000', 'Nancy'),
(16, '56', 'Avenue des Vosges', '57000', 'Metz'),
(17, '8', 'Rue du Mirabelle', '54520', 'Laxou'),
(18, '22', 'Boulevard de la Lorraine', '54000', 'Nancy'),
(19, '41', 'Rue des Quiches', '57000', 'Metz'),
(20, '6', 'Avenue de la Tarte', '54520', 'Laxou');

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `raisonSociale` varchar(30) NOT NULL,
  `telephone` varchar(15) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `commentaires` text,
  `chiffreAffaires` decimal(19,4) NOT NULL,
  `nbEmployes` int NOT NULL,
  `idAdresse` int NOT NULL,
  PRIMARY KEY (`identifiant`),
  UNIQUE KEY `raisonSociale` (`raisonSociale`),
  KEY `idAdresse` (`idAdresse`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`identifiant`, `raisonSociale`, `telephone`, `mail`, `commentaires`, `chiffreAffaires`, `nbEmployes`, `idAdresse`) VALUES
(1, 'Quiche Lorraine Express', '0383543400', 'contact@quiche-express.fr', 'Spécialiste de la quiche lorraine depuis 1985', 1250000.0000, 45, 1),
(2, 'Mirabelle & Co', '0383375640', 'mirabelle@mirabelle-co.fr', 'Production de mirabelle de Lorraine', 850000.0000, 23, 2),
(3, 'Bretzel Brothers', '0387758575', 'contact@bretzel-bros.fr', 'Bretzels artisanaux made in Lorraine', 320000.0000, 12, 3),
(4, 'Choucroute Royale', '0388551234', 'info@choucroute-royale.fr', 'Choucroute traditionnelle de la région', 680000.0000, 18, 4),
(5, 'Schnaps & Schnitzel', '0387123456', 'hello@schnaps-schnitzel.fr', 'Restaurant allemand avec terrasse', 450000.0000, 15, 5),
(6, 'Vosges Mountain Coffee', '0387987654', 'cafe@vosges-mountain.fr', 'Café torréfié dans les Vosges', 920000.0000, 28, 6),
(7, 'Tarte Flambée Factory', '0387654321', 'tarte@flambee-factory.fr', 'Tarte flambée à emporter', 380000.0000, 8, 7),
(8, 'Spätzle & Spaetzle', '0387543210', 'spaetzle@spaetzle-spaetzle.fr', 'Spätzle frais du jour', 290000.0000, 11, 8),
(9, 'Bière de Lorraine', '0387123456', 'biere@lorraine-biere.fr', 'Brasserie artisanale lorraine', 750000.0000, 22, 9),
(10, 'Quatre Vents Consulting', '0387987654', 'contact@quatre-vents.fr', 'Conseil en développement durable', 2100000.0000, 35, 10);

-- --------------------------------------------------------

--
-- Structure de la table `contrats`
--

DROP TABLE IF EXISTS `contrats`;
CREATE TABLE IF NOT EXISTS `contrats` (
  `idContrat` int NOT NULL AUTO_INCREMENT,
  `libelleContrat` varchar(30) NOT NULL,
  `montant` decimal(19,4) NOT NULL,
  `idClient` int NOT NULL,
  PRIMARY KEY (`idContrat`),
  KEY `idClient` (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `contrats`
--

INSERT INTO `contrats` (`idContrat`, `libelleContrat`, `montant`, `idClient`) VALUES
(1, 'Contrat Quiche Premium', 25000.0000, 1),
(2, 'Mirabelle Gold', 18000.0000, 2),
(3, 'Bretzel Deluxe', 12000.0000, 3),
(4, 'Choucroute Royale', 15000.0000, 4),
(5, 'Schnaps Special', 22000.0000, 5),
(6, 'Café Vosges', 32000.0000, 6),
(7, 'Tarte Flambée Pro', 8500.0000, 7),
(8, 'Spätzle Master', 11000.0000, 8),
(9, 'Bière Lorraine', 28000.0000, 9),
(10, 'Consulting Quatre Vents', 45000.0000, 10),
(11, 'Contrat Maintenance Quiche', 8000.0000, 1),
(12, 'Support Mirabelle', 12000.0000, 2),
(13, 'Formation Bretzel', 6000.0000, 3),
(14, 'Audit Choucroute', 9500.0000, 4);

-- --------------------------------------------------------

--
-- Structure de la table `prospects`
--

DROP TABLE IF EXISTS `prospects`;
CREATE TABLE IF NOT EXISTS `prospects` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `raisonSociale` varchar(30) NOT NULL,
  `telephone` varchar(15) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `commentaires` text,
  `dateProspection` date NOT NULL,
  `prospectInteresse` tinyint(1) NOT NULL,
  `idAdresse` int NOT NULL,
  PRIMARY KEY (`identifiant`),
  UNIQUE KEY `raisonSociale` (`raisonSociale`),
  KEY `idAdresse` (`idAdresse`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `prospects`
--

INSERT INTO `prospects` (`identifiant`, `raisonSociale`, `telephone`, `mail`, `commentaires`, `dateProspection`, `prospectInteresse`, `idAdresse`) VALUES
(1, 'Flammekueche Express', '0388553370', 'contact@flammekueche-express.fr', 'Tarte flambée à domicile', '2024-10-10', 1, 11),
(2, 'Spätzle & Co', '0387260000', 'contact@spaetzle-co.fr', 'Spätzle bio et locaux', '2024-05-28', 0, 12),
(3, 'Bière de Metz', '0387172390', 'contact@biere-metz.fr', 'Brasserie artisanale', '2024-10-10', 1, 13),
(4, 'Bretzel Artisan', '0385626298', 'contact@bretzel-artisan.fr', 'Bretzels traditionnels', '2023-10-15', 0, 14),
(5, 'Choucroute Bio', '0388551234', 'bio@choucroute-bio.fr', 'Choucroute biologique', '2024-03-15', 1, 15),
(6, 'Mirabelle Gold', '0387123456', 'gold@mirabelle-gold.fr', 'Mirabelle premium', '2024-01-20', 1, 16),
(7, 'Quiche Gourmet', '0387654321', 'gourmet@quiche-gourmet.fr', 'Quiche haut de gamme', '2024-02-10', 0, 17),
(8, 'Schnaps Premium', '0387543210', 'premium@schnaps-premium.fr', 'Schnaps d\'exception', '2024-04-05', 1, 18),
(9, 'Tarte Lorraine', '0387123456', 'lorraine@tarte-lorraine.fr', 'Tartes traditionnelles', '2024-06-12', 0, 19),
(10, 'Vosges Mountain', '0387987654', 'mountain@vosges-mountain.fr', 'Produits de montagne', '2024-07-08', 1, 20);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USER',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `expire` datetime DEFAULT NULL,
  PRIMARY KEY (`identifiant`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`identifiant`, `username`, `password`, `email`, `role`, `token`, `expire`) VALUES
(2, 'benja2', '$argon2i$v=19$m=65536,t=2,p=1$BjNdGWSkGXEDhBbuGBsWKg$QuzeVfrtxolZ8F3Zp1PHTwnEq8FAlD+Enwm+V/h1rfE', 'benja2@example.com', 'USER', '86c0cc71-4bc4-4c97-9788-9c885afb5223', '2025-04-02 00:00:00');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `clients`
--
ALTER TABLE `clients`
  ADD CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`idAdresse`) REFERENCES `adresses` (`identifiant`);

--
-- Contraintes pour la table `contrats`
--
ALTER TABLE `contrats`
  ADD CONSTRAINT `contrats_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `clients` (`identifiant`);

--
-- Contraintes pour la table `prospects`
--
ALTER TABLE `prospects`
  ADD CONSTRAINT `prospects_ibfk_1` FOREIGN KEY (`idAdresse`) REFERENCES `adresses` (`identifiant`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
