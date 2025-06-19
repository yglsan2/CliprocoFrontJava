<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CliprocoJEE - Test</title>
</head>
<body>
    <h1>CliprocoJEE - Application de test</h1>
    <p>L'application fonctionne !</p>
    <p>Date et heure : <%= new java.util.Date() %></p>
    <p><a href="${pageContext.request.contextPath}/signin">Aller à la page de connexion</a></p>
</body>
</html> 