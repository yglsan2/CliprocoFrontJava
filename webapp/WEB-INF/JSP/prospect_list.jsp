<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Prospects</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Liste des Prospects</h1>
            <a href="${pageContext.request.contextPath}/prospect/create" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> Nouveau Prospect
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Email</th>
                                <th>Téléphone</th>
                                <th>Secteur d'activité</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${prospects}" var="prospect">
                                <tr>
                                    <td>${prospect.id}</td>
                                    <td>${prospect.nom}</td>
                                    <td>${prospect.email}</td>
                                    <td>${prospect.telephone}</td>
                                    <td>${prospect.secteurActivite}</td>
                                    <td>
                                        <span class="badge ${prospect.statut == 'NOUVEAU' ? 'bg-primary' : 
                                                          prospect.statut == 'EN_COURS' ? 'bg-warning' : 
                                                          prospect.statut == 'CONVERTI' ? 'bg-success' : 'bg-danger'}">
                                            ${prospect.statut}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/prospect/view?id=${prospect.id}" 
                                           class="btn btn-sm btn-info">
                                            <i class="bi bi-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/prospect/edit?id=${prospect.id}" 
                                           class="btn btn-sm btn-warning">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/prospect/delete?id=${prospect.id}" 
                                           class="btn btn-sm btn-danger"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce prospect ?')">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 