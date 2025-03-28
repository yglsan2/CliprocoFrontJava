<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur serveur - CliProCo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container mt-5 text-center">
    <h1 class="display-1">500</h1>
    <h2>Erreur serveur</h2>
    <p class="lead">Une erreur est survenue sur le serveur. Veuillez réessayer plus tard.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Retour à l'accueil</a>
</div>
</body>
</html> 