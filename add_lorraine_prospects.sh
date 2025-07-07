#!/bin/bash

echo "=== Ajout des prospects lorrains ==="

# Vérifier si les variables d'environnement sont définies
if [ -z "$MYSQL_USER" ] || [ -z "$MYSQL_PASSWORD" ]; then
    echo "Erreur: Variables d'environnement MYSQL_USER et MYSQL_PASSWORD non définies"
    echo "Exemple d'utilisation:"
    echo "export MYSQL_USER=root"
    echo "export MYSQL_PASSWORD=votre_mot_de_passe"
    echo "./add_lorraine_prospects.sh"
    exit 1
fi

# Ajouter les prospects lorrains
echo "1. Ajout des prospects lorrains..."
mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" cliproco < add_lorraine_data.sql

if [ $? -eq 0 ]; then
    echo "✅ Prospects lorrains ajoutés avec succès !"
    echo ""
    echo "Prospects ajoutés :"
    echo "- Becker Mireille (Café Becker, Nancy)"
    echo "- Simonin Armand (Fleuriste Simonin, Épinal)"
    echo "- Collin Léa (Coiffure Collin, Metz)"
    echo ""
    echo "Application accessible sur: http://localhost:8080/CliprocoJEE/app?cmd=prospects.liste"
else
    echo "❌ Erreur lors de l'ajout des prospects lorrains"
    exit 1
fi 