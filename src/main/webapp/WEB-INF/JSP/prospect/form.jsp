<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${prospect.id == null ? 'Ajouter' : 'Modifier'} un Prospect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1>${prospect.id == null ? 'Ajouter' : 'Modifier'} un Prospect</h1>
        
        <c:if test="${not empty message}">
            <div class="alert alert-danger">
                ${message}
            </div>
        </c:if>
        
        <form action="?action=${prospect.id == null ? 'add' : 'edit'}" method="post">
            <c:if test="${prospect.id != null}">
                <input type="hidden" name="id" value="${prospect.id}">
            </c:if>
            
            <div class="mb-3">
                <label for="nom" class="form-label">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" 
                       value="${prospect.nom}" required>
            </div>
            
            <div class="mb-3">
                <label for="prenom" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" 
                       value="${prospect.prenom}" required>
            </div>
            
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" 
                       value="${prospect.email}" required>
            </div>
            
            <div class="mb-3">
                <label for="adresse" class="form-label">Adresse</label>
                <textarea class="form-control" id="adresse" name="adresse" 
                          required>${prospect.adresse}</textarea>
            </div>
            
            <div class="mb-3">
                <label for="telephone" class="form-label">Téléphone</label>
                <input type="tel" class="form-control" id="telephone" name="telephone" 
                       value="${prospect.telephone}" required>
            </div>
            
            <div class="mb-3">
                <label for="statut" class="form-label">Statut</label>
                <select class="form-control" id="statut" name="statut" required>
                    <option value="">Sélectionnez un statut</option>
                    <option value="Nouveau" ${prospect.statut == 'Nouveau' ? 'selected' : ''}>Nouveau</option>
                    <option value="En cours" ${prospect.statut == 'En cours' ? 'selected' : ''}>En cours</option>
                    <option value="Converti" ${prospect.statut == 'Converti' ? 'selected' : ''}>Converti</option>
                    <option value="Perdu" ${prospect.statut == 'Perdu' ? 'selected' : ''}>Perdu</option>
                </select>
            </div>
            
            <div class="mb-3">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="?action=list" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 