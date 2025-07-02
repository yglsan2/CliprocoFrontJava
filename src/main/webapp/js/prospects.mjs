import { getCoordinates, fetchWeather, formatWeatherDisplay } from './geolocWeather.mjs';

class Prospects {
    constructor() {
        // Limiter le nombre de prospects en mémoire
        this.maxProspects = 100;
        this.prospects = this.loadProspects();
        this.currentProspect = null;
        this.map = null;
        this.marker = null;

        // Références aux éléments DOM
        const el = document.getElementById('prospectForm'); if (!el) return;
        this.form = el;
        el.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (this.validateForm()) {
                await this.saveProspect();
            }
        });

        el = document.getElementById('prospectTable').querySelector('tbody'); if (!el) return;
        this.tableElement = el;

        el = document.getElementById('prospectDetails'); if (!el) return;
        this.detailsSection = el;

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
                const el = document.getElementById('prospectMap'); if (!el) return;
                this.map = L.map(el).setView([46.603354, 1.888334], 5);
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
        const requiredFields = ['nom', 'prenom', 'adresse', 'codePostal', 'ville', 'pays', 'email', 'telephone', 'interet', 'source'];
        for (const fieldId of requiredFields) {
            const field = document.getElementById(fieldId);
            if (!field || !field.value.trim()) {
                alert('Tous les champs sont obligatoires');
                return false;
            }
        }

        // Validation email
        const email = document.getElementById('email')?.value;
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

        // Validation code postal
        const codePostal = document.getElementById('codePostal')?.value;
        if (codePostal && !/^\d{5}$/.test(codePostal)) {
            alert('Veuillez entrer un code postal valide (5 chiffres)');
            return false;
        }

        return true;
    }

    async saveProspect() {
        const prospect = {
            id: this.currentProspect ? this.currentProspect.id : Date.now(),
            nom: document.getElementById('nom')?.value,
            prenom: document.getElementById('prenom')?.value,
            adresse: document.getElementById('adresse')?.value,
            ville: document.getElementById('ville')?.value,
            codePostal: document.getElementById('codePostal')?.value,
            pays: document.getElementById('pays')?.value,
            email: document.getElementById('email')?.value,
            telephone: document.getElementById('telephone')?.value,
            interet: document.getElementById('interet')?.value,
            source: document.getElementById('source')?.value
        };

        try {
            const address = `${prospect.adresse}, ${prospect.codePostal} ${prospect.ville}`;
            prospect.coordinates = await getCoordinates(address);

            if (this.currentProspect) {
                const index = this.prospects.findIndex(p => p.id === this.currentProspect.id);
                this.prospects[index] = prospect;
            } else {
                this.prospects.push(prospect);
            }

            localStorage.setItem('prospects', JSON.stringify(this.prospects));
            this.updateProspectsList();
            this.clearForm();
            this.currentProspect = null;
        } catch (error) {
            console.error('Erreur lors de la sauvegarde du prospect:', error);
            alert('Erreur lors de la sauvegarde du prospect. Vérifiez l\'adresse.');
        }
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

    updateProspectsList() {
        this.tableElement.innerHTML = '';
        this.prospects.forEach(prospect => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${prospect.nom}</td>
                <td>${prospect.prenom}</td>
                <td>${prospect.dateNaissance}</td>
                <td>${prospect.adresse}</td>
                <td>${prospect.ville}</td>
                <td>${prospect.codePostal}</td>
                <td>${prospect.email}</td>
                <td>${prospect.telephone}</td>
                <td>${prospect.raisonSociale || '-'}</td>
                <td>${prospect.dateProspection}</td>
                <td>${prospect.interesse}</td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="prospects.showProspect(${prospect.id})">
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

    async showProspect(id) {
        const prospect = this.prospects.find(p => p.id === id);
        if (!prospect) return;

        document.getElementById('prospectName').textContent = `${prospect.prenom} ${prospect.nom}`;
        document.getElementById('prospectAddress').textContent = `${prospect.adresse}, ${prospect.codePostal} ${prospect.ville}`;
        
        this.detailsSection.style.display = 'block';
        
        // Initialiser la carte si nécessaire
        this.setupMap();
        
        try {
            const address = `${prospect.adresse}, ${prospect.codePostal} ${prospect.ville}`;
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

    editProspect(id) {
        const prospect = this.prospects.find(p => p.id === id);
        if (!prospect) return;

        this.currentProspect = prospect;
        
        // Remplir le formulaire avec les données du prospect
        document.getElementById('prospectNom').value = prospect.nom;
        document.getElementById('prospectPrenom').value = prospect.prenom;
        document.getElementById('prospectDateNaissance').value = prospect.dateNaissance;
        document.getElementById('prospectAdresse').value = prospect.adresse;
        document.getElementById('prospectVille').value = prospect.ville;
        document.getElementById('prospectCodePostal').value = prospect.codePostal;
        document.getElementById('prospectEmail').value = prospect.email;
        document.getElementById('prospectTel').value = prospect.telephone;
        document.getElementById('prospectRaisonSociale').value = prospect.raisonSociale || '';
        document.getElementById('prospectDateProspection').value = prospect.dateProspection;
        document.getElementById('prospectInteresse').value = prospect.interesse;
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

// Rendre l'instance accessible globalement
window.prospects = prospects;

export default prospects;

document.addEventListener('DOMContentLoaded', () => {
    try {
        new Prospects();
    } catch (e) {
        console.error('Erreur lors de l\'initialisation des prospects :', e);
    }
});
