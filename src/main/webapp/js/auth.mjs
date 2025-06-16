class AuthManager {
    constructor() {
        this.isAuthenticated = false;
        this.setupModals();
        this.setupEventListeners();
    }

    setupModals() {
        // S'assurer que l'élément existe avant de créer le modal
        const authModalElement = document.getElementById('authModal');
        if (authModalElement) {
            this.authModal = new bootstrap.Modal(authModalElement);
        }
        
        this.btnAuth = document.getElementById('btnAuth');
        this.authForm = document.getElementById('authForm');
    }

    setupEventListeners() {
        if (this.btnAuth) {
            this.btnAuth.addEventListener('click', () => {
                if (this.isAuthenticated) {
                    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
                    document.getElementById('confirmModalYes').onclick = () => {
                        this.logout();
                        confirmModal.hide();
                    };
                    confirmModal.show();
                } else {
                    this.authModal?.show();
                }
            });
        }

        if (this.authForm) {
            this.authForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.login();
            });
        }
    }

    login() {
        const username = document.getElementById('username')?.value;
        const password = document.getElementById('password')?.value;

        if (username && password) {
            this.isAuthenticated = true;
            if (this.btnAuth) this.btnAuth.textContent = 'Déconnexion';
            this.authModal?.hide();
            this.authForm?.reset();

            document.dispatchEvent(new CustomEvent('authStateChanged', {
                detail: { isAuthenticated: true }
            }));
        }
    }

    logout() {
        this.isAuthenticated = false;
        if (this.btnAuth) this.btnAuth.textContent = 'Connexion';

        document.dispatchEvent(new CustomEvent('authStateChanged', {
            detail: { isAuthenticated: false }
        }));
    }

    isUserAuthenticated() {
        return this.isAuthenticated;
    }
}

// Exporter la fonction d'initialisation
export function initAuth() {
    return new AuthManager();
}

// Pour la rétrocompatibilité
export const authManager = new AuthManager();

