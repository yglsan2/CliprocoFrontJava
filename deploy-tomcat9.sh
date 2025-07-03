#!/bin/bash

echo "=== Déploiement de CliprocoJEE sur Tomcat 9 ==="

# Configuration Tomcat 9
TOMCAT_HOME="/opt/tomcat9"
WAR_FILE="target/CliprocoJEE.war"
WEBAPPS_DIR="$TOMCAT_HOME/webapps"
APP_NAME="CliprocoJEE"

echo "1. Arrêt de Tomcat 9..."
$TOMCAT_HOME/bin/shutdown.sh

echo "2. Build du projet Maven..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du build Maven"
    exit 1
fi

echo "3. Suppression de l'ancien déploiement..."
rm -rf "$WEBAPPS_DIR/$APP_NAME"
rm -f "$WEBAPPS_DIR/$APP_NAME.war"

echo "4. Copie du nouveau WAR..."
cp "$WAR_FILE" "$WEBAPPS_DIR/"

echo "5. Démarrage de Tomcat 9..."
$TOMCAT_HOME/bin/startup.sh

echo "6. Attente du déploiement..."
sleep 10

echo "=== Déploiement terminé ==="
echo "Application accessible sur: http://localhost:8080/$APP_NAME/"
echo "Page clients: http://localhost:8080/$APP_NAME/clients"
echo "Page prospects: http://localhost:8080/$APP_NAME/prospects" 