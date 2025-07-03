<header class="header">
    <nav class="navbar navbar-expand-lg fixed-top">
        <div class="container-fluid">
            <a class="navbar-brand"
               href="?cmd=index">
                Cliproco
            </a>
            <button class="navbar-toggler"
                    aria-label="navbar-toggler"
                    type="button"
                    data-bs-target="#offcanvasNavbar"
                    data-bs-toggle="offcanvas">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>

        <div class="offcanvas offcanvas-end"
             id="offcanvasNavbar"
             tabindex="-1">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title"
                    id="offcanvasNavbarLabel">
                    Cliproco
                </h5>
                <button class="btn-close"
                        aria-label="Close"
                        type="button"
                        data-bs-toggle="offcanvas">
                </button>
            </div>

            <div class="offcanvas-body">
                <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
                    <li class="nav-item"
                        aria-label="Page d'accueil">
                        <a class="nav-link active"
                           href="?cmd=index">
                            Accueil
                        </a>
                    </li>

                    <li class="nav-item"
                        aria-label="Partie Clients">
                        <a class="nav-link active"
                           href="?cmd=clients.liste">
                            Clients
                        </a>
                    </li>
                    <li class="nav-item"
                        aria-label="Partie Prospects">
                        <a class="nav-link active"
                           href="?cmd=prospects.liste">
                            Prospects
                        </a>
                    </li>
                </ul>

                <hr>

                <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">

                    <c:if test="${empty sessionScope.currentUser}">
                        <li class="nav-item" aria-label="Page de connexion">
                            <a class="nav-link active" href="?cmd=connexion">
                                <div class="material-symbols-outlined">login</div>
                                <div>Connexion</div>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${not empty sessionScope.currentUser}">
                        <li class="nav-item" aria-label="Page de déconnexion">
                            <a class="nav-link active" href="?cmd=deconnexion">
                                <div class="material-symbols-outlined">logout</div>
                                <div>Deconnexion</div>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
</header>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">