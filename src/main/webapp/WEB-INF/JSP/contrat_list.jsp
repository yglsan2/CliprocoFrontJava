<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Contrats</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Liste des Contrats</h2>
            <a href="${pageContext.request.contextPath}/contrat/create" class="btn btn-primary">
                Nouveau Contrat
            </a>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">
                ${success}
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>

        <c:if test="${empty contrats}">
            <div class="alert alert-info">
                Aucun contrat trouvé.
            </div>
        </c:if>

        <c:if test="${not empty contrats}">
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Client</th>
                                    <th>Libellé</th>
                                    <th>Montant</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${contrats}" var="contrat">
                                    <tr>
                                        <td>${contrat.id}</td>
                                        <td>${contrat.client.nom}</td>
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
                </div>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 