import { handleLocationAndWeather } from './geolocWeather.mjs';
import { isAuthenticated } from './auth.mjs';

// Stockage local des clients
const CLIENTS_KEY = 'clients_data';

// Récupérer tous les clients
export function getClients() {
    const clients = localStorage.getItem(CLIENTS_KEY);
    return clients ? JSON.parse(clients) : [];
}

// Sauvegarder les clients
function saveClients(clients) {
    localStorage.setItem(CLIENTS_KEY, JSON.stringify(clients));
}

// Ajouter un client
export function addClient(client) {
    const clients = getClients();
    client.id = Date.now(); // ID unique basé sur le timestamp
    clients.push(client);
    saveClients(clients);
    return client;
}

// Supprimer un client
export function deleteClient(id) {
    const clients = getClients();
    const filteredClients = clients.filter(client => client.id !== id);
    saveClients(filteredClients);
}

// Mettre à jour un client
export function updateClient(id, updatedClient) {
    const clients = getClients();
    const index = clients.findIndex(client => client.id === id);
    if (index !== -1) {
        clients[index] = { ...clients[index], ...updatedClient };
        saveClients(clients);
        return clients[index];
    }
    return null;
}

// Calculer l'âge
function calculateAge(birthDate) {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    return age;
}

// Gérer le formulaire d'ajout de client
export function handleClientForm() {
    const form = document.getElementById('clientForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            if (!isAuthenticated()) {
                alert('Veuillez vous connecter pour ajouter un client');
                return;
            }

            const client = {
                nom: document.getElementById('clientNom').value,
                prenom: document.getElementById('clientPrenom').value,
                dateNaissance: document.getElementById('clientDateNaissance').value,
                adresse: document.getElementById('clientAdresse').value,
                ville: document.getElementById('clientVille').value,
                codePostal: document.getElementById('clientCodePostal').value,
                email: document.getElementById('clientEmail').value,
                telephone: document.getElementById('clientTel').value,
                raisonSociale: document.getElementById('clientRaisonSociale').value,
                chiffreAffaires: document.getElementById('clientCA').value,
                nombreEmployes: document.getElementById('clientEmployes').value
            };

            addClient(client);
            updateClientsTable();
            form.reset();
        });
    }
}

// Mettre à jour le tableau des clients
export function updateClientsTable() {
    const tbody = document.querySelector('#clientTable tbody');
    if (!tbody) return;

    const clients = getClients();
    tbody.innerHTML = '';

    clients.forEach(client => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${client.nom}</td>
            <td>${client.prenom}</td>
            <td>${client.dateNaissance}</td>
            <td>${client.adresse}</td>
            <td>${client.ville}</td>
            <td>${client.codePostal}</td>
            <td>${client.email}</td>
            <td>${client.telephone}</td>
            <td>${client.raisonSociale || '-'}</td>
            <td>${client.chiffreAffaires}€</td>
            <td>${client.nombreEmployes}</td>
            <td>
                <button class="btn btn-info btn-sm view-client" data-id="${client.id}">Voir</button>
                <button class="btn btn-danger btn-sm delete-client" data-id="${client.id}">Supprimer</button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    // Ajouter les événements pour les boutons
    document.querySelectorAll('.view-client').forEach(btn => {
        btn.addEventListener('click', () => viewClientDetails(btn.dataset.id));
    });

    document.querySelectorAll('.delete-client').forEach(btn => {
        btn.addEventListener('click', () => {
            if (confirm('Voulez-vous vraiment supprimer ce client ?')) {
                deleteClient(parseInt(btn.dataset.id));
                updateClientsTable();
            }
        });
    });
}

// Afficher les détails d'un client
async function viewClientDetails(id) {
    const client = getClients().find(c => c.id === parseInt(id));
    if (!client) return;

    const detailsSection = document.getElementById('clientDetails');
    if (detailsSection) {
        detailsSection.style.display = 'block';
        document.getElementById('clientName').textContent = `${client.prenom} ${client.nom}`;
        document.getElementById('clientAddress').textContent = `${client.adresse}, ${client.codePostal} ${client.ville}`;

        try {
            await handleLocationAndWeather(
                client.adresse,
                client.codePostal,
                'clientDetails',
                'clientMap'
            );
        } catch (error) {
            console.error('Erreur lors de la récupération des informations:', error);
        }
    }
}

// Initialiser la gestion des clients
export function initClients() {
    handleClientForm();
    updateClientsTable();

    // Mettre à jour l'âge automatiquement
    const dateNaissanceInput = document.getElementById('clientDateNaissance');
    const ageInput = document.getElementById('clientAge');
    if (dateNaissanceInput && ageInput) {
        dateNaissanceInput.addEventListener('change', () => {
            ageInput.value = calculateAge(dateNaissanceInput.value);
        });
    }
} 