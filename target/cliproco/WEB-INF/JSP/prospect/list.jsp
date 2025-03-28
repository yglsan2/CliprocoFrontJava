<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Prospects</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1>Gestion des Prospects</h1>
        
        <div class="mb-3">
            <a href="?action=add" class="btn btn-primary">Ajouter un prospect</a>
        </div>

        <c:if test="${empty prospects}">
            <div class="alert alert-info">
                Aucun prospect n'est enregistré.
            </div>
        </c:if>

        <c:if test="${not empty prospects}">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Adresse</th>
                        <th>Téléphone</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${prospects}" var="prospect">
                        <tr>
                            <td>${prospect.id}</td>
                            <td>${prospect.nom}</td>
                            <td>${prospect.prenom}</td>
                            <td>${prospect.email}</td>
                            <td>${prospect.adresse}</td>
                            <td>${prospect.telephone}</td>
                            <td>${prospect.statut}</td>
                            <td>
                                <a href="?action=edit&id=${prospect.id}" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="?action=delete&id=${prospect.id}" class="btn btn-sm btn-danger" 
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce prospect ?')">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 