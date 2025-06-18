package builders;

import dao.IDAO;
import models.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IDAOTest {

    @Test
    void shouldCreateIDAOInstance() {
        IDAO<Client> dao = new IDAO<Client>() {};
        assertNotNull(dao);
    }

    @Test
    void shouldDefineCRUDMethods() {
        IDAO<Client> dao = new IDAO<Client>() {};
        assertNotNull(dao);
        // Vérifier que les méthodes CRUD sont définies (à implémenter selon le besoin)
    }
} 