// Classe pour gérer les cookies
class CookieManager {
    constructor() {
        this.cookieBanner = null;
        this.cookiePreferences = {
            necessary: true,
            analytics: false,
            marketing: false
        };
        this.init();
    }

    init() {
        // Créer la bannière de cookies si elle n'existe pas
        if (!document.getElementById('cookie-banner')) {
            this.createCookieBanner();
        }

        // Charger les préférences existantes
        this.loadCookiePreferences();

        // Vérifier si l'utilisateur a déjà fait un choix
        if (!this.hasUserConsent()) {
            this.showCookieBanner();
        }
    }

    createCookieBanner() {
        const banner = document.createElement('div');
        banner.id = 'cookie-banner';
        banner.className = 'cookie-banner';
        banner.innerHTML = `
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <p class="mb-0">
                            Nous utilisons des cookies pour améliorer votre expérience sur notre site. 
                            En continuant à naviguer, vous acceptez notre utilisation des cookies.
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <button class="btn btn-light me-2" onclick="cookieManager.showPreferences()">
                            <i class="fas fa-cog"></i> Préférences
                        </button>
                        <button class="btn btn-primary" onclick="cookieManager.acceptAll()">
                            <i class="fas fa-check"></i> Tout accepter
                        </button>
                    </div>
                </div>
            </div>
        `;
        document.body.appendChild(banner);
        this.cookieBanner = banner;
    }

    showCookieBanner() {
        if (this.cookieBanner) {
            this.cookieBanner.classList.add('show');
        }
    }

    hideCookieBanner() {
        if (this.cookieBanner) {
            this.cookieBanner.classList.remove('show');
        }
    }

    showPreferences() {
        // Créer le modal des préférences
        const modal = document.createElement('div');
        modal.className = 'modal fade';
        modal.id = 'cookie-preferences-modal';
        modal.innerHTML = `
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Préférences des cookies</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="cookie-preferences-form">
                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="necessary-cookies" 
                                           checked disabled>
                                    <label class="form-check-label" for="necessary-cookies">
                                        Cookies nécessaires
                                    </label>
                                </div>
                            </div>
                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="analytics-cookies">
                                    <label class="form-check-label" for="analytics-cookies">
                                        Cookies analytiques
                                    </label>
                                </div>
                            </div>
                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="marketing-cookies">
                                    <label class="form-check-label" for="marketing-cookies">
                                        Cookies marketing
                                    </label>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                        <button type="button" class="btn btn-primary" onclick="cookieManager.savePreferences()">
                            Enregistrer
                        </button>
                    </div>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();

        // Charger les préférences actuelles
        document.getElementById('analytics-cookies').checked = this.cookiePreferences.analytics;
        document.getElementById('marketing-cookies').checked = this.cookiePreferences.marketing;

        // Supprimer le modal quand il est fermé
        modal.addEventListener('hidden.bs.modal', () => {
            document.body.removeChild(modal);
        });
    }

    savePreferences() {
        this.cookiePreferences.analytics = document.getElementById('analytics-cookies').checked;
        this.cookiePreferences.marketing = document.getElementById('marketing-cookies').checked;
        this.saveCookiePreferences();
        this.hideCookieBanner();
        bootstrap.Modal.getInstance(document.getElementById('cookie-preferences-modal')).hide();
    }

    acceptAll() {
        this.cookiePreferences.analytics = true;
        this.cookiePreferences.marketing = true;
        this.saveCookiePreferences();
        this.hideCookieBanner();
    }

    hasUserConsent() {
        return localStorage.getItem('cookie-consent') === 'true';
    }

    loadCookiePreferences() {
        const savedPreferences = localStorage.getItem('cookie-preferences');
        if (savedPreferences) {
            this.cookiePreferences = JSON.parse(savedPreferences);
        }
    }

    saveCookiePreferences() {
        localStorage.setItem('cookie-consent', 'true');
        localStorage.setItem('cookie-preferences', JSON.stringify(this.cookiePreferences));
    }

    // Méthodes pour gérer les cookies spécifiques
    setCookie(name, value, days = 365) {
        if (!this.hasUserConsent()) return;

        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = `expires=${date.toUTCString()}`;
        document.cookie = `${name}=${value};${expires};path=/`;
    }

    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    deleteCookie(name) {
        document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
    }
}

// Initialiser le gestionnaire de cookies
const cookieManager = new CookieManager();

// Exporter pour utilisation dans d'autres fichiers
window.cookieManager = cookieManager; 