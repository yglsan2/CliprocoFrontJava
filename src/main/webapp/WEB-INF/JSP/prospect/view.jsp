<%@ include file="../taglibs.jsp" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Prospect - CliProCo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h2 class="mb-0">Détails du Prospect</h2>
                <div>
                    <a href="?action=edit&id=${prospect.id}" class="btn btn-warning">
                        <i class="fas fa-edit"></i> Modifier
                    </a>
                    <a href="?action=list" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Retour
                    </a>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h3 class="mb-3">Informations générales</h3>
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
                        </table>
                    </div>
                    <div class="col-md-6">
                        <h3 class="mb-3">Informations commerciales</h3>
                        <table class="table">
                            <tr>
                                <th>Secteur d'activité</th>
                                <td>${prospect.secteurActivite}</td>
                            </tr>
                            <tr>
                                <th>Statut</th>
                                <td>
                                    <span class="badge ${prospect.statut == 'NOUVEAU' ? 'bg-primary' : 
                                                      prospect.statut == 'EN_COURS' ? 'bg-warning' : 
                                                      prospect.statut == 'CONVERTI' ? 'bg-success' : 'bg-secondary'}">
                                        ${prospect.statut}
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="row mt-4">
                    <div class="col-12">
                        <h3 class="mb-3">Commentaires</h3>
                        <div class="card">
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty prospect.commentaires}">
                                        ${prospect.commentaires}
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