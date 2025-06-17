/**
 * Package contenant les interfaces et implémentations des Data Access Objects (DAO).
 * 
 * Ce package définit :
 * - IDAO : interface générique pour les opérations CRUD
 * - Implémentations spécifiques pour différents types de persistance :
 *   - jpa : implémentations utilisant JPA/Hibernate
 *   - mysql : implémentations utilisant MySQL (à déplacer dans un sous-package dédié)
 * 
 * Les DAOs sont responsables de l'accès aux données et de la persistance des entités.
 */
package dao; 