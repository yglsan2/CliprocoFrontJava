import { initAuth } from './auth.mjs';
import { initClients } from './clients.mjs';
import { initProspects } from './prospects.mjs';

// Gestion du bandeau RGPD
function handleRGPD() {
    const banner = document.getElementById('rgpdBanner');
    const acceptBtn = document.getElementById('acceptCookies');
    const refuseBtn = document.getElementById('refuseCookies');

    if (!localStorage.getItem('rgpd_accepted')) {
        banner.style.display = 'block';
    }

    if (acceptBtn) {
        acceptBtn.addEventListener('click', () => {
            localStorage.setItem('rgpd_accepted', 'true');
            banner.style.display = 'none';
        });
    }

    if (refuseBtn) {
        refuseBtn.addEventListener('click', () => {
            localStorage.setItem('rgpd_accepted', 'false');
            banner.style.display = 'none';
        });
    }
}

// Initialisation de l'application
document.addEventListener('DOMContentLoaded', () => {
    initAuth();
    handleRGPD();

    // Initialiser les fonctionnalités en fonction de la page
    const path = window.location.pathname;
    if (path.includes('/clients')) {
        initClients();
    } else if (path.includes('/prospects')) {
        initProspects();
    }
}); 