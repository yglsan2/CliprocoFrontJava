/**
 * Système de validation robuste avec bulles d'information contextuelles
 */

// ===== CONFIGURATION =====
const VALIDATION_CONFIG = {
    // Délai avant affichage de la bulle (ms)
    tooltipDelay: 300,
    
    // Durée d'affichage de la bulle (ms)
    tooltipDuration: 1500,
    
    // Classes CSS
    classes: {
        error: 'validation-error',
        success: 'validation-success',
        tooltip: 'validation-tooltip',
        tooltipError: 'validation-tooltip-error',
        tooltipSuccess: 'validation-tooltip-success',
        fieldError: 'field-error',
        fieldSuccess: 'field-success'
    },
    
    // Messages d'erreur par défaut
    messages: {
        email: {
            required: "L'adresse email est obligatoire.",
            invalid: "Format d'email invalide. Exemple : nom@domaine.com",
            tooLong: "L'adresse email ne peut pas dépasser 254 caractères."
        },
        phone: {
            required: "Le numéro de téléphone est obligatoire.",
            invalid: "Format de téléphone invalide. Formats acceptés : 0612345678, +33612345678",
            tooShort: "Le numéro de téléphone doit contenir au moins 10 chiffres.",
            tooLong: "Le numéro de téléphone ne peut pas dépasser 15 caractères."
        },
        postal: {
            required: "Le code postal est obligatoire.",
            invalid: "Code postal invalide. Format attendu : 5 chiffres (ex: 75001)",
            wrongLength: "Le code postal doit contenir exactement 5 caractères."
        },
        name: {
            required: "Le nom/prénom est obligatoire.",
            invalid: "Le nom/prénom ne peut contenir que des lettres, espaces, tirets et caractères accentués.",
            tooShort: "Le nom/prénom doit contenir au moins 2 caractères.",
            tooLong: "Le nom/prénom ne peut pas dépasser 50 caractères."
        },
        company: {
            required: "La raison sociale est obligatoire.",
            invalid: "La raison sociale contient des caractères non autorisés.",
            tooShort: "La raison sociale doit contenir au moins 2 caractères.",
            tooLong: "La raison sociale ne peut pas dépasser 200 caractères."
        },
        address: {
            required: "L'adresse est obligatoire.",
            invalid: "L'adresse contient des caractères non autorisés.",
            tooShort: "L'adresse doit contenir au moins 5 caractères.",
            tooLong: "L'adresse ne peut pas dépasser 255 caractères."
        },
        city: {
            required: "La ville est obligatoire.",
            invalid: "La ville ne peut contenir que des lettres, espaces, tirets et caractères accentués.",
            tooShort: "La ville doit contenir au moins 2 caractères.",
            tooLong: "La ville ne peut pas dépasser 100 caractères."
        },
        country: {
            required: "Le pays est obligatoire.",
            invalid: "Le pays ne peut contenir que des lettres, espaces, tirets et caractères accentués.",
            tooShort: "Le pays doit contenir au moins 2 caractères.",
            tooLong: "Le pays ne peut pas dépasser 100 caractères."
        },
        streetnumber: {
            required: "Le numéro de rue est obligatoire.",
            invalid: "Format de numéro de rue invalide. Exemple : 123, 12B, A, etc.",
            tooLong: "Le numéro de rue ne peut pas dépasser 10 caractères."
        },
        amount: {
            required: "Le montant est obligatoire.",
            negative: "Le montant doit être positif.",
            invalid: "Format de montant invalide."
        }
    },
    
    // Suggestions par type de champ
    suggestions: {
        email: "Vérifiez que l'email contient un @ et un domaine valide (ex: nom@domaine.com)",
        phone: "Utilisez 0612345678 (national) ou +33612345678 (international)",
        postal: "Utilisez 5 chiffres (ex: 75001 pour Paris, 69001 pour Lyon)",
        name: "Utilisez uniquement des lettres, espaces, tirets (-) et apostrophes (')",
        company: "Utilisez des lettres, chiffres, espaces et caractères spéciaux courants",
        address: "Utilisez des lettres, chiffres, espaces et caractères spéciaux courants",
        city: "Utilisez uniquement des lettres, espaces, tirets et caractères accentués",
        country: "Utilisez uniquement des lettres, espaces, tirets et caractères accentués",
        streetnumber: "Utilisez des chiffres et éventuellement une lettre (ex: 123, 12B)",
        amount: "Utilisez des chiffres et une virgule pour les décimales"
    }
};

// ===== CLASSE PRINCIPALE DE VALIDATION =====
class RobustValidator {
    constructor() {
        this.tooltips = new Map();
        this.validationTimers = new Map();
        this.init();
    }
    
    init() {
        this.setupGlobalStyles();
        this.bindEvents();
    }
    
    // ===== SETUP =====
    
    setupGlobalStyles() {
        const style = document.createElement('style');
        style.textContent = `
            .validation-tooltip {
                position: absolute;
                background: #333;
                color: white;
                padding: 8px 12px;
                border-radius: 4px;
                font-size: 12px;
                max-width: 300px;
                z-index: 10000;
                box-shadow: 0 2px 8px rgba(0,0,0,0.3);
                opacity: 0;
                transition: opacity 0.3s ease;
                pointer-events: auto;
            }
            
            .validation-tooltip::before {
                content: '';
                position: absolute;
                top: -5px;
                left: 10px;
                width: 0;
                height: 0;
                border-left: 5px solid transparent;
                border-right: 5px solid transparent;
                border-bottom: 5px solid #333;
            }
            
            .validation-tooltip-error {
                background: #d32f2f;
                border-left: 4px solid #b71c1c;
            }
            
            .validation-tooltip-error::before {
                border-bottom-color: #d32f2f;
            }
            
            .validation-tooltip-success {
                background: #388e3c;
                border-left: 4px solid #2e7d32;
            }
            
            .validation-tooltip-success::before {
                border-bottom-color: #388e3c;
            }
            
            .field-error {
                border-color: #d32f2f !important;
                box-shadow: 0 0 0 2px rgba(211, 47, 47, 0.2) !important;
            }
            
            .field-success {
                border-color: #388e3c !important;
                box-shadow: 0 0 0 2px rgba(56, 142, 60, 0.2) !important;
            }
            
            .validation-icon {
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 16px;
            }
            
            .validation-icon.error {
                color: #d32f2f;
            }
            
            .validation-icon.success {
                color: #388e3c;
            }
        `;
        document.head.appendChild(style);
    }
    
    bindEvents() {
        // Validation au blur
        document.addEventListener('blur', (e) => {
            if (e.target && e.target.dataset && e.target.dataset.validate) {
                this.validateField(e.target);
                // Ne pas masquer immédiatement le tooltip - laisser l'auto-hide s'en charger
            }
        }, true);
        
        // Validation au focus - seulement si le champ est vide ET pas de tooltip déjà affiché
        document.addEventListener('focus', (e) => {
            if (e.target && e.target.dataset && e.target.dataset.validate && !e.target.value.trim() && !this.tooltips.has(e.target)) {
                // Délai plus court pour l'aide au focus
                setTimeout(() => {
                    if (e.target === document.activeElement && !e.target.value.trim()) {
                this.showTooltip(e.target);
                    }
                }, 200);
            }
        }, true);
        
        // Masquer les tooltips quand on commence à taper
        document.addEventListener('input', (e) => {
            if (e.target && e.target.dataset && e.target.dataset.validate && this.tooltips.has(e.target)) {
                const tooltip = this.tooltips.get(e.target);
                if (tooltip && !tooltip.classList.contains('validation-tooltip-success') && !tooltip.classList.contains('validation-tooltip-error')) {
                    this.hideTooltip(e.target);
                }
            }
        });
        
        // Masquer les tooltips au clic ailleurs - mais permettre la saisie
        document.addEventListener('click', (e) => {
            // Masquer tous les tooltips sauf si on clique sur un champ de saisie
            if (!e.target.closest('.validation-tooltip') && (!e.target.dataset || !e.target.dataset.validate) && !e.target.tagName.match(/^(INPUT|TEXTAREA|SELECT)$/)) {
                this.hideAllTooltips();
            }
        });
        
        // Masquer les tooltips quand on change de focus
        document.addEventListener('blur', (e) => {
            if (e.target && e.target.dataset && e.target.dataset.validate) {
                // Masquer le tooltip après un court délai
                setTimeout(() => {
                    if (e.target !== document.activeElement) {
                        this.hideTooltip(e.target);
                    }
                }, 100);
            }
        }, true);
        
        // Masquer les tooltips avec la touche Escape
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.hideAllTooltips();
            }
        });
        
        // Masquer les tooltips lors du scroll
        document.addEventListener('scroll', () => {
            this.hideAllTooltips();
        });
        
        // Masquer les tooltips lors du changement de focus
        document.addEventListener('focusin', (e) => {
            // Masquer tous les tooltips sauf celui du champ actuel
            this.tooltips.forEach((tooltip, field) => {
                if (field !== e.target) {
                    this.hideTooltip(field);
                }
            });
        });
    }
    
    // ===== VALIDATION =====
    
    debounceValidation(field) {
        const timerId = this.validationTimers.get(field);
        if (timerId) {
            clearTimeout(timerId);
        }
        
        const newTimerId = setTimeout(() => {
            this.validateField(field);
        }, VALIDATION_CONFIG.tooltipDelay);
        
        this.validationTimers.set(field, newTimerId);
    }
    
    validateField(field) {
        const fieldType = field.dataset.validate;
        const value = field.value.trim();
        
        // Validation côté client
        const result = this.clientValidate(fieldType, value, field.name);
        
        // Mise à jour visuelle
        this.updateFieldVisual(field, result);
        
        // Affichage du tooltip
        this.showTooltip(field, result);
        
        return result;
    }
    
    clientValidate(fieldType, value, fieldName) {
        // Validation de base
        if (!value) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages[fieldType]?.required || "Ce champ est obligatoire.",
                errorType: 'required'
            };
        }
        
        // Validation spécifique par type
        switch (fieldType) {
            case 'email':
                return this.validateEmail(value, fieldName);
            case 'phone':
                return this.validatePhone(value, fieldName);
            case 'postal':
                return this.validatePostalCode(value, fieldName);
            case 'name':
                return this.validateName(value, fieldName);
            case 'company':
                return this.validateCompany(value, fieldName);
            case 'address':
                return this.validateAddress(value, fieldName);
            case 'city':
                return this.validateCity(value, fieldName);
            case 'country':
                return this.validateCountry(value, fieldName);
            case 'streetnumber':
                return this.validateStreetNumber(value, fieldName);
            case 'amount':
                return this.validateAmount(value, fieldName);
            default:
                return { valid: true, message: "Validation réussie" };
        }
    }
    
    // ===== VALIDATIONS SPÉCIFIQUES =====
    
    validateEmail(value, fieldName) {
        const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
        
        if (value.length > 254) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.email.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!emailRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.email.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Email valide" };
    }
    
    validatePhone(value, fieldName) {
        // Regex plus permissive pour les numéros français
        const phoneRegex = /^(?:(?:\+33|0033|33)[1-9](?:[0-9]{8})|0[1-9](?:[0-9]{8}))$/;
        
        // Nettoyer la valeur (enlever les espaces, tirets, points)
        const cleanValue = value.replace(/[\s\-\.]/g, '');
        
        if (cleanValue.length < 10) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.phone.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (cleanValue.length > 15) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.phone.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!phoneRegex.test(cleanValue)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.phone.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Numéro de téléphone valide" };
    }
    
    validatePostalCode(value, fieldName) {
        if (value.length !== 5) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.postal.wrongLength,
                errorType: 'wrongLength'
            };
        }
        
        // Validation spécifique pour 20000
        if (value === '20000') {
            return {
                valid: false,
                message: "Le code postal 20000 n'est pas valide.",
                errorType: 'invalidValue'
            };
        }
        
        // Validation Corse
        if (value.startsWith('2A') || value.startsWith('2B')) {
            return { valid: true, message: "Code postal Corse valide" };
        }
        
        // Validation métropolitain
        const metroRegex = /^(0[1-9]|[1-8][0-9]|9[0-5])[0-9]{3}$/;
        if (metroRegex.test(value)) {
            if (value.startsWith('20')) {
                return {
                    valid: false,
                    message: "Le code postal 20xxx n'est pas valide (sauf 2Axxx et 2Bxxx).",
                    errorType: 'invalidValue'
                };
            }
            return { valid: true, message: "Code postal métropolitain valide" };
        }
        
        // Validation DOM-TOM
        const domtomRegex = /^(97[0-8]|98[0-9]|99[0-9])[0-9]{2}$/;
        if (domtomRegex.test(value)) {
            return { valid: true, message: "Code postal DOM-TOM valide" };
        }
        
        return {
            valid: false,
            message: VALIDATION_CONFIG.messages.postal.invalid,
            errorType: 'invalidFormat'
        };
    }
    
    validateName(value, fieldName) {
        const nameRegex = /^[a-zA-ZÀ-ÿ\s\-']+$/;
        
        if (value.length < 2) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.name.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (value.length > 50) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.name.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!nameRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.name.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Nom/prénom valide" };
    }
    
    validateCompany(value, fieldName) {
        const companyRegex = /^[a-zA-ZÀ-ÿ0-9\s\-'&.,()]+$/;
        
        if (value.length < 2) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.company.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (value.length > 200) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.company.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!companyRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.company.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Raison sociale valide" };
    }
    
    validateAddress(value, fieldName) {
        const addressRegex = /^[a-zA-ZÀ-ÿ0-9\s'.,()-]+$/;
        
        if (value.length < 5) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.address.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (value.length > 255) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.address.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!addressRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.address.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Adresse valide" };
    }
    
    validateCity(value, fieldName) {
        const cityRegex = /^[a-zA-ZÀ-ÿ\s'-]+$/;
        
        if (value.length < 2) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.city.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (value.length > 100) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.city.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!cityRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.city.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Ville valide" };
    }
    
    validateCountry(value, fieldName) {
        const countryRegex = /^[a-zA-ZÀ-ÿ\s'-]+$/;
        
        if (value.length < 2) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.country.tooShort,
                errorType: 'tooShort'
            };
        }
        
        if (value.length > 100) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.country.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!countryRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.country.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Pays valide" };
    }
    
    validateStreetNumber(value, fieldName) {
        // Regex plus permissive pour les numéros de rue
        const streetNumberRegex = /^[0-9]+[a-zA-Z]?$|^[a-zA-Z][0-9]*$/;
        
        if (value.length > 10) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.streetnumber.tooLong,
                errorType: 'tooLong'
            };
        }
        
        if (!streetNumberRegex.test(value)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.streetnumber.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        return { valid: true, message: "Numéro de rue valide" };
    }
    
    validateAmount(value, fieldName) {
        const amount = parseFloat(value.replace(',', '.'));
        
        if (isNaN(amount)) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.amount.invalid,
                errorType: 'invalidFormat'
            };
        }
        
        if (amount <= 0) {
            return {
                valid: false,
                message: VALIDATION_CONFIG.messages.amount.negative,
                errorType: 'negativeValue'
            };
        }
        
        return { valid: true, message: "Montant valide" };
    }
    
    // ===== VISUEL =====
    
    updateFieldVisual(field, result) {
        // Supprimer les classes précédentes
        field.classList.remove(VALIDATION_CONFIG.classes.fieldError, VALIDATION_CONFIG.classes.fieldSuccess);
        
        // Ajouter la nouvelle classe
        if (result.valid) {
            field.classList.add(VALIDATION_CONFIG.classes.fieldSuccess);
        } else {
            field.classList.add(VALIDATION_CONFIG.classes.fieldError);
        }
        
        // Mettre à jour l'icône
        this.updateValidationIcon(field, result);
    }
    
    updateValidationIcon(field, result) {
        let icon = field.parentNode.querySelector('.validation-icon');
        
        if (!icon) {
            icon = document.createElement('span');
            icon.className = 'validation-icon';
            field.parentNode.style.position = 'relative';
            field.parentNode.appendChild(icon);
        }
        
        icon.className = 'validation-icon ' + (result.valid ? 'success' : 'error');
        icon.innerHTML = result.valid ? '✓' : '✗';
    }
    
    // ===== TOOLTIPS =====
    
    showTooltip(field, result = null) {
        // Masquer les autres tooltips
        this.hideAllTooltips();
        
        // Créer le tooltip
        const tooltip = document.createElement('div');
        tooltip.className = VALIDATION_CONFIG.classes.tooltip;
        
        if (result) {
            tooltip.classList.add(result.valid ? 
                VALIDATION_CONFIG.classes.tooltipSuccess : 
                VALIDATION_CONFIG.classes.tooltipError);
            
            // Message principal
            tooltip.innerHTML = result.message;
            
            // Ajouter les suggestions si erreur
            if (!result.valid && VALIDATION_CONFIG.suggestions[field.dataset.validate]) {
                tooltip.innerHTML += '<br><small>💡 ' + VALIDATION_CONFIG.suggestions[field.dataset.validate] + '</small>';
            }
        } else {
            // Message d'aide au focus - seulement si le champ est vide
            const fieldType = field.dataset.validate;
            if (!field.value.trim()) {
                tooltip.innerHTML = VALIDATION_CONFIG.suggestions[fieldType] || "Saisissez une valeur valide.";
            } else {
                // Ne pas afficher de tooltip si le champ a déjà une valeur
                return;
            }
        }
        
        // Positionner le tooltip
        this.positionTooltip(tooltip, field);
        
        // Ajouter au DOM
        document.body.appendChild(tooltip);
        
        // Animation d'apparition
        setTimeout(() => {
            tooltip.style.opacity = '1';
        }, 10);
        
        // Stocker la référence
        this.tooltips.set(field, tooltip);
        
        // Auto-hide : tous les tooltips s'effacent après 1.5 secondes
        setTimeout(() => {
            this.hideTooltip(field);
        }, VALIDATION_CONFIG.tooltipDuration);
    }
    
    hideTooltip(field) {
        const tooltip = this.tooltips.get(field);
        if (tooltip) {
            tooltip.style.opacity = '0';
            setTimeout(() => {
                if (tooltip.parentNode) {
                    tooltip.parentNode.removeChild(tooltip);
                }
                this.tooltips.delete(field);
            }, 300);
        }
    }
    
    hideAllTooltips() {
        this.tooltips.forEach((tooltip, field) => {
            this.hideTooltip(field);
        });
    }
    
    positionTooltip(tooltip, field) {
        const rect = field.getBoundingClientRect();
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        const scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;
        
        tooltip.style.left = (rect.left + scrollLeft) + 'px';
        tooltip.style.top = (rect.bottom + scrollTop + 5) + 'px';
    }
    
    // ===== API PUBLIQUE =====
    
    validateForm(form) {
        const fields = form.querySelectorAll('[data-validate]');
        let isValid = true;
        
        fields.forEach(field => {
            const result = this.validateField(field);
            if (!result.valid) {
                isValid = false;
            }
        });
        
        return isValid;
    }
    
    addValidationToField(field, fieldType) {
        field.dataset.validate = fieldType;
        this.validateField(field);
    }
    
    removeValidationFromField(field) {
        delete field.dataset.validate;
        field.classList.remove(VALIDATION_CONFIG.classes.fieldError, VALIDATION_CONFIG.classes.fieldSuccess);
        
        const icon = field.parentNode.querySelector('.validation-icon');
        if (icon) {
            icon.remove();
        }
        
        this.hideTooltip(field);
    }
}

// ===== INITIALISATION =====
const validator = new RobustValidator();

// Exposer l'API globale
window.RobustValidator = validator;

// Auto-initialisation pour les champs existants
document.addEventListener('DOMContentLoaded', () => {
    const fields = document.querySelectorAll('[data-validate]');
    fields.forEach(field => {
        validator.validateField(field);
    });
});

export default validator;
