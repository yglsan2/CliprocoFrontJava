<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Gestion des clients - Application de suivi client et prospect">
    
    <!-- Styles -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/fontawesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/leaflet.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    
    <title>Gestion des Clients</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark" role="navigation" aria-label="Navigation principale">
    <div class="container-fluid">
        <div class="d-flex w-100">
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/clients" class="btn btn-dark active">Clients</a>
                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-dark">Prospects</a>
            </div>
            <div class="ms-auto">
                <div id="user-section" class="d-flex">
                    <form id="login-form" class="d-flex">
                        <label for="username"></label><input type="text" id="username" class="form-control me-2" placeholder="Identifiant">
                        <label for="password"></label><input type="password" id="password" class="form-control me-2" placeholder="Mot de passe">
                        <button id="login-btn" class="btn btn-light">Connexion</button>
                    </form>
                    <button id="logout-btn" class="btn btn-danger ms-2" style="display:none;">Déconnexion</button>
                </div>
            </div>
        </div>
    </div>
</nav>

<main class="container mt-4" role="main">
    <h1>Gestion des Clients</h1>

    <form id="clientForm" class="mb-4 border p-3 rounded" role="form" aria-labelledby="formTitle">
        <h2 id="formTitle">Nouveau client</h2>
        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Nom *</label>
                <label for="clientNom"></label><input type="text" class="form-control" id="clientNom" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Prénom *</label>
                <label for="clientPrenom"></label><input type="text" class="form-control" id="clientPrenom" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Date de naissance *</label>
                <label for="clientDateNaissance"></label><input type="date" class="form-control" id="clientDateNaissance" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Âge</label>
                <label for="clientAge"></label><input type="number" class="form-control" id="clientAge" readonly>
            </div>
            <div class="col-md-6">
                <label class="form-label">Adresse *</label>
                <label for="clientAdresse"></label><input type="text" class="form-control" id="clientAdresse" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Ville *</label>
                <label for="clientVille"></label><input type="text" class="form-control" id="clientVille" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Code postal *</label>
                <label for="clientCodePostal"></label><input type="text" class="form-control" id="clientCodePostal" required maxlength="5">
            </div>
            <div class="col-md-6">
                <label class="form-label">Email *</label>
                <label for="clientEmail"></label><input type="email" class="form-control" id="clientEmail" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Téléphone *</label>
                <input type="tel" class="form-control" id="clientTel" required
                       pattern="^\d{10}$"
                       maxlength="10"
                       title="Veuillez entrer un numéro de téléphone valide (10 chiffres uniquement)."
                       oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);"/>
            </div>
            <div class="col-12">
                <label class="form-label">Raison sociale</label>
                <label for="clientRaisonSociale"></label><input type="text" class="form-control" id="clientRaisonSociale">
            </div>
            <div class="col-md-6">
                <label class="form-label">Chiffre d'affaires (€) *</label>
                <label for="clientCA"></label><input type="number" class="form-control" id="clientCA" min="201" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Nombre d'employés *</label>
                <label for="clientEmployes"></label><input type="number" class="form-control" id="clientEmployes" min="1" required>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </div>
    </form>

    <section aria-label="Liste des clients">
        <h2>Liste des Clients</h2>
        <table id="clientTable" class="table table-bordered" role="grid" aria-label="Liste des clients">
            <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Date de naissance</th>
                <th>Adresse</th>
                <th>Ville </th>
                <th>Code Postal</th>
                <th>Email</th>
                <th>Téléphone</th>
                <th>Raison sociale</th>
                <th>Chiffre d'affaires</th>
                <th>nombre d'employés</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <!-- Données des clients à ajouter -->
            </tbody>
        </table>
    </section>

    <section id="clientDetails" class="mt-3 p-3 border rounded bg-light" style="display:none;" aria-label="Détails du client">
        <h2>Détails du Client</h2>
        <p><strong>Nom :</strong> <span id="clientName">En attente...</span></p>
        <p><strong>Adresse :</strong> <span id="clientAddress">En attente...</span></p>
        <p><strong>Météo :</strong> <span id="clientWeather">En attente...</span></p>
        <div id="clientMap" style="height: 400px;"></div>
    </section>
</main>

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

<!-- Modal de confirmation de suppression -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmModalLabel">Confirmation</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
            </div>
            <div class="modal-body">
                Voulez-vous vraiment supprimer ce client ?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                <button type="button" class="btn btn-danger" id="confirmModalYes">Supprimer</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal d'authentification -->
<div class="modal fade" id="authModal" tabindex="-1" aria-labelledby="authModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="authModalLabel">Connexion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
            </div>
            <div class="modal-body">
                <form id="authForm">
                    <div class="mb-3">
                        <label for="authUsername" class="form-label">Identifiant</label>
                        <input type="text" class="form-control" id="authUsername" required>
                    </div>
                    <div class="mb-3">
                        <label for="authPassword" class="form-label">Mot de passe</label>
                        <input type="password" class="form-control" id="authPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Se connecter</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/auth.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/clients.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html> 