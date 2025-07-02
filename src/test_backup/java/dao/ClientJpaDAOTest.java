package dao;

import dao.jpa.ClientJpaDAO;
import models.Client;
import models.Contrat;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientJpaDAOTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientJpaDAOTest.class);
    private ClientJpaDAO dao;
    private Client testClient;
    private Contrat testContrat;

    @BeforeEach
    void setUp() throws ValidationException {
        logger.info("Initialisation du test ClientJpaDAO");
        dao = new ClientJpaDAO();
        
        // Création d'une adresse
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("12");
        adresse.setNomRue("Rue de Paris");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        
        // Création du client avec le bon constructeur
        testClient = new Client();
        testClient.setRaisonSociale("Test Client");
        testClient.setAdresse(adresse);
        testClient.setTelephone("0123456789");
        testClient.setMail("client@test.com");
        testClient.setChiffreAffaire(Integer.valueOf(1000));
        testClient.setNbrEmploye(Integer.valueOf(10));
        testClient.setCommentaire("Commentaire test");
        
        testContrat = new Contrat(testClient, "Contrat Test", new BigDecimal("1000.00"));
        testContrat.setDateDebut(LocalDate.now());
        testContrat.setDateFin(LocalDate.now().plusYears(1L));
        testClient.addContrat(testContrat);
        logger.debug("Client de test créé : {}", testClient);
    }

    @Test
    @DisplayName("Devrait sauvegarder un client et le retrouver par son ID")
    void shouldSaveAndFindClientById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.info("Test : Sauvegarde et recherche d'un client par ID");
        
        // Sauvegarde du client
        logger.debug("Sauvegarde du client : {}", testClient);
        dao.save(testClient);
        
        // Recherche du client
        logger.debug("Recherche du client avec l'ID : {}", testClient.getIdentifiantClient());
        Optional<Client> found = dao.findById(testClient.getIdentifiantClient());
        
        // Vérifications
        logger.debug("Vérification des résultats");
        assertTrue(found.isPresent());
        assertEquals("Test Client", found.get().getRaisonSociale());
        assertEquals("12", found.get().getAdresse().getNumeroRue());
        assertEquals("0123456789", found.get().getTelephone());
        assertEquals("client@test.com", found.get().getMail());
        assertEquals(Integer.valueOf(1000), found.get().getChiffreAffaire());
        assertEquals(Integer.valueOf(10), found.get().getNbrEmploye());
        assertEquals("Commentaire test", found.get().getCommentaire());
        assertEquals(Integer.valueOf(1), Integer.valueOf(found.get().getContrats().size()));
        
        logger.info("Test réussi : Le client a été correctement sauvegardé et retrouvé");
    }

    @Test
    @DisplayName("Devrait retourner vide si le client n'existe pas")
    void shouldReturnEmptyIfClientNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.info("Test : Recherche d'un client inexistant");
        
        // Recherche d'un client avec un ID inexistant
        logger.debug("Recherche d'un client avec un ID inexistant");
        Optional<Client> found = dao.findById(Integer.valueOf(9999));
        
        // Vérification
        logger.debug("Vérification que le client n'est pas trouvé");
        assertTrue(found.isEmpty());
        
        logger.info("Test réussi : Aucun client n'a été trouvé pour un ID inexistant");
    }

    @Test
    @DisplayName("Devrait supprimer un client")
    void shouldDeleteClient() throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.info("Test : Suppression d'un client");
        
        // Sauvegarde du client
        logger.debug("Sauvegarde du client : {}", testClient);
        dao.save(testClient);
        
        // Suppression du client
        logger.debug("Suppression du client avec l'ID : {}", testClient.getIdentifiantClient());
        dao.delete(testClient);
        
        // Vérification que le client n'existe plus
        logger.debug("Vérification que le client a été supprimé");
        Optional<Client> found = dao.findById(testClient.getIdentifiantClient());
        assertTrue(found.isEmpty());
        
        logger.info("Test réussi : Le client a été correctement supprimé");
    }

    @Test
    @DisplayName("Devrait mettre à jour un client")
    void shouldUpdateClient() throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.info("Test : Mise à jour d'un client");
        
        // Sauvegarde du client initial
        logger.debug("Sauvegarde du client initial : {}", testClient);
        dao.save(testClient);
        
        // Modification des informations du client
        logger.debug("Modification des informations du client");
        testClient.setRaisonSociale("Client Updated");
        
        Adresse nouvelleAdresse = new Adresse();
        nouvelleAdresse.setNumeroRue("15");
        nouvelleAdresse.setNomRue("Rue de Lyon");
        nouvelleAdresse.setCodePostal("69000");
        nouvelleAdresse.setVille("Lyon");
        nouvelleAdresse.setPays("France");
        testClient.setAdresse(nouvelleAdresse);
        
        testClient.setTelephone("0987654321");
        testClient.setMail("updated@client.com");
        testClient.setChiffreAffaire(Integer.valueOf(2000));
        testClient.setNbrEmploye(Integer.valueOf(20));
        testClient.setCommentaire("Commentaire mis à jour");
        
        // Mise à jour du client
        logger.debug("Mise à jour du client : {}", testClient);
        dao.update(testClient);
        
        // Vérification des modifications
        logger.debug("Vérification des modifications");
        Optional<Client> found = dao.findById(testClient.getIdentifiantClient());
        assertTrue(found.isPresent());
        assertEquals("Client Updated", found.get().getRaisonSociale());
        assertEquals("15", found.get().getAdresse().getNumeroRue());
        assertEquals("0987654321", found.get().getTelephone());
        assertEquals("updated@client.com", found.get().getMail());
        assertEquals(Integer.valueOf(2000), found.get().getChiffreAffaire());
        assertEquals(Integer.valueOf(20), found.get().getNbrEmploye());
        assertEquals("Commentaire mis à jour", found.get().getCommentaire());
        
        logger.info("Test réussi : Le client a été correctement mis à jour");
    }
} 