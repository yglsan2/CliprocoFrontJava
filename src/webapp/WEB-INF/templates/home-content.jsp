<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-body">
                <h1 class="card-title">Bienvenue sur CliProCo</h1>
                <p class="card-text">
                    CliProCo est votre solution de gestion des clients et prospects. 
                    Gérez efficacement vos relations commerciales et suivez vos opportunités d'affaires.
                </p>
                <div class="row mt-4">
                    <div class="col-md-6">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title">Gestion des Clients</h5>
                                <p class="card-text">
                                    Gérez vos clients existants, suivez leurs informations et leurs interactions.
                                </p>
                                <a href="${pageContext.request.contextPath}/clients" class="btn btn-primary">Voir les clients</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title">Gestion des Prospects</h5>
                                <p class="card-text">
                                    Suivez vos prospects et convertissez-les en clients.
                                </p>
                                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-primary">Voir les prospects</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Météo</h5>
                <div id="weather-widget" class="text-center">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Chargement...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Appel à l'API météo
    fetch('${pageContext.request.contextPath}/api/meteo')
        .then(response => response.json())
        .then(data => {
            const weatherWidget = document.getElementById('weather-widget');
            weatherWidget.innerHTML = `
                <div class="weather-info">
                    <h3>${data.temperature}°C</h3>
                    <p>${data.description}</p>
                    <img src="${data.iconUrl}" alt="Météo" class="weather-icon">
                </div>
            `;
        })
        .catch(error => {
            console.error('Erreur lors du chargement de la météo:', error);
            document.getElementById('weather-widget').innerHTML = 
                '<p class="text-danger">Impossible de charger la météo</p>';
        });
});
</script> 