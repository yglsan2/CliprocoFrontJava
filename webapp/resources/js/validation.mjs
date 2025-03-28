document.addEventListener('DOMContentLoaded', () => {
    const prospectForm = document.getElementById('prospectForm');

    prospectForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const nom = document.getElementById('prospect-nom').value;
        const prenom = document.getElementById('prospect-prenom').value;
        const dateNaissance = document.getElementById('prospect-dateNaissance').value;
        const adresse = document.getElementById('prospect-adresse').value;
        const codePostal = document.getElementById('prospect-codePostal').value;
        const email = document.getElementById('prospect-email').value;
        const telephone = document.getElementById('prospect-telephone').value;
        const raisonSociale = document.getElementById('prospect-raisonSociale').value;

        // Validation des champs spécifiques
        if (!validateEmail(email)) {
            alert("Veuillez entrer une adresse email valide.");
            return;
        }

        if (!validateTelephone(telephone)) {
            alert("Veuillez entrer un numéro de téléphone valide (10 chiffres uniquement).");
            return;
        }

        // Ajoutez ici d'autres validations métier comme le nombre d'employés, le chiffre d'affaires, etc.

        // Si toutes les validations passent, soumettez le formulaire
        alert("Formulaire soumis avec succès !");
        prospectForm.submit();
    });

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function validateTelephone(telephone) {
        const re = /^\d{10}$/;
        return re.test(telephone);
    }
});