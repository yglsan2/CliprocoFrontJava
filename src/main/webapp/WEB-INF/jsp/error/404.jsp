<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>404 - Page non trouvée</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6 text-center">
                <h1 class="display-1">404</h1>
                <h2>Page non trouvée</h2>
                <p class="lead">La page que vous recherchez n'existe pas ou a été déplacée.</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Retour à l'accueil</a>
            </div>
        </div>
    </div>
</body>
</html> 