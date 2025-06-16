-- Création de la base de données
CREATE DATABASE IF NOT EXISTS Cliprocobdd;
USE Cliprocobdd;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    expire DATE,
    role VARCHAR(20) NOT NULL
);

-- Table des adresses
CREATE TABLE IF NOT EXISTS adresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rue VARCHAR(100) NOT NULL,
    code_postal VARCHAR(10) NOT NULL,
    ville VARCHAR(50) NOT NULL
);

-- Table des sociétés (classe de base pour Client et Prospect)
CREATE TABLE IF NOT EXISTS societes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    raison_sociale VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100),
    telephone VARCHAR(20),
    commentaire TEXT,
    adresse_id BIGINT,
    FOREIGN KEY (adresse_id) REFERENCES adresses(id)
);

-- Table des clients
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT PRIMARY KEY,
    chiffre_affaires DECIMAL(15,2) NOT NULL,
    nombre_employes INT NOT NULL,
    FOREIGN KEY (id) REFERENCES societes(id)
);

-- Table des prospects
CREATE TABLE IF NOT EXISTS prospects (
    id BIGINT PRIMARY KEY,
    interet VARCHAR(50) NOT NULL,
    FOREIGN KEY (id) REFERENCES societes(id)
);

-- Table des contrats
CREATE TABLE IF NOT EXISTS contrats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    libelle VARCHAR(100) NOT NULL,
    montant DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Insertion d'un utilisateur admin par défaut (mot de passe: admin123)
INSERT INTO users (username, password, role) 
VALUES ('admin', '$2a$10$rDkPvvAFV6GgJzKYQ5wNYO3vqH9QZ5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username; 