<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Création d'un prospect</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Création d'un nouveau prospect</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/prospects/create" method="post" class="mt-4">
            <div class="form-group">
                <label for="raisonSociale">Raison Sociale</label>
                <input type="text" class="form-control" id="raisonSociale" name="raisonSociale" required>
            </div>

            <div class="form-group">
                <label for="siret">SIRET</label>
                <input type="text" class="form-control" id="siret" name="siret" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="tel" class="form-control" id="telephone" name="telephone" required>
            </div>

            <div class="form-group">
                <label for="rue">Rue</label>
                <input type="text" class="form-control" id="rue" name="rue" required>
            </div>

            <div class="form-group">
                <label for="codePostal">Code Postal</label>
                <input type="text" class="form-control" id="codePostal" name="codePostal" required>
            </div>

            <div class="form-group">
                <label for="ville">Ville</label>
                <input type="text" class="form-control" id="ville" name="ville" required>
            </div>

            <div class="form-group">
                <label for="commentaire">Commentaire</label>
                <textarea class="form-control" id="commentaire" name="commentaire" rows="3"></textarea>
            </div>

            <div class="form-group">
                <label for="interet">Niveau d'intérêt</label>
                <select class="form-control" id="interet" name="interet" required>
                    <option value="FAIBLE">Faible</option>
                    <option value="MOYEN">Moyen</option>
                    <option value="FORT">Fort</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Créer</button>
            <a href="${pageContext.request.contextPath}/prospects/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>

    <jsp:include page="../scripts.jsp"/>
</body>
</html> 