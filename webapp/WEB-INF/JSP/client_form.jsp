<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${client.id == null ? 'Nouveau Client' : 'Modifier Client'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h2>${client.id == null ? 'Nouveau Client' : 'Modifier Client'}</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/client/save" method="post" class="needs-validation" novalidate>
                    <input type="hidden" name="id" value="${client.id}">
                    
                    <div class="mb-3">
                        <label for="nom" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="nom" name="nom" 
                               value="${client.nom}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un nom.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" 
                               value="${client.email}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un email valide.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="telephone" class="form-label">Téléphone</label>
                        <input type="tel" class="form-control" id="telephone" name="telephone" 
                               value="${client.telephone}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un numéro de téléphone.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="secteur" class="form-label">Secteur d'activité</label>
                        <input type="text" class="form-control" id="secteur" name="secteur" 
                               value="${client.secteur}" required>
                        <div class="invalid-feedback">
                            Veuillez saisir un secteur d'activité.
                        </div>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/client/list" class="btn btn-secondary">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-primary">
                            ${client.id == null ? 'Créer' : 'Modifier'}
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