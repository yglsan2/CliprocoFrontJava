# TODO - CliprocoJEE

## Date : 2 juillet 2025

### ✅ Tâches accomplies
- [x] Migration Java EE → Jakarta EE 10
- [x] Mise à jour Tomcat 11
- [x] Mise à jour Hibernate 6
- [x] Correction des imports javax.persistence → jakarta.persistence
- [x] Configuration JPA avec unité de persistance "cliprocoUP"
- [x] Correction des mappings FrontController
- [x] Ajout des dépendances JSTL
- [x] Correction des chemins JSP dans les contrôleurs
- [x] Application fonctionnelle avec navigation
- [x] Pages Clients et Prospects affichées

### 🔧 Tâches restantes à implémenter

#### CRUD Operations
- [ ] **Corriger les actions CRUD** (Create, Read, Update, Delete)
  - [ ] Bouton "Voir" - Afficher les détails d'un client/prospect
  - [ ] Bouton "Modifier" - Formulaire de modification
  - [ ] Bouton "Supprimer" - Suppression avec gestion des contraintes FK
  - [ ] Bouton "Ajouter" - Formulaire de création

#### Base de données
- [ ] **Corriger les contraintes de clés étrangères**
  - [ ] Problème de suppression à cause des FK entre prospects et sociétés
  - [ ] Implémenter la suppression en cascade ou validation

#### Données d'exemple
- [ ] **Remplacer les données d'exemple**
  - [ ] Créer des entreprises factices basées en Lorraine (Laxou, Nancy)
  - [ ] Remplacer EfluidSAS, OGMI, HappiSO par des noms plus réalistes
  - [ ] Adresses dans la région Lorraine

#### Authentification
- [ ] **Réactiver l'authentification**
  - [ ] Implémenter la page de connexion
  - [ ] Gérer les sessions utilisateur
  - [ ] Contrôler l'accès aux pages

#### Interface utilisateur
- [ ] **Améliorations UX**
  - [ ] Messages d'erreur plus clairs
  - [ ] Validation des formulaires côté client
  - [ ] Confirmation avant suppression

### 🚀 Prochaines étapes
1. Tester et corriger les actions CRUD
2. Nettoyer les données d'exemple
3. Finaliser l'authentification
4. Tests complets de l'application

---
*Application Jakarta EE 10 fonctionnelle avec Tomcat 11 et Hibernate 6*
