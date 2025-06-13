package dao;

import models.User;

/**
 * Interface for User data access operations.
 */
public interface UserDAO {
    /**
     * Save a new user.
     *
     * @param user User to save
     * @throws SocieteDatabaseException if database error occurs
     */
    void save(User user) throws SocieteDatabaseException;

    /**
     * Update an existing user.
     *
     * @param user User to update
     * @throws SocieteDatabaseException if database error occurs
     */
    void update(User user) throws SocieteDatabaseException;

    /**
     * Delete a user.
     *
     * @param user User to delete
     * @throws SocieteDatabaseException if database error occurs
     */
    void delete(User user) throws SocieteDatabaseException;

    /**
     * Find a user by username.
     *
     * @param username Username to search for
     * @return Found user or null
     * @throws SocieteDatabaseException if database error occurs
     */
    User findByUsername(String username) throws SocieteDatabaseException;

    /**
     * Find a user by token.
     *
     * @param token Token to search for
     * @return Found user or null
     * @throws SocieteDatabaseException if database error occurs
     */
    User findByToken(String token) throws SocieteDatabaseException;
} 