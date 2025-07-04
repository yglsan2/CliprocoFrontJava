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
    <jsp:include page="../meta.jsp"/>
    <title>Détail du client</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main>
    <article>
        <header><h1>Détail du client</h1></header>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty client}">
            <div class="container">
                <div class="row">
                    <!-- Informations du client -->
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h3>Informations du client</h3>
                            </div>
                            <div class="card-body">
                                <table class="table table-borderless">
                                    <tr>
                                        <th>ID :</th>
                                        <td>${client.identifiant}</td>
                                    </tr>
                                    <tr>
                                        <th>Raison sociale :</th>
                                        <td>${client.raisonSociale}</td>
                                    </tr>
                                    <tr>
                                        <th>Téléphone :</th>
                                        <td>${client.telephone}</td>
                                    </tr>
                                    <tr>
                                        <th>Email :</th>
                                        <td>${client.mail}</td>
                                    </tr>
                                    <tr>
                                        <th>Chiffre d'affaires :</th>
                                        <td>${client.chiffreAffaires} €</td>
                                    </tr>
                                    <tr>
                                        <th>Nombre d'employés :</th>
                                        <td>${client.nbEmployes}</td>
                                    </tr>
                                    <tr>
                                        <th>Commentaires :</th>
                                        <td>${client.commentaires}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Adresse et géolocalisation -->
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h3>Adresse</h3>
                            </div>
                            <div class="card-body">
                                <p>
                                    ${client.adresse.numeroRue} ${client.adresse.nomRue}<br>
                                    ${client.adresse.codePostal} ${client.adresse.ville}
                                </p>
                                
                                <!-- Carte de géolocalisation -->
                                <div id="map" style="height: 300px; width: 100%; margin-top: 20px;"></div>
                                
                                <!-- Météo -->
                                <div id="weather" class="mt-3">
                                    <h4>Météo locale</h4>
                                    <div id="weather-info">Chargement...</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Boutons d'action -->
                <div class="row mt-4">
                    <div class="col-12">
                        <a href="${pageContext.request.contextPath}/app?cmd=clients.update&id=${client.identifiant}" class="btn btn-warning">
                            <i class="fas fa-edit"></i> Modifier
                        </a>
                        <a href="${pageContext.request.contextPath}/app?cmd=clients.liste" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Retour à la liste
                        </a>
                        <a href="${pageContext.request.contextPath}/app?cmd=clients.delete&id=${client.identifiant}" 
                           class="btn btn-danger float-end"
                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce client ?')">
                            <i class="fas fa-trash"></i> Supprimer
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </article>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
<script src="${pageContext.request.contextPath}/js/geolocWeather.mjs" type="module"></script>

<script>
let map; // Variable globale pour la carte

document.addEventListener('DOMContentLoaded', function() {
    // Initialisation de la carte
    map = L.map('map').setView([48.8566, 2.3522], 13);
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);
    
    // Utilisation du module de géolocalisation existant
    const address = '${client.adresse.numeroRue} ${client.adresse.nomRue}, ${client.adresse.codePostal} ${client.adresse.ville}';
    
    // Charger les coordonnées et la météo via le module existant
    loadLocationAndWeather(address);
});

async function loadLocationAndWeather(address) {
    try {
        // Import du module existant
        const { getCoordinates, fetchWeather, formatWeatherDisplay } = await import('${pageContext.request.contextPath}/js/geolocWeather.mjs');
        
        // Récupérer les coordonnées
        const coordinates = await getCoordinates(address);
        
        if (coordinates) {
            // Centrer la carte sur l'adresse
            map.setView([coordinates.latitude, coordinates.longitude], 15);
            
            // Ajouter un marqueur
            L.marker([coordinates.latitude, coordinates.longitude]).addTo(map)
                .bindPopup('${client.raisonSociale}<br>' + address)
                .openPopup();
            
            // Récupérer et afficher la météo
            const weatherData = await fetchWeather(coordinates.latitude, coordinates.longitude);
            const weatherHtml = formatWeatherDisplay(weatherData);
            document.getElementById('weather-info').innerHTML = weatherHtml;
        } else {
            document.getElementById('weather-info').innerHTML = 'Adresse non trouvée';
        }
    } catch (error) {
        console.error('Erreur:', error);
        document.getElementById('weather-info').innerHTML = 'Erreur de chargement';
    }
}
</script>
</body>
</html>