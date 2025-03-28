<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'CliProCo - Gestion des Clients et Prospects'}</title>
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
    
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    
    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/js/dist/bundle.js" defer></script>
</head>
<body>
    <!-- En-tête -->
    <header class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="CliProCo Logo" height="30">
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
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/meteo">Météo</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <c:if test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light me-2">Connexion</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-light">Inscription</a>
                    </c:if>
                    <c:if test="${not empty sessionScope.user}">
                        <span class="navbar-text me-3">
                            Bienvenue, ${sessionScope.user.username}
                        </span>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light">Déconnexion</a>
                    </c:if>
                </div>
            </div>
        </div>
    </header>

    <!-- Contenu principal -->
    <main class="container my-4">
        <c:if test="${not empty requestScope.message}">
            <div class="alert alert-${requestScope.messageType != null ? requestScope.messageType : 'info'} alert-dismissible fade show">
                ${requestScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <jsp:include page="${param.content}" />
    </main>

    <!-- Pied de page -->
    <footer class="footer mt-auto py-3 bg-light">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <p class="mb-0">&copy; 2024 CliProCo. Tous droits réservés.</p>
                </div>
                <div class="col-md-6 text-end">
                    <a href="${pageContext.request.contextPath}/mentions-legales" class="text-muted me-3">Mentions légales</a>
                    <a href="${pageContext.request.contextPath}/contact" class="text-muted">Contact</a>
                </div>
            </div>
        </div>
    </footer>
</body>
</html> 