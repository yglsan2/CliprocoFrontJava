# CliProCo - Mon Application de Gestion des Clients et Prospects

Salut ! Je suis ravi de vous présenter CliProCo, une application que j'ai développée pour gérer mes clients et prospects. J'ai créé cette application en utilisant Jakarta EE et en suivant les bonnes pratiques de développement.

## Ce que j'ai mis en place

J'ai développé une application web qui me permet de :

* Gérer mes clients et prospects efficacement
* Suivre mes relations commerciales
* Avoir un aperçu rapide des conditions météo pour chaque contact
* Gérer les utilisateurs de l'application
* Assurer la conformité RGPD et RGAA

## Optimisations et Bonnes Pratiques

### Optimisation des Images
J'ai choisi d'utiliser un logo en SVG miniaturisé au maximum, avec des media queries qui dimensionnent l'image. Cette technique permet d'obtenir :
* La meilleure qualité d'image possible avec un minimum d'espace utilisé
* Une adaptation automatique à toutes les tailles d'écran
* Une meilleure performance de chargement
* Un avantage dans les algorithmes de Google pour le référencement naturel (SEO)

### Sécurité
* Authentification sécurisée avec Argon2 pour le hachage des mots de passe
* Protection CSRF sur tous les formulaires
* Gestion sécurisée des sessions avec remember me
* Conformité RGPD et RGAA

## Prérequis

* Java 21 (OpenJDK)
* Maven 3.8 ou supérieur
* Tomcat 10.1.19 ou supérieur

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

4. Configurez la variable d'environnement APP_SECRET :
```bash
export APP_SECRET=votre_secret_ici
```

## Compilation et Déploiement

1. Compilez le projet avec Maven :
```bash
mvn clean package
```

2. Le fichier WAR sera généré dans le dossier `target/cliproco.war`
3. Déployez le fichier WAR dans Tomcat :
   * Copiez le fichier `target/cliproco.war` dans le dossier `webapps` de Tomcat
   * Redémarrez Tomcat

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
│   │   │           ├── controller/
│   │   │           ├── dao/
│   │   │           ├── filter/
│   │   │           ├── model/
│   │   │           └── service/
│   │   ├── resources/
│   │   │   ├── hibernate.cfg.xml
│   │   │   └── logback.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── views/
│   │       ├── resources/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── index.jsp
│   └── test/
├── pom.xml
└── README.md
```

## Fonctionnalités

* Gestion des clients
* Gestion des prospects
* Géolocalisation
* Intégration météo
* Interface responsive avec Bootstrap
* Authentification utilisateur
* Remember me
* Protection CSRF

## Technologies Utilisées

* Java 21
* Maven
* Tomcat 10.1.19
* JSP/JSTL
* Bootstrap 5.3.0
* Leaflet.js
* JavaScript (ES6+)
* Hibernate
* Jakarta EE
* Argon2

## Contribution

1. Fork le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails. 