<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../meta.jsp"/>
    <title>Création d'un prospect</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main class="container mt-5">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="fas fa-plus"></i> Création d'un nouveau prospect</h1>
                <a href="${pageContext.request.contextPath}/app?cmd=prospects.liste" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Retour à la liste
                </a>
            </div>
        </div>
    </div>
    
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

    <form id="prospectForm" action="${pageContext.request.contextPath}/app?cmd=prospects.create" method="post" class="mt-4">
        <!-- Token CSRF pour la sécurité -->
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-3">
                    <label for="raisonSociale" class="form-label">Raison Sociale *</label>
                    <input type="text" class="form-control" id="raisonSociale" name="raisonSociale" required>
                </div>

                <div class="form-group mb-3">
                    <label for="telephone" class="form-label">Téléphone *</label>
                    <input type="tel" id="telephone" name="telephone" class="form-control"
                           pattern="^(?:(?:\+33|0033)[1-9]|0[1-9])(?:[ .-]?\d{2}){4}$"
                           placeholder="Ex : 0612345678 ou +33612345678"
                           title="Numéro français ou international, ex : 0612345678, +33612345678, 0033612345678"
                           required>
                    <div class="invalid-feedback">
                        Merci de saisir un numéro de téléphone français valide.
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label for="mail" class="form-label">Email *</label>
                    <input type="email" class="form-control" id="mail" name="mail" required>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-group mb-3">
                    <label for="numeroRue" class="form-label">Numéro de rue *</label>
                    <input type="text" class="form-control" id="numeroRue" name="numeroRue" required>
                </div>

                <div class="form-group mb-3">
                    <label for="nomRue" class="form-label">Nom de rue *</label>
                    <input type="text" class="form-control" id="nomRue" name="nomRue" required>
                </div>

                <div class="form-group mb-3">
                    <label for="codePostal" class="form-label">Code postal *</label>
                    <input type="text" id="codePostal" name="codePostal" class="form-control"
                           pattern="^(?:0[1-9]|[1-8][0-9]|9[0-8])\d{3}$|^97[1-8]\d{2}$|^98[46-8]\d{2}$"
                           placeholder="Ex : 75001, 20000, 97100"
                           title="Code postal français à 5 chiffres, y compris DOM/TOM et Corse"
                           required>
                    <div class="invalid-feedback">
                        Merci de saisir un code postal français valide.
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label for="ville" class="form-label">Ville *</label>
                    <input type="text" class="form-control" id="ville" name="ville" required>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group mb-3">
                    <label for="commentaires" class="form-label">Commentaires</label>
                    <textarea class="form-control" id="commentaires" name="commentaires" rows="3"></textarea>
                </div>

                <div class="form-group mb-3">
                    <label for="dateProspection" class="form-label">Date de prospection *</label>
                    <input type="date" class="form-control" id="dateProspection" name="dateProspection" required>
                </div>

                <div class="form-group mb-3">
                    <label for="prospectInteresse" class="form-label">Prospect intéressé</label>
                    <select class="form-control" id="prospectInteresse" name="prospectInteresse">
                        <option value="true">Oui</option>
                        <option value="false">Non</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-12 text-center">
                <button type="submit" class="btn btn-crud-create btn-lg me-3">
                    <i class="fas fa-save"></i> Créer
                </button>
                <a href="${pageContext.request.contextPath}/app?cmd=prospects.liste" class="btn btn-secondary btn-lg">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </div>
    </form>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
</body>
</html> 