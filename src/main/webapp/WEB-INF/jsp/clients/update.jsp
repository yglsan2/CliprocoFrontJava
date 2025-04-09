<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modification d'un client</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Modification du client</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/clients/update" method="post" class="mt-4">
            <input type="hidden" name="id" value="${client.id}">
            
            <div class="form-group">
                <label for="raisonSociale">Raison Sociale</label>
                <input type="text" class="form-control" id="raisonSociale" name="raisonSociale" value="${client.raisonSociale}" required>
            </div>

            <div class="form-group">
                <label for="siret">SIRET</label>
                <input type="text" class="form-control" id="siret" name="siret" value="${client.siret}" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${client.email}" required>
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="tel" class="form-control" id="telephone" name="telephone" value="${client.telephone}" required>
            </div>

            <div class="form-group">
                <label for="rue">Rue</label>
                <input type="text" class="form-control" id="rue" name="rue" value="${client.adresse.rue}" required>
            </div>

            <div class="form-group">
                <label for="codePostal">Code Postal</label>
                <input type="text" class="form-control" id="codePostal" name="codePostal" value="${client.adresse.codePostal}" required>
            </div>

            <div class="form-group">
                <label for="ville">Ville</label>
                <input type="text" class="form-control" id="ville" name="ville" value="${client.adresse.ville}" required>
            </div>

            <div class="form-group">
                <label for="commentaire">Commentaire</label>
                <textarea class="form-control" id="commentaire" name="commentaire" rows="3">${client.commentaire}</textarea>
            </div>

            <button type="submit" class="btn btn-primary">Modifier</button>
            <a href="${pageContext.request.contextPath}/clients/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html> 