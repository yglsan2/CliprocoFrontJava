package builders;

import models.Client;
import models.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Classe constructrice Client.
 */
public class ClientBuilder extends SocieteBuilder<Client> {

    /**
     * Constructor.
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    public ClientBuilder() throws SocieteEntityException {
        super(createInstance(Client.class));
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
     * Setter chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the chiffre d'affaires setter.
     */
    public ClientBuilder avecChiffreAffaires(final Double chiffreAffaires)
            throws SocieteEntityException {
        setField("chiffreAffaires", chiffreAffaires);
        return this;
    }

    /**
     * Setter nombre d'employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the nombre d'employés setter.
     */
    public ClientBuilder avecNombreEmployes(final Integer nombreEmployes)
            throws SocieteEntityException {
        setField("nombreEmployes", nombreEmployes);
        return this;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    @Override
    public Client build() {
        return getEntity();
    }
}
