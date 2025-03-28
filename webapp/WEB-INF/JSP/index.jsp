<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CliProCo - Gestion des Clients et Prospects</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/fontawesome/all.min.css">
    <link rel="stylesheet" href="css/styles.css">
    
    <!-- Favicon -->
    <link rel="icon" type="image/svg+xml" href="Image/logo.svg">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">
                <img src="Image/logo.svg" alt="CliProCo Logo" height="30" class="d-inline-block align-text-top">
                CliProCo
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="index.jsp">Accueil</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="clients.html">Clients</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="prospects.html">Prospects</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <button class="btn btn-outline-light me-2" id="loginBtn">Connexion</button>
                    <button class="btn btn-light" id="registerBtn">Inscription</button>
                </div>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="container my-5">
        <div class="row">
            <div class="col-md-8 offset-md-2 text-center">
                <h1 class="display-4 mb-4">Bienvenue sur CliProCo</h1>
                <p class="lead">Votre solution de gestion des clients et prospects</p>
                <div class="mt-4">
                    <a href="clients.html" class="btn btn-primary me-2">Gérer les Clients</a>
                    <a href="prospects.html" class="btn btn-success">Gérer les Prospects</a>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>CliProCo</h5>
                    <p>Solution de gestion des clients et prospects</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p>&copy; 2024 CliProCo. Tous droits réservés.</p>
                </div>
            </div>
        </div>
    </footer>

    <!-- JavaScript -->
    <script src="js/bootstrap/bootstrap.bundle.min.js"></script>
    <script src="js/auth.mjs" type="module"></script>
    <script src="js/main.mjs" type="module"></script>
</body>
</html> 