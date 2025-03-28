// Classe pour gérer la géolocalisation et la météo
class LocationWeatherManager {
    constructor() {
        this.map = null;
        this.marker = null;
        this.weatherApiKey = 'YOUR_API_KEY'; // À remplacer par votre clé API OpenWeatherMap
        this.init();
    }

    init() {
        // Vérifier si la carte existe
        if (document.getElementById('map')) {
            this.initMap();
        }

        // Vérifier si le widget météo existe
        if (document.getElementById('weather')) {
            this.initWeather();
        }
    }

    // Initialiser la carte
    initMap() {
        // Créer la carte avec une vue par défaut sur Paris
        this.map = L.map('map').setView([48.8566, 2.3522], 13);

        // Ajouter la couche de tuiles OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(this.map);

        // Ajouter le contrôle de géolocalisation
        L.control.locate({
            position: 'topleft',
            strings: {
                title: 'Me localiser',
                popup: 'Vous êtes ici',
                outsideMapBoundsMsg: 'Vous êtes en dehors de la carte'
            },
            locateOptions: {
                maxZoom: 16
            }
        }).addTo(this.map);

        // Obtenir la position de l'utilisateur
        this.getUserLocation();
    }

    // Initialiser le widget météo
    initWeather() {
        this.getUserLocation().then(position => {
            this.updateWeather(position);
        }).catch(error => {
            console.error('Erreur de géolocalisation:', error);
            this.showWeatherError();
        });
    }

    // Obtenir la position de l'utilisateur
    getUserLocation() {
        return new Promise((resolve, reject) => {
            if (!navigator.geolocation) {
                reject(new Error('La géolocalisation n\'est pas supportée par votre navigateur'));
                return;
            }

            navigator.geolocation.getCurrentPosition(
                position => {
                    const { latitude, longitude } = position.coords;
                    resolve({ latitude, longitude });
                },
                error => {
                    reject(error);
                },
                {
                    enableHighAccuracy: true,
                    timeout: 5000,
                    maximumAge: 0
                }
            );
        });
    }

    // Mettre à jour la carte avec la position de l'utilisateur
    updateMap(position) {
        if (this.map) {
            const { latitude, longitude } = position;
            
            // Mettre à jour la vue de la carte
            this.map.setView([latitude, longitude], 13);

            // Supprimer le marqueur existant s'il y en a un
            if (this.marker) {
                this.map.removeLayer(this.marker);
            }

            // Ajouter un nouveau marqueur
            this.marker = L.marker([latitude, longitude])
                .addTo(this.map)
                .bindPopup('Votre position')
                .openPopup();
        }
    }

    // Mettre à jour le widget météo
    updateWeather(position) {
        const { latitude, longitude } = position;
        const weatherDiv = document.getElementById('weather');

        // Afficher un indicateur de chargement
        weatherDiv.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Chargement...</span>
            </div>
        `;

        // Appeler l'API OpenWeatherMap
        fetch(`https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=${this.weatherApiKey}&units=metric&lang=fr`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération des données météo');
                }
                return response.json();
            })
            .then(data => {
                // Mettre à jour l'affichage de la météo
                weatherDiv.innerHTML = `
                    <div class="weather-content">
                        <h3>${data.name}</h3>
                        <div class="weather-icon">
                            <img src="https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png" 
                                 alt="${data.weather[0].description}">
                        </div>
                        <p class="h2">${Math.round(data.main.temp)}°C</p>
                        <p class="weather-description">${data.weather[0].description}</p>
                        <div class="weather-details">
                            <p><i class="fas fa-tint"></i> Humidité: ${data.main.humidity}%</p>
                            <p><i class="fas fa-wind"></i> Vent: ${data.wind.speed} km/h</p>
                        </div>
                    </div>
                `;
            })
            .catch(error => {
                console.error('Erreur météo:', error);
                this.showWeatherError();
            });
    }

    // Afficher une erreur dans le widget météo
    showWeatherError() {
        const weatherDiv = document.getElementById('weather');
        weatherDiv.innerHTML = `
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i>
                Impossible de charger les données météo
            </div>
        `;
    }

    // Obtenir la distance entre deux points
    calculateDistance(lat1, lon1, lat2, lon2) {
        const R = 6371; // Rayon de la Terre en km
        const dLat = this.toRad(lat2 - lat1);
        const dLon = this.toRad(lon2 - lon1);
        const a = 
            Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(this.toRad(lat1)) * Math.cos(this.toRad(lat2)) * 
            Math.sin(dLon/2) * Math.sin(dLon/2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    // Convertir les degrés en radians
    toRad(degrees) {
        return degrees * (Math.PI / 180);
    }
}

// Initialiser le gestionnaire de géolocalisation et météo
const locationWeatherManager = new LocationWeatherManager();

// Exporter pour utilisation dans d'autres fichiers
window.locationWeatherManager = locationWeatherManager; 