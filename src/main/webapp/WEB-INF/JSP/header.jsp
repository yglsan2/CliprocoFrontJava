<%@ include file="taglibs.jsp" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/accueil">CliProCo</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/client/list">Clients</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/prospect/list">Prospects</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/contrat/list">Contrats</a>
                </li>
            </ul>
            <div class="navbar-text text-light me-3">
                Pages visitées : ${sessionScope.compteurPage}
                <c:if test="${not empty derniereVisite}">
                    <br>
                    <small>Dernière visite : ${derniereVisite}</small>
                </c:if>
            </div>
            <div class="navbar-nav">
                <c:if test="${not empty sessionScope.user}">
                    <span class="nav-item nav-link text-light">
                        <i class="fas fa-user"></i> ${sessionScope.user}
                    </span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt"></i> Déconnexion
                    </a>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login">
                        <i class="fas fa-sign-in-alt"></i> Connexion
                    </a>
                </c:if>
            </div>
        </div>
    </div>
</nav> 