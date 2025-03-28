<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Clients</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Liste des Clients</h2>
            <a href="${pageContext.request.contextPath}/client/create" class="btn btn-primary">
                Nouveau Client
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

        <c:if test="${empty clients}">
            <div class="alert alert-info">
                Aucun client trouvé.
            </div>
        </c:if>

        <c:if test="${not empty clients}">
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nom</th>
                                    <th>Email</th>
                                    <th>Téléphone</th>
                                    <th>Secteur</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${clients}" var="client">
                                    <tr>
                                        <td>${client.id}</td>
                                        <td>${client.nom}</td>
                                        <td>${client.email}</td>
                                        <td>${client.telephone}</td>
                                        <td>${client.secteur}</td>
                                        <td>
                                            <div class="btn-group">
                                                <a href="${pageContext.request.contextPath}/client/view?id=${client.id}" 
                                                   class="btn btn-sm btn-info">
                                                    Voir
                                                </a>
                                                <a href="${pageContext.request.contextPath}/client/edit?id=${client.id}" 
                                                   class="btn btn-sm btn-warning">
                                                    Modifier
                                                </a>
                                                <a href="${pageContext.request.contextPath}/client/delete?id=${client.id}" 
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