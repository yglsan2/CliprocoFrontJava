package services;

import dao.jpa.ProspectJpaDAO;
import models.Adresse;
import models.Prospect;
import services.ProspectService;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires optimisés pour ProspectService
 */
@DisplayName("Tests unitaires optimisés pour ProspectService")
class ProspectServiceTest {

    @Mock
    private ProspectJpaDAO mockProspectDAO;
    
    private ProspectService prospectService;
    private Prospect testProspect;
    private Adresse testAdresse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prospectService = new ProspectService(mockProspectDAO);
        
        // Création des objets de test
        testAdresse = new Adresse();
        testAdresse.setIdentifiant(1);
        testAdresse.setNumeroRue("123");
        testAdresse.setNomRue("Rue de Test");
        testAdresse.setCodePostal("75001");
        testAdresse.setVille("Paris");
        testAdresse.setPays("France");
        
        testProspect = new Prospect();
        testProspect.setIdentifiant(1);
        testProspect.setRaisonSociale("Test Prospect");
        testProspect.setMail("test@prospect.com");
        testProspect.setTelephone("0123456789");
        testProspect.setAdresse(testAdresse);
        testProspect.setProspectInteresse(true);
        testProspect.setCommentaires("Prospect de test");
    }

    @Nested
    @DisplayName("Tests de base")
    class BasicTests {
        
        @Test
        @DisplayName("Doit créer un prospect avec succès")
        void testCreateProspectSuccess() throws Exception {
            // Arrange
            when(mockProspectDAO.save(any(Prospect.class))).thenReturn(null);
            
            // Act
            prospectService.create(testProspect);
            
            // Assert
            verify(mockProspectDAO).save(testProspect);
        }

        @Test
        @DisplayName("Doit récupérer un prospect par son identifiant")
        void testGetProspectById() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(1)).thenReturn(Optional.of(testProspect));
            
            // Act
            Prospect result = prospectService.findById(1);
            
            // Assert
            assertNotNull(result);
            assertEquals(testProspect.getIdentifiant(), result.getIdentifiant());
            assertEquals(testProspect.getRaisonSociale(), result.getRaisonSociale());
            verify(mockProspectDAO).findById(1);
        }

        @Test
        @DisplayName("Doit lever une exception si le prospect n'existe pas")
        void testGetProspectByIdNotFound() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(999)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> {
                prospectService.findById(999);
            });
            verify(mockProspectDAO).findById(999);
        }

        @Test
        @DisplayName("Doit récupérer tous les prospects")
        void testGetAllProspects() throws Exception {
            // Arrange
            Prospect prospect1 = new Prospect();
            prospect1.setIdentifiant(1);
            prospect1.setRaisonSociale("Prospect 1");

            Prospect prospect2 = new Prospect();
            prospect2.setIdentifiant(2);
            prospect2.setRaisonSociale("Prospect 2");

            List<Prospect> expectedProspects = Arrays.asList(prospect1, prospect2);
            when(mockProspectDAO.findAll()).thenReturn(expectedProspects);
            
            // Act
            List<Prospect> result = prospectService.findAll();
            
            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(prospect1.getIdentifiant(), result.get(0).getIdentifiant());
            assertEquals(prospect2.getIdentifiant(), result.get(1).getIdentifiant());
            verify(mockProspectDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de validation métier")
    class BusinessValidationTests {
        
        @Test
        @DisplayName("Doit valider l'intérêt du prospect")
        void testValidateProspectInterest() throws Exception {
            // Arrange
            Prospect prospectWithoutInterest = new Prospect();
            prospectWithoutInterest.setProspectInteresse(null);
            prospectWithoutInterest.setRaisonSociale("Prospect Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                prospectService.createWithValidation(prospectWithoutInterest);
            });
        }
        
        @Test
        @DisplayName("Doit valider l'email au format correct")
        void testValidateEmailFormat() throws Exception {
            // Arrange
            Prospect prospectWithInvalidEmail = new Prospect();
            prospectWithInvalidEmail.setMail("invalid-email");
            prospectWithInvalidEmail.setRaisonSociale("Prospect Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                prospectService.createWithValidation(prospectWithInvalidEmail);
            });
        }
        
        @Test
        @DisplayName("Doit valider le téléphone au format français")
        void testValidatePhoneFormat() throws Exception {
            // Arrange
            Prospect prospectWithInvalidPhone = new Prospect();
            prospectWithInvalidPhone.setTelephone("123");
            prospectWithInvalidPhone.setRaisonSociale("Prospect Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                prospectService.createWithValidation(prospectWithInvalidPhone);
            });
        }
    }

    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
        
        @Test
        @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Doit récupérer 100 prospects rapidement")
        void testGetLargeProspectList() throws Exception {
            // Arrange
            List<Prospect> largeProspectList = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Prospect prospect = new Prospect();
                    prospect.setIdentifiant(i);
                    prospect.setRaisonSociale("Prospect " + i);
                    prospect.setProspectInteresse(i % 2 == 0);
                    return prospect;
                })
                .collect(Collectors.toList());
            
            when(mockProspectDAO.findAll()).thenReturn(largeProspectList);
            
            // Act
            long startTime = System.currentTimeMillis();
            List<Prospect> result = prospectService.findAll();
            long endTime = System.currentTimeMillis();
            
            // Assert
            assertEquals(100, result.size());
            assertTrue(endTime - startTime < 50, "L'opération doit être rapide (< 50ms)");
        }
        
        @RepeatedTest(5)
        @DisplayName("Doit maintenir des performances constantes sur plusieurs exécutions")
        void testConsistentPerformance() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(1)).thenReturn(Optional.of(testProspect));
            
            // Act
            long startTime = System.nanoTime();
            prospectService.findById(1);
            long endTime = System.nanoTime();
            
            // Assert
            long duration = endTime - startTime;
            assertTrue(duration < 50_000_000, "L'opération doit être rapide (< 50ms)");
        }
    }

    @Nested
    @DisplayName("Tests de cas limites")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Doit gérer un prospect avec des valeurs maximales")
        void testProspectWithMaxValues() throws Exception {
            // Arrange
            Prospect maxProspect = new Prospect();
            maxProspect.setIdentifiant(Integer.MAX_VALUE);
            maxProspect.setRaisonSociale("A".repeat(100)); // Nom très long
            maxProspect.setCommentaires("A".repeat(1000)); // Commentaire très long
            maxProspect.setProspectInteresse(true);
            
            when(mockProspectDAO.save(any(Prospect.class))).thenReturn(null);
            
            // Act
            prospectService.create(maxProspect);
            
            // Assert
            verify(mockProspectDAO).save(any(Prospect.class));
        }
        
        @Test
        @DisplayName("Doit gérer un prospect avec des valeurs minimales")
        void testProspectWithMinValues() throws Exception {
            // Arrange
            Prospect minProspect = new Prospect();
            minProspect.setIdentifiant(1);
            minProspect.setRaisonSociale("A"); // Nom très court
            minProspect.setCommentaires(""); // Commentaire vide
            minProspect.setProspectInteresse(false);
            
            when(mockProspectDAO.save(any(Prospect.class))).thenReturn(null);
            
            // Act
            prospectService.create(minProspect);
            
            // Assert
            verify(mockProspectDAO).save(any(Prospect.class));
        }
        
        @Test
        @DisplayName("Doit gérer une liste vide de prospects")
        void testEmptyProspectList() throws Exception {
            // Arrange
            when(mockProspectDAO.findAll()).thenReturn(Arrays.asList());
            
            // Act
            List<Prospect> result = prospectService.findAll();
            
            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de résilience")
    class ResilienceTests {
        
        @Test
        @DisplayName("Doit gérer les exceptions lors de la création")
        void testCreateProspectException() throws Exception {
            // Arrange
            doThrow(new DatabaseException("Erreur de base de données"))
                .when(mockProspectDAO).save(any(Prospect.class));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                prospectService.create(testProspect);
            });
        }
        
        @Test
        @DisplayName("Doit gérer les exceptions lors de la lecture")
        void testGetProspectByIdException() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(1))
                .thenThrow(new DatabaseException("Erreur de base de données"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                prospectService.findById(1);
            });
        }
        
        @Test
        @DisplayName("Doit gérer les exceptions lors de la mise à jour")
        void testUpdateProspectException() throws Exception {
            // Arrange
            when(mockProspectDAO.existsById(1)).thenReturn(true);
            doThrow(new DatabaseException("Erreur de base de données"))
                .when(mockProspectDAO).update(any(Prospect.class));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                prospectService.update(testProspect);
            });
        }
        
        @Test
        @DisplayName("Doit gérer les exceptions lors de la suppression")
        void testDeleteProspectException() throws Exception {
            // Arrange
            when(mockProspectDAO.existsById(1)).thenReturn(true);
            doThrow(new DatabaseException("Erreur de base de données"))
                .when(mockProspectDAO).delete(any(Prospect.class));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                prospectService.delete(testProspect);
            });
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        
        @Test
        @DisplayName("Doit gérer les accès concurrents en lecture")
        void testConcurrentReads() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(1)).thenReturn(Optional.of(testProspect));
            
            // Act
            List<CompletableFuture<Prospect>> futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return prospectService.findById(1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .collect(Collectors.toList());
            
            // Assert
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            futures.forEach(future -> {
                try {
                    Prospect result = future.get();
                    assertNotNull(result);
                    assertEquals(testProspect.getIdentifiant(), result.getIdentifiant());
                } catch (Exception e) {
                    fail("Erreur lors de l'accès concurrent: " + e.getMessage());
                }
            });
        }
    }

    @Nested
    @DisplayName("Tests de métriques")
    class MetricsTests {
        
        @Test
        @DisplayName("Doit mesurer le temps de réponse pour findById")
        void testFindByIdResponseTime() throws Exception {
            // Arrange
            when(mockProspectDAO.findById(1)).thenReturn(Optional.of(testProspect));
            
            // Act
            long startTime = System.nanoTime();
            prospectService.findById(1);
            long endTime = System.nanoTime();
            
            // Assert
            long responseTime = endTime - startTime;
            assertTrue(responseTime < 10_000_000, 
                "Le temps de réponse doit être inférieur à 10ms, actuel: " + responseTime + "ns");
        }
        
        @Test
        @DisplayName("Doit mesurer le temps de réponse pour findAll")
        void testFindAllResponseTime() throws Exception {
            // Arrange
            List<Prospect> prospects = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Prospect prospect = new Prospect();
                    prospect.setIdentifiant(i);
                    prospect.setRaisonSociale("Prospect " + i);
                    prospect.setProspectInteresse(i % 2 == 0);
                    return prospect;
                })
                .collect(Collectors.toList());
            when(mockProspectDAO.findAll()).thenReturn(prospects);
            
            // Act
            long startTime = System.nanoTime();
            prospectService.findAll();
            long endTime = System.nanoTime();
            
            // Assert
            long responseTime = endTime - startTime;
            assertTrue(responseTime < 50_000_000, 
                "Le temps de réponse doit être inférieur à 50ms, actuel: " + responseTime + "ns");
        }
    }

    @Test
    @DisplayName("Doit mettre à jour un prospect existant")
    void testUpdateProspect() throws Exception {
        // Arrange
        when(mockProspectDAO.existsById(1)).thenReturn(true);
        when(mockProspectDAO.update(any(Prospect.class))).thenReturn(null);
        
        // Act
        prospectService.update(testProspect);
        
        // Assert
        verify(mockProspectDAO).existsById(1);
        verify(mockProspectDAO).update(testProspect);
    }

    @Test
    @DisplayName("Doit lever une exception si le prospect à mettre à jour n'existe pas")
    void testUpdateProspectNotFound() throws Exception {
        // Arrange
        when(mockProspectDAO.existsById(1)).thenReturn(false);
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            prospectService.update(testProspect);
        });
        verify(mockProspectDAO).existsById(1);
    }

    @Test
    @DisplayName("Doit supprimer un prospect existant")
    void testDeleteProspect() throws Exception {
        // Arrange
        when(mockProspectDAO.existsById(1)).thenReturn(true);
        doNothing().when(mockProspectDAO).delete(any(Prospect.class));
        
        // Act
        prospectService.delete(testProspect);
        
        // Assert
        verify(mockProspectDAO).existsById(1);
        verify(mockProspectDAO).delete(testProspect);
    }

    @Test
    @DisplayName("Doit lever une exception si le prospect à supprimer n'existe pas")
    void testDeleteProspectNotFound() throws Exception {
        // Arrange
        when(mockProspectDAO.existsById(1)).thenReturn(false);
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            prospectService.delete(testProspect);
        });
        verify(mockProspectDAO).existsById(1);
    }

    @Test
    @DisplayName("Doit rechercher des prospects par raison sociale")
    void testSearchProspectsByRaisonSociale() throws Exception {
        // Arrange
        Prospect prospect1 = new Prospect();
        prospect1.setIdentifiant(1);
        prospect1.setRaisonSociale("Entreprise ABC");

        Prospect prospect2 = new Prospect();
        prospect2.setIdentifiant(2);
        prospect2.setRaisonSociale("Entreprise XYZ");

        List<Prospect> allProspects = Arrays.asList(prospect1, prospect2);
        when(mockProspectDAO.findAll()).thenReturn(allProspects);
        
        // Act
        List<Prospect> result = prospectService.searchByRaisonSociale("Entreprise");
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getRaisonSociale().contains("Entreprise")));
    }

    @Test
    @DisplayName("Doit retourner une liste vide si aucun prospect ne correspond")
    void testSearchProspectsByRaisonSocialeNoMatch() throws Exception {
        // Arrange
        Prospect prospect1 = new Prospect();
        prospect1.setIdentifiant(1);
        prospect1.setRaisonSociale("Entreprise ABC");

        List<Prospect> allProspects = Arrays.asList(prospect1);
        when(mockProspectDAO.findAll()).thenReturn(allProspects);
        
        // Act
        List<Prospect> result = prospectService.searchByRaisonSociale("Inexistant");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Doit sauvegarder un prospect")
    void testSaveProspect() throws Exception {
        // Arrange
        when(mockProspectDAO.save(any(Prospect.class))).thenReturn(null);
        
        // Act
        prospectService.save(testProspect);
        
        // Assert
        verify(mockProspectDAO).save(testProspect);
    }
} 