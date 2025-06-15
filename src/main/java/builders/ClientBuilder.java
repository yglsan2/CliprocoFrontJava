package builders;

import models.Client;
import models.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Classe constructrice Client.
 */
public class ClientBuilder extends AbstractSocieteBuilder<Client> {

    public ClientBuilder() {
        super(new Client());
    }

    /**
     * Constructor.
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    @Contract(" -> new")
    public static @NotNull ClientBuilder createInstance() throws SocieteEntityException {
        return new ClientBuilder();
    }

    /**
     * Setter chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the chiffre d'affaires setter.
     */
    public ClientBuilder deChiffreAffaires(Double chiffreAffaires) throws SocieteEntityException {
        if (chiffreAffaires == null || chiffreAffaires < 0) {
            throw new SocieteEntityException("Le chiffre d'affaires ne peut pas être négatif");
        }
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
    public ClientBuilder deNombreEmployes(Integer nombreEmployes) throws SocieteEntityException {
        if (nombreEmployes == null || nombreEmployes < 0) {
            throw new SocieteEntityException("Le nombre d'employés ne peut pas être négatif");
        }
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
        return super.build();
    }

    public ClientBuilder deCommentaire(String commentaire) {
        setField("commentaire", commentaire);
        return this;
    }

    public static ClientBuilder getNewClientBuilder() {
        return new ClientBuilder();
    }
}
