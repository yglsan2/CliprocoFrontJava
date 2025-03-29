<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - CliProCo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">Connexion</h3>
                    </div>
                    <div class="card-body">
                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger">
                                ${form.error != null ? form.error : "Identifiants incorrects"}
                            </div>
                        </c:if>
                        <form action="<c:url value='/?cmd=login'/>" method="post">
                            <input type="hidden" name="_csrf" value="${sessionScope._csrf}"/>
                            <div class="mb-3">
                                <label for="user" class="form-label">Nom d'utilisateur</label>
                                <input type="text" class="form-control" id="user" name="user" value="${form.user}" required>
                            </div>
                            <div class="mb-3">
                                <label for="pwd" class="form-label">Mot de passe</label>
                                <input type="password" class="form-control" id="pwd" name="pwd" required>
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="remember" name="remember" ${form.remember ? 'checked' : ''}>
                                <label class="form-check-label" for="remember">Se souvenir de moi</label>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Se connecter</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 