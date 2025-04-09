package builders;

import models.Adresse;
import models.Client;
import models.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Classe constructrice Client.
 */
public class ClientBuilder {
    private final Client client;

    /**
     * Constructor.
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    public ClientBuilder() throws SocieteEntityException {
        this.client = new Client();
    }

    /**
     * New builder from static call.
     *
     * @return new ClientBuilder
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    @Contract(" -> new")
    public static @NotNull ClientBuilder getNewClientBuilder() throws SocieteEntityException {
        return new ClientBuilder();
    }

    /**
     * Setter raison sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     */
    public ClientBuilder deRaisonSociale(final String raisonSociale) {
        client.setRaisonSociale(raisonSociale);
        return this;
    }

    /**
     * Setter téléphone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     */
    public ClientBuilder deTelephone(final String telephone) {
        client.setTelephone(telephone);
        return this;
    }

    /**
     * Setter email.
     *
     * @param email Nouvelle adresse email.
     * @return This builder.
     */
    public ClientBuilder dEmail(final String email) {
        client.setMail(email);
        return this;
    }

    /**
     * Setter commentaire.
     *
     * @param commentaire Nouveau commentaire.
     * @return This builder.
     */
    public ClientBuilder deCommentaire(final String commentaire) {
        client.setCommentaires(commentaire);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param id Nouvel identifiant.
     * @return This builder.
     */
    public ClientBuilder dIdentifiant(final Long id) {
        client.setIdentifiant(id);
        return this;
    }

    /**
     * Setter adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     */
    public ClientBuilder dAdresse(final Adresse adresse) {
        client.setAdresse(adresse);
        return this;
    }

    /**
     * Setter chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     */
    public ClientBuilder avecChiffreAffaires(final Double chiffreAffaires) {
        client.setChiffreAffaires(chiffreAffaires);
        return this;
    }

    /**
     * Setter nombre d'employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     */
    public ClientBuilder avecNombreEmployes(final Integer nombreEmployes) {
        client.setNombreEmployes(nombreEmployes);
        return this;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    public Client build() {
        return client;
    }
}
