<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails du Client</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h2>Détails du Client</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/client/edit?id=${client.id}" class="btn btn-warning me-2">
                        Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/client/list" class="btn btn-secondary">
                        Retour à la liste
                    </a>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h3>Informations générales</h3>
                        <table class="table">
                            <tr>
                                <th>ID :</th>
                                <td>${client.id}</td>
                            </tr>
                            <tr>
                                <th>Nom :</th>
                                <td>${client.nom}</td>
                            </tr>
                            <tr>
                                <th>Email :</th>
                                <td>${client.email}</td>
                            </tr>
                            <tr>
                                <th>Téléphone :</th>
                                <td>${client.telephone}</td>
                            </tr>
                            <tr>
                                <th>Secteur d'activité :</th>
                                <td>${client.secteur}</td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="mt-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h3>Contrats associés</h3>
                        <a href="${pageContext.request.contextPath}/contrat/create?clientId=${client.id}" class="btn btn-primary">
                            Nouveau Contrat
                        </a>
                    </div>

                    <c:if test="${empty client.contrats}">
                        <div class="alert alert-info">
                            Aucun contrat associé à ce client.
                        </div>
                    </c:if>

                    <c:if test="${not empty client.contrats}">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Libellé</th>
                                        <th>Montant</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${client.contrats}" var="contrat">
                                        <tr>
                                            <td>${contrat.id}</td>
                                            <td>${contrat.libelle}</td>
                                            <td>${contrat.montant} €</td>
                                            <td>
                                                <div class="btn-group">
                                                    <a href="${pageContext.request.contextPath}/contrat/view?id=${contrat.id}" 
                                                       class="btn btn-sm btn-info">
                                                        Voir
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/contrat/edit?id=${contrat.id}" 
                                                       class="btn btn-sm btn-warning">
                                                        Modifier
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/contrat/delete?id=${contrat.id}" 
                                                       class="btn btn-sm btn-danger">
                                                        Supprimer
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 