#!/bin/bash

echo "=== Déploiement de CliprocoJEE ==="

# Arrêt de Tomcat
echo "1. Arrêt de Tomcat..."
sudo systemctl stop tomcat
sleep 3

# Build du projet
echo "2. Build du projet Maven..."
./mvnw clean package

# Suppression de l'ancien déploiement
echo "3. Suppression de l'ancien déploiement..."
sudo rm -rf /opt/tomcat11/webapps/CliprocoJEE

# Copie du nouveau WAR
echo "4. Copie du nouveau WAR..."
sudo cp target/CliprocoJEE.war /opt/tomcat11/webapps/

# Démarrage de Tomcat
echo "5. Démarrage de Tomcat..."
sudo systemctl start tomcat

echo "6. Attente du déploiement..."
sleep 10

echo "=== Déploiement terminé ==="
echo "Application accessible sur: http://localhost:8080/CliprocoJEE/"
echo "Page clients: http://localhost:8080/CliprocoJEE/app?cmd=clients.liste"
echo "Page prospects: http://localhost:8080/CliprocoJEE/app?cmd=prospects.liste" 