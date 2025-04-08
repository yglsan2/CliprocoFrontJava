import { getCoordinates, fetchWeather, formatWeatherDisplay } from './geolocWeather.mjs';

class Clients {
    constructor() {
        // Limiter le nombre de clients en mémoire
        this.maxClients = 100;
        this.clients = this.loadClients();
        this.currentClient = null;
        this.map = null;
        this.marker = null;
        this.weatherCache = new Map(); // Cache pour les données météo
        this.coordinatesCache = new Map(); // Cache pour les coordonnées

        // Références aux éléments DOM
        this.form = document.getElementById('clientForm');
        this.tableElement = document.getElementById('clientTable')?.querySelector('tbody');
        this.detailsSection = document.getElementById('clientDetails');

        if (!this.form || !this.tableElement || !this.detailsSection) {
            console.error('Éléments DOM manquants pour l\'initialisation des clients');
            return;
        }

        // Initialisation différée de la carte
        this.mapInitialized = false;
        this.setupEventListeners();
        this.updateClientsList();
    }

    loadClients() {
        const clients = JSON.parse(localStorage.getItem('clients')) || [];
        // Garder uniquement les 100 derniers clients
        return clients.slice(-this.maxClients);
    }

    setupMap() {
        if (!this.mapInitialized && window.L) {
            try {
                this.map = L.map('clientMap').setView([46.603354, 1.888334], 5);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© OpenStreetMap contributors'
                }).addTo(this.map);
                this.mapInitialized = true;
            } catch (error) {
                console.error('Erreur lors de l\'initialisation de la carte:', error);
            }
        }
    }

    setupEventListeners() {
        // Écouteur pour le formulaire
        this.form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (this.validateForm()) {
                await this.saveClient();
            }
        });

        // Écouteur pour la date de naissance
        document.getElementById('clientDateNaissance')?.addEventListener('change', (e) => {
            const age = this.calculateAge(e.target.value);
            const ageInput = document.getElementById('clientAge');
            if (ageInput) {
                ageInput.value = age;
            }
        });

        // Écouteur pour la gestion de l'état d'authentification
        document.addEventListener('authStateChanged', (e) => {
            if (!e.detail.isAuthenticated) {
                this.clearForm();
                this.clearMap();
                this.clients = [];
                this.updateClientsList();
            }
        });
    }

    validateForm() {
        const caInput = document.getElementById('clientCA');
        const employesInput = document.getElementById('clientEmployes');

        if (!caInput || !employesInput) {
            console.error('Champs du formulaire manquants');
            return false;
        }

        const ca = Number(caInput.value);
        const employes = Number(employesInput.value);

        if (ca < 0) {
            alert('Le chiffre d\'affaires ne peut pas être négatif');
            return false;
        }

        if (employes < 1) {
            alert('Le nombre d\'employés doit être au moins 1');
            return false;
        }

        // Vérification des champs obligatoires
        const requiredFields = ['clientNom', 'clientPrenom', 'clientDateNaissance', 'clientAdresse', 'clientVille', 'clientCodePostal', 'clientEmail', 'clientTel'];
        for (const fieldId of requiredFields) {
            const field = document.getElementById(fieldId);
            if (!field || !field.value.trim()) {
                alert('Tous les champs marqués d\'un astérisque sont obligatoires');
                return false;
            }
        }

        return true;
    }

    async saveClient() {
        try {
            const client = {
                id: this.currentClient ? this.currentClient.id : Date.now(),
                nom: document.getElementById('clientNom')?.value,
                prenom: document.getElementById('clientPrenom')?.value,
                dateNaissance: document.getElementById('clientDateNaissance')?.value,
                age: this.calculateAge(document.getElementById('clientDateNaissance')?.value),
                adresse: document.getElementById('clientAdresse')?.value,
                ville: document.getElementById('clientVille')?.value,
                codePostal: document.getElementById('clientCodePostal')?.value,
                email: document.getElementById('clientEmail')?.value,
                telephone: document.getElementById('clientTel')?.value,
                raisonSociale: document.getElementById('clientRaisonSociale')?.value,
                ca: document.getElementById('clientCA')?.value,
                employes: document.getElementById('clientEmployes')?.value
            };

            // Vérifier que tous les champs requis sont remplis
            if (!this.validateClientData(client)) {
                throw new Error('Tous les champs requis doivent être remplis');
            }

            // Récupérer les coordonnées
            const address = `${client.adresse}, ${client.codePostal} ${client.ville}`;
            const coordinates = await this.getCoordinates(client.codePostal, client.ville);
            client.coordinates = coordinates;

            if (this.currentClient) {
                const index = this.clients.findIndex(c => c.id === this.currentClient.id);
                if (index !== -1) {
                    this.clients[index] = client;
                }
            } else {
                this.clients.push(client);
                // Garder uniquement les 100 derniers clients
                if (this.clients.length > this.maxClients) {
                    this.clients = this.clients.slice(-this.maxClients);
                }
            }

            localStorage.setItem('clients', JSON.stringify(this.clients));
            this.updateClientsList();
            this.clearForm();
            this.currentClient = null;

            console.log('Client sauvegardé avec succès:', client);
        } catch (error) {
            console.error('Erreur lors de la sauvegarde du client:', error);
            alert('Erreur lors de la sauvegarde du client. Vérifiez l\'adresse et réessayez.');
        }
    }

    validateClientData(client) {
        const requiredFields = ['nom', 'prenom', 'dateNaissance', 'adresse', 'ville', 'codePostal', 'email', 'telephone', 'ca', 'employes'];
        return requiredFields.every(field => client[field] && client[field].toString().trim() !== '');
    }

    calculateAge(dateNaissance) {
        if (!dateNaissance) return 0;
        const birthDate = new Date(dateNaissance);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDifference = today.getMonth() - birthDate.getMonth();
        if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    }

    async getCoordinates(codePostal, ville) {
        const cacheKey = `${codePostal}-${ville}`;
        
        // Vérifier le cache
        if (this.coordinatesCache.has(cacheKey)) {
            return this.coordinatesCache.get(cacheKey);
        }

        const encodedAddress = encodeURIComponent(`${codePostal} ${ville}`);
        const response = await fetch(`https://api-adresse.data.gouv.fr/search/?q=${encodedAddress}`);
        const data = await response.json();

        if (data.features && data.features.length > 0) {
            const [lng, lat] = data.features[0].geometry.coordinates;
            const coordinates = { lat, lng };
            
            // Mettre en cache
            this.coordinatesCache.set(cacheKey, coordinates);
            
            return coordinates;
        }
        throw new Error('Adresse non trouvée');
    }

    async getWeather(coordinates) {
        const cacheKey = `${coordinates.lat}-${coordinates.lng}`;
        const now = Date.now();
        
        // Vérifier le cache (validité : 1 heure)
        if (this.weatherCache.has(cacheKey)) {
            const cached = this.weatherCache.get(cacheKey);
            if (now - cached.timestamp < 3600000) { // 1 heure
                return cached.data;
            }
        }

        const weatherData = await fetchWeather(coordinates.lat, coordinates.lng);
        
        // Mettre en cache avec timestamp
        this.weatherCache.set(cacheKey, {
            data: weatherData,
            timestamp: now
        });

        return weatherData;
    }

    async displayWeather(coordinates) {
        const weatherContainer = document.getElementById('clientWeather');
        if (!weatherContainer) return;

        try {
            const weatherData = await this.getWeather(coordinates);
            weatherContainer.innerHTML = formatWeatherDisplay(weatherData);
        } catch (error) {
            console.error('Erreur météo:', error);
            weatherContainer.innerHTML = `
                <div class="card-body">
                    <p class="text-danger">Erreur lors de la récupération de la météo</p>
                </div>
            `;
        }
    }

    updateClientsList() {
        if (!this.tableElement) return;
        
        this.tableElement.innerHTML = '';
        this.clients.forEach(client => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${client.nom}</td>
                <td>${client.prenom}</td>
                <td>${client.dateNaissance}</td>
                <td>${client.adresse}</td>
                <td>${client.ville}</td>
                <td>${client.codePostal}</td>
                <td>${client.email}</td>
                <td>${client.telephone}</td>
                <td>${client.raisonSociale || '-'}</td>
                <td>${client.ca}€</td>
                <td>${client.employes}</td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="clients.viewClient(${client.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-warning btn-sm" onclick="clients.editClient(${client.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="clients.showDeleteConfirmation(${client.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            `;
            this.tableElement.appendChild(row);
        });
    }

    showDeleteConfirmation(clientId) {
        const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
        const confirmBtn = document.getElementById('confirmModalYes');
        
        const handleConfirm = () => {
            this.deleteClient(clientId);
            modal.hide();
            confirmBtn.removeEventListener('click', handleConfirm);
        };
        
        confirmBtn.addEventListener('click', handleConfirm);
        modal.show();
    }

    deleteClient(clientId) {
        const index = this.clients.findIndex(c => c.id === clientId);
        if (index !== -1) {
            this.clients.splice(index, 1);
            localStorage.setItem('clients', JSON.stringify(this.clients));
            this.updateClientsList();
        }
    }

    async viewClient(clientId) {
        const client = this.clients.find(c => c.id === clientId);
        if (!client) return;

        document.getElementById('clientName').textContent = `${client.prenom} ${client.nom}`;
        document.getElementById('clientAddress').textContent = `${client.adresse}, ${client.codePostal} ${client.ville}`;
        
        this.detailsSection.style.display = 'block';
        
        // Initialiser la carte si nécessaire
        this.setupMap();
        
        try {
            const address = `${client.adresse}, ${client.codePostal} ${client.ville}`;
            console.log("Recherche des coordonnées pour:", address);
            
            // Obtenir les coordonnées via l'API de géocodage
            const coordinates = await getCoordinates(address);
            console.log("Coordonnées obtenues:", coordinates);
            
            if (coordinates && this.map) {
                // Mettre à jour la position de la carte
                this.map.setView([coordinates.latitude, coordinates.longitude], 13);
                
                // Mettre à jour ou créer le marqueur
                if (this.marker) {
                    this.marker.setLatLng([coordinates.latitude, coordinates.longitude]);
                } else {
                    this.marker = L.marker([coordinates.latitude, coordinates.longitude]).addTo(this.map);
                }
                
                // Mettre à jour la météo avec les coordonnées correctes
                const weatherData = await fetchWeather(coordinates.latitude, coordinates.longitude);
                const weatherContainer = document.getElementById('clientWeather');
                if (weatherContainer && weatherData) {
                    weatherContainer.innerHTML = formatWeatherDisplay(weatherData);
                }
            }
        } catch (error) {
            console.error('Erreur lors de l\'affichage du client:', error);
            document.getElementById('clientWeather').innerHTML = `
                <div class="card-body">
                    <p class="text-danger">Erreur lors de la récupération de la météo: ${error.message}</p>
                </div>
            `;
        }
    }

    editClient(clientId) {
        const client = this.clients.find(c => c.id === clientId);
        if (!client) return;

        this.currentClient = client;
        
        // Remplir le formulaire avec les données du client
        document.getElementById('clientNom').value = client.nom;
        document.getElementById('clientPrenom').value = client.prenom;
        document.getElementById('clientDateNaissance').value = client.dateNaissance;
        document.getElementById('clientAge').value = client.age;
        document.getElementById('clientAdresse').value = client.adresse;
        document.getElementById('clientVille').value = client.ville;
        document.getElementById('clientCodePostal').value = client.codePostal;
        document.getElementById('clientEmail').value = client.email;
        document.getElementById('clientTel').value = client.telephone;
        document.getElementById('clientRaisonSociale').value = client.raisonSociale || '';
        document.getElementById('clientCA').value = client.ca;
        document.getElementById('clientEmployes').value = client.employes;
    }

    clearForm() {
        this.form.reset();
        this.currentClient = null;
    }

    clearMap() {
        if (this.marker) {
            this.marker.remove();
            this.marker = null;
        }
        if (this.map) {
            this.map.remove();
            this.map = null;
            this.mapInitialized = false;
        }
    }
}

// Exporter l'instance
const clients = new Clients();
export default clients; 