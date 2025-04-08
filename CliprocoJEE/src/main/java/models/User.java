package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Represents a user entity in the system.
 */
@Entity
@Table(name = "users")
public final class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username (non-nullable).
     */
    @NotNull
    @Column(unique = true)
    private String username;

    /**
     * User's password (non-nullable).
     */
    @NotNull
    private String password;

    /**
     * Authentication token.
     */
    private String token;

    /**
     * Expiration time.
     */
    private LocalDate expire;

    /**
     * User's role.
     */
    @Column(nullable = false)
    private String role;

    /**
     * Constructs a User with specified details.
     *
     * @param idParam       Unique identifier
     * @param usernameParam Unique username
     * @param passwordParam User's password
     * @param tokenParam    Authentication token
     * @param expireParam   Expiration timer
     * @param roleParam     User's role
     */
    public User(
            final Long idParam,
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
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructs a User with basic information.
     *
     * @param usernameParam Unique username
     * @param passwordParam User's password
     * @param emailParam User's email
     */
    public User(final String usernameParam,
               final String passwordParam,
               final String emailParam) {
        this.username = usernameParam;
        this.password = passwordParam;
        this.role = "USER";
    }

    /**
     * Returns the authentication timer.
     *
     * @return LocalDate authentication timer
     */
    public LocalDate getExpire() {
        return expire;
    }

    /**
     * Sets the authentication timer.
     *
     * @param expireParam New authentication timer
     */
    public void setExpire(final LocalDate expireParam) {
        this.expire = expireParam;
    }

    /**
     * Returns the authentication token.
     *
     * @return String authentication token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param tokenParam New authentication token
     */
    public void setToken(final String tokenParam) {
        this.token = tokenParam;
    }

    /**
     * Returns the user's password.
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param passwordParam New password
     */
    public void setPassword(final String passwordParam) {
        this.password = passwordParam;
    }

    /**
     * Returns the username.
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param usernameParam New username
     */
    public void setUsername(final String usernameParam) {
        this.username = usernameParam;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return Long identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param idParam New identifier
     */
    public void setId(final Long idParam) {
        this.id = idParam;
    }

    /**
     * Returns the user's role.
     *
     * @return String role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param roleParam New role
     */
    public void setRole(final String roleParam) {
        this.role = roleParam;
    }
}
