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
