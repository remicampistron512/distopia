package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.repositories.CinemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires de CinemaService.
 *
 * Cette classe teste uniquement la logique du service CinemaService.
 * Le CinemaRepository est remplacé par un mock Mockito afin d'éviter
 * d'utiliser une vraie base de données.
 *
 * Objectifs des tests :
 * - vérifier la récupération de tous les cinémas ;
 * - vérifier la récupération d'un cinéma par son id ;
 * - vérifier le comportement lorsqu'un cinéma n'existe pas ;
 * - vérifier la récupération d'un cinéma avec ses séances ;
 * - vérifier la sauvegarde d'un cinéma ;
 * - vérifier la suppression d'un cinéma ;
 * - vérifier la recherche de cinémas par nom.
 */
@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

  /**
   * Mock du repository des cinémas.
   *
   * Ce faux repository permet de contrôler les réponses retournées
   * par les méthodes findAll(), findById(), findByIdWithShowings(),
   * save(), deleteById() et findByNameContains().
   *
   * On peut donc tester CinemaService sans dépendre de Spring Data JPA
   * ni d'une base de données réelle.
   */
  @Mock
  private CinemaRepository cinemaRepository;

  /**
   * Service réellement testé.
   *
   * Contrairement au repository, CinemaService n'est pas mocké.
   * On veut exécuter son vrai code pour vérifier son comportement.
   */
  private CinemaService cinemaService;

  /**
   * Méthode exécutée avant chaque test.
   *
   * Elle crée une nouvelle instance de CinemaService
   * en lui injectant le repository mocké.
   *
   * Cela garantit que chaque test démarre avec une configuration propre.
   */
  @BeforeEach
  void setUp() {
    cinemaService = new CinemaService(cinemaRepository);
  }

  /**
   * Vérifie que findAll() retourne tous les cinémas.
   *
   * Dans ce scénario :
   * - le repository retourne une liste contenant deux cinémas ;
   * - le service doit retourner exactement cette même liste.
   */
  @Test
  void findAll_shouldReturnAllCinemas() {
    // Arrange
    // Création de deux cinémas simulant des données existantes.
    Cinema cinema1 = new Cinema();
    Cinema cinema2 = new Cinema();

    // Liste simulant le résultat retourné par le repository.
    List<Cinema> cinemas = List.of(cinema1, cinema2);

    // Configuration du mock :
    // lorsque cinemaRepository.findAll() est appelé,
    // Mockito retourne la liste cinemas.
    when(cinemaRepository.findAll()).thenReturn(cinemas);

    // Act
    // Exécution de la méthode findAll() du service.
    List<Cinema> result = cinemaService.findAll();

    // Assert
    // Vérifie que le service retourne exactement la même liste que le repository.
    assertSame(cinemas, result);

    // Vérifie que la liste contient bien deux éléments.
    assertEquals(2, result.size());

    // Vérifie que le service a bien appelé cinemaRepository.findAll().
    verify(cinemaRepository).findAll();
  }

  /**
   * Vérifie que findById() retourne un cinéma lorsqu'il existe.
   *
   * Dans ce scénario :
   * - on recherche un cinéma avec l'id 1 ;
   * - le repository retourne Optional.of(cinema) ;
   * - le service extrait le cinéma de l'Optional et le retourne.
   */
  @Test
  void findById_shouldReturnCinema_whenCinemaExists() {
    // Arrange
    // Id du cinéma recherché.
    Long cinemaId = 1L;

    // Cinéma simulant un résultat trouvé en base.
    Cinema cinema = new Cinema();

    // Configuration du mock :
    // lorsque findById(1L) est appelé,
    // le repository retourne un Optional contenant le cinéma.
    when(cinemaRepository.findById(cinemaId)).thenReturn(Optional.of(cinema));

    // Act
    // Exécution de la méthode findById() du service.
    Cinema result = cinemaService.findById(cinemaId);

    // Assert
    // Vérifie que le service retourne exactement le cinéma fourni par le repository.
    assertSame(cinema, result);

    // Vérifie que le service a bien demandé le cinéma au repository.
    verify(cinemaRepository).findById(cinemaId);
  }

  /**
   * Vérifie que findById() lève une exception lorsque le cinéma n'existe pas.
   *
   * Dans ce scénario :
   * - on recherche un cinéma avec l'id 99 ;
   * - le repository retourne Optional.empty() ;
   * - le service doit lever une RuntimeException avec le message attendu.
   */
  @Test
  void findById_shouldThrowRuntimeException_whenCinemaDoesNotExist() {
    // Arrange
    // Id d'un cinéma inexistant.
    Long cinemaId = 99L;

    // Configuration du mock :
    // Optional.empty() signifie qu'aucun cinéma n'a été trouvé.
    when(cinemaRepository.findById(cinemaId)).thenReturn(Optional.empty());

    // Act + Assert
    // Vérifie que l'appel à findById() provoque bien une RuntimeException.
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> cinemaService.findById(cinemaId)
    );

    // Vérifie que le message de l'exception est celui attendu.
    assertEquals("Cinéma non trouvé avec id : 99", exception.getMessage());

    // Vérifie que le service a bien tenté de rechercher le cinéma.
    verify(cinemaRepository).findById(cinemaId);
  }

  /**
   * Vérifie que findByIdWithShowings() retourne un cinéma avec ses séances lorsqu'il existe.
   *
   * Dans ce scénario :
   * - on recherche un cinéma avec l'id 1 ;
   * - le repository retourne Optional.of(cinema) ;
   * - le service retourne le cinéma trouvé.
   *
   * La présence réelle des séances n'est pas testée ici :
   * on vérifie seulement que le service appelle la bonne méthode du repository.
   */
  @Test
  void findByIdWithShowings_shouldReturnCinema_whenCinemaExists() {
    // Arrange
    // Id du cinéma recherché avec ses séances.
    Long cinemaId = 1L;

    // Cinéma simulant un cinéma trouvé avec ses relations chargées.
    Cinema cinema = new Cinema();

    // Configuration du mock :
    // lorsque findByIdWithShowings(1L) est appelé,
    // le repository retourne le cinéma trouvé.
    when(cinemaRepository.findByIdWithShowings(cinemaId)).thenReturn(Optional.of(cinema));

    // Act
    // Exécution de la méthode findByIdWithShowings() du service.
    Cinema result = cinemaService.findByIdWithShowings(cinemaId);

    // Assert
    // Vérifie que le service retourne exactement le cinéma fourni par le repository.
    assertSame(cinema, result);

    // Vérifie que le service utilise bien la méthode spécifique avec showings.
    verify(cinemaRepository).findByIdWithShowings(cinemaId);
  }

  /**
   * Vérifie que findByIdWithShowings() lève une exception lorsque le cinéma n'existe pas.
   *
   * Dans ce scénario :
   * - on recherche un cinéma avec l'id 99 ;
   * - le repository retourne Optional.empty() ;
   * - le service doit lever une RuntimeException avec le message "Cinéma non trouvé".
   */
  @Test
  void findByIdWithShowings_shouldThrowRuntimeException_whenCinemaDoesNotExist() {
    // Arrange
    // Id d'un cinéma inexistant.
    Long cinemaId = 99L;

    // Configuration du mock :
    // aucun cinéma n'est trouvé avec ses séances.
    when(cinemaRepository.findByIdWithShowings(cinemaId)).thenReturn(Optional.empty());

    // Act + Assert
    // Vérifie que l'appel à findByIdWithShowings() lève bien une RuntimeException.
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> cinemaService.findByIdWithShowings(cinemaId)
    );

    // Vérifie le message d'erreur retourné par le service.
    assertEquals("Cinéma non trouvé", exception.getMessage());

    // Vérifie que le service a bien appelé la méthode attendue du repository.
    verify(cinemaRepository).findByIdWithShowings(cinemaId);
  }

  /**
   * Vérifie que save() retourne le cinéma sauvegardé.
   *
   * Dans ce scénario :
   * - on fournit un cinéma à sauvegarder ;
   * - le repository retourne un cinéma sauvegardé ;
   * - le service doit retourner exactement ce cinéma sauvegardé.
   */
  @Test
  void save_shouldReturnSavedCinema() {
    // Arrange
    // Cinéma envoyé au service pour être sauvegardé.
    Cinema cinema = new Cinema();

    // Cinéma simulant le résultat retourné par le repository après sauvegarde.
    // Dans une vraie base, cet objet pourrait contenir un id généré.
    Cinema savedCinema = new Cinema();

    // Configuration du mock :
    // lorsque cinemaRepository.save(cinema) est appelé,
    // Mockito retourne savedCinema.
    when(cinemaRepository.save(cinema)).thenReturn(savedCinema);

    // Act
    // Exécution de la méthode save() du service.
    Cinema result = cinemaService.save(cinema);

    // Assert
    // Vérifie que le service retourne exactement l'objet retourné par le repository.
    assertSame(savedCinema, result);

    // Vérifie que le service a bien appelé le repository pour sauvegarder le cinéma.
    verify(cinemaRepository).save(cinema);
  }

  /**
   * Vérifie que deleteById() transmet bien la suppression au repository.
   *
   * Dans ce scénario :
   * - on demande au service de supprimer le cinéma avec l'id 1 ;
   * - le service doit appeler cinemaRepository.deleteById(1L).
   */
  @Test
  void deleteById_shouldDeleteCinemaById() {
    // Arrange
    // Id du cinéma à supprimer.
    Long cinemaId = 1L;

    // Configuration optionnelle du mock :
    // deleteById() est une méthode void.
    // Mockito ne fait déjà rien par défaut sur les méthodes void,
    // donc cette ligne peut être supprimée sans changer le test.
    doNothing().when(cinemaRepository).deleteById(cinemaId);

    // Act
    // Exécution de la méthode deleteById() du service.
    cinemaService.deleteById(cinemaId);

    // Assert
    // Vérifie que le service a bien demandé au repository de supprimer ce cinéma.
    verify(cinemaRepository).deleteById(cinemaId);
  }

  /**
   * Vérifie que findByNameContains() retourne les cinémas correspondant au mot-clé.
   *
   * Dans ce scénario :
   * - on recherche les cinémas contenant "gaumont" dans leur nom ;
   * - le repository retourne une liste de deux cinémas ;
   * - le service doit retourner exactement cette même liste.
   */
  @Test
  void findByNameContains_shouldReturnMatchingCinemas() {
    // Arrange
    // Mot-clé utilisé pour rechercher des cinémas par nom.
    String keyword = "gaumont";

    // Création de deux cinémas simulant les résultats de recherche.
    Cinema cinema1 = new Cinema();
    Cinema cinema2 = new Cinema();

    // Liste simulant le résultat retourné par le repository.
    List<Cinema> cinemas = List.of(cinema1, cinema2);

    // Configuration du mock :
    // lorsque findByNameContains("gaumont") est appelé,
    // le repository retourne la liste cinemas.
    when(cinemaRepository.findByNameContains(keyword)).thenReturn(cinemas);

    // Act
    // Exécution de la méthode de recherche du service.
    Object result = cinemaService.findByNameContains(keyword);

    // Assert
    // Vérifie que le service retourne exactement la même liste que le repository.
    assertSame(cinemas, result);

    // Vérifie que le service a bien appelé la méthode de recherche du repository.
    verify(cinemaRepository).findByNameContains(keyword);
  }
}