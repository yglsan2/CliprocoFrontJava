import { handleLocationAndWeather } from './geolocWeather.mjs';
import { isAuthenticated } from './auth.mjs';

// Stockage local des prospects
const PROSPECTS_KEY = 'prospects_data';

// Récupérer tous les prospects
export function getProspects() {
    const prospects = localStorage.getItem(PROSPECTS_KEY);
    return prospects ? JSON.parse(prospects) : [];
}

// Sauvegarder les prospects
function saveProspects(prospects) {
    localStorage.setItem(PROSPECTS_KEY, JSON.stringify(prospects));
}

// Ajouter un prospect
export function addProspect(prospect) {
    const prospects = getProspects();
    prospect.id = Date.now(); // ID unique basé sur le timestamp
    prospects.push(prospect);
    saveProspects(prospects);
    return prospect;
}

// Supprimer un prospect
export function deleteProspect(id) {
    const prospects = getProspects();
    const filteredProspects = prospects.filter(prospect => prospect.id !== id);
    saveProspects(filteredProspects);
}

// Mettre à jour un prospect
export function updateProspect(id, updatedProspect) {
    const prospects = getProspects();
    const index = prospects.findIndex(prospect => prospect.id === id);
    if (index !== -1) {
        prospects[index] = { ...prospects[index], ...updatedProspect };
        saveProspects(prospects);
        return prospects[index];
    }
    return null;
}

// Calculer l'âge
function calculateAge(birthDate) {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    return age;
}

// Gérer le formulaire d'ajout de prospect
export function handleProspectForm() {
    const form = document.getElementById('prospectForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            if (!isAuthenticated()) {
                alert('Veuillez vous connecter pour ajouter un prospect');
                return;
            }

            const prospect = {
                nom: document.getElementById('prospectNom').value,
                prenom: document.getElementById('prospectPrenom').value,
                dateNaissance: document.getElementById('prospectDateNaissance').value,
                adresse: document.getElementById('prospectAdresse').value,
                ville: document.getElementById('prospectVille').value,
                codePostal: document.getElementById('prospectCodePostal').value,
                email: document.getElementById('prospectEmail').value,
                telephone: document.getElementById('prospectTel').value,
                raisonSociale: document.getElementById('prospectRaisonSociale').value,
                chiffreAffaires: document.getElementById('prospectCA').value,
                nombreEmployes: document.getElementById('prospectEmployes').value
            };

            addProspect(prospect);
            updateProspectsTable();
            form.reset();
        });
    }
}

// Mettre à jour le tableau des prospects
export function updateProspectsTable() {
    const tbody = document.querySelector('#prospectTable tbody');
    if (!tbody) return;

    const prospects = getProspects();
    tbody.innerHTML = '';

    prospects.forEach(prospect => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${prospect.nom}</td>
            <td>${prospect.prenom}</td>
            <td>${prospect.dateNaissance}</td>
            <td>${prospect.adresse}</td>
            <td>${prospect.ville}</td>
            <td>${prospect.codePostal}</td>
            <td>${prospect.email}</td>
            <td>${prospect.telephone}</td>
            <td>${prospect.raisonSociale || '-'}</td>
            <td>${prospect.chiffreAffaires}€</td>
            <td>${prospect.nombreEmployes}</td>
            <td>
                <button class="btn btn-info btn-sm view-prospect" data-id="${prospect.id}">Voir</button>
                <button class="btn btn-danger btn-sm delete-prospect" data-id="${prospect.id}">Supprimer</button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    // Ajouter les événements pour les boutons
    document.querySelectorAll('.view-prospect').forEach(btn => {
        btn.addEventListener('click', () => viewProspectDetails(btn.dataset.id));
    });

    document.querySelectorAll('.delete-prospect').forEach(btn => {
        btn.addEventListener('click', () => {
            if (confirm('Voulez-vous vraiment supprimer ce prospect ?')) {
                deleteProspect(parseInt(btn.dataset.id));
                updateProspectsTable();
            }
        });
    });
}

// Afficher les détails d'un prospect
async function viewProspectDetails(id) {
    const prospect = getProspects().find(p => p.id === parseInt(id));
    if (!prospect) return;

    const detailsSection = document.getElementById('prospectDetails');
    if (detailsSection) {
        detailsSection.style.display = 'block';
        document.getElementById('prospectName').textContent = `${prospect.prenom} ${prospect.nom}`;
        document.getElementById('prospectAddress').textContent = `${prospect.adresse}, ${prospect.codePostal} ${prospect.ville}`;

        try {
            await handleLocationAndWeather(
                prospect.adresse,
                prospect.codePostal,
                'prospectDetails',
                'prospectMap'
            );
        } catch (error) {
            console.error('Erreur lors de la récupération des informations:', error);
        }
    }
}

// Initialiser la gestion des prospects
export function initProspects() {
    handleProspectForm();
    updateProspectsTable();

    // Mettre à jour l'âge automatiquement
    const dateNaissanceInput = document.getElementById('prospectDateNaissance');
    const ageInput = document.getElementById('prospectAge');
    if (dateNaissanceInput && ageInput) {
        dateNaissanceInput.addEventListener('change', () => {
            ageInput.value = calculateAge(dateNaissanceInput.value);
        });
    }
} 