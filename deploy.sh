#!/bin/bash

echo "=== Déploiement de CliprocoJEE ==="

# Arrêt de Tomcat
echo "1. Arrêt de Tomcat..."
./apache-tomcat-11.0.8/bin/shutdown.sh
sleep 3

# Build du projet
echo "2. Build du projet Maven..."
./mvnw clean package

# Suppression de l'ancien déploiement
echo "3. Suppression de l'ancien déploiement..."
sudo rm -rf /opt/apache-tomcat-11.0.0-M18/webapps/CliprocoJEE

# Copie du nouveau WAR
echo "4. Copie du nouveau WAR..."
sudo cp target/CliprocoJEE.war /opt/apache-tomcat-11.0.0-M18/webapps/

# Démarrage de Tomcat
echo "5. Démarrage de Tomcat..."
./apache-tomcat-11.0.8/bin/startup.sh

echo "6. Attente du déploiement..."
sleep 10

echo "=== Déploiement terminé ==="
echo "Application accessible sur: http://localhost:8080/CliprocoJEE/"
echo "Page clients: http://localhost:8080/CliprocoJEE/clients"
echo "Page prospects: http://localhost:8080/CliprocoJEE/prospects" 