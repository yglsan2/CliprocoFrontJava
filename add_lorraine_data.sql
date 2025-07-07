-- Script pour ajouter des clients et prospects lorrains avec noms/prénoms typiques
-- Exécuter après avoir ajouté les colonnes nom et prénom aux tables

-- Ajout d'adresses pour les clients lorrains
INSERT INTO adresses (numRue, nomRue, codePostal, ville, pays) VALUES
('15', 'Rue de la Paix', '54000', 'Nancy', 'France'),
('28', 'Avenue de Strasbourg', '57000', 'Metz', 'France'),
('7', 'Place Stanislas', '54000', 'Nancy', 'France'),
('42', 'Rue des Jardiniers', '88000', 'Épinal', 'France'),
('12', 'Boulevard de la Marne', '57000', 'Metz', 'France'),
('3', 'Rue du Château', '54000', 'Nancy', 'France'),
('18', 'Avenue de la Gare', '88000', 'Épinal', 'France'),
('25', 'Rue de la République', '57000', 'Metz', 'France');

-- Ajout de clients lorrains
INSERT INTO clients (idAdresse, raisonSociale, nom, prenom, telephone, mail, chiffreAffaires, nbEmployes, commentaires) VALUES
(1, 'Boulangerie Kieffer', 'Kieffer', 'Pierrot', '0383123456', 'pierrot.kieffer@boulangerie.fr', 150000.00, 8, 'Boulangerie traditionnelle au centre de Nancy'),
(2, 'Charcuterie Schmitt', 'Schmitt', 'Jeanne', '0387123456', 'jeanne.schmitt@charcuterie.fr', 200000.00, 12, 'Charcuterie artisanale depuis 3 générations'),
(3, 'Boucherie Thirion', 'Thirion', 'Gérard', '0383123457', 'gerard.thirion@boucherie.fr', 180000.00, 6, 'Boucherie de qualité, viandes locales'),
(4, 'Pâtisserie Marchal', 'Marchal', 'Odile', '0388123456', 'odile.marchal@patisserie.fr', 120000.00, 4, 'Pâtisserie fine, spécialités lorraines'),
(5, 'Épicerie Muller', 'Muller', 'Lucien', '0387123457', 'lucien.muller@epicerie.fr', 90000.00, 3, 'Épicerie de quartier, produits locaux');

-- Ajout de prospects lorrains
INSERT INTO prospects (idAdresse, raisonSociale, nom, prenom, telephone, mail, commentaires, dateProspection, prospectInteresse) VALUES
(6, 'Café Becker', 'Becker', 'Mireille', '0383123458', 'mireille.becker@cafe.fr', 'Café-restaurant, projet d\'ouverture', '2024-01-15', true),
(7, 'Fleuriste Simonin', 'Simonin', 'Armand', '0388123457', 'armand.simonin@fleuriste.fr', 'Fleuriste, recherche local commercial', '2024-01-20', true),
(8, 'Coiffure Collin', 'Collin', 'Léa', '0387123458', 'lea.collin@coiffure.fr', 'Salon de coiffure, clientèle fidèle', '2024-01-25', false);

-- Mise à jour des commentaires pour rendre l'application plus vivante
UPDATE clients SET commentaires = 'Boulangerie traditionnelle au centre de Nancy, spécialités lorraines : quiche lorraine, madeleines de Commercy' WHERE nom = 'Kieffer';
UPDATE clients SET commentaires = 'Charcuterie artisanale depuis 3 générations, spécialités : pâté lorrain, quiche lorraine' WHERE nom = 'Schmitt';
UPDATE clients SET commentaires = 'Boucherie de qualité, viandes locales, spécialités : potée lorraine, boudin blanc' WHERE nom = 'Thirion';
UPDATE clients SET commentaires = 'Pâtisserie fine, spécialités lorraines : bergamotes de Nancy, madeleines de Commercy' WHERE nom = 'Marchal';
UPDATE clients SET commentaires = 'Épicerie de quartier, produits locaux, spécialités lorraines et vins de Moselle' WHERE nom = 'Muller';

UPDATE prospects SET commentaires = 'Café-restaurant, projet d\'ouverture dans le centre historique de Nancy, spécialités lorraines' WHERE nom = 'Becker';
UPDATE prospects SET commentaires = 'Fleuriste, recherche local commercial à Metz, spécialisé dans les fleurs de saison' WHERE nom = 'Simonin';
UPDATE prospects SET commentaires = 'Salon de coiffure, clientèle fidèle, projet d\'agrandissement à Épinal' WHERE nom = 'Collin'; 