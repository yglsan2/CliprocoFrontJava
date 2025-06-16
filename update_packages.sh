#!/bin/bash

# Déplacement des fichiers
cd CliprocoJEE/src/main/java
mkdir -p temp
mv fr/afpa/pompey/cda17/* temp/
rm -rf fr
mv temp/* .
rmdir temp

# Mise à jour des déclarations de package
find . -name "*.java" -type f -exec sed -i '' 's/package fr\.afpa\.pompey\.cda17\./package /g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import fr\.afpa\.pompey\.cda17\./import /g' {} +

# Mettre à jour les références dans les fichiers JSP
find src/main/webapp -name "*.jsp" -type f -exec sed -i '' 's/type="models\.afpa\.pompey\.cda17\.models\./type="models./g' {} +

# Supprimer les imports en double
find src/main/java -name "*.java" -type f -exec sed -i '' '/^import models\./d' {} +

# Ajouter les imports manquants dans les contrôleurs
find src/main/java/controllers -name "*Controller.java" -type f -exec sed -i '' '/package/a\
import models.Client;\
import models.Prospect;\
import models.Societe;\
import models.Adresse;\
import models.Contrat;\
import models.User;' {} +

echo "Mise à jour des packages terminée." 