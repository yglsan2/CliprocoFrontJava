# TODO - 18/06/2025 17:10

## Corrections restantes

1. **Adresse.java**
   - Corriger les annotations @Max et @Min pour utiliser des valeurs constantes primitives
   - Lignes 38 et 40

2. **ContratBuilder.java**
   - Déclarer/initialiser les variables manquantes :
     - identifiant (ligne 54)
     - idClient (ligne 115)

3. **UpdateClientsController.java**
   - Corriger le constructeur de Client (ligne 57)
   - Gérer la conversion double vers Integer (ligne 64)

4. **MetricsManager.java**
   - Corriger les conversions long vers Integer (lignes 13, 18, 28)
   - Utiliser Long au lieu de Integer pour les compteurs

## Notes
- Toutes les autres conversions Long/Integer ont été effectuées
- Les tests ont été supprimés et seront à recréer
- Vérifier la compilation après chaque correction

## TODO - 2025-06-19 16:58:08

- Continuer la correction des erreurs de compilation restantes (surtout sur les modèles et certains builders).
- Vérifier la compilation sans les tests (`-DskipTests` fonctionne, mais il reste des erreurs à corriger pour une compilation complète).
- Finaliser la migration du projet pour compatibilité Tomcat 11 (suppression CDI, validation, etc.).
- Préparer la documentation pour la nouvelle organisation du projet (architecture MVC simple, plus de CDI, plus de validation Jakarta).
- Notifier le changement d'organisation sur le dépôt [CliprocoFrontJava](https://github.com/yglsan2/CliprocoFrontJava).
- Faire un push sur une branche dédiée à la migration/transition.
