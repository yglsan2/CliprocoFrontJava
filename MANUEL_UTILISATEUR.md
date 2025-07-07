# 📖 Manuel d'Utilisation - CliprocoJEE

<div align="center">

![CliprocoJEE](https://img.shields.io/badge/CliprocoJEE-1.0.0-blue)
![Interface](https://img.shields.io/badge/Interface-Responsive-green)
![Sécurité](https://img.shields.io/badge/Sécurité-Authentifiée-red)

**Guide complet pour l'utilisation de l'application de gestion commerciale**

[🚀 Démarrage Rapide](#-démarrage-rapide) • [👥 Gestion Clients](#-gestion-des-clients) • [🎯 Gestion Prospects](#-gestion-des-prospects) • [⚙️ Configuration](#-configuration)

</div>

---

## 📋 Table des matières

- [🚀 Démarrage rapide](#-démarrage-rapide)
- [🔐 Connexion et sécurité](#-connexion-et-sécurité)
- [🏠 Interface principale](#-interface-principale)
- [👥 Gestion des clients](#-gestion-des-clients)
- [🎯 Gestion des prospects](#-gestion-des-prospects)
- [📊 Tableau de bord](#-tableau-de-bord)
- [🔍 Recherche et filtres](#-recherche-et-filtres)
- [📱 Interface mobile](#-interface-mobile)
- [❓ FAQ](#-faq)
- [🆘 Support](#-support)

---

## 🚀 Démarrage rapide

### 📋 Prérequis système

Avant de commencer, assurez-vous que votre système répond aux exigences suivantes :

| Composant | Version minimale | Vérification |
|-----------|------------------|--------------|
| **Navigateur web** | Chrome 90+, Firefox 88+, Safari 14+ | Vérifiez dans les paramètres |
| **Connexion internet** | Stable | Testez votre connexion |
| **Résolution écran** | 1024x768 minimum | Vérifiez les paramètres d'affichage |

### 🌐 Accès à l'application

#### 1. **Ouvrir l'application**
- Ouvrez votre navigateur web
- Tapez l'adresse : `http://localhost:8080/CliprocoJEE`
- Appuyez sur **Entrée**

#### 2. **Page d'accueil**
Vous devriez voir la page de connexion avec :
- Logo CliprocoJEE
- Formulaire de connexion
- Liens vers les informations légales

### 🔐 Première connexion

#### Identifiants par défaut
- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin123`

> ⚠️ **Important** : Changez ces identifiants dès votre première connexion !

#### Étapes de connexion
1. **Saisir** votre nom d'utilisateur
2. **Taper** votre mot de passe
3. **Cocher** "Se souvenir de moi" (optionnel)
4. **Cliquer** sur "Se connecter"

---

## 🔐 Connexion et sécurité

### 🔑 Gestion des comptes

#### 👤 Types d'utilisateurs

| Rôle | Permissions | Accès |
|------|-------------|-------|
| **Administrateur** | Toutes les fonctionnalités | Complet |
| **Manager** | Gestion clients/prospects | Limité |
| **Commercial** | Consultation et création | Basique |

#### 🔒 Sécurité des sessions

**Durée de session** : 30 minutes d'inactivité
- **Renouvellement automatique** lors des actions
- **Déconnexion forcée** après expiration
- **Protection CSRF** sur tous les formulaires

#### 🚪 Déconnexion

**Méthodes de déconnexion :**
1. **Menu utilisateur** → "Se déconnecter"
2. **Bouton déconnexion** dans l'en-tête
3. **Fermeture du navigateur** (session expirée)

### 🔐 Bonnes pratiques

#### 🛡️ Sécurité des mots de passe
- **Longueur minimale** : 8 caractères
- **Complexité** : Majuscules, minuscules, chiffres
- **Changement régulier** : Tous les 90 jours
- **Pas de partage** : Un compte par utilisateur

#### 🔒 Protection des données
- **Ne jamais** partager vos identifiants
- **Fermer** la session après utilisation
- **Utiliser** un navigateur privé si nécessaire
- **Signaler** toute activité suspecte

---

## 🏠 Interface principale

### 🎨 Navigation générale

#### 📱 En-tête (Header)
```
┌─────────────────────────────────────────────────────────────┐
│ [Logo] CliprocoJEE                    [User] ▼ [Settings] ⚙️ │
├─────────────────────────────────────────────────────────────┤
│ [🏠] [👥 Clients] [🎯 Prospects] [📊 Dashboard] [⚙️ Config] │
└─────────────────────────────────────────────────────────────┘
```

#### 🎯 Éléments de navigation
- **🏠 Accueil** : Retour au tableau de bord
- **👥 Clients** : Gestion des clients existants
- **🎯 Prospects** : Suivi des prospects
- **📊 Dashboard** : Statistiques et analyses
- **⚙️ Configuration** : Paramètres système

### 📊 Tableau de bord principal

#### 🎯 Widgets disponibles
```
┌─────────────┬─────────────┬─────────────┐
│ 📈 CA Total │ 👥 Clients  │ 🎯 Prospects │
│   €125,000  │     45      │     12      │
└─────────────┴─────────────┴─────────────┘
┌─────────────┬─────────────┬─────────────┐
│ 🌍 Carte    │ 🌤️ Météo    │ 📅 Agenda   │
│ Géographique│  15°C Pluie │ 3 Rendez-vous│
└─────────────┴─────────────┴─────────────┘
```

#### 🎨 Personnalisation
- **Réorganisation** : Glisser-déposer des widgets
- **Masquage** : Clic droit → "Masquer"
- **Taille** : Redimensionnement des widgets

---

## 👥 Gestion des clients

### 📋 Vue d'ensemble des clients

#### 🎯 Liste des clients
```
┌─────┬─────────────────┬──────────┬──────────┬─────────────────┬──────────┬──────────┐
│ ID  │ Raison Sociale  │   Nom    │ Prénom   │    Adresse      │ Téléphone│  Email   │
├─────┼─────────────────┼──────────┼──────────┼─────────────────┼──────────┼──────────┤
│ 1   │ Entreprise A    │ Dupont   │ Jean     │ 123 Rue de...   │ 012345.. │ jean@... │
│ 2   │ Société B       │ Martin   │ Marie    │ 456 Avenue...   │ 098765.. │ marie@.. │
└─────┴─────────────────┴──────────┴──────────┴─────────────────┴──────────┴──────────┘
```

#### 🎨 Colonnes personnalisées
- **ID** : Identifiant unique (60px)
- **Raison sociale** : Nom de l'entreprise (15%)
- **Nom/Prénom** : Contact principal (12% chacun)
- **Adresse** : Adresse complète (18%)
- **Téléphone/Email** : Coordonnées (10-15%)
- **Actions** : Boutons d'action (15%)

#### 👁️ **Voir un client (Fonctionnalité principale)**
La fonctionnalité "Voir" est la plus spectaculaire de l'application ! Elle offre une vue complète et interactive :

1. **Dans la liste des clients**, cliquer sur **"👁️ Voir"**
2. **Vue détaillée complète** avec :
   - **Informations complètes** du client
   - **🗺️ Carte interactive** avec géolocalisation précise
   - **🌤️ Météo en temps réel** de la localisation
   - **📊 Statistiques** et historique
   - **📍 Coordonnées GPS** exactes

**🎯 Fonctionnalités de la vue détaillée :**
- **Carte Leaflet** : Navigation interactive, zoom, déplacement
- **Météo Infoclimat** : Température, humidité, vent en temps réel
- **Géolocalisation** : Conversion automatique adresse → coordonnées
- **Responsive** : Adaptation parfaite mobile/tablet/desktop

### ➕ Créer un nouveau client

#### 📝 Formulaire de création
```
┌─────────────────────────────────────────────────────────────┐
│                    📝 Créer un nouveau client               │
├─────────────────────────────────────────────────────────────┤
│ Raison sociale *: [________________________]                │
│ Nom *:           [__________] Prénom *: [__________]        │
│                                                                 │
│ 📍 Adresse :                                                  │
│ N° rue:         [____] Rue: [________________________]       │
│ Code postal:    [_____] Ville: [________________]            │
│ Pays:           [France________________]                     │
│                                                                 │
│ 📞 Coordonnées :                                              │
│ Téléphone:      [__________] Email: [________________]       │
│                                                                 │
│ 💰 Informations commerciales :                               │
│ CA annuel:      [€________] Nb employés: [____]              │
│                                                                 │
│ 💬 Commentaires:                                              │
│ [________________________________________________]           │
│                                                                 │
│ [✅ Enregistrer] [❌ Annuler]                                 │
└─────────────────────────────────────────────────────────────┘
```

#### ✅ Validation des champs

| Champ | Obligatoire | Format | Validation |
|-------|-------------|--------|------------|
| **Raison sociale** | ✅ | Texte libre | 2-100 caractères |
| **Nom** | ✅ | Texte libre | 2-50 caractères |
| **Prénom** | ✅ | Texte libre | 2-50 caractères |
| **N° rue** | ✅ | Texte libre | Format flexible |
| **Rue** | ✅ | Texte libre | 5-100 caractères |
| **Code postal** | ✅ | 5 chiffres | 01000-99999 |
| **Ville** | ✅ | Texte libre | 2-50 caractères |
| **Pays** | ✅ | Liste | Sélection |
| **Téléphone** | ❌ | 10 chiffres | Format français |
| **Email** | ❌ | Email valide | Format standard |
| **CA annuel** | ❌ | Nombre positif | €0 - €999,999,999 |
| **Nb employés** | ❌ | Nombre entier | 0 - 999,999 |

#### 🎯 Messages d'aide (Tooltips)
- **Raison sociale** : "Nom officiel de l'entreprise"
- **Code postal** : "Code postal français (ex: 54000)"
- **Téléphone** : "Format: 0123456789 (10 chiffres)"
- **Email** : "Format: nom@domaine.com"
- **CA annuel** : "Chiffre d'affaires annuel en euros"

### ✏️ Modifier un client existant

#### 🔄 Processus de modification
1. **Accéder** à la liste des clients
2. **Cliquer** sur le bouton "✏️ Modifier"
3. **Modifier** les champs souhaités
4. **Valider** avec "✅ Enregistrer"

#### 🔒 Champs protégés
- **ID** : Non modifiable (généré automatiquement)
- **Date de création** : Non modifiable
- **Historique** : Conservé automatiquement

### 🗑️ Supprimer un client

#### ⚠️ Procédure de suppression
1. **Accéder** à la liste des clients
2. **Cliquer** sur le bouton "🗑️ Supprimer"
3. **Confirmer** la suppression dans la popup
4. **Valider** définitivement

#### 🛡️ Sécurités
- **Confirmation obligatoire** : Double validation
- **Vérification des liens** : Prospects associés
- **Sauvegarde automatique** : Log de suppression
- **Récupération possible** : 30 jours

### 👁️ Consulter un client

#### 📋 Vue détaillée
```
┌─────────────────────────────────────────────────────────────┐
│                    👁️ Détails du client                     │
├─────────────────────────────────────────────────────────────┤
│ 🏢 Entreprise A                    ID: 1                    │
│ 👤 Contact: Jean Dupont                                     │
│ 📍 123 Rue de la Paix, 54000 Nancy, France                  │
│ 📞 0123456789 | 📧 jean@entreprise-a.fr                     │
│ 💰 CA: €125,000 | 👥 25 employés                            │
│                                                                 │
│ 💬 Commentaires:                                              │
│ Client fidèle depuis 2020, très satisfait de nos services.   │
│                                                                 │
│ [✏️ Modifier] [🗑️ Supprimer] [📊 Historique]                │
└─────────────────────────────────────────────────────────────┘
```

#### 🗺️ Informations géographiques
- **Carte interactive** : Localisation précise
- **Coordonnées GPS** : Latitude/Longitude
- **Météo locale** : Conditions actuelles
- **Distance** : Depuis votre localisation

---

## 🎯 Gestion des prospects

### 📋 Vue d'ensemble des prospects

#### 🎯 Liste des prospects
```
┌─────┬─────────────────┬──────────┬──────────┬─────────────────┬──────────────┬──────────┐
│ ID  │ Raison Sociale  │   Nom    │ Prénom   │    Adresse      │ Date Prospect│ Intéressé│
├─────┼─────────────────┼──────────┼──────────┼─────────────────┼──────────────┼──────────┤
│ 1   │ Prospect A      │ Durand   │ Pierre   │ 789 Boulevard.. │ 2024-01-15   │ ✅ Oui   │
│ 2   │ Société B       │ Leroy    │ Sophie   │ 321 Place de... │ 2024-01-20   │ ❌ Non   │
└─────┴─────────────────┴──────────┴──────────┴─────────────────┴──────────────┴──────────┘
```

#### 🎨 Colonnes spécifiques
- **Date de prospection** : Premier contact
- **Intéressé** : Statut d'intérêt (Oui/Non)
- **Actions** : Convertir, Modifier, Supprimer

#### 👁️ **Voir un prospect (Fonctionnalité principale)**
Comme pour les clients, la vue détaillée des prospects est spectaculaire :

1. **Dans la liste des prospects**, cliquer sur **"👁️ Voir"**
2. **Vue détaillée complète** avec :
   - **Informations complètes** du prospect
   - **🗺️ Carte interactive** avec géolocalisation
   - **🌤️ Météo en temps réel** de la localisation
   - **📅 Historique des contacts** et suivi
   - **📍 Coordonnées GPS** exactes

**🎯 Fonctionnalités de la vue détaillée :**
- **Carte Leaflet** : Navigation interactive, zoom, déplacement
- **Météo Infoclimat** : Température, humidité, vent en temps réel
- **Géolocalisation** : Conversion automatique adresse → coordonnées
- **Responsive** : Adaptation parfaite mobile/tablet/desktop
- **Historique** : Tous les contacts et notes de suivi

### ➕ Créer un nouveau prospect

#### 📝 Formulaire de création
```
┌─────────────────────────────────────────────────────────────┐
│                    📝 Créer un nouveau prospect             │
├─────────────────────────────────────────────────────────────┤
│ Raison sociale *: [________________________]                │
│ Nom *:           [__________] Prénom *: [__________]        │
│                                                                 │
│ 📍 Adresse :                                                  │
│ N° rue:         [____] Rue: [________________________]       │
│ Code postal:    [_____] Ville: [________________]            │
│ Pays:           [France________________]                     │
│                                                                 │
│ 📞 Coordonnées :                                              │
│ Téléphone:      [__________] Email: [________________]       │
│                                                                 │
│ 📅 Prospection :                                              │
│ Date contact:   [2024-01-15] Intéressé: [✅ Oui] [❌ Non]    │
│                                                                 │
│ 💬 Commentaires:                                              │
│ [________________________________________________]           │
│                                                                 │
│ [✅ Enregistrer] [❌ Annuler]                                 │
└─────────────────────────────────────────────────────────────┘
```

#### ✅ Validation spécifique
- **Date de prospection** : Date valide (pas dans le futur)
- **Intéressé** : Oui/Non obligatoire
- **Commentaires** : Détails du contact

### 🔄 Convertir un prospect en client

#### 🎯 Processus de conversion
1. **Accéder** à la liste des prospects
2. **Cliquer** sur "🔄 Convertir"
3. **Compléter** les informations client :
   - Chiffre d'affaires estimé
   - Nombre d'employés
   - Informations complémentaires
4. **Valider** la conversion

#### ⚠️ Conséquences de la conversion
- **Création** d'un nouveau client
- **Suppression** du prospect original
- **Conservation** de l'historique
- **Notification** automatique

### 📊 Suivi des prospects

#### 📈 Statistiques de conversion
- **Taux de conversion** : % prospects → clients
- **Durée moyenne** : Temps avant conversion
- **Sources** : Origine des prospects
- **Performance** : Par commercial

#### 📅 Calendrier de suivi
- **Rappels automatiques** : Contacts à relancer
- **Échéances** : Dates importantes
- **Historique** : Tous les contacts

---

## 📊 Tableau de bord

### 📈 Statistiques générales

#### 💰 Indicateurs financiers
```
┌─────────────────────────────────────────────────────────────┐
│                    📊 Indicateurs financiers                │
├─────────────────────────────────────────────────────────────┤
│ 💰 Chiffre d'affaires total: €1,250,000                     │
│ 📈 Évolution mensuelle: +12.5%                              │
│ 🎯 Objectif atteint: 85%                                    │
│ 💵 CA moyen par client: €27,777                             │
└─────────────────────────────────────────────────────────────┘
```

#### 👥 Indicateurs clients
```
┌─────────────────────────────────────────────────────────────┐
│                    👥 Indicateurs clients                   │
├─────────────────────────────────────────────────────────────┤
│ 👥 Nombre total de clients: 45                              │
│ ➕ Nouveaux ce mois: 8                                      │
│ 📈 Croissance: +21.6%                                       │
│ 🎯 Prospects convertis: 12                                  │
└─────────────────────────────────────────────────────────────┘
```

### 📊 Graphiques et visualisations

#### 📈 Graphiques disponibles
- **Évolution CA** : Courbe temporelle
- **Répartition clients** : Secteurs d'activité
- **Géographie** : Carte de répartition
- **Performance** : Par commercial

#### 🎨 Personnalisation
- **Période** : Jour/Semaine/Mois/Année
- **Filtres** : Par région, secteur, commercial
- **Export** : PDF, Excel, PNG

### 🌍 Carte géographique

#### 🗺️ Fonctionnalités
- **Localisation clients** : Points sur la carte
- **Zones d'activité** : Régions colorées
- **Clustering** : Regroupement automatique
- **Navigation** : Zoom, déplacement

#### 📍 Informations détaillées
- **Clic sur client** : Fiche détaillée
- **Météo locale** : Conditions actuelles
- **Distance** : Calcul automatique
- **Itinéraire** : Navigation GPS

---

## 🔍 Recherche et filtres

### 🔍 Recherche globale

#### 🎯 Barre de recherche
```
┌─────────────────────────────────────────────────────────────┐
│ 🔍 [Rechercher clients, prospects, adresses...] [🔍]        │
└─────────────────────────────────────────────────────────────┘
```

#### ✅ Champs recherchés
- **Raison sociale** : Nom de l'entreprise
- **Nom/Prénom** : Contact principal
- **Adresse** : Ville, rue, code postal
- **Téléphone/Email** : Coordonnées
- **Commentaires** : Notes et détails

### 🎛️ Filtres avancés

#### 📊 Filtres clients
```
┌─────────────────────────────────────────────────────────────┐
│                    🎛️ Filtres avancés                       │
├─────────────────────────────────────────────────────────────┤
│ 📍 Région: [Toutes les régions ▼]                           │
│ 💰 CA min: [€________] CA max: [€________]                  │
│ 👥 Employés: [Tous ▼]                                       │
│ 📅 Date création: [____] à [____]                           │
│                                                                 │
│ [🔍 Appliquer] [🗑️ Effacer]                                 │
└─────────────────────────────────────────────────────────────┘
```

#### 🎯 Filtres prospects
- **Date de prospection** : Période
- **Statut d'intérêt** : Intéressé/Non intéressé
- **Commercial** : Responsable
- **Source** : Origine du prospect

### 📋 Tri et organisation

#### 🔄 Options de tri
- **Par défaut** : Date de création (plus récent)
- **Alphabétique** : Raison sociale A-Z
- **Chiffre d'affaires** : Croissant/Décroissant
- **Date de prospection** : Plus récent/Ancien
- **Statut** : Intéressé en premier

#### 📊 Affichage
- **Nombre par page** : 10, 25, 50, 100
- **Pagination** : Navigation entre pages
- **Export** : Téléchargement des résultats

---

## 📱 Interface mobile

### 📱 Adaptation mobile

#### 🎨 Design responsive
- **Colonnes adaptées** : Masquage automatique
- **Navigation simplifiée** : Menu hamburger
- **Boutons tactiles** : Taille optimisée
- **Formulaires** : Champs adaptés

#### 📱 Fonctionnalités mobiles
- **Géolocalisation** : Position GPS
- **Appareil photo** : Scan de documents
- **Notifications** : Push browser
- **Mode hors ligne** : Données en cache

### 📱 Utilisation sur mobile

#### 🎯 Navigation tactile
- **Swipe** : Navigation entre pages
- **Tap** : Sélection d'éléments
- **Pinch** : Zoom sur la carte
- **Pull to refresh** : Actualisation

#### 📱 Optimisations
- **Chargement rapide** : Images optimisées
- **Bande passante** : Données compressées
- **Batterie** : Mode économie d'énergie
- **Stockage** : Cache intelligent

---

## ❓ FAQ

### 🔐 Questions d'authentification

#### Q: J'ai oublié mon mot de passe, que faire ?
**R:** Contactez votre administrateur système qui pourra réinitialiser votre mot de passe.

#### Q: Ma session expire trop rapidement
**R:** La session expire après 30 minutes d'inactivité. Activez "Se souvenir de moi" pour prolonger la session.

#### Q: Je ne peux pas me connecter
**R:** Vérifiez :
- Votre nom d'utilisateur et mot de passe
- Que votre compte n'est pas verrouillé
- Votre connexion internet

### 👥 Questions sur les clients

#### Q: Comment modifier les informations d'un client ?
**R:** Dans la liste des clients, cliquez sur le bouton "✏️ Modifier" à côté du client concerné.

#### Q: Puis-je supprimer un client par erreur ?
**R:** Non, la suppression nécessite une double confirmation et les données sont récupérables pendant 30 jours.

#### Q: Comment ajouter des commentaires à un client ?
**R:** Dans la vue détaillée du client, cliquez sur "Modifier" et utilisez le champ "Commentaires".

### 🎯 Questions sur les prospects

#### Q: Quelle est la différence entre un client et un prospect ?
**R:** Un prospect est un contact potentiel, un client a déjà effectué des achats.

#### Q: Comment convertir un prospect en client ?
**R:** Dans la liste des prospects, cliquez sur "🔄 Convertir" et complétez les informations client.

#### Q: Puis-je suivre l'historique des contacts avec un prospect ?
**R:** Oui, dans la vue détaillée du prospect, vous avez accès à tout l'historique des contacts.

### 📊 Questions sur les données

#### Q: Mes données sont-elles sauvegardées ?
**R:** Oui, toutes les données sont sauvegardées automatiquement et sauvegardées quotidiennement.

#### Q: Puis-je exporter mes données ?
**R:** Oui, utilisez les boutons d'export dans les listes pour télécharger en CSV ou Excel.

#### Q: Comment rechercher un client spécifique ?
**R:** Utilisez la barre de recherche en haut de page ou les filtres avancés.

### 🎨 Questions sur l'interface

#### Q: L'interface ne s'affiche pas correctement
**R:** Vérifiez que vous utilisez un navigateur récent (Chrome 90+, Firefox 88+, Safari 14+).

#### Q: Les colonnes sont trop petites sur mon écran
**R:** L'interface s'adapte automatiquement. Utilisez le zoom du navigateur si nécessaire.

#### Q: Je ne vois pas toutes les colonnes sur mobile
**R:** Certaines colonnes sont masquées sur mobile pour optimiser l'affichage. Utilisez la vue détaillée.

---

## 🆘 Support

### 📞 Contact support

#### 🎯 Équipe support
- **Email** : support@cliproco.fr
- **Téléphone** : 03 83 XX XX XX
- **Horaires** : Lundi-Vendredi 9h-18h

#### 📋 Informations à fournir
- **Nom d'utilisateur** : Votre identifiant
- **Description** : Problème détaillé
- **Étapes** : Actions effectuées
- **Screenshot** : Capture d'écran si possible

### 🐛 Signaler un bug

#### 📝 Formulaire de signalement
```
┌─────────────────────────────────────────────────────────────┐
│                    🐛 Signaler un bug                       │
├─────────────────────────────────────────────────────────────┤
│ Titre: [________________________________]                   │
│                                                                 │
│ Description:                                                   │
│ [________________________________________________]           │
│                                                                 │
│ Étapes pour reproduire:                                       │
│ [________________________________________________]           │
│                                                                 │
│ Navigateur: [Chrome 90+ ▼]                                    │
│ Système: [Windows 10 ▼]                                       │
│                                                                 │
│ [📎 Joindre capture] [📤 Envoyer]                            │
└─────────────────────────────────────────────────────────────┘
```

### 📚 Ressources d'aide

#### 📖 Documentation
- **Guide utilisateur** : Ce manuel
- **Vidéos tutorielles** : Chaîne YouTube
- **FAQ en ligne** : Base de connaissances

#### 🎓 Formation
- **Formation initiale** : 2 heures
- **Formation continue** : Sessions mensuelles
- **Support personnalisé** : Sur demande

---

<div align="center">

**📖 Manuel d'utilisation CliprocoJEE v1.0**

*Dernière mise à jour : Janvier 2024*

[🏠 Retour au début](#-démarrage-rapide) • [📞 Support](#-support)

</div> 