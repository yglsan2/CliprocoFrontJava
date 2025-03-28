// Validation des formulaires
document.addEventListener('DOMContentLoaded', function() {
    // Validation du formulaire client
    const clientForm = document.getElementById('clientForm');
    if (clientForm) {
        clientForm.addEventListener('submit', validateClientForm);
    }

    // Validation du formulaire prospect
    const prospectForm = document.getElementById('prospectForm');
    if (prospectForm) {
        prospectForm.addEventListener('submit', validateProspectForm);
    }

    // Validation du formulaire utilisateur
    const userForm = document.getElementById('userForm');
    if (userForm) {
        userForm.addEventListener('submit', validateUserForm);
    }
});

// Validation du formulaire client
function validateClientForm(event) {
    event.preventDefault();
    
    const nom = document.getElementById('nom');
    const email = document.getElementById('email');
    const telephone = document.getElementById('telephone');
    const secteurActivite = document.getElementById('secteurActivite');
    const chiffreAffaires = document.getElementById('chiffreAffaires');
    
    let isValid = true;
    let errorMessage = '';

    // Validation du nom
    if (!nom.value.trim()) {
        isValid = false;
        errorMessage += 'Le nom est obligatoire.\n';
    } else if (nom.value.length < 2 || nom.value.length > 100) {
        isValid = false;
        errorMessage += 'Le nom doit contenir entre 2 et 100 caractères.\n';
    }

    // Validation de l'email
    if (!email.value.trim()) {
        isValid = false;
        errorMessage += 'L\'email est obligatoire.\n';
    } else if (!isValidEmail(email.value)) {
        isValid = false;
        errorMessage += 'L\'email n\'est pas valide.\n';
    }

    // Validation du téléphone
    if (!telephone.value.trim()) {
        isValid = false;
        errorMessage += 'Le numéro de téléphone est obligatoire.\n';
    } else if (!isValidPhone(telephone.value)) {
        isValid = false;
        errorMessage += 'Le numéro de téléphone n\'est pas valide.\n';
    }

    // Validation du secteur d'activité
    if (!secteurActivite.value.trim()) {
        isValid = false;
        errorMessage += 'Le secteur d\'activité est obligatoire.\n';
    } else if (secteurActivite.value.length > 50) {
        isValid = false;
        errorMessage += 'Le secteur d\'activité ne doit pas dépasser 50 caractères.\n';
    }

    // Validation du chiffre d'affaires
    if (!chiffreAffaires.value.trim()) {
        isValid = false;
        errorMessage += 'Le chiffre d\'affaires est obligatoire.\n';
    } else if (isNaN(chiffreAffaires.value) || parseFloat(chiffreAffaires.value) < 0) {
        isValid = false;
        errorMessage += 'Le chiffre d\'affaires doit être un nombre positif.\n';
    }

    if (!isValid) {
        showError(errorMessage);
    } else {
        event.target.submit();
    }
}

// Validation du formulaire prospect
function validateProspectForm(event) {
    event.preventDefault();
    
    const nom = document.getElementById('nom');
    const email = document.getElementById('email');
    const telephone = document.getElementById('telephone');
    const secteurActivite = document.getElementById('secteurActivite');
    const statut = document.getElementById('statut');
    
    let isValid = true;
    let errorMessage = '';

    // Validation du nom
    if (!nom.value.trim()) {
        isValid = false;
        errorMessage += 'Le nom est obligatoire.\n';
    } else if (nom.value.length < 2 || nom.value.length > 100) {
        isValid = false;
        errorMessage += 'Le nom doit contenir entre 2 et 100 caractères.\n';
    }

    // Validation de l'email
    if (!email.value.trim()) {
        isValid = false;
        errorMessage += 'L\'email est obligatoire.\n';
    } else if (!isValidEmail(email.value)) {
        isValid = false;
        errorMessage += 'L\'email n\'est pas valide.\n';
    }

    // Validation du téléphone
    if (!telephone.value.trim()) {
        isValid = false;
        errorMessage += 'Le numéro de téléphone est obligatoire.\n';
    } else if (!isValidPhone(telephone.value)) {
        isValid = false;
        errorMessage += 'Le numéro de téléphone n\'est pas valide.\n';
    }

    // Validation du secteur d'activité
    if (!secteurActivite.value.trim()) {
        isValid = false;
        errorMessage += 'Le secteur d\'activité est obligatoire.\n';
    } else if (secteurActivite.value.length > 50) {
        isValid = false;
        errorMessage += 'Le secteur d\'activité ne doit pas dépasser 50 caractères.\n';
    }

    // Validation du statut
    if (!statut.value) {
        isValid = false;
        errorMessage += 'Le statut est obligatoire.\n';
    }

    if (!isValid) {
        showError(errorMessage);
    } else {
        event.target.submit();
    }
}

// Validation du formulaire utilisateur
function validateUserForm(event) {
    event.preventDefault();
    
    const nom = document.getElementById('nom');
    const email = document.getElementById('email');
    const motDePasse = document.getElementById('motDePasse');
    const role = document.getElementById('role');
    
    let isValid = true;
    let errorMessage = '';

    // Validation du nom
    if (!nom.value.trim()) {
        isValid = false;
        errorMessage += 'Le nom est obligatoire.\n';
    } else if (nom.value.length < 2 || nom.value.length > 100) {
        isValid = false;
        errorMessage += 'Le nom doit contenir entre 2 et 100 caractères.\n';
    }

    // Validation de l'email
    if (!email.value.trim()) {
        isValid = false;
        errorMessage += 'L\'email est obligatoire.\n';
    } else if (!isValidEmail(email.value)) {
        isValid = false;
        errorMessage += 'L\'email n\'est pas valide.\n';
    }

    // Validation du mot de passe
    if (!motDePasse.value.trim()) {
        isValid = false;
        errorMessage += 'Le mot de passe est obligatoire.\n';
    } else if (motDePasse.value.length < 8) {
        isValid = false;
        errorMessage += 'Le mot de passe doit contenir au moins 8 caractères.\n';
    }

    // Validation du rôle
    if (!role.value) {
        isValid = false;
        errorMessage += 'Le rôle est obligatoire.\n';
    }

    if (!isValid) {
        showError(errorMessage);
    } else {
        event.target.submit();
    }
}

// Fonctions utilitaires
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function isValidPhone(phone) {
    const phoneRegex = /^[0-9+\s-()]{10,20}$/;
    return phoneRegex.test(phone);
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger';
    errorDiv.role = 'alert';
    errorDiv.textContent = message;
    
    const form = document.querySelector('form');
    form.insertBefore(errorDiv, form.firstChild);
    
    // Supprimer le message après 5 secondes
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
} 