package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

public final class ListeProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ListeProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de ListeProspectsController");

        try {
            ProspectJpaDAO prospectDAO = new ProspectJpaDAO();

            LOGGER.info("Récupération de tous les prospects");
            List<Prospect> prospects = prospectDAO.findAll();

            LOGGER.info("Nombre de prospects trouvés: " + prospects.size());

            request.setAttribute("prospects", prospects);
            LOGGER.info("Prospects ajoutés aux attributs de la requête");

        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération des prospects: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement des prospects.");
        }

        return "/WEB-INF/jsp/prospects/liste.jsp";
    }
}
