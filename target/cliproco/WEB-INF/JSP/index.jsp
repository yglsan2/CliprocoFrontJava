<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil - CliProCo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg fixed-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/accueil">
                <img src="${pageContext.request.contextPath}/image/logo.svg" alt="CliProCo Logo">
                CliProCo
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/clients">Clients</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/prospects">Prospects</a>
                    </li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-login">
                    <i class="fas fa-sign-in-alt me-2"></i>Connexion
                </a>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="container mt-5">
        <div class="row">
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-body">
                        <h2 class="card-title">Gestion des Clients</h2>
                        <p class="card-text">Accédez à votre base de clients et gérez leurs informations efficacement.</p>
                        <a href="${pageContext.request.contextPath}/clients" class="btn btn-primary">
                            <i class="fas fa-users me-2"></i>Voir les clients
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-body">
                        <h2 class="card-title">Gestion des Prospects</h2>
                        <p class="card-text">Suivez vos prospects et optimisez votre pipeline de vente.</p>
                        <a href="${pageContext.request.contextPath}/prospects" class="btn btn-primary">
                            <i class="fas fa-user-plus me-2"></i>Voir les prospects
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>CliProCo</h3>
                    <p>Votre solution de gestion de clients et prospects</p>
                </div>
                <div class="footer-section">
                    <h3>Liens rapides</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/accueil">Accueil</a></li>
                        <li><a href="${pageContext.request.contextPath}/clients">Clients</a></li>
                        <li><a href="${pageContext.request.contextPath}/prospects">Prospects</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Contact</h3>
                    <ul>
                        <li><i class="fas fa-envelope me-2"></i>contact@cliproco.com</li>
                        <li><i class="fas fa-phone me-2"></i>01 23 45 67 89</li>
                        <li><i class="fas fa-map-marker-alt me-2"></i>Paris, France</li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2024 CliProCo. Tous droits réservés.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 
</html> 