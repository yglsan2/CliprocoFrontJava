package services;

import dao.IDAO;
import models.Client;
import models.Adresse;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des clients.
 */
public class ClientService {
    private final IDAO<Client, Long> clientDAO;
    private final IDAO<Adresse, Long> adresseDAO;

    public ClientService(IDAO<Client, Long> clientDAO, IDAO<Adresse, Long> adresseDAO) {
        this.clientDAO = clientDAO;
        this.adresseDAO = adresseDAO;
        LogManager.logInfo("ClientService initialisé avec succès");
    }

    public Optional<Client> findById(Long id) {
        LogManager.logInfo("Recherche du client avec l'ID: " + id);
        return clientDAO.findById(id);
    }

    public List<Client> findAll() {
        LogManager.logInfo("Récupération de tous les clients");
        return clientDAO.findAll();
    }

    public Client create(Client client) {
        LogManager.logInfo("Création d'un nouveau client: " + client.getRaisonSociale());
        return clientDAO.save(client);
    }

    public Client update(Client client) {
        LogManager.logInfo("Mise à jour du client avec l'ID: " + client.getIdentifiant());
        return clientDAO.update(client);
    }

    public void delete(Long id) {
        LogManager.logInfo("Suppression du client avec l'ID: " + id);
        clientDAO.delete(id);
    }

    public void close() {
        LogManager.logInfo("Fermeture du ClientService");
        if (clientDAO instanceof AutoCloseable) {
            try {
                ((AutoCloseable) clientDAO).close();
            } catch (Exception e) {
                LogManager.logError("Erreur lors de la fermeture du clientDAO: " + e.getMessage());
            }
        }
        if (adresseDAO instanceof AutoCloseable) {
            try {
                ((AutoCloseable) adresseDAO).close();
            } catch (Exception e) {
                LogManager.logError("Erreur lors de la fermeture du adresseDAO: " + e.getMessage());
            }
        }
    }

    public Optional<Client> findByEmail(String email) {
        return findAll().stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    public List<Client> findByRaisonSociale(String raisonSociale) {
        LogManager.logInfo("Recherche des clients avec la raison sociale: " + raisonSociale);
        try {
            List<Client> clients = ((ClientJpaDAO) clientDAO).findByRaisonSociale(raisonSociale);
            LogManager.logInfo("Nombre de clients trouvés: " + clients.size());
            return clients;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des clients par raison sociale", e);
            throw new RuntimeException("Erreur lors de la recherche des clients par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) {
        LogManager.logInfo("Vérification de l'existence d'un client avec la raison sociale: " + raisonSociale);
        try {
            boolean exists = ((ClientJpaDAO) clientDAO).existsByRaisonSociale(raisonSociale);
            LogManager.logInfo("Client " + (exists ? "existe" : "n'existe pas") + " avec cette raison sociale");
            return exists;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence du client", e);
            throw new RuntimeException("Erreur lors de la vérification de l'existence du client", e);
        }
    }
} 