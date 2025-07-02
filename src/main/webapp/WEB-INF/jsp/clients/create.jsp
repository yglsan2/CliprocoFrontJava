<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Création d'un client</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Création d'un nouveau client</h2>
        
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

        <form id="clientForm" action="${pageContext.request.contextPath}/clients/create" method="post" class="mt-4">
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

            <div class="mb-3">
                <label for="telephone" class="form-label">Téléphone</label>
                <input type="tel" id="telephone" name="telephone" class="form-control"
                       pattern="^(?:(?:\+33|0033)[1-9]|0[1-9])(?:[ .-]?\d{2}){4}$"
                       placeholder="Ex : 0612345678 ou +33612345678"
                       title="Numéro français ou international, ex : 0612345678, +33612345678, 0033612345678"
                       required>
                <div class="invalid-feedback">
                    Merci de saisir un numéro de téléphone français valide.
                </div>
            </div>

            <div class="form-group">
                <label for="rue">Rue</label>
                <input type="text" class="form-control" id="rue" name="rue" required>
            </div>

            <div class="mb-3">
                <label for="codePostal" class="form-label">Code postal</label>
                <input type="text" id="codePostal" name="codePostal" class="form-control"
                       pattern="^(?:0[1-9]|[1-8][0-9]|9[0-8])\d{3}$|^97[1-8]\d{2}$|^98[46-8]\d{2}$"
                       placeholder="Ex : 75001, 20000, 97100"
                       title="Code postal français à 5 chiffres, y compris DOM/TOM et Corse"
                       required>
                <div class="invalid-feedback">
                    Merci de saisir un code postal français valide.
                </div>
            </div>

            <div class="form-group">
                <label for="ville">Ville</label>
                <input type="text" class="form-control" id="ville" name="ville" required>
            </div>

            <div class="form-group">
                <label for="commentaire">Commentaire</label>
                <textarea class="form-control" id="commentaire" name="commentaire" rows="3"></textarea>
            </div>

            <button type="submit" class="btn btn-primary">Créer</button>
            <a href="${pageContext.request.contextPath}/clients/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html> 