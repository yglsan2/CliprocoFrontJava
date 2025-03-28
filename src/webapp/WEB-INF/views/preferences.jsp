<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des préférences RGPD - Cliproco</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .preferences-section {
            margin-bottom: 2rem;
        }
        .preferences-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1rem;
        }
        .preferences-title {
            color: #0d6efd;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1>Gestion de vos préférences RGPD</h1>
        
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                ${message}
            </div>
        </c:if>

        <div class="preferences-section">
            <h2 class="preferences-title">Vos préférences actuelles</h2>
            
            <form action="${pageContext.request.contextPath}/consent/update" method="post">
                <input type="hidden" name="preferencesId" value="${preferences.id}">
                
                <div class="preferences-card">
                    <h3>Cookies analytiques</h3>
                    <p>Ces cookies nous permettent d'analyser l'utilisation du site pour améliorer nos services.</p>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="analytics" id="analytics"
                               ${preferences.analyticsEnabled ? 'checked' : ''}>
                        <label class="form-check-label" for="analytics">
                            Autoriser les cookies analytiques
                        </label>
                    </div>
                </div>

                <div class="preferences-card">
                    <h3>Cookies marketing</h3>
                    <p>Ces cookies sont utilisés pour vous proposer des contenus personnalisés.</p>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="marketing" id="marketing"
                               ${preferences.marketingEnabled ? 'checked' : ''}>
                        <label class="form-check-label" for="marketing">
                            Autoriser les cookies marketing
                        </label>
                    </div>
                </div>

                <div class="preferences-card">
                    <h3>Cookies de préférences</h3>
                    <p>Ces cookies mémorisent vos préférences pour améliorer votre expérience.</p>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="preferences" id="preferences"
                               ${preferences.preferencesEnabled ? 'checked' : ''}>
                        <label class="form-check-label" for="preferences">
                            Autoriser les cookies de préférences
                        </label>
                    </div>
                </div>

                <div class="preferences-card">
                    <h3>Durée de conservation des données</h3>
                    <p>Période pendant laquelle nous conservons vos données personnelles.</p>
                    <div class="form-group">
                        <label for="retentionPeriod">Durée de conservation (en mois)</label>
                        <input type="number" class="form-control" id="retentionPeriod" name="retentionPeriod"
                               value="${preferences.dataRetentionPeriod}" min="1" max="36">
                    </div>
                </div>

                <div class="mt-4">
                    <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
                </div>
            </form>
        </div>

        <div class="preferences-section">
            <h2 class="preferences-title">Suppression des données</h2>
            <div class="preferences-card">
                <p>Vous pouvez demander la suppression de toutes vos données personnelles.</p>
                <p class="text-danger">Cette action est irréversible.</p>
                <form action="${pageContext.request.contextPath}/consent/delete" method="post" 
                      onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer toutes vos données ?');">
                    <button type="submit" class="btn btn-danger">Supprimer mes données</button>
                </form>
            </div>
        </div>

        <div class="preferences-section">
            <h2 class="preferences-title">Informations sur vos données</h2>
            <div class="preferences-card">
                <p><strong>Dernier consentement :</strong> ${preferences.lastConsentDate}</p>
                <p><strong>Adresse IP :</strong> ${preferences.ipAddress}</p>
                <p><strong>Version du consentement :</strong> ${preferences.consentVersion}</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 