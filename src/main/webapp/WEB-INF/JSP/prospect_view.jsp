<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails du Prospect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h2>Détails du Prospect</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/prospect/edit?id=${prospect.id}" 
                       class="btn btn-warning">
                        <i class="bi bi-pencil"></i> Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/prospect/list" 
                       class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Retour
                    </a>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h3>Informations générales</h3>
                        <table class="table">
                            <tr>
                                <th>ID</th>
                                <td>${prospect.id}</td>
                            </tr>
                            <tr>
                                <th>Nom</th>
                                <td>${prospect.nom}</td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td>${prospect.email}</td>
                            </tr>
                            <tr>
                                <th>Téléphone</th>
                                <td>${prospect.telephone}</td>
                            </tr>
                            <tr>
                                <th>Secteur d'activité</th>
                                <td>${prospect.secteurActivite}</td>
                            </tr>
                            <tr>
                                <th>Statut</th>
                                <td>
                                    <span class="badge ${prospect.statut == 'NOUVEAU' ? 'bg-primary' : 
                                                      prospect.statut == 'EN_COURS' ? 'bg-warning' : 
                                                      prospect.statut == 'CONVERTI' ? 'bg-success' : 'bg-danger'}">
                                        ${prospect.statut}
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="col-md-6">
                        <h3>Commentaires</h3>
                        <div class="card">
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty prospect.commentaires}">
                                        <p>${prospect.commentaires}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">Aucun commentaire</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 