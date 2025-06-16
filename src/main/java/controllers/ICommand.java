package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface définissant le contrat pour les commandes de l'application.
 * Chaque commande représente une action spécifique qui peut être exécutée
 * en réponse à une requête HTTP.
 *
 * @since 1.0
 * @version 1.0
 */
public interface ICommand {
    /**
     * Exécute la commande associée à la requête HTTP.
     * Cette méthode est appelée par le FrontController pour traiter une requête
     * et retourne le chemin de la vue à afficher.
     *
     * @param request La requête HTTP reçue
     * @param response La réponse HTTP à envoyer
     * @return Le chemin de la vue à afficher (ex: "WEB-INF/views/home.jsp")
     * @throws Exception Si une erreur survient pendant l'exécution de la commande
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
