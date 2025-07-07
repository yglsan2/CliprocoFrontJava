import { getCoordinates, fetchWeather, formatWeatherDisplay } from './geolocWeather.mjs';

class Prospects {
    constructor() {
        // Limiter le nombre de prospects en mémoire
        this.maxProspects = 100;
        this.prospects = this.loadProspects();
        this.currentProspect = null;
        this.map = null;
        this.marker = null;
        this.weatherCache = new Map(); // Cache pour les données météo
        this.coordinatesCache = new Map(); // Cache pour les coordonnées

        // Références aux éléments DOM
        this.form = document.getElementById('prospectForm');
        if (!this.form) return;
        this.tableElement = document.getElementById('prospectsTable')?.querySelector('tbody');
        if (!this.tableElement) return;
        this.detailsSection = document.getElementById('prospectDetails');
        if (!this.detailsSection) return;

        // Initialisation différée de la carte
        this.mapInitialized = false;
        this.setupEventListeners();
        this.updateProspectsList();
    }

    loadProspects() {
        const prospects = JSON.parse(localStorage.getItem('prospects')) || [];
        // Garder uniquement les 100 derniers prospects
        return prospects.slice(-this.maxProspects);
    }

    setupMap() {
        if (!this.mapInitialized && window.L) {
            try {
                // Configuration des icônes Leaflet pour corriger les erreurs 404
                L.Icon.Default.imagePath = '/CliprocoJEE/img/';
                
                this.map = L.map('prospectMap').setView([46.603354, 1.888334], 5);
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
                await this.saveProspect();
            }
        });

        // Écouteur pour la gestion de l'état d'authentification
        document.addEventListener('authStateChanged', (e) => {
            if (!e.detail.isAuthenticated) {
                this.clearForm();
                this.clearMap();
                this.prospects = [];
                this.updateProspectsList();
            }
        });
    }

    validateForm() {
        // Vérification des champs obligatoires
        const requiredFields = ['nom', 'prenom', 'raisonSociale', 'telephone', 'mail'];
        for (const fieldId of requiredFields) {
            const field = document.getElementById(fieldId);
            if (!field || !field.value.trim()) {
                alert('Tous les champs sont obligatoires');
                return false;
            }
        }

        // Validation email
        const email = document.getElementById('mail')?.value;
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            alert('Veuillez entrer une adresse email valide');
            return false;
        }

        // Validation téléphone
        const telephone = document.getElementById('telephone')?.value;
        if (telephone && !/^\d{10}$/.test(telephone)) {
            alert('Veuillez entrer un numéro de téléphone valide (10 chiffres)');
            return false;
        }

        return true;
    }

    async saveProspect() {
        try {
            const prospect = {
                id: this.currentProspect ? this.currentProspect.id : Date.now(),
                nom: document.getElementById('nom')?.value,
                prenom: document.getElementById('prenom')?.value,
                raisonSociale: document.getElementById('raisonSociale')?.value,
                telephone: document.getElementById('telephone')?.value,
                mail: document.getElementById('mail')?.value,
                commentaires: document.getElementById('commentaires')?.value || '',
                numeroRue: document.getElementById('numeroRue')?.value,
                nomRue: document.getElementById('nomRue')?.value,
                codePostal: document.getElementById('codePostal')?.value,
                ville: document.getElementById('ville')?.value
            };

            // Vérifier que tous les champs requis sont remplis
            if (!this.validateProspectData(prospect)) {
                throw new Error('Tous les champs requis doivent être remplis');
            }

            // Récupérer les coordonnées
            const address = `${prospect.numeroRue} ${prospect.nomRue}, ${prospect.codePostal} ${prospect.ville}`;
            const coordinates = await this.getCoordinates(prospect.codePostal, prospect.ville);
            prospect.coordinates = coordinates;

            if (this.currentProspect) {
                const index = this.prospects.findIndex(p => p.id === this.currentProspect.id);
                if (index !== -1) {
                    this.prospects[index] = prospect;
                }
            } else {
                this.prospects.push(prospect);
                // Garder uniquement les 100 derniers prospects
                if (this.prospects.length > this.maxProspects) {
                    this.prospects = this.prospects.slice(-this.maxProspects);
                }
            }

            localStorage.setItem('prospects', JSON.stringify(this.prospects));
            this.updateProspectsList();
            this.clearForm();
            this.currentProspect = null;

            console.log('Prospect sauvegardé avec succès:', prospect);
        } catch (error) {
            console.error('Erreur lors de la sauvegarde du prospect:', error);
            alert('Erreur lors de la sauvegarde du prospect. Vérifiez l\'adresse et réessayez.');
        }
    }

    validateProspectData(prospect) {
        const requiredFields = ['nom', 'prenom', 'raisonSociale', 'telephone', 'mail'];
        return requiredFields.every(field => prospect[field] && prospect[field].toString().trim() !== '');
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
        const weatherContainer = document.getElementById('prospectWeather');
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

    updateProspectsList() {
        if (!this.tableElement) return;
        
        this.tableElement.innerHTML = '';
        this.prospects.forEach(prospect => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${prospect.nom || ''}</td>
                <td>${prospect.prenom || ''}</td>
                <td>${prospect.mail || ''}</td>
                <td>${prospect.telephone || ''}</td>
                <td>${prospect.numeroRue || ''} ${prospect.nomRue || ''}, ${prospect.codePostal || ''} ${prospect.ville || ''}</td>
                <td>${prospect.commentaires || ''}</td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="prospects.viewProspect(${prospect.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-warning btn-sm" onclick="prospects.editProspect(${prospect.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="prospects.showDeleteConfirmation(${prospect.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            `;
            this.tableElement.appendChild(row);
        });
    }

    showDeleteConfirmation(prospectId) {
        const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
        const confirmBtn = document.getElementById('confirmModalYes');
        
        const handleConfirm = () => {
            this.deleteProspect(prospectId);
            modal.hide();
            confirmBtn.removeEventListener('click', handleConfirm);
        };
        
        confirmBtn.addEventListener('click', handleConfirm);
        modal.show();
    }

    deleteProspect(prospectId) {
        const index = this.prospects.findIndex(p => p.id === prospectId);
        if (index !== -1) {
            this.prospects.splice(index, 1);
            localStorage.setItem('prospects', JSON.stringify(this.prospects));
            this.updateProspectsList();
        }
    }

    async viewProspect(prospectId) {
        const prospect = this.prospects.find(p => p.id === prospectId);
        if (!prospect) return;

        document.getElementById('prospectName').textContent = `${prospect.prenom} ${prospect.nom}`;
        document.getElementById('prospectAddress').textContent = `${prospect.numeroRue} ${prospect.nomRue}, ${prospect.codePostal} ${prospect.ville}`;
        
        this.detailsSection.style.display = 'block';
        
        // Initialiser la carte si nécessaire
        this.setupMap();
        
        try {
            const address = `${prospect.numeroRue} ${prospect.nomRue}, ${prospect.codePostal} ${prospect.ville}`;
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
                const weatherContainer = document.getElementById('prospectWeather');
                if (weatherContainer && weatherData) {
                    weatherContainer.innerHTML = formatWeatherDisplay(weatherData);
                }
            }
        } catch (error) {
            console.error('Erreur lors de l\'affichage du prospect:', error);
            document.getElementById('prospectWeather').innerHTML = `
                <div class="card-body">
                    <p class="text-danger">Erreur lors de la récupération de la météo: ${error.message}</p>
                </div>
            `;
        }
    }

    editProspect(prospectId) {
        const prospect = this.prospects.find(p => p.id === prospectId);
        if (!prospect) return;

        this.currentProspect = prospect;
        
        // Remplir le formulaire avec les données du prospect
        document.getElementById('nom').value = prospect.nom;
        document.getElementById('prenom').value = prospect.prenom;
        document.getElementById('raisonSociale').value = prospect.raisonSociale;
        document.getElementById('telephone').value = prospect.telephone;
        document.getElementById('mail').value = prospect.mail;
        document.getElementById('commentaires').value = prospect.commentaires || '';
        document.getElementById('numeroRue').value = prospect.numeroRue;
        document.getElementById('nomRue').value = prospect.nomRue;
        document.getElementById('codePostal').value = prospect.codePostal;
        document.getElementById('ville').value = prospect.ville;
    }

    clearForm() {
        this.form.reset();
        this.currentProspect = null;
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
const prospects = new Prospects();
export default prospects; 
document.addEventListener('DOMContentLoaded', () => {
    try {
        new Prospects();
    } catch (e) {
        console.error('Erreur lors de l\'initialisation des prospects :', e);
    }
});
