import express from 'express';
import cors from 'cors';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';
import dotenv from 'dotenv';

// Configuration des variables d'environnement
dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Configuration des chemins statiques
app.use(express.static(__dirname)); // Pour servir les fichiers HTML, JS et CSS depuis /src
app.use('/node_modules', express.static(join(__dirname, 'node_modules'))); // Pour servir les dépendances npm
app.use('/js', express.static(join(__dirname, 'js'), {
    extensions: ['js', 'mjs'], // Permet de servir les fichiers .js et .mjs
    index: false
}));

// Middleware pour le logging des requêtes
app.use((req, res, next) => {
    console.log(`${req.method} ${req.url}`);
    next();
});

// Middleware pour gérer les erreurs 404 pour les fichiers statiques
app.use((err, req, res, next) => {
    console.error(`Erreur pour ${req.path}:`, err);
    if (err.code === 'ENOENT') {
        res.status(404).send(`Fichier non trouvé: ${req.path}`);
    } else {
        res.status(500).send('Erreur serveur');
    }
});

// Routes principales
app.get('/', (req, res) => {
    res.send('Le serveur fonctionne !');
});

app.get('/clients', (req, res) => {
    res.sendFile(join(__dirname, 'clients.html'));
});

app.get('/prospects', (req, res) => {
    res.sendFile(join(__dirname, 'prospects.html'));
});

// Middleware pour gérer les routes non trouvées (404)
app.use((req, res) => {
    console.log(`Route non trouvée : ${req.url}`);
    res.status(404).send('Page non trouvée');
});

// Gestion des erreurs process
process.on('uncaughtException', (err) => {
    console.error('Erreur non gérée:', err);
});

process.on('SIGTERM', () => {
    console.log('Arrêt du serveur...');
    process.exit(0);
});

// Démarrage du serveur
const server = app.listen(PORT, () => {
    console.log(`Serveur démarré sur http://localhost:${PORT}`);
    console.log('Appuyez sur Ctrl+C pour arrêter le serveur');
});

server.on('error', (err) => {
    if (err.code === 'EADDRINUSE') {
        console.error(`Le port ${PORT} est déjà utilisé. Essayez un autre port.`);
        process.exit(1);
    } else {
        console.error('Erreur du serveur:', err);
    }
}); 