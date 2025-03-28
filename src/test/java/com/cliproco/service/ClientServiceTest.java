package com.cliproco.service;

import com.cliproco.dao.ClientDao;
import com.cliproco.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testClient = new Client();
        testClient.setId(1L);
        testClient.setNom("Test Client");
        testClient.setEmail("test@example.com");
        testClient.setTelephone("0123456789");
        testClient.setSecteurActivite("IT");
        testClient.setChiffreAffaires(100000.0);
        testClient.setDateCreation(LocalDateTime.now());
    }

    @Test
    void createClient_Success() {
        when(clientDao.create(any(Client.class))).thenReturn(testClient);

        Client createdClient = clientService.createClient(testClient);

        assertNotNull(createdClient);
        assertEquals(testClient.getNom(), createdClient.getNom());
        assertEquals(testClient.getEmail(), createdClient.getEmail());
        verify(clientDao).create(any(Client.class));
    }

    @Test
    void createClient_InvalidData_ThrowsException() {
        Client invalidClient = new Client();
        invalidClient.setNom(""); // Nom vide

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(invalidClient);
        });

        verify(clientDao, never()).create(any(Client.class));
    }

    @Test
    void updateClient_Success() {
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientDao.update(any(Client.class))).thenReturn(testClient);

        Client updatedClient = clientService.updateClient(1L, testClient);

        assertNotNull(updatedClient);
        assertEquals(testClient.getNom(), updatedClient.getNom());
        verify(clientDao).update(any(Client.class));
    }

    @Test
    void updateClient_NotFound_ThrowsException() {
        when(clientDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(1L, testClient);
        });

        verify(clientDao, never()).update(any(Client.class));
    }

    @Test
    void deleteClient_Success() {
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));

        clientService.deleteClient(1L);

        verify(clientDao).delete(testClient);
    }

    @Test
    void deleteClient_NotFound_ThrowsException() {
        when(clientDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.deleteClient(1L);
        });

        verify(clientDao, never()).delete(any(Client.class));
    }

    @Test
    void getClientById_Success() {
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));

        Client foundClient = clientService.getClientById(1L);

        assertNotNull(foundClient);
        assertEquals(testClient.getId(), foundClient.getId());
        assertEquals(testClient.getNom(), foundClient.getNom());
    }

    @Test
    void getClientById_NotFound_ThrowsException() {
        when(clientDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.getClientById(1L);
        });
    }

    @Test
    void getAllClients_Success() {
        List<Client> clients = Arrays.asList(testClient);
        when(clientDao.findAll()).thenReturn(clients);

        List<Client> foundClients = clientService.getAllClients();

        assertNotNull(foundClients);
        assertEquals(1, foundClients.size());
        assertEquals(testClient.getId(), foundClients.get(0).getId());
    }

    @Test
    void findBySecteurActivite_Success() {
        List<Client> clients = Arrays.asList(testClient);
        when(clientDao.findBySecteurActivite("IT")).thenReturn(clients);

        List<Client> foundClients = clientService.findBySecteurActivite("IT");

        assertNotNull(foundClients);
        assertEquals(1, foundClients.size());
        assertEquals("IT", foundClients.get(0).getSecteurActivite());
    }

    @Test
    void findByChiffreAffairesMinimal_Success() {
        List<Client> clients = Arrays.asList(testClient);
        when(clientDao.findByChiffreAffairesMinimal(50000.0)).thenReturn(clients);

        List<Client> foundClients = clientService.findByChiffreAffairesMinimal(50000.0);

        assertNotNull(foundClients);
        assertEquals(1, foundClients.size());
        assertTrue(foundClients.get(0).getChiffreAffaires() >= 50000.0);
    }
} 