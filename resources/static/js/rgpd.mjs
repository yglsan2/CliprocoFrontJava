document.addEventListener('DOMContentLoaded', () => {
    const rgpdBanner = document.getElementById('rgpdBanner');
    const acceptButton = document.getElementById('acceptCookies');
    const refuseButton = document.getElementById('refuseCookies');

    // Vérifier si l'utilisateur a déjà fait son choix
    const rgpdChoice = localStorage.getItem('rgpdChoice');

    if (!rgpdChoice) {
        rgpdBanner.style.display = 'block';
    }

    acceptButton.addEventListener('click', () => {
        localStorage.setItem('rgpdChoice', 'accepted');
        rgpdBanner.style.display = 'none';
        // Émettre un événement pour indiquer que le consentement a été donné
        document.dispatchEvent(new CustomEvent('rgpdConsent', { detail: { accepted: true } }));
    });

    refuseButton.addEventListener('click', () => {
        localStorage.setItem('rgpdChoice', 'refused');
        rgpdBanner.style.display = 'none';
        // Émettre un événement pour indiquer que le consentement a été refusé
        document.dispatchEvent(new CustomEvent('rgpdConsent', { detail: { accepted: false } }));
    });
});
