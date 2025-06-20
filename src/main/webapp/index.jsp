<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CliprocoJEE - Redirection</title>
    <meta http-equiv="refresh" content="0;url=${pageContext.request.contextPath}/app?cmd=index">
</head>
<body>
    <p>Redirection vers l'application...</p>
    <p>Si vous n'êtes pas redirigé automatiquement, <a href="${pageContext.request.contextPath}/app?cmd=index">cliquez ici</a>.</p>
</body>
</html> 