# Installation des dépendances

Pour installer les dépendances NPM, exécutez la commande suivante dans le dossier webapp :

```bash
npm install
```

Cela installera Bootstrap et ses dépendances dans le dossier `node_modules`.

Les fichiers CSS et JS de Bootstrap seront disponibles via les URLs suivantes dans les JSP :
- CSS : `<c:url value="/node_modules/bootstrap/dist/css/bootstrap.min.css"/>`
- JS : `<c:url value="/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"/>` 