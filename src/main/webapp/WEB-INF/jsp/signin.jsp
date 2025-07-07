<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Créer un compte</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<main class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header text-center">
                    <h3>Créer un compte</h3>
                </div>
                <div class="card-body">
                    <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post">
                        <!-- Token CSRF pour la sécurité -->
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        
                        <div class="mb-3">
                            <label class="form-label">Je m'inscris en tant que :</label><br>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="userType" id="typeClient" value="client" checked>
                                <label class="form-check-label" for="typeClient">Client</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="userType" id="typeProspect" value="prospect">
                                <label class="form-check-label" for="typeProspect">Prospect</label>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="username" class="form-label">Nom d'utilisateur</label>
                            <input type="text" class="form-control" id="username" name="username" required minlength="3" maxlength="30">
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" required pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$" placeholder="exemple@domaine.fr">
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Mot de passe</label>
                            <input type="password" class="form-control" id="password" name="password" required minlength="6">
                        </div>
                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom</label>
                            <input type="text" class="form-control" id="nom" name="nom" required minlength="2" maxlength="50">
                        </div>
                        <div class="mb-3">
                            <label for="prenom" class="form-label">Prénom</label>
                            <input type="text" class="form-control" id="prenom" name="prenom" required minlength="2" maxlength="50">
                        </div>
                        <div class="mb-3">
                            <label for="telephone" class="form-label">Téléphone</label>
                            <input type="tel" class="form-control" id="telephone" name="telephone"
                                   pattern="^(?:(?:\+33|0033)[1-9]|0[1-9])(?:[ .-]?\d{2}){4}$"
                                   placeholder="Ex : 0612345678 ou +33612345678"
                                   title="Numéro français ou international, ex : 0612345678, +33612345678, 0033612345678"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label for="adresse" class="form-label">Adresse</label>
                            <input type="text" class="form-control" id="adresse" name="adresse" required>
                        </div>
                        <div class="mb-3">
                            <label for="codePostal" class="form-label">Code postal</label>
                            <input type="text" class="form-control" id="codePostal" name="codePostal"
                                   pattern="^(?:0[1-9]|[1-8][0-9]|9[0-8])\d{3}$|^97[1-8]\d{2}$|^98[46-8]\d{2}$"
                                   placeholder="Ex : 75001, 20000, 97100"
                                   title="Code postal français à 5 chiffres, y compris DOM/TOM et Corse"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label for="ville" class="form-label">Ville</label>
                            <input type="text" class="form-control" id="ville" name="ville" required>
                        </div>
                        <!-- Champs spécifiques client -->
                        <div id="clientFields">
                            <div class="mb-3">
                                <label for="raisonSociale" class="form-label">Raison sociale</label>
                                <input type="text" class="form-control" id="raisonSociale" name="raisonSociale">
                            </div>
                            <div class="mb-3">
                                <label for="chiffreAffaire" class="form-label">Chiffre d'affaires</label>
                                <input type="number" class="form-control" id="chiffreAffaire" name="chiffreAffaire" min="0">
                            </div>
                            <div class="mb-3">
                                <label for="nbrEmploye" class="form-label">Nombre d'employés</label>
                                <input type="number" class="form-control" id="nbrEmploye" name="nbrEmploye" min="1">
                            </div>
                        </div>
                        <!-- Champs spécifiques prospect -->
                        <div id="prospectFields" style="display:none;">
                            <div class="mb-3">
                                <label for="dateProspection" class="form-label">Date de prospection</label>
                                <input type="date" class="form-control" id="dateProspection" name="dateProspection">
                            </div>
                            <div class="mb-3">
                                <label for="prospectInteresse" class="form-label">Prospect intéressé</label>
                                <select class="form-select" id="prospectInteresse" name="prospectInteresse">
                                    <option value="">-- Sélectionner --</option>
                                    <option value="oui">Oui</option>
                                    <option value="non">Non</option>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Créer le compte</button>
                    </form>
                    <div class="mt-3 text-center">
                        <a href="?cmd=connexion" class="btn btn-link">Déjà un compte ? Se connecter</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script>
    // Affichage dynamique des champs selon le type choisi
    document.querySelectorAll('input[name="userType"]').forEach(function(radio) {
        radio.addEventListener('change', function() {
            if (this.value === 'client') {
                document.getElementById('clientFields').style.display = '';
                document.getElementById('prospectFields').style.display = 'none';
            } else {
                document.getElementById('clientFields').style.display = 'none';
                document.getElementById('prospectFields').style.display = '';
            }
        });
    });
</script>
</body>
</html> 