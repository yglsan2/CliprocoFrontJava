<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Clients</title>

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/node_modules/@fortawesome/fontawesome-free/css/all.min.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <div class="d-flex w-100">
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/" class="btn btn-light">Accueil</a>
                <a href="${pageContext.request.contextPath}/clients" class="btn btn-light active">Clients</a>
                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-light">Prospects</a>
            </div>
            <div class="ms-auto">
                <button id="btnAuth" class="btn btn-light" data-bs-toggle="modal" data-bs-target="#authModal">Connexion</button>
            </div>
        </div>
    </div>
</nav>

<!-- Modal de connexion -->
<div class="modal fade" id="authModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Connexion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="authForm" action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Utilisateur</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Mot de passe</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Se connecter</button>
                </form>
            </div>
        </div>
    </div>
</div>

<main class="container mt-4">
    <h1 class="text-center mb-4">Gestion des Clients</h1>

    <!-- Formulaire d'ajout de client -->
    <div class="card mb-4">
        <div class="card-header">
            <h5 class="mb-0">Ajouter un nouveau client</h5>
        </div>
        <div class="card-body">
            <form id="clientForm">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nom" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="nom" name="nom" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="prenom" class="form-label">Prénom</label>
                        <input type="text" class="form-control" id="prenom" name="prenom" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="telephone" class="form-label">Téléphone</label>
                        <input type="tel" class="form-control" id="telephone" name="telephone" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="adresse" class="form-label">Adresse</label>
                        <input type="text" class="form-control" id="adresse" name="adresse" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="codePostal" class="form-label">Code Postal</label>
                        <input type="text" class="form-control" id="codePostal" name="codePostal" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="ville" class="form-label">Ville</label>
                        <input type="text" class="form-control" id="ville" name="ville" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="pays" class="form-label">Pays</label>
                        <input type="text" class="form-control" id="pays" name="pays" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="latitude" class="form-label">Latitude</label>
                        <input type="text" class="form-control" id="latitude" name="latitude" readonly>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="longitude" class="form-label">Longitude</label>
                        <input type="text" class="form-control" id="longitude" name="longitude" readonly>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="meteo" class="form-label">Météo</label>
                        <input type="text" class="form-control" id="meteo" name="meteo" readonly>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="temperature" class="form-label">Température</label>
                        <input type="text" class="form-control" id="temperature" name="temperature" readonly>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Ajouter le client</button>
            </form>
        </div>
    </div>

    <!-- Liste des clients -->
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0">Liste des clients</h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Téléphone</th>
                        <th>Adresse</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody id="clientsList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<!-- Modal de confirmation de suppression -->
<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirmer la suppression</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Êtes-vous sûr de vouloir supprimer ce client ?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                <button type="button" class="btn btn-danger" id="confirmDelete">Supprimer</button>
            </div>
        </div>
    </div>
</div>

<!-- Bandeau RGPD -->
<div id="rgpdBanner" class="fixed-bottom bg-dark text-light p-3" style="display: none;" role="alert" aria-live="polite">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-9">
                <p class="mb-0">
                    Ce site utilise des cookies pour améliorer votre expérience. En continuant à naviguer sur ce site, vous acceptez notre utilisation des cookies.
                    Nous collectons uniquement les données nécessaires au bon fonctionnement du site (géolocalisation et météo).
                    Ces données ne sont pas partagées avec des tiers et sont stockées localement sur votre navigateur.
                </p>
            </div>
            <div class="col-md-3 text-end">
                <button id="acceptCookies" class="btn btn-primary">Accepter</button>
                <button id="refuseCookies" class="btn btn-secondary ms-2">Refuser</button>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/auth.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/clients.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/rgpd.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/main.mjs"></script>
</body>
</html> 