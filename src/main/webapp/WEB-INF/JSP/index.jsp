<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Clients & Prospects</title>

    <!-- Styles -->
    <link href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/fontawesome/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/leaflet.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    
    <meta name="description" content="Application de gestion des clients et prospects avec géolocalisation et météo">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/Image/Logo Cliproco.svg" alt="CliProCo Logo" height="30">
            CliProCo
        </a>
        <div class="d-flex w-100">
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/clients" class="btn btn-dark">Clients</a>
                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-dark">Prospects</a>
            </div>
            <div class="ms-auto">
                <div id="user-section" class="d-flex">
                    <form id="login-form" class="d-flex">
                        <label for="username"></label><input type="text" id="username" class="form-control me-2" placeholder="Identifiant">
                        <label for="password"></label><input type="password" id="password" class="form-control me-2" placeholder="Mot de passe">
                        <button id="login-btn" class="btn btn-light">Connexion</button>
                    </form>
                    <button id="logout-btn" class="btn btn-danger ms-2" style="display:none;">Déconnexion</button>
                </div>
            </div>
        </div>
    </div>
</nav>

<main class="container mt-5 pt-4 text-center">
    <img src="${pageContext.request.contextPath}/Image/Logo Cliproco.svg" alt="Logo CliProCo" class="logo" width="300" height="auto">

    <h1 class="welcome-title">Bienvenue sur CliProCo</h1>
    <h4>l'application de gestion de clients et de prospects.</h4>
    <br>
    <p>Sélectionnez une catégorie dans la barre de navigation</p>
    <div id="weather" class="weather"></div>
</main>

<br><br><br><br><br>
<hr />
<section class="container">
    <h2>À propos de nous</h2>
    <br>
    <p>Depuis sa création, Cliproco accompagne les entreprises dans la gestion et le développement de leur relation client.</p>
    <br>
    <p>Spécialisés dans les solutions CRM, nous aidons les professionnels à structurer, analyser et optimiser leurs interactions avec leurs clients et prospects.</p>
    <br>
    <p>Grâce à notre expertise et à notre engagement envers l'innovation, nous avons conçu un outil intuitif et performant, adapté aux besoins des PME comme des grandes entreprises. Notre mission est simple : vous offrir une plateforme fiable et évolutive pour centraliser vos contacts, automatiser vos tâches et améliorer votre efficacité commerciale.</p>
    <br>
    <p>Chez CliProCo, nous plaçons l'humain au cœur de notre approche.</p>
    <br>
    <ul>
        <li>Expertise et innovation</li>
        <li>Sécurité et confidentialité des données</li>
        <li>Accompagnement sur mesure</li>
    </ul>
    <p>Nous croyons en une gestion client simplifiée, efficace et tournée vers l'avenir. Rejoignez-nous dès aujourd'hui et optimisez votre relation client avec CliProCo !</p>
    <p>📞 Une question ? Besoin d'une démonstration ? Contactez-nous dès maintenant !</p>
</section>
<hr />

<!-- Localisation -->
<div class="div-responsive">
    <div id="location"></div>
    <div id="map" style="height: 800px; margin: 400px 0;"></div>
</div>

<!-- Footer -->
<footer class="text-center text-lg-start text-white" style="background-color: #1c2331">
    <section class="d-flex justify-content-between p-4" style="background-color: #556462">
        <div class="me-5">
            <span>Restez connecté(e) à votre société préférée:</span>
        </div>

        <p class="text-muted">
            Les données personnelles collectées sont utilisées uniquement dans le cadre de la gestion des clients et prospects.
            Conformément au RGPD, vous disposez d'un droit d'accès, de rectification et de suppression de vos données.
        </p>

        <div>
            <a href="#" class="text-white me-4"><i class="fab fa-facebook-f"></i></a>
            <a href="#" class="text-white me-4"><i class="fab fa-twitter"></i></a>
            <a href="#" class="text-white me-4"><i class="fab fa-google"></i></a>
            <a href="#" class="text-white me-4"><i class="fab fa-instagram"></i></a>
            <a href="#" class="text-white me-4"><i class="fab fa-linkedin"></i></a>
            <a href="#" class="text-white me-4"><i class="fab fa-github"></i></a>
        </div>
    </section>
    <section>
        <div class="container text-center text-md-start mt-5">
            <div class="row mt-3">
                <div class="col-md-3 col-lg-4 col-xl-3 mx-auto mb-4">
                    <h6 class="text-uppercase fw-bold">CliProCo</h6>
                    <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 70px; background-color: #556462; height: 2px"/>
                    <p>Depuis sa création, notre société a toujours eu pour objectif de répondre aux besoins spécifiques de ses clients tout en anticipant les attentes futures.</p>
                </div>
                <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4">
                    <h6 class="text-uppercase fw-bold">Production</h6>
                    <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #556462; height: 2px"/>
                    <p><a href="#" class="text-white">MDBootstrap</a></p>
                    <p><a href="#" class="text-white">MDWordPress</a></p>
                    <p><a href="#" class="text-white">BrandFlow</a></p>
                    <p><a href="#" class="text-white">Bootstrap Angular</a></p>
                </div>
                <div class="col-md-3 col-lg-2 col-xl-2 mx-auto mb-4">
                    <h6 class="text-uppercase fw-bold">Liens Utiles</h6>
                    <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #556462; height: 2px"/>
                    <p><a href="#" class="text-white">Votre compte</a></p>
                    <p><a href="#" class="text-white">Prenez un abonnement</a></p>
                    <p><a href="#" class="text-white">Ventes préférées</a></p>
                    <p><a href="#" class="text-white">Aide</a></p>
                </div>
                <div class="col-md-4 col-lg-3 col-xl-3 mx-auto mb-md-0 mb-4">
                    <h6 class="text-uppercase fw-bold">Contact</h6>
                    <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #556462; height: 2px"/>
                    <p><i class="fas fa-home mr-3"></i> LaxouVille, 45454, FR</p>
                    <p><i class="fas fa-envelope mr-3"></i> infoSocieteClientProspect@hotmail.com</p>
                    <p><i class="fas fa-phone mr-3"></i> + 01 234 567 88</p>
                    <p><i class="fas fa-print mr-3"></i> + 01 234 567 89</p>
                </div>
            </div>
        </div>
    </section>
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2)">
        © 2020 Copyright: <a class="text-white" href="https://mdbootstrap.com/">MDBootstrap.com</a>
    </div>
</footer>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/bootstrap/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/auth.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/main.mjs"></script>
<script type="module" src="${pageContext.request.contextPath}/js/geolocWeather.mjs"></script>
</body>
</html> 