<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Erreur - Cliproco</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="${header}" />
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-danger text-white">
                        <h2>Erreur</h2>
                    </div>
                    <div class="card-body text-center">
                        <i class="bi bi-exclamation-triangle-fill text-danger" style="font-size: 4rem;"></i>
                        <h3 class="mt-3">${error}</h3>
                        <p class="text-muted">${message}</p>
                        <a href="${pageContext.request.contextPath}/accueil" class="btn btn-primary">
                            Retour à l'accueil
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 