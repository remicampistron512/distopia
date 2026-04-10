# Distopia - gestion de cinémas

- [Enoncé](#enoncé-de-lévaluation)
- [Description](#description)
- [Technologies](#technologies)
- [Installation](#installation)
- [Configuration](#configuration)
- [Structure du projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)

## Enoncé de l'évaluation
*Une appli simple de gestion de cinémas un peu partout en France. 
L’administrateur doit pouvoir ajouter/modifier/supprimer une ou plusieurs villes, 
les cinémas associés et un certain nombre de film à l'affiche. L’utilisateur non connecté peut
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
Spring Web  

## Installation

### Prérequis

- Java 17 ou supérieur
- Maven
- MariaDB

### Étapes d'installation

1. Cloner le dépôt :

```bash id="d17k4r"
git clone https://github.com/remicampistron512/distopia
cd distopia
````


2. Configurer le fichier `application.properties` :

```properties id="x6g2j1"
spring.datasource.url=jdbc:mariadb://localhost:3306/distopia
spring.datasource.username=root
spring.datasource.password=motdepasse

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Lancer l'application, une base de donnée sera automatiquement créée :

```bash id="wb7qkf"
./mvnw spring-boot:run
```
4. Importer bdd.sql dans la base donnée créée

5. Relancer l'application

6. Ouvrir l'application dans le navigateur :

```text id="7f6h1k"
http://localhost:8080
```

## Configuration

### Gestion des images

Les images uploadées (films, cinémas, villes) sont stockées dans le dossier :

```text id="i1x7vs"
/uploads
```

Le dossier est créé automatiquement si nécessaire.

### Authentification

Le système inclut :

* connexion utilisateur,
* session utilisateur,
* accès réservé à l'administration pour les utilisateurs autorisés.

## Structure du projet

```text id="ccqzr7"
src/
 ├── main/
 │   ├── java/com/fms_ea/distopia/
 │   │   ├── controllers/
 │   │   ├── entities/
 │   │   ├── repositories/
 │   │   ├── services/
 │   │   └── DistopiaApplication.java
 │   │
 │   ├── resources/
 │   │   ├── templates/
 │   │   │   ├── admin/
 │   │   │   ├── cities/
 │   │   │   ├── cinemas/
 │   │   │   ├── movies/
 │   │   │   ├── showings/
 │   │   │   ├── users/
 │   │   │   └── layout.html
 │   │   │
 │   │   ├── static/
 │   │   │   ├── css/
 │   │   │   ├── images/
 │   │   │   └── uploads/
 │   │   │
 │   │   └── application.properties
```

## Fonctionnalités

### Utilisateurs non connectés

* consulter la page d'accueil avec films, villes et cinémas mis en avant,
* rechercher un cinéma par mot-clé,
* consulter la liste des villes,
* afficher les cinémas d'une ville,
* consulter les détails d'un cinéma,
* afficher les films à l'affiche,
* consulter les détails d’un film,
* afficher les séances disponibles.

### Administrateur

* gérer les villes (ajout, modification, suppression),
* gérer les cinémas (ajout, modification, suppression),
* gérer les films (ajout, modification, suppression),
* gérer les séances (ajout, modification, suppression),
* gérer les utilisateurs,
* uploader des images pour les villes, cinémas et films.

### Utilisateurs connectés

* se connecter / se déconnecter,
* réserver une ou plusieurs places pour une séance,
* consulter leurs réservations.

### Interface

* interface responsive avec Bootstrap,
* interface d'administration harmonisée,
* messages flash de succès / erreur,
* navigation simplifiée avec menu public et menu admin.


