package services;

import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProspectServiceTest {

    @Mock
    private ProspectJpaDAO prospectDAO;

    private ProspectService prospectService;

    @BeforeEach
    void setUp() {
        prospectService = new ProspectService(prospectDAO);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner un prospect quand l'ID existe")
        void shouldReturnProspectWhenIdExists() throws ValidationException, DatabaseException, ResourceNotFoundException {
            // Arrange
            Long id = 1L;
            Prospect expectedProspect = new Prospect();
            expectedProspect.setIdentifiant(id);
            when(prospectDAO.findById(id)).thenReturn(Optional.of(expectedProspect));

            // Act
            Prospect result = prospectService.findById(id);

            // Assert
            assertNotNull(result);
            assertEquals(expectedProspect, result);
            verify(prospectDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand l'ID n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            when(prospectDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> prospectService.findById(id));
            verify(prospectDAO).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner la liste de tous les prospects")
        void shouldReturnAllProspects() throws DatabaseException {
            // Arrange
            List<Prospect> expectedProspects = Arrays.asList(
                new Prospect(), new Prospect(), new Prospect()
            );
            when(prospectDAO.findAll()).thenReturn(expectedProspects);

            // Act
            List<Prospect> result = prospectService.findAll();

            // Assert
            assertEquals(expectedProspects.size(), result.size());
            assertEquals(expectedProspects, result);
            verify(prospectDAO).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand il n'y a pas de prospects")
        void shouldReturnEmptyListWhenNoProspects() throws DatabaseException {
            // Arrange
            when(prospectDAO.findAll()).thenReturn(List.of());

            // Act
            List<Prospect> result = prospectService.findAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(prospectDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de findByRaisonSociale")
    class FindByRaisonSocialeTests {
        @Test
        @DisplayName("Devrait trouver des prospects par raison sociale")
        void shouldFindProspectsByRaisonSociale() throws DatabaseException, ValidationException {
            // Arrange
            String raisonSociale = "Test Company";
            List<Prospect> expectedProspects = Arrays.asList(
                new Prospect(), new Prospect()
            );
            when(prospectDAO.findByRaisonSociale(raisonSociale)).thenReturn(expectedProspects);

            // Act
            List<Prospect> result = prospectService.findByRaisonSociale(raisonSociale);

            // Assert
            assertEquals(expectedProspects.size(), result.size());
            assertEquals(expectedProspects, result);
            verify(prospectDAO).findByRaisonSociale(raisonSociale);
        }
    }

    @Nested
    @DisplayName("Tests de save")
    class SaveTests {
        @Test
        @DisplayName("Devrait sauvegarder un prospect avec succès")
        void shouldSaveProspectSuccessfully() throws ValidationException, DatabaseException {
            // Arrange
            Prospect prospect = new Prospect();
            prospect.setRaisonSociale("Test Company");
            when(prospectDAO.existsByRaisonSociale(prospect.getRaisonSociale())).thenReturn(false);

            // Act
            prospectService.save(prospect);

            // Assert
            verify(prospectDAO).existsByRaisonSociale(prospect.getRaisonSociale());
            verify(prospectDAO).save(prospect);
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException quand la raison sociale existe déjà")
        void shouldThrowDatabaseExceptionWhenRaisonSocialeExists() throws ValidationException, DatabaseException {
            // Arrange
            Prospect prospect = new Prospect();
            prospect.setRaisonSociale("Test Company");
            when(prospectDAO.existsByRaisonSociale(prospect.getRaisonSociale())).thenReturn(true);

            // Act & Assert
            assertThrows(DatabaseException.class, () -> prospectService.save(prospect));
            verify(prospectDAO).existsByRaisonSociale(prospect.getRaisonSociale());
            verify(prospectDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour un prospect avec succès")
        void shouldUpdateProspectSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Prospect prospect = new Prospect();
            prospect.setIdentifiant(1L);

            // Act
            prospectService.update(prospect);

            // Assert
            verify(prospectDAO).update(prospect);
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer un prospect avec succès")
        void shouldDeleteProspectSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Prospect prospect = new Prospect();
            prospect.setIdentifiant(1L);

            // Act
            prospectService.delete(prospect);

            // Assert
            verify(prospectDAO).delete(prospect);
        }
    }
} 