/**
 * Package contenant les classes utilitaires de l'application.
 *
 * <p>
 * Ce package regroupe des classes d'aide et des outils réutilisables pour la gestion
 * de la sécurité, des connexions à la base de données, des formats, des motifs de validation,
 * et d'autres fonctionnalités transversales.
 * </p>
 *
 * <h3>Principales classes utilitaires</h3>
 * <ul>
 *   <li><b>{@link utilities.DatabaseConnection}</b> : Gestion des connexions JDBC à la base de données</li>
 *   <li><b>{@link utilities.JPAUtil}</b> : Gestion centralisée de l'EntityManagerFactory JPA</li>
 *   <li><b>{@link utilities.Security}</b> : Méthodes de hachage, vérification de mot de passe, sécurité</li>
 *   <li><b>{@link utilities.Patterns}</b> : Motifs regex pour la validation des emails, téléphones, etc.</li>
 *   <li><b>{@link utilities.Formatters}</b> : Méthodes de formatage pour affichage ou export</li>
 *   <li><b>{@link utilities.SocieteUtilitiesException}</b> : Exception spécifique aux utilitaires</li>
 * </ul>
 *
 * <h3>Bonnes pratiques</h3>
 * <ul>
 *   <li>Centraliser les méthodes utilitaires pour éviter la duplication de code</li>
 *   <li>Utiliser des exceptions spécifiques pour faciliter le débogage</li>
 *   <li>Documenter chaque méthode utilitaire pour clarifier son usage</li>
 *   <li>Respecter la sécurité et la robustesse, notamment pour les accès base de données et la gestion des mots de passe</li>
 * </ul>
 *
 * @see utilities.DatabaseConnection
 * @see utilities.JPAUtil
 * @see utilities.Security
 * @see utilities.Patterns
 * @see utilities.Formatters
 * @see utilities.SocieteUtilitiesException
 * @since 1.0
 * @version 1.0
 */
package utilities;
