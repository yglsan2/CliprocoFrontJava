<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../meta.jsp"/>
    <title>Création d'un client</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main class="container mt-5">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="fas fa-plus"></i> Création d'un nouveau client</h1>
                <a href="${pageContext.request.contextPath}/app?cmd=clients.liste" class="btn btn-secondary">
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

    <form id="clientForm" action="${pageContext.request.contextPath}/app?cmd=clients.create" method="post" class="mt-4">
        <!-- Token CSRF pour la sécurité -->
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-3">
                    <label for="raisonSociale" class="form-label">Raison Sociale *</label>
                    <input type="text" class="form-control" id="raisonSociale" name="raisonSociale" 
                           data-validate="company" placeholder="Ex : Société ABC SARL" required>
                </div>

                <div class="form-group mb-3">
                    <label for="nom" class="form-label">Nom du contact *</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="nom" name="nom" 
                               data-validate="name" placeholder="Ex : Dupont" required 
                               pattern="^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,40}$" 
                               title="Lettres, espaces, tirets, apostrophes uniquement">
                        <span class="input-group-text" data-bs-toggle="tooltip" data-bs-placement="top" 
                              title="Lettres, espaces, tirets, apostrophes uniquement. Ex: Dupont, O'Connor, Jean-Pierre">
                            <i class="fas fa-info-circle"></i>
                        </span>
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label for="prenom" class="form-label">Prénom du contact *</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="prenom" name="prenom" 
                               data-validate="name" placeholder="Ex : Jean" required 
                               pattern="^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,40}$" 
                               title="Lettres, espaces, tirets, apostrophes uniquement">
                        <span class="input-group-text" data-bs-toggle="tooltip" data-bs-placement="top" 
                              title="Lettres, espaces, tirets, apostrophes uniquement. Ex: Jean, Marie-Claire, François">
                            <i class="fas fa-info-circle"></i>
                        </span>
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label for="telephone" class="form-label">Téléphone *</label>
                    <input type="tel" id="telephone" name="telephone" class="form-control"
                           data-validate="phone" pattern="^(?:(?:\+|00)33|0)\s*[1-9](?:[\s.-]*\d{2}){4}$"
                           placeholder="Ex : 0612345678 ou +33612345678" required>
                </div>

                <div class="form-group mb-3">
                    <label for="mail" class="form-label">Email *</label>
                    <input type="email" class="form-control" id="mail" name="mail" 
                           data-validate="email" pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
                           placeholder="Ex : contact@entreprise.com" required>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-group mb-3">
                    <label for="numeroRue" class="form-label">Numéro de rue *</label>
                    <input type="text" class="form-control" id="numeroRue" name="numeroRue" 
                           data-validate="streetnumber" pattern="^([0-9]+[a-zA-Z]?|[a-zA-Z][0-9]*|[0-9]+\s+(bis|ter))$"
                           placeholder="Ex : 123, 12B, A, A123, 123 bis" required>
                </div>

                <div class="form-group mb-3">
                    <label for="nomRue" class="form-label">Nom de rue *</label>
                    <input type="text" class="form-control" id="nomRue" name="nomRue" 
                           data-validate="address" placeholder="Ex : Rue de la Paix" required>
                </div>

                <div class="form-group mb-3">
                    <label for="codePostal" class="form-label">Code postal *</label>
                    <input type="text" id="codePostal" name="codePostal" class="form-control"
                           data-validate="postal" pattern="^(?:[0-8]\d|9[0-5])\d{3}|2[AB]\d{3}|9[7-8]\d{3}$"
                           placeholder="Ex : 75001, 2A000, 97000" required>
                </div>

                <div class="form-group mb-3">
                    <label for="ville" class="form-label">Ville *</label>
                    <input type="text" class="form-control" id="ville" name="ville" 
                           data-validate="city" pattern="[a-zA-Z\u00C0-\u00FF\s\-']+"
                           placeholder="Ex : Paris, Lyon, Ajaccio" required>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group mb-3">
                    <label for="chiffreAffaires" class="form-label">Chiffre d'affaires *</label>
                    <input type="number" class="form-control" id="chiffreAffaires" name="chiffreAffaires" 
                           data-validate="amount" step="0.01" min="0" placeholder="Ex : 100000.50" required>
                </div>

                <div class="form-group mb-3">
                    <label for="nbEmployes" class="form-label">Nombre d'employés *</label>
                    <input type="number" class="form-control" id="nbEmployes" name="nbEmployes" 
                           min="1" required>
                </div>

                <div class="form-group mb-3">
                    <label for="commentaires" class="form-label">Commentaires</label>
                    <textarea class="form-control" id="commentaires" name="commentaires" rows="3"></textarea>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-12 text-center">
                <button type="submit" class="btn btn-crud-create btn-lg me-3">
                    <i class="fas fa-save"></i> Créer
                </button>
                <a href="${pageContext.request.contextPath}/app?cmd=clients.liste" class="btn btn-secondary btn-lg">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </div>
    </form>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
<script type="module" src="${pageContext.request.contextPath}/js/validation.mjs"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('clientForm');
    
    form.addEventListener('submit', function(e) {
        // Validation côté client avec notre système robuste
        if (!window.RobustValidator.validateForm(form)) {
            e.preventDefault();
            alert('Veuillez corriger les erreurs de validation avant de soumettre le formulaire.');
            return false;
        }
        
        // Si validation OK, soumettre le formulaire
        console.log('Formulaire validé avec succès !');
    });
});
</script>
</body>
</html> 