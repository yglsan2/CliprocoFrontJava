// Classe pour gérer la validation des formulaires
class FormValidator {
    constructor() {
        this.patterns = {
            email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            phone: /^(\+33|0)[1-9](\d{2}){4}$/,
            postalCode: /^[0-9]{5}$/,
            password: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
        };

        this.messages = {
            required: 'Ce champ est obligatoire',
            email: 'Veuillez entrer une adresse email valide',
            phone: 'Veuillez entrer un numéro de téléphone valide',
            postalCode: 'Veuillez entrer un code postal valide',
            password: 'Le mot de passe doit contenir au moins 8 caractères, une lettre et un chiffre',
            minLength: (min) => `Ce champ doit contenir au moins ${min} caractères`,
            maxLength: (max) => `Ce champ ne doit pas dépasser ${max} caractères`,
            min: (min) => `La valeur doit être supérieure à ${min}`,
            max: (max) => `La valeur doit être inférieure à ${max}`
        };
    }

    // Initialiser la validation pour un formulaire
    initForm(formId) {
        const form = document.getElementById(formId);
        if (!form) return;

        // Ajouter les écouteurs d'événements
        form.addEventListener('submit', (e) => this.handleSubmit(e));
        form.addEventListener('input', (e) => this.validateField(e.target));

        // Ajouter la validation en temps réel pour tous les champs
        form.querySelectorAll('input, select, textarea').forEach(field => {
            field.addEventListener('blur', (e) => this.validateField(e.target));
        });
    }

    // Gérer la soumission du formulaire
    handleSubmit(event) {
        event.preventDefault();
        const form = event.target;
        const isValid = this.validateForm(form);

        if (isValid) {
            form.submit();
        }
    }

    // Valider un champ spécifique
    validateField(field) {
        const value = field.value.trim();
        const rules = this.getFieldRules(field);
        let isValid = true;
        let errorMessage = '';

        // Vérifier si le champ est requis
        if (field.required && !value) {
            isValid = false;
            errorMessage = this.messages.required;
        }

        // Vérifier le type de validation
        if (isValid && value) {
            switch (field.type) {
                case 'email':
                    if (!this.patterns.email.test(value)) {
                        isValid = false;
                        errorMessage = this.messages.email;
                    }
                    break;

                case 'tel':
                    if (!this.patterns.phone.test(value)) {
                        isValid = false;
                        errorMessage = this.messages.phone;
                    }
                    break;

                case 'number':
                    const numValue = parseFloat(value);
                    if (rules.min !== undefined && numValue < rules.min) {
                        isValid = false;
                        errorMessage = this.messages.min(rules.min);
                    }
                    if (rules.max !== undefined && numValue > rules.max) {
                        isValid = false;
                        errorMessage = this.messages.max(rules.max);
                    }
                    break;

                case 'password':
                    if (!this.patterns.password.test(value)) {
                        isValid = false;
                        errorMessage = this.messages.password;
                    }
                    break;
            }

            // Vérifier la longueur minimale et maximale
            if (rules.minLength !== undefined && value.length < rules.minLength) {
                isValid = false;
                errorMessage = this.messages.minLength(rules.minLength);
            }
            if (rules.maxLength !== undefined && value.length > rules.maxLength) {
                isValid = false;
                errorMessage = this.messages.maxLength(rules.maxLength);
            }
        }

        // Mettre à jour l'affichage de l'erreur
        this.updateFieldError(field, isValid, errorMessage);

        return isValid;
    }

    // Valider tous les champs du formulaire
    validateForm(form) {
        let isValid = true;
        form.querySelectorAll('input, select, textarea').forEach(field => {
            if (!this.validateField(field)) {
                isValid = false;
            }
        });
        return isValid;
    }

    // Obtenir les règles de validation pour un champ
    getFieldRules(field) {
        const rules = {};
        
        // Récupérer les attributs data-* pour les règles personnalisées
        if (field.dataset.minLength) rules.minLength = parseInt(field.dataset.minLength);
        if (field.dataset.maxLength) rules.maxLength = parseInt(field.dataset.maxLength);
        if (field.dataset.min) rules.min = parseFloat(field.dataset.min);
        if (field.dataset.max) rules.max = parseFloat(field.dataset.max);

        return rules;
    }

    // Mettre à jour l'affichage de l'erreur pour un champ
    updateFieldError(field, isValid, errorMessage) {
        const errorDiv = field.nextElementSibling;
        
        if (!isValid) {
            field.classList.add('is-invalid');
            if (!errorDiv || !errorDiv.classList.contains('invalid-feedback')) {
                const newErrorDiv = document.createElement('div');
                newErrorDiv.className = 'invalid-feedback';
                newErrorDiv.textContent = errorMessage;
                field.parentNode.insertBefore(newErrorDiv, field.nextSibling);
            } else {
                errorDiv.textContent = errorMessage;
            }
        } else {
            field.classList.remove('is-invalid');
            if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
                errorDiv.remove();
            }
        }
    }

    // Valider un champ de code postal
    validatePostalCode(value) {
        return this.patterns.postalCode.test(value);
    }

    // Valider un champ de mot de passe
    validatePassword(value) {
        return this.patterns.password.test(value);
    }

    // Valider un champ d'email
    validateEmail(value) {
        return this.patterns.email.test(value);
    }

    // Valider un champ de téléphone
    validatePhone(value) {
        return this.patterns.phone.test(value);
    }
}

// Initialiser le validateur de formulaires
const formValidator = new FormValidator();

// Exporter pour utilisation dans d'autres fichiers
window.formValidator = formValidator;

// Initialiser la validation pour tous les formulaires au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('form').forEach(form => {
        if (form.id) {
            formValidator.initForm(form.id);
        }
    });
}); 