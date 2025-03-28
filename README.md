# Application de Gestion des Clients et Prospects

Cette application web Jakarta EE permet de gérer des clients et des prospects. Elle est construite avec le pattern MVC et utilise les technologies suivantes :

- Jakarta EE 10
- Maven
- Tomcat 10
- Bootstrap 5
- JSTL
- Hibernate Validator

## Fonctionnalités

- Gestion des clients (CRUD)
- Gestion des prospects (CRUD)
- Validation des données
- Interface utilisateur responsive
- Compteur de pages visitées

## Prérequis

- JDK 21
- Maven 3.8+
- Tomcat 10
- Navigateur web moderne

## Installation

1. Cloner le repository
2. Configurer Tomcat :
   - Créer un utilisateur admin dans `tomcat-users.xml`
   - Démarrer Tomcat

3. Configurer Maven :
   - Créer le fichier `~/.m2/settings.xml` avec les identifiants Tomcat
   - Exécuter `mvn package cargo:redeploy`

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── cliproco/
│   │           ├── controller/
│   │           ├── models/
│   │           └── servlet/
│   └── webapp/
│       └── WEB-INF/
│           └── JSP/
└── resources/
    └── checkstyle.xml
```

## Commandes utiles

- Compiler le projet : `mvn package`
- Déployer l'application : `mvn cargo:deploy`
- Retirer l'application : `mvn cargo:undeploy`
- Redéployer l'application : `mvn cargo:redeploy`
- Vérifier le style du code : `mvn checkstyle:checkstyle`

## Validation du code

Le projet utilise Checkstyle pour la validation du code. La configuration se trouve dans `src/main/resources/checkstyle.xml`.

## Navigation

- Page d'accueil : `http://localhost:8080/cliproco/accueil`
- Gestion des clients : `http://localhost:8080/cliproco/client`
- Gestion des prospects : `http://localhost:8080/cliproco/prospect`

## Prérequis

- Java 21 (OpenJDK)
- Maven 3.8 ou supérieur
- Tomcat 10.1.19 ou supérieur

## Configuration

1. Assurez-vous d'avoir Java 21 installé :
```bash
java -version
```

2. Vérifiez que Maven est installé :
```bash
mvn -version
```

3. Clonez le projet :
```bash
git clone [URL_DU_PROJET]
cd cliproco
```

## Compilation et Déploiement

1. Compilez le projet avec Maven :
```bash
mvn clean package
```

2. Le fichier WAR sera généré dans le dossier `target/cliproco.war`

3. Déployez le fichier WAR dans Tomcat :
   - Copiez le fichier `target/cliproco.war` dans le dossier `webapps` de Tomcat
   - Redémarrez Tomcat

## Développement

Pour le développement, vous pouvez utiliser le plugin Tomcat de Maven :

```bash
mvn tomcat7:run
```

L'application sera accessible à l'adresse : http://localhost:8080/cliproco

## Structure du Projet

```
cliproco/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cliproco/
│   │   │           └── servlet/
│   │   ├── resources/
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── css/
│   │       ├── js/
│   │       ├── images/
│   │       └── index.jsp
│   └── test/
├── pom.xml
└── README.md
```

## Fonctionnalités

- Gestion des clients
- Gestion des prospects
- Géolocalisation
- Intégration météo
- Interface responsive avec Bootstrap
- Authentification utilisateur

## Technologies Utilisées

- Java 21
- Maven
- Tomcat 10.1.19
- JSP/JSTL
- Bootstrap 5.3.0
- Leaflet.js
- JavaScript (ES6+)

## Contribution

1. Fork le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

# Mon Application de Gestion des Clients et Prospects, CliProCo (Client-Prospect_Corporation)

J'ai développé cette application pour gérer efficacement mes clients et prospects. Elle me permet de suivre mes relations commerciales et d'avoir un aperçu rapide des conditions météorologiques à l'emplacement de chaque contact.

## Fonctionnalités que j'ai implémentées

- Gestion séparée des clients et prospects
- Géolocalisation des contacts avec affichage sur carte
- Affichage des conditions météo en temps réel
- Système d'authentification
- Gestion des cookies et conformité RGPD
- Stockage local des données

## Pourquoi j'ai choisi d'utiliser .mjs

J'ai décidé d'utiliser l'extension .mjs pour tous mes fichiers JavaScript au lieu de .js. Voici pourquoi :
- C'est plus clair : je vois direct que c'est un module ES6
- Node.js reconnaît automatiquement que c'est un module ES6 (pas besoin de "type": "module" dans package.json)
- Je peux mélanger des fichiers .js classiques et .mjs dans le même projet
- Ça rend le code plus maintenable : on sait directement qu'on peut utiliser import/export
- Ça évite les surprises côté serveur : le fichier sera toujours traité comme un module ES6

Note importante : Dans le HTML, il faut quand même mettre type="module" dans les balises script car c'est une exigence du navigateur, indépendamment de l'extension du fichier.

## Comment installer mon application

1. Cloner mon repo :
git clone [URL_DU_REPO]
cd [NOM_DU_DOSSIER]

2. Installer les dépendances :
npm install

3. Démarrer le serveur :
npm start

L'application sera accessible sur http://localhost:5000

## Organisation de mes fichiers

Dans le dossier js, j'ai mis tous mes fichiers JavaScript :
- auth.mjs pour gérer l'authentification
- clients.mjs et prospects.mjs pour la gestion des contacts
- geolocWeather.mjs pour la météo et la géolocalisation
- config.mjs pour mes clés d'API
- main.mjs comme point d'entrée
- rgpd.mjs pour la gestion des cookies

À la racine, j'ai mes fichiers principaux :
- clients.html et prospects.html pour les pages web
- server.mjs pour le serveur Express
- styles.css pour le design

## Les APIs que j'utilise

- data.gouv.fr pour trouver les coordonnées des adresses
- InfoClimat pour avoir la météo
- Leaflet pour afficher les cartes

## Mes pense-bêtes

- Ne pas oublier de mettre à jour les dépendances de temps en temps
- Les clés d'API sont dans config.mjs
- Tout est sauvegardé dans le navigateur
- Si le port 5000 pose problème, je peux le changer dans server.mjs

## Bonnes Pratiques Implémentées

### Éco-conception 🌱
- Compression GZIP des ressources
- Mise en cache optimisée
- Lazy loading des images
- Minimisation des requêtes HTTP
- Optimisation des ressources statiques

### Accessibilité (RGAA) ♿
- Structure HTML5 sémantique
- Navigation au clavier
- Labels ARIA
- Contrastes de couleurs respectés
- Textes alternatifs pour les images

### RGPD 🔒
- Bannière de cookies détaillée
- Choix granulaires des préférences
- Export des données utilisateur
- Suppression des données sur demande
- Stockage local sécurisé

### SEO 🔍
- Meta tags optimisés
- Structure HTML sémantique
- Sitemap XML
- URLs propres
- Contenu accessible aux robots

## Performances et Optimisation

Pour maintenir les performances optimales :
- Utiliser la compression des images
- Mettre à jour régulièrement les dépendances
- Vérifier les scores Lighthouse
- Tester l'accessibilité avec WAVE
- Monitorer la consommation énergétique

## Vous rencontrez des problèmes ?

Pas de panique ! Si le serveur refuse de démarrer correctement ou si npm s'obstine à chercher server.js au lieu de server.mjs, voici comment s'en sortir :

### 1. D'abord, on arrête tout !
Sur Mac/Linux :

pkill -f node

Sur Windows :

taskkill /F /IM node.exe


### 2. On fait le grand nettoyage
La solution la plus efficace est de nettoyer le cache de npm. Ça marche sur toutes les plateformes :

npm cache clean --force
npm cache verify


Sur Mac/Linux, vous pouvez aussi supprimer le dossier cache de npm :

rm -rf ~/.npm

Sur Windows :

rd /s /q %AppData%\npm-cache


### 3. On repart de zéro
Maintenant, on supprime les dépendances et on réinstalle tout proprement :

Sur Mac/Linux :

rm -rf node_modules
npm install

Sur Windows :

rd /s /q node_modules
npm install


### 4. Et on relance !

npm start


Si malgré tout ça, le serveur s'entête à démarrer sur le port 3000 ou avec server.js, vérifiez que :
- Le fichier s'appelle bien server.mjs (et pas server.js)
- Le package.json contient bien "type": "module"
- Le script de démarrage dans package.json pointe bien vers server.mjs

Toujours pas ? Essayez de redémarrer votre éditeur de code ou votre terminal, parfois ça aide !

### Solution de dernier recours
Si vraiment rien ne fonctionne après avoir tout essayé, voici la solution nucléaire :

1. Sauvegardez d'abord votre code :

cp server.mjs server.mjs.backup


2. Supprimez TOUT (sauf votre backup) :

rm -rf node_modules package-lock.json .npm .cache/npm


3. Supprimez même le package.json et recréez-le :

rm package.json
npm init -y


4. Modifiez le nouveau package.json :
- Ajoutez `"type": "module"`
- Mettez `"start": "node server.mjs"` dans les scripts
- Réinstallez vos dépendances :

npm install express cors bootstrap @fortawesome/fontawesome-free leaflet node-fetch
npm install nodemon --save-dev


