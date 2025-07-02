<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Clients & Prospects</title>

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <meta name="description" content="Application de gestion des clients et prospects avec géolocalisation et météo">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <div class="d-flex w-100">
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/" class="btn btn-light active">Accueil</a>
                <a href="${pageContext.request.contextPath}/clients" class="btn btn-light">Clients</a>
                <a href="${pageContext.request.contextPath}/prospects" class="btn btn-light">Prospects</a>
            </div>
            <div class="ms-auto">
                <button id="btnAuth" class="btn btn-light" data-bs-toggle="modal" data-bs-target="#authModal">Connexion</button>
            </div>
        </div>
    </div>
</nav>

<!-- Modal de connexion -->
<div class="modal fade" id="authModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Connexion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="authForm" action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Utilisateur</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Mot de passe</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Se connecter</button>
                </form>
            </div>
        </div>
    </div>
</div>

<main class="container mt-4 text-center">
    <img src="${pageContext.request.contextPath}/img/Logo Cliproco.svg" alt="Logo CliProCo" class="logo" width="300" height="auto">

    <h1 class="welcome-title">Bienvenue sur CliProCo</h1>
    <h4>l'application de gestion de clients et de prospects.</h4>
    <br>
    <p>Sélectionnez une catégorie dans la barre de navigation</p>
    <div id="weather" class="weather"></div>
</main>
<br><br><br><br><br>
<hr />
<section>
    <h2>À propos de nous</h2>
    <br>
    <p>Depuis sa création, Cliproco accompagne les entreprises dans la gestion et le développement de leur relation client.</p>
    <br>
    <p>Spécialisés dans les solutions CRM, nous aidons les professionnels à structurer, analyser et optimiser leurs interactions avec leurs clients et prospects.</p>
    <br>
    <p>Grâce à notre expertise et à notre engagement envers l'innovation, nous avons conçu un outil intuitif et performant, adapté aux besoins des PME comme des grandes entreprises. Notre mission est simple : vous offrir une plateforme fiable et évolutive pour centraliser vos contacts, automatiser vos tâches et améliorer votre efficacité commerciale.</p>
    <br>
    <p>Chez [Nom de l'entreprise], nous plaçons l'humain au cœur de notre approche.</p>
    <br>
    <ul>
        <li>Expertise et innovation</li>
        <li>Sécurité et confidentialité des données</li>
        <li>Accompagnement sur mesure</li>
    </ul>
    <p>Nous croyons en une gestion client simplifiée, efficace et tournée vers l'avenir. Rejoignez-nous dès aujourd'hui et optimisez votre relation client avec [Nom de l'entreprise] !</p>
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

<!-- Bandeau RGPD -->
<div id="rgpdBanner" class="fixed-bottom bg-dark text-light p-3" style="display: none;" role="alert" aria-live="polite">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-9">
                <p class="mb-0">
                    Ce site utilise des cookies pour améliorer votre expérience. En continuant à naviguer sur ce site, vous acceptez notre utilisation des cookies.
                    Nous collectons uniquement les données nécessaires au bon fonctionnement du site (géolocalisation et météo).
                    Ces données ne sont pas partagées avec des tiers et sont stockées localement sur votre navigateur.
                </p>
            </div>
            <div class="col-md-3 text-end">
                <button id="acceptCookies" class="btn btn-primary">Accepter</button>
                <button id="refuseCookies" class="btn btn-secondary ms-2">Refuser</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="scripts.jsp"/>
</body>
</html> 