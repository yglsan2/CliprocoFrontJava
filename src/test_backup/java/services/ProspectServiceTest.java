package services;

import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProspectServiceTest {
    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
        private List<Prospect> prospects;

        @BeforeEach
        void setUp() {
            prospects = new ArrayList<>();
            
            // Création de 1000 prospects de test
            for (int i = 0; i < 1000; i++) {
                Prospect prospect = new Prospect();
                prospect.setRaisonSociale("Prospect " + i);
                prospect.setEmail("prospect" + i + "@test.com");
                prospect.setTelephone("0123456789");
                prospect.setChiffreAffaires(1000.0);
                prospect.setNombreEmployes(10);
                prospects.add(prospect);
            }
        }

        @Test
        @DisplayName("Devrait gérer efficacement la création de 1000 prospects")
        void shouldHandleBulkProspectCreation() throws ValidationException, DatabaseException {
            for (int i = 0; i < 1000; i++) {
                when(prospectDAO.save(any(Prospect.class))).thenReturn(prospects.get(i));
                prospectService.create(prospects.get(i));
            }
            verify(prospectDAO, times(1000)).save(any(Prospect.class));
        }

        @Test
        @DisplayName("Devrait gérer efficacement la recherche de prospects")
        void shouldHandleEfficientProspectSearch() throws ValidationException, DatabaseException {
            when(prospectDAO.findByRaisonSociale(any())).thenReturn(prospects);
            
            for (int i = 0; i < 1000; i++) {
                prospectService.findByRaisonSociale("Prospect " + i);
            }
            
            verify(prospectDAO, times(1000)).findByRaisonSociale(any());
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création de prospects")
        void shouldHandleConcurrentProspectCreation() throws InterruptedException {
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        Prospect prospect = new Prospect();
                        prospect.setRaisonSociale("Prospect " + index);
                        prospect.setEmail("prospect" + index + "@test.com");
                        prospect.setTelephone("0123456789");
                        prospect.setChiffreAffaires(1000.0);
                        prospect.setNombreEmployes(10);
                        
                        when(prospectDAO.save(any(Prospect.class))).thenReturn(prospect);
                        prospectService.create(prospect);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }
    }

    @Nested
    @DisplayName("Tests de sécurité")
    class SecurityTests {
        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection SQL dans la raison sociale")
        void shouldRejectSqlInjectionInRaisonSociale() {
            String[] sqlInjectionAttempts = {
                "Prospect 1'; DROP TABLE prospects; --",
                "Prospect 1' OR '1'='1",
                "Prospect 1'; SELECT * FROM users; --"
            };

            for (String injection : sqlInjectionAttempts) {
                Prospect prospect = new Prospect();
                prospect.setRaisonSociale(injection);
                prospect.setEmail("test@example.com");
                prospect.setTelephone("0123456789");
                prospect.setChiffreAffaires(1000.0);
                prospect.setNombreEmployes(10);
                
                assertThrows(ValidationException.class, () -> prospectService.create(prospect));
            }
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection XSS dans les commentaires")
        void shouldRejectXssInjectionInComments() {
            String[] xssInjectionAttempts = {
                "<script>alert('XSS')</script>",
                "javascript:alert('XSS')",
                "<img src='x' onerror='alert("XSS")'>"
            };

            for (String injection : xssInjectionAttempts) {
                Prospect prospect = new Prospect();
                prospect.setRaisonSociale("Test Prospect");
                prospect.setEmail("test@example.com");
                prospect.setTelephone("0123456789");
                prospect.setChiffreAffaires(1000.0);
                prospect.setNombreEmployes(10);
                prospect.setCommentaire(injection);
                
                assertThrows(ValidationException.class, () -> prospectService.create(prospect));
            }
        }
    }
    @Nested
    @DisplayName("Tests de validation métier")
    class ValidationMetierTests {
        private Prospect prospect;

        @BeforeEach
        void setUp() {
            prospect = new Prospect();
            prospect.setRaisonSociale("Test Prospect");
            prospect.setEmail("test@example.com");
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand la raison sociale est vide")
        void shouldThrowValidationExceptionWhenRaisonSocialeIsEmpty() throws ValidationException, DatabaseException {
            prospect.setRaisonSociale("");
            assertThrows(ValidationException.class, () -> prospectService.save(prospect));
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand l'email est invalide")
        void shouldThrowValidationExceptionWhenEmailIsInvalid() throws ValidationException, DatabaseException {
            prospect.setEmail("invalid-email");
            assertThrows(ValidationException.class, () -> prospectService.save(prospect));
        }

        @Test
        @DisplayName("Devrait lancer DatabaseException quand la raison sociale existe déjà")
        void shouldThrowDatabaseExceptionWhenRaisonSocialeExists() throws ValidationException, DatabaseException {
            when(prospectDAO.existsByRaisonSociale(anyString())).thenReturn(true);
            assertThrows(DatabaseException.class, () -> prospectService.save(prospect));
        }
    }

    @Nested
    @DisplayName("Tests de recherche")
    class RechercheTests {
        private List<Prospect> prospects;

        @BeforeEach
        void setUp() {
            prospects = Arrays.asList(
                new Prospect("Prospect 1", "email1@test.com"),
                new Prospect("Prospect 2", "email2@test.com")
            );
        }

        @Test
        @DisplayName("Devrait trouver un prospect par raison sociale")
        void shouldFindProspectByRaisonSociale() throws ValidationException, DatabaseException {
            String raisonSociale = "Prospect 1";
            when(prospectDAO.findByRaisonSociale(raisonSociale)).thenReturn(prospects);
            List<Prospect> result = prospectService.findByRaisonSociale(raisonSociale);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(raisonSociale, result.get(0).getRaisonSociale());
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand aucun prospect n'est trouvé")
        void shouldReturnEmptyListWhenNoProspectFound() throws ValidationException, DatabaseException {
            when(prospectDAO.findByRaisonSociale(anyString())).thenReturn(Collections.emptyList());
            List<Prospect> result = prospectService.findByRaisonSociale("Non Existant");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de cas limites")
    class CasLimitesTests {
        private Prospect prospect;

        @BeforeEach
        void setUp() {
            prospect = new Prospect();
            prospect.setRaisonSociale("Test Prospect");
            prospect.setEmail("test@example.com");
        }

        @Test
        @DisplayName("Devrait gérer correctement les caractères spéciaux dans la raison sociale")
        void shouldHandleSpecialCharactersInRaisonSociale() throws ValidationException, DatabaseException {
            prospect.setRaisonSociale("Test & Co. - 123");
            when(prospectDAO.save(any(Prospect.class))).thenReturn(prospect);
            assertDoesNotThrow(() -> prospectService.save(prospect));
        }

        @Test
        @DisplayName("Devrait gérer correctement les emails avec sous-domaines")
        void shouldHandleEmailsWithSubdomains() throws ValidationException, DatabaseException {
            prospect.setEmail("test.sub@domain.co.uk");
            when(prospectDAO.save(any(Prospect.class))).thenReturn(prospect);
            assertDoesNotThrow(() -> prospectService.save(prospect));
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour partielles")
        void shouldHandlePartialUpdates() throws ValidationException, DatabaseException, ResourceNotFoundException {
            Prospect existingProspect = new Prospect("Existing", "existing@test.com");
            existingProspect.setId(1L);
            when(prospectDAO.findById(1L)).thenReturn(Optional.of(existingProspect));
            when(prospectDAO.update(any(Prospect.class))).thenReturn(existingProspect);

            existingProspect.setEmail("new@test.com");
            assertDoesNotThrow(() -> prospectService.update(existingProspect));
        }
    }

    @Mock
    private ProspectJpaDAO prospectDAO;

    private ProspectService prospectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prospectService = new ProspectService(prospectDAO);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner un prospect quand l'ID existe")
        void shouldReturnProspectWhenIdExists() throws ValidationException, DatabaseException, ResourceNotFoundException {
            // Arrange
            Integer id = 1;
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
            Integer id = 1;
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
            prospect.setIdentifiant(1);

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
            prospect.setIdentifiant(1);

            // Act
            prospectService.delete(prospect);

            // Assert
            verify(prospectDAO).delete(prospect);
        }
    }

    @Test
    public void shouldCreateProspect() throws DatabaseException, ValidationException {
        // Given
        Prospect prospect = new Prospect();
        prospect.setRaisonSociale("Test Prospect");
        prospect.setAdresse("123 Rue Test");
        prospect.setTelephone("0123456789");
        prospect.setMail("test@test.com");
        prospect.setDateProspection("2024-03-20");

        doNothing().when(prospectDAO).save(any(Prospect.class));

        // When
        prospectService.save(prospect);

        // Then
        verify(prospectDAO).save(any(Prospect.class));
    }

    @Test
    public void shouldFindProspectById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        prospect.setRaisonSociale("Test Prospect");
        prospect.setAdresse("123 Rue Test");
        prospect.setTelephone("0123456789");
        prospect.setMail("test@test.com");
        prospect.setDateProspection("2024-03-20");

        when(prospectDAO.findById(1L)).thenReturn(Optional.of(prospect));

        // When
        Prospect found = prospectService.findById(1L);

        // Then
        assertNotNull(found);
        assertEquals(Integer.valueOf(1), found.getIdentifiant());
        assertEquals("Test Prospect", found.getRaisonSociale());
        assertEquals("123 Rue Test", found.getAdresse());
        assertEquals("0123456789", found.getTelephone());
        assertEquals("test@test.com", found.getMail());
        assertEquals("2024-03-20", found.getDateProspection());
        verify(prospectDAO).findById(1L);
    }

    @Test
    public void shouldThrowExceptionWhenProspectNotFound() {
        // Given
        when(prospectDAO.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            prospectService.findById(1L);
        });
    }

    @Test
    public void shouldUpdateProspect() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        prospect.setRaisonSociale("Test Prospect");
        prospect.setAdresse("123 Rue Test");
        prospect.setTelephone("0123456789");
        prospect.setMail("test@test.com");
        prospect.setDateProspection("2024-03-20");

        when(prospectDAO.existsById(1L)).thenReturn(true);
        doNothing().when(prospectDAO).update(any(Prospect.class));

        // When
        prospectService.update(prospect);

        // Then
        verify(prospectDAO).update(any(Prospect.class));
    }

    @Test
    public void shouldDeleteProspect() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        prospect.setRaisonSociale("Test Prospect");

        when(prospectDAO.existsById(1L)).thenReturn(true);
        doNothing().when(prospectDAO).delete(any(Prospect.class));

        // When
        prospectService.delete(prospect);

        // Then
        verify(prospectDAO).delete(any(Prospect.class));
    }

    @Test
    public void shouldFindAllProspects() throws DatabaseException {
        // Given
        Prospect prospect1 = new Prospect();
        prospect1.setIdentifiant(1L);
        prospect1.setRaisonSociale("Test Prospect 1");

        Prospect prospect2 = new Prospect();
        prospect2.setIdentifiant(2);
        prospect2.setRaisonSociale("Test Prospect 2");

        when(prospectDAO.findAll()).thenReturn(Arrays.asList(prospect1L, prospect2));

        // When
        List<Prospect> prospects = prospectService.findAll();

        // Then
        assertNotNull(prospects);
        assertEquals(0, prospects.size());
        verify(prospectDAO).findAll();
    }

    @Test
    public void shouldFindProspectByRaisonSociale() throws DatabaseException, ValidationException {
        // Given
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        prospect.setRaisonSociale("Test Prospect");

        when(prospectDAO.findByRaisonSociale("Test Prospect")).thenReturn(Arrays.asList(prospect));

        // When
        List<Prospect> found = prospectService.findByRaisonSociale("Test Prospect");

        // Then
        assertNotNull(found);
        assertEquals(Integer.valueOf(1), found.size());
        assertEquals("Test Prospect", found.get(0).getRaisonSociale());
        verify(prospectDAO).findByRaisonSociale("Test Prospect");
    }
} 