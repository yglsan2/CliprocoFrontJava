import clients from './clients.mjs';
import { initAuth } from './auth.mjs';

// Initialiser l'authentification
initAuth();

// Rendre l'instance clients accessible globalement pour les événements onclick
window.clients = clients; 