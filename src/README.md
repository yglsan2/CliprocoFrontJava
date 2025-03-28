# Projet ECF6

## Structure des Branches

### Branches Principales
- `main` : Branche principale de développement
- `production` : Branche de production stable
- `staging` : Branche de pré-production pour les tests

### Branches de Fonctionnalités
- `feature/auth` : Authentification et autorisation
- `feature/admin` : Gestion des administrateurs
- `feature/products` : Gestion des produits
- `feature/security` : Sécurité et protection CSRF

### Branches de Versions
- `version/arraylist` : Version alternative utilisant ArrayList
- `version/database` : Version utilisant la base de données

### Branches de Maintenance
- `hotfix/*` : Corrections urgentes pour la production
- `release/*` : Préparation des releases

## Workflow Git

1. **Développement**
   - Créer une branche feature depuis `main`
   - Développer et tester localement
   - Créer une Pull Request vers `main`

2. **Intégration**
   - Les Pull Requests sont revues et validées
   - Les tests automatisés doivent passer
   - Merge dans `main` après validation

3. **Déploiement**
   - `main` → `staging` pour les tests
   - `staging` → `production` après validation

4. **Maintenance**
   - Les hotfixes partent de `production`
   - Les releases partent de `main`

## Commandes Git Utiles

```bash
# Créer une nouvelle branche feature
git checkout -b feature/nom-feature main

# Mettre à jour sa branche locale
git checkout main
git pull origin main

# Créer un hotfix
git checkout -b hotfix/nom-hotfix production

# Merger une feature
git checkout main
git merge feature/nom-feature
```

## Versions des Dépendances

### Production (LTS)
- Jakarta EE 10.0.0
- Hibernate 6.4.0
- JUnit 5.10.0
- Mockito 5.10.0
- SLF4J 2.0.12
- Logback 1.4.14

### Développement
- Jakarta EE 10.1.0
- Hibernate 6.4.1
- JUnit 5.10.1
- Mockito 5.10.1
- SLF4J 2.0.13
- Logback 1.4.15 