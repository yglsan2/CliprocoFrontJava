# Tests unitaires simplifiés pour les classes métier (`models`)

## Pourquoi ces tests ?

Dans ce projet, nous avons ajouté des tests unitaires très simples pour les classes du dossier `models` (entités métier) afin de garantir que les constructeurs, getters, setters et méthodes utilitaires de base (comme `toString`) fonctionnent correctement.

## Pourquoi ce n'est généralement pas nécessaire ?

Dans la plupart des projets Java, les classes métier (aussi appelées POJOs) ne contiennent que des attributs, des constructeurs, des getters/setters et parfois une méthode `toString`. Ces méthodes sont très simples et sont déjà testées indirectement par le compilateur et par les tests des couches supérieures (services, contrôleurs, DAO, etc.).

Écrire des tests unitaires pour ces méthodes n'apporte donc généralement pas de valeur ajoutée, sauf si la classe contient de la logique métier non triviale (calculs, règles de gestion, etc.).

## Pourquoi les ajouter ici ?

- Pour illustrer la couverture complète du code.
- Pour rassurer sur la non-régression lors de modifications futures.
- Pour répondre à des exigences pédagogiques ou de conformité.

**En résumé :**
Ces tests sont présents à titre d'exemple et de démonstration. Dans un contexte professionnel, il est préférable de concentrer les tests unitaires sur la logique métier, les services, les contrôleurs et les DAO. 