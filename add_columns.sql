-- Script simple pour ajouter les colonnes nom et prénom
USE cliprocobdd;

-- Ajouter les colonnes à la table clients
ALTER TABLE clients ADD COLUMN nom VARCHAR(100) AFTER raisonSociale;
ALTER TABLE clients ADD COLUMN prenom VARCHAR(100) AFTER nom;

-- Ajouter les colonnes à la table prospects
ALTER TABLE prospects ADD COLUMN nom VARCHAR(100) AFTER raisonSociale;
ALTER TABLE prospects ADD COLUMN prenom VARCHAR(100) AFTER nom;

-- Mettre à jour les données existantes avec des valeurs par défaut
UPDATE clients SET nom = 'Contact', prenom = 'Principal' WHERE nom IS NULL OR nom = '';
UPDATE prospects SET nom = 'Contact', prenom = 'Principal' WHERE nom IS NULL OR nom = '';

-- Vérifier les tables
DESCRIBE clients;
DESCRIBE prospects; 