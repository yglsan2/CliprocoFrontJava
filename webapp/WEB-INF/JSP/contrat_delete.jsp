<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Supprimer Contrat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h2>Supprimer Contrat</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <div class="alert alert-warning">
                    <h4>Êtes-vous sûr de vouloir supprimer ce contrat ?</h4>
                    <p>Cette action est irréversible.</p>
                </div>

                <div class="card mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Informations du contrat</h5>
                        <p><strong>ID :</strong> ${contrat.id}</p>
                        <p><strong>Libellé :</strong> ${contrat.libelle}</p>
                        <p><strong>Montant :</strong> ${contrat.montant} €</p>
                        <p><strong>Client :</strong> ${contrat.client.nom}</p>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/contrat/delete" method="post">
                    <input type="hidden" name="id" value="${contrat.id}">
                    
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/contrat/list" class="btn btn-secondary">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-danger">
                            Supprimer
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 