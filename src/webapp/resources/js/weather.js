class WeatherWidget {
    constructor(apiKey, city = 'Paris') {
        this.apiKey = apiKey;
        this.city = city;
        this.container = document.getElementById('weather-widget');
        this.init();
    }

    async init() {
        try {
            const weather = await this.fetchWeather();
            this.displayWeather(weather);
        } catch (error) {
            console.error('Erreur lors de la récupération de la météo:', error);
            this.displayError();
        }
    }

    async fetchWeather() {
        const response = await fetch(
            `https://api.openweathermap.org/data/2.5/weather?q=${this.city}&appid=${this.apiKey}&units=metric&lang=fr`
        );
        
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des données météo');
        }
        
        return await response.json();
    }

    displayWeather(data) {
        const temperature = Math.round(data.main.temp);
        const description = data.weather[0].description;
        const icon = data.weather[0].icon;
        const humidity = data.main.humidity;
        const windSpeed = data.wind.speed;

        this.container.innerHTML = `
            <div class="weather-card">
                <div class="weather-header">
                    <h3>${this.city}</h3>
                    <img src="https://openweathermap.org/img/wn/${icon}@2x.png" alt="${description}">
                </div>
                <div class="weather-info">
                    <p class="temperature">${temperature}°C</p>
                    <p class="description">${description}</p>
                    <div class="weather-details">
                        <p>Humidité: ${humidity}%</p>
                        <p>Vent: ${windSpeed} km/h</p>
                    </div>
                </div>
            </div>
        `;
    }

    displayError() {
        this.container.innerHTML = `
            <div class="weather-error">
                <p>Impossible de charger les informations météo</p>
            </div>
        `;
    }
}

// Initialisation du widget
document.addEventListener('DOMContentLoaded', () => {
    const weatherWidget = new WeatherWidget('VOTRE_API_KEY');
}); 