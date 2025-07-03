<%--@elvariable id="client" type="models.Client"--%>
<%--@elvariable id="titlePage" type="String"--%>
<%--@elvariable id="violations" type="Set<ConstraintViolation<Client>>"--%>
<%--
  Created by IntelliJ IDEA.
  User: CDA-01
  Date: 18/03/2025
  Time: 09:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <c:import url="../meta.jsp" />
    <title>Détails du client - ${client.raisonSociale}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        #map {
            height: 400px;
            width: 100%;
            border-radius: 8px;
            margin: 20px 0;
        }
        .weather-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 12px;
            padding: 20px;
            margin: 20px 0;
        }
        .client-info {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin: 15px 0;
        }
        .info-label {
            font-weight: bold;
            color: #495057;
        }
        .info-value {
            color: #212529;
        }
    </style>
</head>
<body>
<c:import url="../header.jsp" />
<main class="container mt-5">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="fas fa-building"></i> ${client.raisonSociale}</h1>
                <a href="${pageContext.request.contextPath}/app?cmd=clients.liste" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Retour à la liste
                </a>
            </div>
        </div>
    </div>

    <div class="row">
        <!-- Informations du client -->
        <div class="col-md-6">
            <div class="client-info">
                <h3><i class="fas fa-info-circle"></i> Informations générales</h3>
                <div class="row">
                    <div class="col-6">
                        <p><span class="info-label">Raison sociale :</span></p>
                        <p><span class="info-label">Téléphone :</span></p>
                        <p><span class="info-label">Email :</span></p>
                        <p><span class="info-label">Chiffre d'affaires :</span></p>
                        <p><span class="info-label">Nombre d'employés :</span></p>
                    </div>
                    <div class="col-6">
                        <p class="info-value">${client.raisonSociale}</p>
                        <p class="info-value">${client.telephone}</p>
                        <p class="info-value">${client.mail}</p>
                        <p class="info-value">${client.chiffreAffaire} €</p>
                        <p class="info-value">${client.nbrEmploye}</p>
                    </div>
                </div>
                <c:if test="${not empty client.commentaires}">
                    <div class="mt-3">
                        <p><span class="info-label">Commentaires :</span></p>
                        <p class="info-value">${client.commentaires}</p>
                    </div>
                </c:if>
            </div>

            <!-- Adresse -->
            <div class="client-info">
                <h3><i class="fas fa-map-marker-alt"></i> Adresse</h3>
                <p class="info-value">
                    ${client.adresse.numeroRue} ${client.adresse.nomRue}<br>
                    ${client.adresse.codePostal} ${client.adresse.ville}
                </p>
            </div>
        </div>

        <!-- Météo -->
        <div class="col-md-6">
            <div class="weather-card">
                <h3><i class="fas fa-cloud-sun"></i> Météo locale</h3>
                <div id="weather-info">
                    <div class="text-center">
                        <i class="fas fa-spinner fa-spin fa-2x"></i>
                        <p>Chargement de la météo...</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Carte -->
    <div class="row">
        <div class="col-12">
            <div class="client-info">
                <h3><i class="fas fa-map"></i> Géolocalisation</h3>
                <div id="map"></div>
            </div>
        </div>
    </div>

    <!-- Actions -->
    <div class="row mt-4">
        <div class="col-12 text-center">
            <a href="${pageContext.request.contextPath}/app?cmd=clients.update&id=${client.identifiant}" 
               class="btn btn-edit btn-lg me-3">
                <i class="fas fa-edit"></i> Modifier
            </a>
            <a href="${pageContext.request.contextPath}/app?cmd=clients.delete&id=${client.identifiant}" 
               class="btn btn-delete btn-lg"
               onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce client ?')">
                <i class="fas fa-trash"></i> Supprimer
            </a>
        </div>
    </div>
</main>
<c:import url="../footer.jsp" />
<c:import url="../scripts.jsp" />
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
<script src="${pageContext.request.contextPath}/js/geolocWeather.mjs" type="module"></script>
<script>
    // Données du client
    const clientAddress = '${client.adresse.numeroRue} ${client.adresse.nomRue}, ${client.adresse.codePostal} ${client.adresse.ville}';
    
    // Initialisation de la carte
    let map = L.map('map').setView([48.6844, 6.1844], 10); // Centre sur Nancy
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Fonction pour charger les données de géolocalisation et météo
    async function loadLocationAndWeather() {
        try {
            // Récupérer les coordonnées via l'API interne
            const response = await fetch(`https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(clientAddress)}`);
            const data = await response.json();
            
            if (data.features && data.features.length > 0) {
                const [longitude, latitude] = data.features[0].geometry.coordinates;
                
                // Centrer la carte sur l'adresse
                map.setView([latitude, longitude], 15);
                
                // Ajouter un marqueur
                L.marker([latitude, longitude])
                    .addTo(map)
                    .bindPopup(`<b>${clientAddress}</b>`)
                    .openPopup();
                
                // Récupérer la météo via l'API interne
                const weatherResponse = await fetch(`https://www.infoclimat.fr/public-api/gfs/json?_ll=${latitude},${longitude}&_auth=BR9XQA5wVHZUeQYxBXMBKAdvBzIBdwgvC3cGZVw5VisAa1Y3UTEDZVc5A34ALwUzWXQObQ02CDhXPApyAHJVNAVvVzsOZVQzVDsGYwUqASoHPQdiATkIMwttBn5cLlY0AGtWOlEsA2NXOwNgAC4FN1lvDnANMwgwVzwKcgByVTcFYVc3DmxUMFQ6BmcFPAE1BzAHeAEhCDYLbwYyXGRWYAAxVjdRMgM1VzoDaQA3BWBZbQ5wDTAIOFcwCm8AalU%2BBWJXNg5yVClUQgYXBSgBdQd2BzIBeAgtCz0GP1xl&_c=bf067ab20c209610ec05e4f052b94397`);
                const weatherData = await weatherResponse.json();
                
                if (weatherData && Object.keys(weatherData).length > 0) {
                    const firstTimestamp = Object.keys(weatherData).sort()[0];
                    const forecast = weatherData[firstTimestamp];
                    
                    const temp = forecast?.temperature?.['2m'] ? (forecast.temperature['2m'] - 273.15).toFixed(1) : 'N/A';
                    const humidite = forecast?.humidite?.['2m'] || 'N/A';
                    const vent = forecast?.vent_moyen?.['10m'] || 'N/A';
                    
                    let description = 'Information non disponible';
                    if (forecast?.pluie > 0) {
                        description = forecast.pluie_convective > 0 ? 'Averses' : 'Pluvieux';
                    } else if (forecast?.nebulosite?.totale > 80) {
                        description = 'Très nuageux';
                    } else if (forecast?.nebulosite?.totale > 50) {
                        description = 'Nuageux';
                    } else if (forecast?.nebulosite?.totale > 20) {
                        description = 'Partiellement nuageux';
                    } else if (temp !== 'N/A') {
                        const tempNum = parseFloat(temp);
                        if (tempNum < 0) description = 'Glacial';
                        else if (tempNum < 10) description = 'Froid';
                        else if (tempNum < 20) description = 'Tempéré';
                        else if (tempNum < 25) description = 'Doux';
                        else if (tempNum < 30) description = 'Chaud';
                        else description = 'Très chaud';
                    }
                    
                    const weatherHtml = `
                        <div class="card-body">
                            <h5 class="card-title">Météo actuelle</h5>
                            <p><i class="fas fa-thermometer-half"></i> Température: ${temp}°C</p>
                            <p><i class="fas fa-tint"></i> Humidité: ${humidite}%</p>
                            <p><i class="fas fa-wind"></i> Vent: ${vent} km/h</p>
                            <p><i class="fas fa-cloud"></i> Conditions: ${description}</p>
                        </div>
                    `;
                    
                    document.getElementById('weather-info').innerHTML = weatherHtml;
                } else {
                    document.getElementById('weather-info').innerHTML = 
                        '<div class="text-center"><i class="fas fa-exclamation-triangle"></i><p>Météo non disponible</p></div>';
                }
            } else {
                document.getElementById('weather-info').innerHTML = 
                    '<div class="text-center"><i class="fas fa-exclamation-triangle"></i><p>Adresse non trouvée</p></div>';
            }
        } catch (error) {
            console.error('Erreur:', error);
            document.getElementById('weather-info').innerHTML = 
                '<div class="text-center"><i class="fas fa-exclamation-triangle"></i><p>Erreur de chargement</p></div>';
        }
    }

    // Initialisation
    document.addEventListener('DOMContentLoaded', function() {
        loadLocationAndWeather();
    });
</script>
</body>
</html>