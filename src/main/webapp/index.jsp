<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CliprocoJEE - Redirection</title>
    <meta http-equiv="refresh" content="0;url=${pageContext.request.contextPath}/signin">
</head>
<body>
    <p>Redirection vers la page de connexion...</p>
    <p>Si vous n'êtes pas redirigé automatiquement, <a href="${pageContext.request.contextPath}/signin">cliquez ici</a>.</p>
</body>
</html> 