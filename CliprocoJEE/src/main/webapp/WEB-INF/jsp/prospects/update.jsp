<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modification d'un prospect</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Modification du prospect</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/prospects/update" method="post" class="mt-4">
            <input type="hidden" name="id" value="${prospect.id}">
            
            <div class="form-group">
                <label for="raisonSociale">Raison Sociale</label>
                <input type="text" class="form-control" id="raisonSociale" name="raisonSociale" value="${prospect.raisonSociale}" required>
            </div>

            <div class="form-group">
                <label for="siret">SIRET</label>
                <input type="text" class="form-control" id="siret" name="siret" value="${prospect.siret}" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${prospect.email}" required>
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="tel" class="form-control" id="telephone" name="telephone" value="${prospect.telephone}" required>
            </div>

            <div class="form-group">
                <label for="rue">Rue</label>
                <input type="text" class="form-control" id="rue" name="rue" value="${prospect.adresse.rue}" required>
            </div>

            <div class="form-group">
                <label for="codePostal">Code Postal</label>
                <input type="text" class="form-control" id="codePostal" name="codePostal" value="${prospect.adresse.codePostal}" required>
            </div>

            <div class="form-group">
                <label for="ville">Ville</label>
                <input type="text" class="form-control" id="ville" name="ville" value="${prospect.adresse.ville}" required>
            </div>

            <div class="form-group">
                <label for="commentaire">Commentaire</label>
                <textarea class="form-control" id="commentaire" name="commentaire" rows="3">${prospect.commentaire}</textarea>
            </div>

            <div class="form-group">
                <label for="interet">Niveau d'intérêt</label>
                <select class="form-control" id="interet" name="interet" required>
                    <option value="FAIBLE" ${prospect.interet == 'FAIBLE' ? 'selected' : ''}>Faible</option>
                    <option value="MOYEN" ${prospect.interet == 'MOYEN' ? 'selected' : ''}>Moyen</option>
                    <option value="FORT" ${prospect.interet == 'FORT' ? 'selected' : ''}>Fort</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Modifier</button>
            <a href="${pageContext.request.contextPath}/prospects/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>

    <jsp:include page="../scripts.jsp"/>
</body>
</html> 