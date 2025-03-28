// Gestion de l'authentification
const AUTH_KEY = 'auth_status';

// Vérifier si l'utilisateur est connecté
export function isAuthenticated() {
    return localStorage.getItem(AUTH_KEY) === 'true';
}

// Connecter l'utilisateur
export function login(username, password) {
    // Simulation d'authentification
    if (username === 'admin' && password === 'admin') {
        localStorage.setItem(AUTH_KEY, 'true');
        return true;
    }
    return false;
}

// Déconnecter l'utilisateur
export function logout() {
    localStorage.removeItem(AUTH_KEY);
}

// Gérer le formulaire de connexion
export function handleLoginForm() {
    const loginForm = document.getElementById('authForm');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const username = document.getElementById('authUsername').value;
            const password = document.getElementById('authPassword').value;

            if (login(username, password)) {
                window.location.reload();
            } else {
                alert('Identifiants incorrects');
            }
        });
    }
}

// Gérer le bouton de déconnexion
export function handleLogoutButton() {
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            logout();
            window.location.reload();
        });
    }
}

// Mettre à jour l'interface en fonction de l'état de connexion
export function updateAuthUI() {
    const loginForm = document.getElementById('login-form');
    const logoutBtn = document.getElementById('logout-btn');
    const authModal = document.getElementById('authModal');

    if (isAuthenticated()) {
        if (loginForm) loginForm.style.display = 'none';
        if (logoutBtn) logoutBtn.style.display = 'block';
        if (authModal) {
            const modal = bootstrap.Modal.getInstance(authModal);
            if (modal) modal.hide();
        }
    } else {
        if (loginForm) loginForm.style.display = 'flex';
        if (logoutBtn) logoutBtn.style.display = 'none';
    }
}

// Initialiser l'authentification
export function initAuth() {
    handleLoginForm();
    handleLogoutButton();
    updateAuthUI();
} 