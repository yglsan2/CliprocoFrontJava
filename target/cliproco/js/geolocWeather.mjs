// Configuration de la carte Leaflet
let map;
let marker;

// Fonction pour initialiser la carte
export function initMap(containerId, lat, lon) {
    if (map) {
        map.remove();
    }
    
    map = L.map(containerId).setView([lat, lon], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    marker = L.marker([lat, lon]).addTo(map);
}

// Fonction pour obtenir les coordonnées à partir d'une adresse
export async function getCoordinates(address, postalCode) {
    try {
        const response = await fetch(`https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(address)}&postcode=${postalCode}&limit=1`);
        const data = await response.json();
        
        if (data.features && data.features.length > 0) {
            const coordinates = data.features[0].geometry.coordinates;
            return {
                lat: coordinates[1],
                lon: coordinates[0]
            };
        }
        throw new Error('Adresse non trouvée');
    } catch (error) {
        console.error('Erreur lors de la géolocalisation:', error);
        throw error;
    }
}

// Fonction pour obtenir la météo
export async function getWeather(lat, lon) {
    try {
        const response = await fetch(`https://api.infoclimat.fr/observations/realtime?lat=${lat}&lon=${lon}&key=${API_KEY}`);
        const data = await response.json();
        
        if (data && data.current) {
            return {
                temperature: data.current.temperature,
                humidity: data.current.humidity,
                description: data.current.description
            };
        }
        throw new Error('Données météo non disponibles');
    } catch (error) {
        console.error('Erreur lors de la récupération de la météo:', error);
        throw error;
    }
}

// Fonction pour mettre à jour l'affichage des informations
export function updateLocationInfo(containerId, address, weather) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `
            <p><strong>Adresse :</strong> ${address}</p>
            <p><strong>Météo :</strong> ${weather.temperature}°C - ${weather.description}</p>
            <p><strong>Humidité :</strong> ${weather.humidity}%</p>
        `;
    }
}

// Fonction pour gérer la géolocalisation et la météo d'un contact
export async function handleLocationAndWeather(address, postalCode, containerId, mapId) {
    try {
        const coordinates = await getCoordinates(address, postalCode);
        const weather = await getWeather(coordinates.lat, coordinates.lon);
        
        initMap(mapId, coordinates.lat, coordinates.lon);
        updateLocationInfo(containerId, `${address}, ${postalCode}`, weather);
        
        return { coordinates, weather };
    } catch (error) {
        console.error('Erreur lors de la récupération des informations:', error);
        throw error;
    }
} 