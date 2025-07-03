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

        <c:if test="${not empty errorValidation}">
            <div class="alert alert-danger">${errorValidation}</div>
        </c:if>
        <c:if test="${not empty errorArgument}">
            <div class="alert alert-warning">${errorArgument}</div>
        </c:if>
        <c:if test="${not empty errorFormat}">
            <div class="alert alert-warning">${errorFormat}</div>
        </c:if>
        <c:if test="${not empty errorGlobal}">
            <div class="alert alert-danger">${errorGlobal}</div>
        </c:if>

        <form id="prospectForm" action="${pageContext.request.contextPath}/prospects/update" method="post" class="mt-4">
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

            <div class="mb-3">
                <label for="telephone" class="form-label">Téléphone</label>
                <input type="tel" id="telephone" name="telephone" class="form-control"
                       pattern="^(?:(?:\+33|0033)[1-9]|0[1-9])(?:[ .-]?\d{2}){4}$"
                       placeholder="Ex : 0612345678 ou +33612345678"
                       title="Numéro français ou international, ex : 0612345678, +33612345678, 0033612345678"
                       required value="${prospect.telephone}">
                <div class="invalid-feedback">
                    Merci de saisir un numéro de téléphone français valide.
                </div>
            </div>

            <div class="form-group">
                <label for="rue">Rue</label>
                <input type="text" class="form-control" id="rue" name="rue" value="${prospect.adresse.rue}" required>
            </div>

            <div class="mb-3">
                <label for="codePostal" class="form-label">Code postal</label>
                <input type="text" id="codePostal" name="codePostal" class="form-control"
                       pattern="^(?:0[1-9]|[1-8][0-9]|9[0-8])\d{3}$|^97[1-8]\d{2}$|^98[46-8]\d{2}$"
                       placeholder="Ex : 75001, 20000, 97100"
                       title="Code postal français à 5 chiffres, y compris DOM/TOM et Corse"
                       required value="${prospect.adresse.codePostal}">
                <div class="invalid-feedback">
                    Merci de saisir un code postal français valide.
                </div>
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

                            <button type="submit" class="btn btn-crud-edit">Modifier</button>
            <a href="${pageContext.request.contextPath}/prospects/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>

    <jsp:include page="../scripts.jsp"/>
</body>
</html> 