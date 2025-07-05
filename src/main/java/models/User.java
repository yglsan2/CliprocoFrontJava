package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Représente une entité utilisateur dans le système.
 * 
 * <p>Cette classe gère les informations d'authentification et d'autorisation
 * des utilisateurs de l'application. Elle inclut les informations de base
 * (nom d'utilisateur, mot de passe, email) ainsi que des informations
 * de sécurité (token d'authentification, expiration, rôle).</p>
 * 
 * <p>Cette entité est mappée sur la table "users" en base de données.
 * La classe est marquée comme final pour empêcher l'héritage.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "users")
public final class User {

    /**
     * Identifiant unique de l'utilisateur.
     * Généré automatiquement par la base de données avec une stratégie d'auto-incrémentation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifiant")
    private Integer id;

    /**
     * Nom d'utilisateur unique.
     * Champ obligatoire et unique en base de données.
     * Utilisé pour l'authentification.
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Mot de passe de l'utilisateur.
     * Champ obligatoire en base de données.
     * Doit être hashé avant stockage en production.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Token d'authentification de l'utilisateur.
     * Utilisé pour maintenir la session utilisateur.
     * Peut être null si l'utilisateur n'est pas connecté.
     */
    private String token;

    /**
     * Date d'expiration du token d'authentification.
     * Utilisée pour invalider automatiquement les sessions expirées.
     * Peut être null si aucun token n'est défini.
     */
    private LocalDate expire;

    /**
     * Rôle de l'utilisateur dans l'application.
     * Champ obligatoire en base de données.
     * Détermine les permissions et accès de l'utilisateur.
     * Exemples : "USER", "ADMIN", "MANAGER"
     */
    @Column(nullable = false)
    private String role;

    /**
     * Adresse email de l'utilisateur.
     * Champ obligatoire et unique en base de données.
     * Utilisée pour la communication et la récupération de compte.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Constructeur pour créer un utilisateur avec toutes ses informations.
     * 
     * <p>Ce constructeur initialise un utilisateur complet avec toutes ses
     * informations d'authentification et de sécurité.</p>
     *
     * @param idParam       Identifiant unique de l'utilisateur
     * @param usernameParam Nom d'utilisateur unique
     * @param passwordParam Mot de passe de l'utilisateur
     * @param tokenParam    Token d'authentification
     * @param expireParam   Date d'expiration du token
     * @param roleParam     Rôle de l'utilisateur
     */
    public User(
            final Integer idParam,
            final String usernameParam,
            final String passwordParam,
            final String tokenParam,
            final LocalDate expireParam,
            final String roleParam) {
        this.id = idParam;
        this.username = usernameParam;
        this.password = passwordParam;
        this.token = tokenParam;
        this.expire = expireParam;
        this.role = roleParam;
    }

    /**
     * Constructeur par défaut requis par JPA.
     * 
     * <p>Ce constructeur est utilisé par JPA pour créer des instances
     * lors du chargement depuis la base de données.</p>
     */
    public User() {
    }

    /**
     * Constructeur pour créer un utilisateur avec les informations de base.
     * 
     * <p>Ce constructeur initialise un utilisateur avec les informations
     * essentielles. Le rôle est automatiquement défini à "USER" et
     * les informations de sécurité (token, expiration) sont laissées à null.</p>
     *
     * @param usernameParam Nom d'utilisateur unique
     * @param passwordParam Mot de passe de l'utilisateur
     * @param emailParam    Adresse email de l'utilisateur
     */
    public User(final String usernameParam,
               final String passwordParam,
               final String emailParam) {
        this.username = usernameParam;
        this.password = passwordParam;
        this.role = "USER";
        this.email = emailParam;
    }

    /**
     * Retourne la date d'expiration du token d'authentification.
     * 
     * @return La date d'expiration du token (peut être null)
     */
    public LocalDate getExpire() {
        return expire;
    }

    /**
     * Définit la date d'expiration du token d'authentification.
     *
     * @param expireParam Nouvelle date d'expiration à définir
     */
    public void setExpire(final LocalDate expireParam) {
        this.expire = expireParam;
    }

    /**
     * Retourne le token d'authentification de l'utilisateur.
     * 
     * @return Le token d'authentification (peut être null)
     */
    public String getToken() {
        return token;
    }

    /**
     * Définit le token d'authentification de l'utilisateur.
     *
     * @param tokenParam Nouveau token à définir
     */
    public void setToken(final String tokenParam) {
        this.token = tokenParam;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     * 
     * <p>Attention : En production, cette méthode ne devrait retourner
     * que le hash du mot de passe, jamais le mot de passe en clair.</p>
     * 
     * @return Le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     * 
     * <p>En production, le mot de passe devrait être hashé avant
     * d'être stocké dans cette propriété.</p>
     *
     * @param passwordParam Nouveau mot de passe à définir
     */
    public void setPassword(final String passwordParam) {
        this.password = passwordParam;
    }

    /**
     * Retourne le nom d'utilisateur.
     * 
     * @return Le nom d'utilisateur unique
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur.
     * 
     * <p>Le nom d'utilisateur doit être unique dans le système.</p>
     *
     * @param usernameParam Nouveau nom d'utilisateur à définir
     */
    public void setUsername(final String usernameParam) {
        this.username = usernameParam;
    }

    /**
     * Retourne l'identifiant unique de l'utilisateur.
     * 
     * @return L'identifiant unique de l'utilisateur
     */
    public Integer getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     * 
     * <p>Attention : Cette méthode ne devrait généralement pas être utilisée
     * directement car l'identifiant est généré automatiquement par la base de données.</p>
     *
     * @param idParam Nouvel identifiant à définir
     */
    public void setId(final Integer idParam) {
        this.id = idParam;
    }

    /**
     * Retourne le rôle de l'utilisateur.
     * 
     * <p>Le rôle détermine les permissions et accès de l'utilisateur
     * dans l'application.</p>
     * 
     * @return Le rôle de l'utilisateur
     */
    public String getRole() {
        return role;
    }

    /**
     * Définit le rôle de l'utilisateur.
     * 
     * <p>Le rôle détermine les permissions et accès de l'utilisateur.
     * Exemples de rôles : "USER", "ADMIN", "MANAGER"</p>
     *
     * @param roleParam Nouveau rôle à définir
     */
    public void setRole(final String roleParam) {
        this.role = roleParam;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     * 
     * @return L'adresse email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     * 
     * <p>L'adresse email doit être unique dans le système et valide.</p>
     *
     * @param emailParam Nouvelle adresse email à définir
     */
    public void setEmail(final String emailParam) {
        this.email = emailParam;
    }
}
