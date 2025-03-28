-- Création de la base de données
CREATE DATABASE IF NOT EXISTS cliproco;
USE cliproco;

-- Table des clients
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(10) NOT NULL,
    type_client VARCHAR(50) NOT NULL,
    secteur_activite VARCHAR(100) NOT NULL,
    date_creation DATE NOT NULL,
    chiffre_affaires DECIMAL(10,2) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    CONSTRAINT chk_telephone CHECK (telephone REGEXP '^[0-9]{10}$'),
    CONSTRAINT chk_chiffre_affaires CHECK (chiffre_affaires >= 0)
);

-- Table des prospects
CREATE TABLE IF NOT EXISTS prospects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(10) NOT NULL,
    secteur_activite VARCHAR(100) NOT NULL,
    date_creation DATE NOT NULL,
    statut VARCHAR(50) NOT NULL,
    commentaire TEXT,
    CONSTRAINT chk_telephone CHECK (telephone REGEXP '^[0-9]{10}$')
);

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_utilisateur VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    date_creation DATE NOT NULL,
    CONSTRAINT chk_role CHECK (role IN ('ADMIN', 'USER'))
); 