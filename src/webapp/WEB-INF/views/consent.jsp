<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consentement des cookies - Cliproco</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .cookie-banner {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            background: rgba(0, 0, 0, 0.9);
            color: white;
            padding: 20px;
            z-index: 1000;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1>Consentement des cookies</h1>
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Utilisation des cookies</h5>
                <p class="card-text">
                    Nous utilisons des cookies pour améliorer votre expérience sur notre site. 
                    Les cookies nous permettent de :
                </p>
                <ul>
                    <li>Maintenir votre session active</li>
                    <li>Mémoriser vos préférences</li>
                    <li>Analyser l'utilisation du site</li>
                    <li>Améliorer nos services</li>
                </ul>
                <form action="${pageContext.request.contextPath}/consent" method="post">
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="necessary" checked disabled>
                        <label class="form-check-label" for="necessary">
                            Cookies nécessaires (toujours actifs)
                        </label>
                    </div>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="analytics" name="analytics">
                        <label class="form-check-label" for="analytics">
                            Cookies d'analyse
                        </label>
                    </div>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="preferences" name="preferences">
                        <label class="form-check-label" for="preferences">
                            Cookies de préférences
                        </label>
                    </div>
                    <button type="submit" class="btn btn-primary">Accepter</button>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Refuser</a>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 