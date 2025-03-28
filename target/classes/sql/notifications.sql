CREATE TABLE IF NOT EXISTS notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    lu BOOLEAN NOT NULL DEFAULT 0
);

-- Insertion de quelques notifications de test
INSERT INTO notifications (message, type, date_creation, lu) VALUES
('Nouveau client ajouté : Entreprise ABC', 'INFO', CURRENT_TIMESTAMP, 0),
('Contrat important à renouveler : Client XYZ', 'ALERTE', CURRENT_TIMESTAMP, 0),
('Prospect converti en client : Société 123', 'SUCCES', CURRENT_TIMESTAMP, 0); 