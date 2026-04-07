# Distopia - gestion de cinémas

- [Enoncé](#enoncé-de-lévaluation)
- [Description](#description)
- [Technologies](#Technologies)

## Enoncé de l'évaluation
*Une appli simple de gestion de cinémas un peu partout en France. 
L’administrateur doit pouvoir ajouter/modifier/supprimer une ou plusieurs villes, 
les cinémas associés et un certain nombre de film à l’affiche. L’utilisateur non connecté peut
afficher tous les cinémas d’une ville ou rechercher un cinéma par mot clé. Il peut afficher tous 
les films d’un cinéma et toutes les séances d’un film. Un cinéma est caractérisé par : nom, adresse,
liste de films*

*Bonus : Après quoi, s’il souhaite réserver sa place, 
il faut être connecté puis sélectionner le film et la séance à une date donnée.*

## Description
Le système à développer est un gestionnaire de cinémas.
Un cinéma est rattaché à une ville, chaque ville pouvant être gérée par un administrateur, chaque 
cinéma ont un certain nombre de films à l'affiche qui, eux aussi, sont gérables par l'administrateur.
L'administrateur a ainsi la possibilité d'ajouter/modifier/supprimer ces différentes entités

Les utilisateurs non connectés peuvent afficher les cinémas d'une ville, rechercher un cinéma par mot-clé
ainsi que visualiser les films à l'affiche et les séances associés.

Le système doit également permettre à un utilisateur authentifié de réserver une ou plusieurs places 
pour une séance donnée, après avoir sélectionné un film, une séance et une date. 
L’accès à la fonctionnalité de réservation est conditionné par une authentification préalable.

## Technologies

Spring Boot 3.5.13
Java 17
Lombok
Mariadb Driver
Thymeleaf
Spring Data JPA
Spring web

## Installation

## Structure du projet

## Fonctionnalités