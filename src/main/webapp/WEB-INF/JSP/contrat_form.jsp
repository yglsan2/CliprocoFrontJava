<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${empty contrat.id ? 'Nouveau Contrat' : 'Modifier Contrat'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h2>${empty contrat.id ? 'Nouveau Contrat' : 'Modifier Contrat'}</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/contrat/${empty contrat.id ? 'create' : 'edit'}" 
                      method="post" class="needs-validation" novalidate>
                    
                    <input type="hidden" name="id" value="${contrat.id}">
                    
                    <div class="mb-3">
                        <label for="clientId" class="form-label">Client</label>
                        <select class="form-select" id="clientId" name="clientId" required>
                            <option value="">Sélectionnez un client</option>
                            <c:forEach items="${clients}" var="client">
                                <option value="${client.id}" ${client.id == contrat.clientId ? 'selected' : ''}>
                                    ${client.nom}
                                </option>
                            </c:forEach>
                        </select>
                        <div class="invalid-feedback">
                            Veuillez sélectionner un client.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="libelle" class="form-label">Libellé</label>
                        <input type="text" class="form-control" id="libelle" name="libelle" 
                               value="${contrat.libelle}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un libellé.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="montant" class="form-label">Montant (€)</label>
                        <input type="number" step="0.01" class="form-control" id="montant" name="montant" 
                               value="${contrat.montant}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un montant valide.
                        </div>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/contrat/list" class="btn btn-secondary">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-primary">
                            ${empty contrat.id ? 'Créer' : 'Modifier'}
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