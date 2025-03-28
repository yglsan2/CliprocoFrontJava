<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails du Contrat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h2>Détails du Contrat</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/contrat/edit?id=${contrat.id}" class="btn btn-warning me-2">
                        Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/contrat/list" class="btn btn-secondary">
                        Retour à la liste
                    </a>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h3>Informations du contrat</h3>
                        <table class="table">
                            <tr>
                                <th>ID :</th>
                                <td>${contrat.id}</td>
                            </tr>
                            <tr>
                                <th>Libellé :</th>
                                <td>${contrat.libelle}</td>
                            </tr>
                            <tr>
                                <th>Montant :</th>
                                <td>${contrat.montant} €</td>
                            </tr>
                        </table>
                    </div>
                    <div class="col-md-6">
                        <h3>Informations du client</h3>
                        <table class="table">
                            <tr>
                                <th>Nom :</th>
                                <td>${contrat.client.nom}</td>
                            </tr>
                            <tr>
                                <th>Email :</th>
                                <td>${contrat.client.email}</td>
                            </tr>
                            <tr>
                                <th>Téléphone :</th>
                                <td>${contrat.client.telephone}</td>
                            </tr>
                            <tr>
                                <th>Secteur d'activité :</th>
                                <td>${contrat.client.secteur}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 