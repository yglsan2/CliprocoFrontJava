#!/bin/bash

# Script de réinitialisation de la base de données CliprocoJEE
# avec les nouvelles données d'entreprises rigolotes de Lorraine

echo "🔄 Réinitialisation de la base de données CliprocoJEE..."
echo "📍 Nouvelles données d'entreprises rigolotes de Lorraine (Laxou, Nancy, Metz)"

# Variables de configuration
DB_NAME="cliprocobdd"
DB_USER="root"
DB_PASS="password"
MYSQL_CMD="mysql"

# Vérifier si MySQL est installé
if ! command -v $MYSQL_CMD &> /dev/null; then
    echo "❌ MySQL n'est pas installé ou n'est pas dans le PATH"
    exit 1
fi

# Demander le mot de passe MySQL si nécessaire
if [ -z "$DB_PASS" ]; then
    echo -n "🔐 Mot de passe MySQL (laissez vide si aucun): "
    read -s DB_PASS
    echo
fi

# Construire la commande MySQL
if [ -z "$DB_PASS" ]; then
    MYSQL_CMD_FULL="$MYSQL_CMD -u $DB_USER"
else
    MYSQL_CMD_FULL="$MYSQL_CMD -u $DB_USER -p$DB_PASS"
fi

# Vérifier la connexion à MySQL
echo "🔍 Test de connexion à MySQL..."
if ! $MYSQL_CMD_FULL -e "SELECT 1;" &> /dev/null; then
    echo "❌ Impossible de se connecter à MySQL"
    echo "   Vérifiez que MySQL est démarré et que les identifiants sont corrects"
    exit 1
fi

echo "✅ Connexion MySQL réussie"

# Supprimer la base de données existante si elle existe
echo "🗑️  Suppression de la base de données existante..."
$MYSQL_CMD_FULL -e "DROP DATABASE IF EXISTS $DB_NAME;"

# Créer la nouvelle base de données
echo "🏗️  Création de la nouvelle base de données..."
$MYSQL_CMD_FULL -e "CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"

# Importer les nouvelles données
echo "📥 Import des nouvelles données d'entreprises rigolotes..."
$MYSQL_CMD_FULL $DB_NAME < production/mysql/cliprocobdd.sql

# Vérifier l'import
echo "🔍 Vérification de l'import..."
CLIENT_COUNT=$($MYSQL_CMD_FULL $DB_NAME -e "SELECT COUNT(*) FROM clients;" -s -N)
PROSPECT_COUNT=$($MYSQL_CMD_FULL $DB_NAME -e "SELECT COUNT(*) FROM prospects;" -s -N)
ADDRESS_COUNT=$($MYSQL_CMD_FULL $DB_NAME -e "SELECT COUNT(*) FROM adresses;" -s -N)

echo "✅ Base de données réinitialisée avec succès !"
echo ""
echo "📊 Statistiques des données importées :"
echo "   🏢 Clients : $CLIENT_COUNT entreprises rigolotes"
echo "   👥 Prospects : $PROSPECT_COUNT prospects amusants"
echo "   📍 Adresses : $ADDRESS_COUNT adresses en Lorraine"
echo ""
echo "🎉 Les nouvelles entreprises incluent :"
echo "   • Quiche Lorraine Express (Laxou)"
echo "   • Mirabelle & Co (Nancy)"
echo "   • Bretzel Brothers (Metz)"
echo "   • Choucroute Royale (Nancy)"
echo "   • Schnaps & Schnitzel (Laxou)"
echo "   • Et bien d'autres..."
echo ""
echo "🔑 Identifiants de connexion :"
echo "   Utilisateur : benja2"
echo "   Mot de passe : (celui configuré dans la base)"
echo ""
echo "🚀 Vous pouvez maintenant redémarrer l'application !" 