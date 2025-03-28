<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Gestion des prospects - Application de suivi client et prospect">
    
    <!-- Styles -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    
    <title>Gestion des Prospects</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark" role="navigation" aria-label="Navigation principale">
    <div class="container-fluid">
        <div class="d-flex w-100">
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/clients" class="btn btn-dark">Clients</a>
                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-dark active">Prospects</a>
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
    <h1>Gestion des Prospects</h1>

    <form id="prospectForm" class="mb-4 border p-3 rounded" role="form" aria-labelledby="formTitle">
        <h2 id="formTitle">Nouveau prospect</h2>
        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Nom *</label>
                <label for="prospectNom"></label><input type="text" class="form-control" id="prospectNom" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Prénom *</label>
                <label for="prospectPrenom"></label><input type="text" class="form-control" id="prospectPrenom" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Date de naissance *</label>
                <label for="prospectDateNaissance"></label><input type="date" class="form-control" id="prospectDateNaissance" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Âge</label>
                <label for="prospectAge"></label><input type="number" class="form-control" id="prospectAge" readonly>
            </div>
            <div class="col-md-6">
                <label class="form-label">Adresse *</label>
                <label for="prospectAdresse"></label><input type="text" class="form-control" id="prospectAdresse" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Ville *</label>
                <label for="prospectVille"></label><input type="text" class="form-control" id="prospectVille" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Code postal *</label>
                <label for="prospectCodePostal"></label><input type="text" class="form-control" id="prospectCodePostal" required maxlength="5">
            </div>
            <div class="col-md-6">
                <label class="form-label">Email *</label>
                <label for="prospectEmail"></label><input type="email" class="form-control" id="prospectEmail" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Téléphone *</label>
                <input type="tel" class="form-control" id="prospectTel" required
                       pattern="^\d{10}$"
                       maxlength="10"
                       title="Veuillez entrer un numéro de téléphone valide (10 chiffres uniquement)."
                       oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);"/>
            </div>
            <div class="col-12">
                <label class="form-label">Raison sociale</label>
                <label for="prospectRaisonSociale"></label><input type="text" class="form-control" id="prospectRaisonSociale">
            </div>
            <div class="col-md-6">
                <label class="form-label">Chiffre d'affaires (€) *</label>
                <label for="prospectCA"></label><input type="number" class="form-control" id="prospectCA" min="201" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Nombre d'employés *</label>
                <label for="prospectEmployes"></label><input type="number" class="form-control" id="prospectEmployes" min="1" required>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </div>
    </form>

    <section aria-label="Liste des prospects">
        <h2>Liste des Prospects</h2>
        <table id="prospectTable" class="table table-bordered" role="grid" aria-label="Liste des prospects">
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
            <!-- Données des prospects à ajouter -->
            </tbody>
        </table>
    </section>

    <section id="prospectDetails" class="mt-3 p-3 border rounded bg-light" style="display:none;" aria-label="Détails du prospect">
        <h2>Détails du Prospect</h2>
        <p><strong>Nom :</strong> <span id="prospectName">En attente...</span></p>
        <p><strong>Adresse :</strong> <span id="prospectAddress">En attente...</span></p>
        <p><strong>Météo :</strong> <span id="prospectWeather">En attente...</span></p>
        <div id="prospectMap" style="height: 400px;"></div>
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
                Voulez-vous vraiment supprimer ce prospect ?
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/auth.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/prospects.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/main.mjs"></script>
</body>
</html> 