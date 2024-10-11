# OBJECTIF DU TP : COMPRENDRE L’API SWING DE JAVA (SQL avec JDBC)

En Java, Swing fournit un ensemble de bibliothèques pour créer une interface graphique (GUI), pour le faire fonctionner indépendamment sur différentes plates-formes. Swing est certainement celui qui est le plus utilisé, car ses propriétés de légèreté, contrôle et de personnalisation facile.

- Insérer un nouveau vol dans la BDR

## ETAPES A SUIVRE 

1. Créer une interface graphique avec 5 champs de texte qui permettent d’enregistrer un nouveau vol.
2.  En plus créer une interface mlkenu qui permet de choisir si on recherche un vol ou si on veut l’enregistrer ou si on veut le modifier/supprimer.
3. Créer l’interface qui permet de chercher un vol en entrant la ville de départ et la ville d’arrivée. Dans un premier temps on affiche toutes les réponses (pour cela enrichissez la base pour qu’il y ait plusieurs réponses) Facultatif : proposez de cocher une case pour n’afficher seulement le vol le plus rapide !
4.  Créer une fenêtre qui permet de modifier ou supprimer un vol connaissant son numéro de vol accessible seulement pour un admin, donc dans la base ajouter une table admin, il faut   donc se connecter dans un premier temps pour pouvoir modifier ou supprimer un vol

## CONSIGNES 

- Le code doit être judicieusement commenté
- Utiliser des classes séparées et des fonctions pour chaque utilité : un Main qui appelle la classe Interface et la gestion des bases de données dans une ou plusieurs classes séparées (on se rapproche du modèle MVC)
- Utiliser les PreparedStatement pour les requetes en insertion
- Penser aux erreurs que l'utilisateur peut faire et gérer  ces erreurs pour qu'il puisse les corriger
- Rendu le 11/10/2024