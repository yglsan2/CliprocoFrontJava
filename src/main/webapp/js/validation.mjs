document.addEventListener("DOMContentLoaded", () => {
    // Validation pour le formulaire prospects
    const prospectForm = document.getElementById("prospectForm");
    if (prospectForm) {
        prospectForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const nom = document.getElementById('nom')?.value || '';
            const prenom = document.getElementById('prenom')?.value || '';
            const adresse = document.getElementById('adresse')?.value || '';
            const codePostal = document.getElementById('codePostal')?.value || '';
            const ville = document.getElementById('ville')?.value || '';
            const pays = document.getElementById('pays')?.value || '';
            const email = document.getElementById('email')?.value || '';
            const telephone = document.getElementById('telephone')?.value || '';
            const interet = document.getElementById('interet')?.value || '';
            const source = document.getElementById('source')?.value || '';

            // Validation des champs obligatoires
            if (!nom.trim()) {
                alert("Le nom est obligatoire.");
                return;
            }

            if (!prenom.trim()) {
                alert("Le prénom est obligatoire.");
                return;
            }

            if (!validateEmail(email)) {
                alert("Veuillez entrer une adresse email valide.");
                return;
            }

            if (!validateTelephone(telephone)) {
                alert("Veuillez entrer un numéro de téléphone valide (10 chiffres uniquement).");
                return;
            }

            if (!validateCodePostal(codePostal)) {
                alert("Veuillez entrer un code postal valide (5 chiffres).");
                return;
            }

            // Si toutes les validations passent, soumettez le formulaire
            console.log("Formulaire prospect soumis avec succès !");
            prospectForm.submit();
        });
    }

    // Validation pour le formulaire clients
    const clientForm = document.getElementById("clientForm");
    if (clientForm) {
        clientForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const nom = document.getElementById('nom')?.value || '';
            const prenom = document.getElementById('prenom')?.value || '';
            const adresse = document.getElementById('adresse')?.value || '';
            const codePostal = document.getElementById('codePostal')?.value || '';
            const ville = document.getElementById('ville')?.value || '';
            const pays = document.getElementById('pays')?.value || '';
            const email = document.getElementById('email')?.value || '';
            const telephone = document.getElementById('telephone')?.value || '';

            // Validation des champs obligatoires
            if (!nom.trim()) {
                alert("Le nom est obligatoire.");
                return;
            }

            if (!prenom.trim()) {
                alert("Le prénom est obligatoire.");
                return;
            }

            if (!validateEmail(email)) {
                alert("Veuillez entrer une adresse email valide.");
                return;
            }

            if (!validateTelephone(telephone)) {
                alert("Veuillez entrer un numéro de téléphone valide (10 chiffres uniquement).");
                return;
            }

            if (!validateCodePostal(codePostal)) {
                alert("Veuillez entrer un code postal valide (5 chiffres).");
                return;
            }

            // Si toutes les validations passent, soumettez le formulaire
            console.log("Formulaire client soumis avec succès !");
            clientForm.submit();
        });
    }

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function validateTelephone(telephone) {
        const re = /^\d{10}$/;
        return re.test(telephone);
    }

    function validateCodePostal(codePostal) {
        const re = /^\d{5}$/;
        return re.test(codePostal);
    }
});
