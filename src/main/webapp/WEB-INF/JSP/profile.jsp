<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profil - CliProCo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .profile-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
        }
        .card {
            margin-bottom: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/accueil">
                <i class="fas fa-users"></i> CliProCo
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/client/list">
                            <i class="fas fa-building"></i> Clients
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/prospect/list">
                            <i class="fas fa-user-tie"></i> Prospects
                        </a>
                    </li>
                </ul>
                <div class="navbar-nav">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/utilisateur/profile">
                        <i class="fas fa-user"></i> ${utilisateur.nom}
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/utilisateur/logout">
                        <i class="fas fa-sign-out-alt"></i> Déconnexion
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container profile-container">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-user-edit"></i> Modifier le profil</h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <c:if test="${not empty errors}">
                            <div class="alert alert-danger" role="alert">
                                <ul class="mb-0">
                                    <c:forEach items="${errors}" var="error">
                                        <li>${error.message}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/utilisateur/update" method="post">
                            <div class="mb-3">
                                <label for="nom" class="form-label">Nom</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="nom" name="nom" 
                                           value="${utilisateur.nom}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="${utilisateur.email}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="nouveauMotDePasse" class="form-label">Nouveau mot de passe</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    <input type="password" class="form-control" id="nouveauMotDePasse" 
                                           name="nouveauMotDePasse">
                                    <button class="btn btn-outline-secondary" type="button" 
                                            onclick="togglePassword('nouveauMotDePasse')">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                                <div class="form-text">Laissez vide pour ne pas modifier le mot de passe</div>
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> Enregistrer les modifications
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-info-circle"></i> Informations</h5>
                    </div>
                    <div class="card-body">
                        <p><strong>Rôle :</strong> ${utilisateur.role}</p>
                        <p><strong>Date d'inscription :</strong> 
                            <fmt:formatDate value="${utilisateur.dateCreation}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-shield-alt"></i> Préférences RGPD</h5>
                    </div>
                    <div class="card-body">
                        <form id="rgpdForm">
                            <div class="mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="analyticsCookies" 
                                           name="analyticsCookies" checked>
                                    <label class="form-check-label" for="analyticsCookies">
                                        Cookies d'analyse
                                    </label>
                                </div>
                            </div>

                            <div class="mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="marketingCookies" 
                                           name="marketingCookies">
                                    <label class="form-check-label" for="marketingCookies">
                                        Cookies marketing
                                    </label>
                                </div>
                            </div>

                            <div class="mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="preferencesCookies" 
                                           name="preferencesCookies" checked>
                                    <label class="form-check-label" for="preferencesCookies">
                                        Cookies de préférences
                                    </label>
                                </div>
                            </div>

                            <button type="button" class="btn btn-info" onclick="saveRGPDPreferences()">
                                <i class="fas fa-save"></i> Enregistrer les préférences
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function togglePassword(inputId) {
            const input = document.getElementById(inputId);
            const icon = input.nextElementSibling.querySelector('i');
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }

        function saveRGPDPreferences() {
            const form = document.getElementById('rgpdForm');
            const formData = new FormData(form);
            
            // Sauvegarder les préférences dans les cookies
            formData.forEach((value, key) => {
                document.cookie = `${key}=${value};path=/;max-age=31536000`; // 1 an
            });

            // Afficher une notification de succès
            alert('Préférences RGPD enregistrées avec succès !');
        }

        // Charger les préférences RGPD au chargement de la page
        document.addEventListener('DOMContentLoaded', function() {
            const cookies = document.cookie.split(';');
            cookies.forEach(cookie => {
                const [name, value] = cookie.trim().split('=');
                const checkbox = document.getElementById(name);
                if (checkbox) {
                    checkbox.checked = value === 'true';
                }
            });
        });
    </script>
</body>
</html> 