package dao;

import exceptions.DatabaseException;
import models.User;
import java.util.List;

/**
 * Interface pour les opérations d'accès aux données des utilisateurs.
 * 
 * <p>Cette interface définit les opérations CRUD spécifiques aux utilisateurs,
 * incluant des méthodes de recherche par email, nom d'utilisateur et token
 * d'authentification. Elle étend les fonctionnalités de base de l'interface
 * générique IDAO avec des méthodes spécifiques aux besoins d'authentification.</p>
 * 
 * <p>Les implémentations de cette interface gèrent la persistance des utilisateurs
 * en base de données et fournissent les méthodes nécessaires pour l'authentification
 * et la gestion des sessions utilisateur.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
public interface UserDAO {
    /**
     * Recherche un utilisateur par son adresse email.
     * 
     * <p>Cette méthode permet de retrouver un utilisateur en utilisant son
     * adresse email unique. Utile pour l'authentification et la récupération
     * de compte.</p>
     *
     * @param email L'adresse email de l'utilisateur à rechercher
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé avec cet email
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    User findByEmail(String email) throws DatabaseException;

    /**
     * Recherche un utilisateur par son identifiant unique.
     * 
     * <p>Cette méthode permet de retrouver un utilisateur spécifique en utilisant
     * son identifiant unique généré par la base de données.</p>
     *
     * @param id L'identifiant unique de l'utilisateur à rechercher
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé avec cet ID
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    User findById(Integer id) throws DatabaseException;

    /**
     * Récupère tous les utilisateurs de la base de données.
     * 
     * <p>Cette méthode retourne la liste complète de tous les utilisateurs
     * enregistrés dans le système. Attention : cette opération peut être
     * coûteuse si le nombre d'utilisateurs est important.</p>
     *
     * @return Une liste de tous les utilisateurs enregistrés
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    List<User> findAll() throws DatabaseException;

    /**
     * Sauvegarde un nouvel utilisateur dans la base de données.
     * 
     * <p>Cette méthode crée un nouvel utilisateur en base de données.
     * L'identifiant sera généré automatiquement par la base de données.
     * Vérifie que l'email et le nom d'utilisateur sont uniques.</p>
     *
     * @param user L'utilisateur à sauvegarder (doit être un nouvel utilisateur)
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    void save(User user) throws DatabaseException;

    /**
     * Met à jour un utilisateur existant dans la base de données.
     * 
     * <p>Cette méthode modifie les informations d'un utilisateur déjà existant.
     * L'utilisateur doit avoir un identifiant valide pour être mis à jour.</p>
     *
     * @param user L'utilisateur à mettre à jour (doit avoir un ID existant)
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    void update(User user) throws DatabaseException;

    /**
     * Supprime un utilisateur de la base de données.
     * 
     * <p>Cette méthode supprime définitivement un utilisateur de la base de données.
     * Attention : cette opération est irréversible.</p>
     *
     * @param user L'utilisateur à supprimer
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    void delete(User user) throws DatabaseException;

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     * 
     * <p>Cette méthode permet de retrouver un utilisateur en utilisant son
     * nom d'utilisateur unique. Utile pour l'authentification et la vérification
     * de l'unicité des noms d'utilisateur.</p>
     *
     * @param username Le nom d'utilisateur à rechercher
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé avec ce nom
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    User findByUsername(String username) throws DatabaseException;

    /**
     * Recherche un utilisateur par son token d'authentification.
     * 
     * <p>Cette méthode permet de retrouver un utilisateur en utilisant son
     * token d'authentification actif. Utile pour maintenir les sessions
     * utilisateur et vérifier la validité des tokens.</p>
     *
     * @param token Le token d'authentification à rechercher
     * @return L'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé avec ce token
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
    User findByToken(String token) throws DatabaseException;
} 