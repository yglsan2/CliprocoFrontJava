USE cliprocobdd;

-- Remplir les colonnes nom et prénom avec des valeurs
UPDATE clients SET nom = 'Muller', prenom = 'Jean-Pierre' WHERE nom IS NULL OR nom = '';
UPDATE clients SET nom = 'Weber', prenom = 'Marie-Claude' WHERE nom = 'Contact';
UPDATE clients SET nom = 'Schneider', prenom = 'François' WHERE nom = 'TestNom';

UPDATE prospects SET nom = 'Martin', prenom = 'Claude' WHERE nom IS NULL OR nom = '';
UPDATE prospects SET nom = 'Bernard', prenom = 'Guy' WHERE nom = 'Contact';
UPDATE prospects SET nom = 'Thomas', prenom = 'Michel' WHERE nom = 'TestNom';

-- Vérifier que ça marche
SELECT 'Clients:' as info;
SELECT identifiant, raisonSociale, nom, prenom FROM clients LIMIT 5;

SELECT 'Prospects:' as info;
SELECT identifiant, raisonSociale, nom, prenom FROM prospects LIMIT 5; 