package com.cliproco.dao.impl;

import com.cliproco.model.Utilisateur;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UtilisateurDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Utilisateur> query;

    private UtilisateurDaoImpl utilisateurDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.createQuery(anyString(), any())).thenReturn(query);
        utilisateurDao = new UtilisateurDaoImpl(sessionFactory);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        // Arrange
        String email = "test@example.com";
        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setEmail(email);
        when(query.setParameter("email", email)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedUser);

        // Act
        Utilisateur result = utilisateurDao.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(session).createQuery("FROM Utilisateur WHERE email = :email", Utilisateur.class);
        verify(query).setParameter("email", email);
        verify(query).uniqueResult();
    }

    @Test
    void findByRole_ShouldReturnUsers() {
        // Arrange
        Utilisateur.Role role = Utilisateur.Role.ADMIN;
        List<Utilisateur> expectedUsers = Arrays.asList(new Utilisateur(), new Utilisateur());
        when(query.setParameter("role", role)).thenReturn(query);
        when(query.list()).thenReturn(expectedUsers);

        // Act
        List<Utilisateur> result = utilisateurDao.findByRole(role);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("FROM Utilisateur WHERE role = :role", Utilisateur.class);
        verify(query).setParameter("role", role);
        verify(query).list();
    }

    @Test
    void updateDernierLogin_ShouldUpdateTimestamp() {
        // Arrange
        Long id = 1L;
        when(session.beginTransaction()).thenReturn(null);
        when(session.createQuery(anyString())).thenReturn(mock(Query.class));

        // Act
        utilisateurDao.updateDernierLogin(id);

        // Assert
        verify(session).beginTransaction();
        verify(session).createQuery("UPDATE Utilisateur SET dernierLogin = CURRENT_TIMESTAMP WHERE id = :id");
        verify(session).getTransaction().commit();
    }

    @Test
    void save_ShouldSaveUser() {
        // Arrange
        Utilisateur user = new Utilisateur();
        when(session.beginTransaction()).thenReturn(null);

        // Act
        utilisateurDao.save(user);

        // Assert
        verify(session).beginTransaction();
        verify(session).save(user);
        verify(session).getTransaction().commit();
    }

    @Test
    void update_ShouldUpdateUser() {
        // Arrange
        Utilisateur user = new Utilisateur();
        when(session.beginTransaction()).thenReturn(null);

        // Act
        utilisateurDao.update(user);

        // Assert
        verify(session).beginTransaction();
        verify(session).update(user);
        verify(session).getTransaction().commit();
    }

    @Test
    void delete_ShouldDeleteUser() {
        // Arrange
        Utilisateur user = new Utilisateur();
        when(session.beginTransaction()).thenReturn(null);

        // Act
        utilisateurDao.delete(user);

        // Assert
        verify(session).beginTransaction();
        verify(session).delete(user);
        verify(session).getTransaction().commit();
    }

    @Test
    void findById_ShouldReturnUser() {
        // Arrange
        Long id = 1L;
        Utilisateur expectedUser = new Utilisateur();
        when(session.get(Utilisateur.class, id)).thenReturn(expectedUser);

        // Act
        Utilisateur result = utilisateurDao.findById(id);

        // Assert
        assertNotNull(result);
        verify(session).get(Utilisateur.class, id);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        List<Utilisateur> expectedUsers = Arrays.asList(new Utilisateur(), new Utilisateur());
        when(query.list()).thenReturn(expectedUsers);

        // Act
        List<Utilisateur> result = utilisateurDao.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("FROM Utilisateur", Utilisateur.class);
        verify(query).list();
    }
} 