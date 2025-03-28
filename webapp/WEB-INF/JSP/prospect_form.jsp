<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${prospect.id == null ? 'Nouveau Prospect' : 'Modifier Prospect'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h2>${prospect.id == null ? 'Nouveau Prospect' : 'Modifier Prospect'}</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/prospect/save" method="post" class="needs-validation" novalidate>
                    <input type="hidden" name="id" value="${prospect.id}">
                    
                    <div class="mb-3">
                        <label for="nom" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="nom" name="nom" 
                               value="${prospect.nom}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un nom.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" 
                               value="${prospect.email}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un email valide.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="telephone" class="form-label">Téléphone</label>
                        <input type="tel" class="form-control" id="telephone" name="telephone" 
                               value="${prospect.telephone}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un numéro de téléphone.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="secteurActivite" class="form-label">Secteur d'activité</label>
                        <input type="text" class="form-control" id="secteurActivite" name="secteurActivite" 
                               value="${prospect.secteurActivite}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un secteur d'activité.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="statut" class="form-label">Statut</label>
                        <select class="form-select" id="statut" name="statut" required>
                            <option value="">Sélectionnez un statut</option>
                            <option value="NOUVEAU" ${prospect.statut == 'NOUVEAU' ? 'selected' : ''}>Nouveau</option>
                            <option value="EN_COURS" ${prospect.statut == 'EN_COURS' ? 'selected' : ''}>En cours</option>
                            <option value="CONVERTI" ${prospect.statut == 'CONVERTI' ? 'selected' : ''}>Converti</option>
                            <option value="PERDU" ${prospect.statut == 'PERDU' ? 'selected' : ''}>Perdu</option>
                        </select>
                        <div class="invalid-feedback">
                            Veuillez sélectionner un statut.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="commentaires" class="form-label">Commentaires</label>
                        <textarea class="form-control" id="commentaires" name="commentaires" 
                                  rows="3">${prospect.commentaires}</textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/prospect/list" class="btn btn-secondary">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-primary">
                            ${prospect.id == null ? 'Créer' : 'Modifier'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validation du formulaire
        (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        })()
    </script>
</body>
</html> 