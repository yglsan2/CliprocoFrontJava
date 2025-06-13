import config from './config.mjs';

// Cache pour stocker les résultats des API
const cache = {
    coordinates: new Map(),
    weather: new Map()
};

// Durée de validité du cache météo (3 heures en millisecondes)
const WEATHER_CACHE_DURATION = 3 * 60 * 60 * 1000;

export async function getCoordinates(address) {
    if (!address) {
        console.error("Adresse manquante");
        return null;
    }

    try {
        // Vérifier le cache
        if (cache.coordinates.has(address)) {
            console.log("Coordonnées trouvées dans le cache");
            return cache.coordinates.get(address);
        }

        console.log("Recherche des coordonnées pour:", address);
        const url = `https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(address)}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        const data = await response.json();
        console.log("Réponse API géocodage:", data);
        
        if (data.features && data.features.length > 0) {
            const [longitude, latitude] = data.features[0].geometry.coordinates;
            const coordinates = { latitude, longitude };
            
            console.log("Coordonnées trouvées:", coordinates);
            cache.coordinates.set(address, coordinates);
            
            return coordinates;
        }
        
        console.error("Aucune coordonnée trouvée pour cette adresse");
        return null;
    } catch (error) {
        console.error("Erreur lors de la récupération des coordonnées:", error);
        return null;
    }
}

export async function fetchWeather(latitude, longitude) {
    if (!latitude || !longitude) {
        console.error("Latitude ou longitude manquante");
        return null;
    }

    const cacheKey = `${latitude},${longitude}`;
    const now = Date.now();

    try {
        // Vérifier le cache
        if (cache.weather.has(cacheKey)) {
            const cachedData = cache.weather.get(cacheKey);
            if (now - cachedData.timestamp < WEATHER_CACHE_DURATION) {
                console.log("Données météo trouvées dans le cache");
                return cachedData.data;
            }
        }

        console.log("Récupération des données météo pour:", { latitude, longitude });
        const url = `https://www.infoclimat.fr/public-api/gfs/json?_ll=${latitude},${longitude}&_auth=${config.infoclimat.auth}&_c=${config.infoclimat.c}`;
        
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        const data = await response.json();
        console.log("Réponse API météo:", data);
        
        if (typeof data === 'object' && Object.keys(data).length > 0) {
            const firstTimestamp = Object.keys(data).sort()[0];
            const forecast = data[firstTimestamp];
            
            const weatherData = {
                temperature: (forecast?.temperature?.['2m'] - 273.15).toFixed(1) ?? 'N/A',  // Conversion de Kelvin en Celsius
                humidite: forecast?.humidite?.['2m'] ?? 'N/A',
                vent_moyen: forecast?.vent_moyen?.['10m'] ?? 'N/A',
                description: getWeatherDescription(forecast)
            };

            console.log("Données météo formatées:", weatherData);
            
            cache.weather.set(cacheKey, {
                data: weatherData,
                timestamp: now
            });

            return weatherData;
        }
        
        console.error("Aucune donnée météo disponible");
        return null;
    } catch (error) {
        console.error("Erreur lors de la récupération des données météo:", error);
        return null;
    }
}

function getWeatherDescription(forecast) {
    if (!forecast) return 'Information non disponible';
    
    // Conversion de Kelvin en Celsius
    const temp = forecast.temperature?.['2m'] ? (forecast.temperature['2m'] - 273.15) : undefined;
    
    // Vérification de la pluie
    if (forecast.pluie > 0) {
        if (forecast.pluie_convective > 0) {
            return 'Averses';
        }
        return 'Pluvieux';
    }
    
    // Vérification de la nébulosité
    const nebulosite = forecast.nebulosite?.totale ?? 0;
    if (nebulosite > 80) {
        return 'Très nuageux';
    } else if (nebulosite > 50) {
        return 'Nuageux';
    } else if (nebulosite > 20) {
        return 'Partiellement nuageux';
    }
    
    // Description basée sur la température
    if (temp === undefined) return 'Température non disponible';
    
    if (temp < 0) return 'Glacial';
    if (temp < 10) return 'Froid';
    if (temp < 20) return 'Tempéré';
    if (temp < 25) return 'Doux';
    if (temp < 30) return 'Chaud';
    return 'Très chaud';
}

export function formatWeatherDisplay(weatherData) {
    if (!weatherData) {
        return `
            <div class="card-body">
                <h5 class="card-title">Météo</h5>
                <p>Données météorologiques non disponibles</p>
            </div>
        `;
    }

    return `
        <div class="card-body">
            <h5 class="card-title">Météo actuelle</h5>
            <p><i class="fas fa-thermometer-half"></i> Température: ${weatherData.temperature}°C</p>
            <p><i class="fas fa-tint"></i> Humidité: ${weatherData.humidite}%</p>
            <p><i class="fas fa-wind"></i> Vent: ${weatherData.vent_moyen} km/h</p>
            <p><i class="fas fa-cloud"></i> Conditions: ${weatherData.description}</p>
        </div>
    `;
}

// Nettoyage automatique du cache
setInterval(() => {
    const now = Date.now();
    
    for (const [key, value] of cache.weather.entries()) {
        if (now - value.timestamp > WEATHER_CACHE_DURATION) {
            cache.weather.delete(key);
        }
    }

    if (cache.coordinates.size > 100) {
        const keysToDelete = Array.from(cache.coordinates.keys()).slice(0, 50);
        keysToDelete.forEach(key => cache.coordinates.delete(key));
    }
}, WEATHER_CACHE_DURATION);

// Fonction de test pour la météo
export async function testWeather() {
    try {
        console.log("Test de la météo...");
        const coordinates = await getCoordinates("1 rue de la Paix, 75002 Paris");
        if (coordinates) {
            console.log("Coordonnées trouvées:", coordinates);
            const weather = await fetchWeather(coordinates.latitude, coordinates.longitude);
            console.log("Données météo:", weather);
            const display = formatWeatherDisplay(weather);
            console.log("Affichage HTML:", display);
            document.getElementById('weatherTest').innerHTML = display;
        }
    } catch (error) {
        console.error("Erreur lors du test météo:", error);
    }
}

// Ajouter la fonction au contexte global pour le test
if (typeof window !== 'undefined') {
    window.testWeather = testWeather;
} 