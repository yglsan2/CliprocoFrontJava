<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Supprimer Client</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h2>Supprimer Client</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <div class="alert alert-warning">
                    <h4>Êtes-vous sûr de vouloir supprimer ce client ?</h4>
                    <p>Cette action est irréversible.</p>
                </div>

                <div class="card mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Informations du client</h5>
                        <p><strong>Nom :</strong> ${client.nom}</p>
                        <p><strong>Email :</strong> ${client.email}</p>
                        <p><strong>Téléphone :</strong> ${client.telephone}</p>
                        <p><strong>Secteur d'activité :</strong> ${client.secteur}</p>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/client/delete" method="post">
                    <input type="hidden" name="id" value="${client.id}">
                    
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/client/list" class="btn btn-secondary">
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