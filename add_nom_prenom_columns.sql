-- Script pour ajouter les colonnes nom et prénom aux tables clients et prospects
USE Cliprocobdd;

-- Ajout des colonnes nom et prénom à la table clients
ALTER TABLE clients ADD COLUMN nom VARCHAR(50) AFTER raisonSociale;
ALTER TABLE clients ADD COLUMN prenom VARCHAR(50) AFTER nom;

-- Ajout des colonnes nom et prénom à la table prospects  
ALTER TABLE prospects ADD COLUMN nom VARCHAR(50) AFTER raisonSociale;
ALTER TABLE prospects ADD COLUMN prenom VARCHAR(50) AFTER nom;

-- Mise à jour des données existantes avec des valeurs par défaut
UPDATE clients SET nom = SUBSTRING_INDEX(raisonSociale, ' ', 1), prenom = 'Contact' WHERE nom IS NULL OR nom = '';

UPDATE prospects SET nom = SUBSTRING_INDEX(raisonSociale, ' ', 1), prenom = 'Contact' WHERE nom IS NULL OR nom = '';

-- Vérification
SELECT 'Table clients mise à jour' as message;
DESCRIBE clients;

SELECT 'Table prospects mise à jour' as message;  
DESCRIBE prospects; 