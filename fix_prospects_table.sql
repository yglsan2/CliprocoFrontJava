-- Script de correction de la table prospects
-- Suppression des colonnes en double et recréation propre

USE cliprocobdd;

-- Sauvegarde de la table actuelle
CREATE TABLE IF NOT EXISTS prospects_backup AS SELECT * FROM prospects;

-- Suppression de la table prospects avec colonnes en double
DROP TABLE IF EXISTS prospects;

-- Recréation propre de la table prospects
CREATE TABLE prospects (
  identifiant INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  raisonSociale VARCHAR(30) NOT NULL UNIQUE,
  telephone VARCHAR(15) NOT NULL,
  mail VARCHAR(50) NOT NULL,
  commentaires TEXT,
  dateProspection DATE NOT NULL,
  prospectInteresse TINYINT(1) NOT NULL,
  idAdresse INT NOT NULL,
  nom VARCHAR(50),
  prenom VARCHAR(50),
  statut VARCHAR(255),
  FOREIGN KEY (idAdresse) REFERENCES adresses(identifiant)
);

-- Réinsertion des données rigolotes de Lorraine
INSERT INTO prospects (raisonSociale, telephone, mail, commentaires, dateProspection, prospectInteresse, idAdresse, nom, prenom, statut) VALUES
('Flammekueche Express', '0388553370', 'contact@flammekueche-express.fr', 'Tarte flambée à domicile', '2024-10-10', 1, 11, NULL, NULL, NULL),
('Spätzle & Co', '0387260000', 'contact@spaetzle-co.fr', 'Spätzle bio et locaux', '2024-05-28', 0, 12, NULL, NULL, NULL),
('Bière de Metz', '0387172390', 'contact@biere-metz.fr', 'Brasserie artisanale', '2024-10-10', 1, 13, NULL, NULL, NULL),
('Bretzel Artisan', '0385626298', 'contact@bretzel-artisan.fr', 'Bretzels traditionnels', '2023-10-15', 0, 14, NULL, NULL, NULL),
('Choucroute Bio', '0388551234', 'bio@choucroute-bio.fr', 'Choucroute biologique', '2024-03-15', 1, 15, NULL, NULL, NULL),
('Mirabelle Gold', '0387123456', 'gold@mirabelle-gold.fr', 'Mirabelle premium', '2024-01-20', 1, 16, NULL, NULL, NULL),
('Quiche Gourmet', '0387654321', 'gourmet@quiche-gourmet.fr', 'Quiche haut de gamme', '2024-02-10', 0, 17, NULL, NULL, NULL),
('Schnaps Premium', '0387543210', 'premium@schnaps-premium.fr', 'Schnaps d''exception', '2024-04-05', 1, 18, NULL, NULL, NULL),
('Tarte Lorraine', '0387123456', 'lorraine@tarte-lorraine.fr', 'Tartes traditionnelles', '2024-06-12', 0, 19, NULL, NULL, NULL),
('Vosges Mountain', '0387987654', 'mountain@vosges-mountain.fr', 'Produits de montagne', '2024-07-08', 1, 20, NULL, NULL, NULL);

-- Vérification
SELECT COUNT(*) as nombre_prospects FROM prospects;

-- Script pour ajouter les colonnes nom et prénom aux tables clients et prospects
-- À exécuter pour mettre à jour la base de données existante

USE gestionclients;

-- Ajout des colonnes nom et prénom à la table clients
ALTER TABLE clients ADD COLUMN IF NOT EXISTS nom VARCHAR(100);
ALTER TABLE clients ADD COLUMN IF NOT EXISTS prenom VARCHAR(100);

-- Ajout des colonnes nom et prénom à la table prospects
ALTER TABLE prospects ADD COLUMN IF NOT EXISTS nom VARCHAR(100);
ALTER TABLE prospects ADD COLUMN IF NOT EXISTS prenom VARCHAR(100);

-- Mettre à jour les données existantes avec des noms lorrains
UPDATE clients SET 
    nom = CASE 
        WHEN identifiant = 1 THEN 'Muller'
        WHEN identifiant = 2 THEN 'Weber'
        WHEN identifiant = 3 THEN 'Schneider'
        WHEN identifiant = 4 THEN 'Fischer'
        WHEN identifiant = 5 THEN 'Meyer'
        ELSE 'Dupont'
    END,
    prenom = CASE 
        WHEN identifiant = 1 THEN 'Jean-Pierre'
        WHEN identifiant = 2 THEN 'Marie-Claude'
        WHEN identifiant = 3 THEN 'François'
        WHEN identifiant = 4 THEN 'Anne-Sophie'
        WHEN identifiant = 5 THEN 'Pierre'
        ELSE 'Michel'
    END
WHERE nom IS NULL OR nom = '';

UPDATE prospects SET 
    nom = CASE 
        WHEN identifiant = 1 THEN 'Martin'
        WHEN identifiant = 2 THEN 'Bernard'
        WHEN identifiant = 3 THEN 'Thomas'
        WHEN identifiant = 4 THEN 'Petit'
        WHEN identifiant = 5 THEN 'Robert'
        ELSE 'Richard'
    END,
    prenom = CASE 
        WHEN identifiant = 1 THEN 'Claude'
        WHEN identifiant = 2 THEN 'Guy'
        WHEN identifiant = 3 THEN 'Michel'
        WHEN identifiant = 4 THEN 'Jean'
        WHEN identifiant = 5 THEN 'Philippe'
        ELSE 'Daniel'
    END
WHERE nom IS NULL OR nom = '';

-- Afficher les résultats pour vérification
SELECT 'Clients mis à jour:' as info;
SELECT identifiant, raisonSociale, nom, prenom FROM clients;

SELECT 'Prospects mis à jour:' as info;
SELECT identifiant, raisonSociale, nom, prenom FROM prospects;
