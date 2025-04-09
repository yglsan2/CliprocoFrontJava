#!/bin/bash

# Détection de l'OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - MAMP
    MYSQL_PATH="/Applications/MAMP/Library/bin/mysql"
    DB_PORT="8889"
    DB_USER="root"
    DB_PASS="root"
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    # Windows - WAMP
    MYSQL_PATH="C:/wamp64/bin/mysql/mysql8.0.31/bin/mysql.exe"
    DB_PORT="3306"
    DB_USER="root"
    DB_PASS=""
else
    echo "Système d'exploitation non supporté"
    exit 1
fi

DB_NAME="cliproco"

# Vérifier si MySQL est disponible
if [ ! -f "$MYSQL_PATH" ]; then
    echo "MySQL n'est pas trouvé. Vérifiez que MAMP/WAMP est installé."
    echo "Chemin recherché : $MYSQL_PATH"
    exit 1
fi

# Créer la base de données et exécuter le script SQL
echo "Initialisation de la base de données..."
"$MYSQL_PATH" -u "$DB_USER" ${DB_PASS:+-p"$DB_PASS"} -P "$DB_PORT" < src/main/resources/schema.sql

if [ $? -eq 0 ]; then
    echo "Base de données initialisée avec succès!"
else
    echo "Erreur lors de l'initialisation de la base de données."
    exit 1
fi 