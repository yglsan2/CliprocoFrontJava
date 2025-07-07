-- Script pour ajouter des noms et prénoms typiquement lorrains
-- Noms de famille lorrains
UPDATE clients SET nom = 'Muller' WHERE identifiant = 1;
UPDATE clients SET nom = 'Schmitt' WHERE identifiant = 2;
UPDATE clients SET nom = 'Weber' WHERE identifiant = 3;
UPDATE clients SET nom = 'Fischer' WHERE identifiant = 4;
UPDATE clients SET nom = 'Meyer' WHERE identifiant = 5;
UPDATE clients SET nom = 'Wagner' WHERE identifiant = 6;
UPDATE clients SET nom = 'Becker' WHERE identifiant = 7;
UPDATE clients SET nom = 'Hoffmann' WHERE identifiant = 8;
UPDATE clients SET nom = 'Schäfer' WHERE identifiant = 9;
UPDATE clients SET nom = 'Koch' WHERE identifiant = 10;

-- Prénoms lorrains
UPDATE clients SET prenom = 'Jean-Pierre' WHERE identifiant = 1;
UPDATE clients SET prenom = 'Marie-Claude' WHERE identifiant = 2;
UPDATE clients SET prenom = 'François' WHERE identifiant = 3;
UPDATE clients SET prenom = 'Anne-Marie' WHERE identifiant = 4;
UPDATE clients SET prenom = 'Michel' WHERE identifiant = 5;
UPDATE clients SET prenom = 'Catherine' WHERE identifiant = 6;
UPDATE clients SET prenom = 'Pierre' WHERE identifiant = 7;
UPDATE clients SET prenom = 'Isabelle' WHERE identifiant = 8;
UPDATE clients SET prenom = 'André' WHERE identifiant = 9;
UPDATE clients SET prenom = 'Monique' WHERE identifiant = 10;

-- Noms de famille lorrains pour les prospects
UPDATE prospects SET nom = 'Klein' WHERE identifiant = 1;
UPDATE prospects SET nom = 'Martin' WHERE identifiant = 2;
UPDATE prospects SET nom = 'Bernard' WHERE identifiant = 3;
UPDATE prospects SET nom = 'Dubois' WHERE identifiant = 4;
UPDATE prospects SET nom = 'Thomas' WHERE identifiant = 5;
UPDATE prospects SET nom = 'Robert' WHERE identifiant = 6;
UPDATE prospects SET nom = 'Richard' WHERE identifiant = 7;
UPDATE prospects SET nom = 'Petit' WHERE identifiant = 8;
UPDATE prospects SET nom = 'Durand' WHERE identifiant = 9;
UPDATE prospects SET nom = 'Leroy' WHERE identifiant = 10;

-- Prénoms lorrains pour les prospects
UPDATE prospects SET prenom = 'Claude' WHERE identifiant = 1;
UPDATE prospects SET prenom = 'Sylvie' WHERE identifiant = 2;
UPDATE prospects SET prenom = 'Philippe' WHERE identifiant = 3;
UPDATE prospects SET prenom = 'Nathalie' WHERE identifiant = 4;
UPDATE prospects SET prenom = 'Laurent' WHERE identifiant = 5;
UPDATE prospects SET prenom = 'Véronique' WHERE identifiant = 6;
UPDATE prospects SET prenom = 'Stéphane' WHERE identifiant = 7;
UPDATE prospects SET prenom = 'Sandrine' WHERE identifiant = 8;
UPDATE prospects SET prenom = 'David' WHERE identifiant = 9;
UPDATE prospects SET prenom = 'Céline' WHERE identifiant = 10;

-- Ajout de nouveaux clients avec des noms lorrains
INSERT INTO clients (raisonSociale, nom, prenom, telephone, mail, chiffreAffaires, nbEmployes, adresse_id) VALUES
('Boulangerie Lorraine', 'Schneider', 'Marc', '0387654321', 'marc.schneider@boulangerie-lorraine.fr', 450000, 8, 11),
('Charcuterie Metz', 'Leroux', 'Sophie', '0387123456', 'sophie.leroux@charcuterie-metz.fr', 320000, 6, 12),
('Fromagerie Nancy', 'Moreau', 'Jean-Luc', '0383456789', 'jeanluc.moreau@fromagerie-nancy.fr', 280000, 5, 13),
('Pâtisserie Épinal', 'Girard', 'Marie-France', '0382987654', 'mariefrance.girard@patisserie-epinal.fr', 380000, 7, 14),
('Boucherie Thionville', 'Bonnet', 'Alain', '0382567890', 'alain.bonnet@boucherie-thionville.fr', 520000, 10, 15);

-- Ajout de nouveaux prospects avec des noms lorrains
INSERT INTO prospects (raisonSociale, nom, prenom, telephone, mail, dateProspection, prospectInteresse, adresse_id) VALUES
('Traiteur Sarreguemines', 'Roux', 'Patricia', '0387234567', 'patricia.roux@traiteur-sarreguemines.fr', '2024-01-15', true, 16),
('Confiserie Lunéville', 'Fontaine', 'Gérard', '0387345678', 'gerard.fontaine@confiserie-luneville.fr', '2024-01-20', false, 17),
('Épicerie Bar-le-Duc', 'Mercier', 'Christine', '0387456789', 'christine.mercier@epicerie-barleduc.fr', '2024-01-25', true, 18),
('Cave à Vins Verdun', 'Blanc', 'Thierry', '0387567890', 'thierry.blanc@cave-verdun.fr', '2024-01-30', true, 19),
('Poissonnerie Saint-Dié', 'Garnier', 'Brigitte', '0387678901', 'brigitte.garnier@poissonnerie-saintdie.fr', '2024-02-05', false, 20);

-- Ajout des adresses correspondantes
INSERT INTO adresses (numeroRue, nomRue, codePostal, ville, pays) VALUES
('15', 'Rue de la Paix', '57000', 'Metz', 'France'),
('8', 'Place Stanislas', '54000', 'Nancy', 'France'),
('22', 'Rue des Vosges', '88000', 'Épinal', 'France'),
('45', 'Avenue de Thionville', '57100', 'Thionville', 'France'),
('12', 'Rue de la République', '57200', 'Sarreguemines', 'France'),
('3', 'Place de la Mairie', '54300', 'Lunéville', 'France'),
('67', 'Rue du Commerce', '55000', 'Bar-le-Duc', 'France'),
('9', 'Place de la Victoire', '55100', 'Verdun', 'France'),
('34', 'Rue de la Gare', '88100', 'Saint-Dié-des-Vosges', 'France'),
('78', 'Avenue de la Libération', '57000', 'Metz', 'France'); 