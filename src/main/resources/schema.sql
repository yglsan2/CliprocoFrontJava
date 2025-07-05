-- Création de la base de données
CREATE DATABASE IF NOT EXISTS Cliprocobdd;
USE Cliprocobdd;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    token VARCHAR(255),
    expire DATE,
    role VARCHAR(20) NOT NULL
);

-- Table des adresses
CREATE TABLE IF NOT EXISTS adresses (
    identifiant INT PRIMARY KEY AUTO_INCREMENT,
    numeroRue VARCHAR(10) NOT NULL,
    nomRue VARCHAR(100) NOT NULL,
    codePostal VARCHAR(10) NOT NULL,
    ville VARCHAR(50) NOT NULL,
    pays VARCHAR(50) NOT NULL
);

-- Table des sociétés (classe de base pour Client et Prospect)
CREATE TABLE IF NOT EXISTS societes (
    identifiant INT PRIMARY KEY AUTO_INCREMENT,
    raisonSociale VARCHAR(100) NOT NULL UNIQUE,
    mail VARCHAR(100),
    telephone VARCHAR(20),
    commentaires TEXT,
    idAdresse INT,
    FOREIGN KEY (idAdresse) REFERENCES adresses(identifiant)
);

-- Table des clients
CREATE TABLE IF NOT EXISTS clients (
    identifiant INT PRIMARY KEY,
    chiffreAffaires DECIMAL(15,2) NOT NULL,
    nbEmployes INT NOT NULL,
    FOREIGN KEY (identifiant) REFERENCES societes(identifiant)
);

-- Table des prospects
CREATE TABLE IF NOT EXISTS prospects (
    identifiant INT PRIMARY KEY,
    prospectInteresse VARCHAR(50) NOT NULL,
    dateProspection DATE,
    FOREIGN KEY (identifiant) REFERENCES societes(identifiant)
);

-- Table des produits
CREATE TABLE IF NOT EXISTS produits (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    prix DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    reference VARCHAR(50) UNIQUE,
    categorie VARCHAR(50)
);

-- Insertion d'un utilisateur admin par défaut (mot de passe: admin123)
INSERT INTO users (username, password, email, role) 
VALUES ('admin', '$2a$10$rDkPvvAFV6GgJzKYQ5wNYO3vqH9QZ5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z', 'admin@cliproco.com', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username; 