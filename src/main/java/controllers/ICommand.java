package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface pour le pattern Command dans l'architecture MVC.
 * 
 * <p>Cette interface définit le contrat pour toutes les commandes
 * de l'application. Elle implémente le pattern Command qui permet
 * d'encapsuler une requête en tant qu'objet, permettant de paramétrer
 * les clients avec différentes requêtes, de mettre en file d'attente
 * les requêtes et de supporter les opérations annulables.</p>
 * 
 * <p>Chaque commande représente une action métier spécifique
 * (création, modification, suppression, consultation) et retourne
 * une chaîne de caractères représentant la vue à afficher ou
 * l'URL de redirection.</p>
 * 
 * <p>Cette interface est utilisée par le FrontController pour
 * dispatcher les requêtes vers les bonnes commandes selon
 * les paramètres de la requête.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
public interface ICommand {
    /**
     * Exécute la commande avec les paramètres de la requête.
     * 
     * <p>Cette méthode contient la logique métier de la commande.
     * Elle traite les paramètres de la requête, effectue les
     * opérations nécessaires (validation, persistance, etc.)
     * et retourne le nom de la vue à afficher ou l'URL de redirection.</p>
     * 
     * <p>La méthode peut :
     * <ul>
     *   <li>Valider les données de la requête</li>
     *   <li>Effectuer des opérations de persistance</li>
     *   <li>Préparer les données pour la vue</li>
     *   <li>Gérer les erreurs et exceptions</li>
     *   <li>Définir des attributs de session ou de requête</li>
     * </ul></p>
     * 
     * @param request  L'objet HttpServletRequest contenant les paramètres de la requête
     * @param response L'objet HttpServletResponse pour la réponse HTTP
     * @return Le nom de la vue à afficher ou l'URL de redirection
     * @throws Exception Si une erreur survient lors de l'exécution de la commande
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}