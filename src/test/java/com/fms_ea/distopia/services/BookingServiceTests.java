package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Booking;
import com.fms_ea.distopia.repositories.BookingRepository;
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
 * Tests unitaires de BookingService.
 *
 * Cette classe vérifie uniquement la logique du service BookingService.
 * Le BookingRepository est remplacé par un mock Mockito afin d'éviter
 * d'utiliser une vraie base de données.
 *
 * Objectifs des tests :
 * - vérifier qu'une réservation peut être sauvegardée ;
 * - vérifier que toutes les réservations peuvent être récupérées ;
 * - vérifier qu'une réservation existante peut être récupérée par son id ;
 * - vérifier qu'une exception est levée si la réservation n'existe pas ;
 * - vérifier qu'une suppression est bien transmise au repository.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  /**
   * Mock du repository des réservations.
   *
   * Ce faux repository permet de contrôler les réponses retournées
   * par les méthodes save(), findAll(), findById() et deleteById().
   *
   * Ainsi, on teste BookingService sans dépendre de Spring Data JPA
   * ni d'une base de données réelle.
   */
  @Mock
  private BookingRepository bookingRepository;

  /**
   * Service réellement testé.
   *
   * Contrairement au repository, BookingService n'est pas mocké.
   * On veut exécuter son vrai code pour vérifier son comportement.
   */
  private BookingService bookingService;

  /**
   * Méthode exécutée avant chaque test.
   *
   * Elle crée une nouvelle instance de BookingService
   * en lui injectant le repository mocké.
   *
   * Cela garantit que chaque test démarre avec une configuration propre.
   */
  @BeforeEach
  void setUp() {
    bookingService = new BookingService(bookingRepository);
  }

  /**
   * Vérifie que save() retourne bien la réservation sauvegardée.
   *
   * Dans ce scénario :
   * - on fournit une réservation à sauvegarder ;
   * - le repository retourne une réservation sauvegardée ;
   * - le service doit retourner exactement cette réservation sauvegardée.
   */
  @Test
  void save_shouldReturnSavedBooking() {
    // Arrange
    // Réservation envoyée au service pour être sauvegardée.
    Booking booking = new Booking();

    // Réservation simulant le résultat retourné par le repository après sauvegarde.
    // Dans une vraie base, cet objet pourrait contenir un id généré.
    Booking savedBooking = new Booking();

    // Configuration du mock :
    // lorsque bookingRepository.save(booking) est appelé,
    // Mockito retourne savedBooking.
    when(bookingRepository.save(booking)).thenReturn(savedBooking);

    // Act
    // Exécution de la méthode réellement testée.
    Booking result = bookingService.save(booking);

    // Assert
    // Vérifie que le service retourne exactement l'objet retourné par le repository.
    assertSame(savedBooking, result);

    // Vérifie que le service a bien appelé le repository pour sauvegarder la réservation.
    verify(bookingRepository).save(booking);
  }

  /**
   * Vérifie que findAll() retourne toutes les réservations.
   *
   * Dans ce scénario :
   * - le repository retourne une liste de deux réservations ;
   * - le service doit retourner exactement cette même liste.
   */
  @Test
  void findAll_shouldReturnAllBookings() {
    // Arrange
    // Création de deux réservations simulant des données existantes.
    Booking booking1 = new Booking();
    Booking booking2 = new Booking();

    // Liste simulant le résultat retourné par le repository.
    List<Booking> bookings = List.of(booking1, booking2);

    // Configuration du mock :
    // lorsque findAll() est appelé, le repository retourne la liste bookings.
    when(bookingRepository.findAll()).thenReturn(bookings);

    // Act
    // Exécution de la méthode findAll() du service.
    List<Booking> result = bookingService.findAll();

    // Assert
    // Vérifie que la liste contient bien deux éléments.
    assertEquals(2, result.size());

    // Vérifie que le service retourne exactement la même liste que le repository.
    assertSame(bookings, result);

    // Vérifie que le service a bien appelé bookingRepository.findAll().
    verify(bookingRepository).findAll();
  }

  /**
   * Vérifie que findById() retourne une réservation lorsqu'elle existe.
   *
   * Dans ce scénario :
   * - on recherche une réservation avec l'id 1 ;
   * - le repository retourne Optional.of(booking) ;
   * - le service doit extraire la réservation de l'Optional et la retourner.
   */
  @Test
  void findById_shouldReturnBooking_whenBookingExists() {
    // Arrange
    // Id de la réservation recherchée.
    Long bookingId = 1L;

    // Réservation simulant une réservation trouvée en base.
    Booking booking = new Booking();

    // Configuration du mock :
    // lorsque findById(1L) est appelé,
    // le repository retourne un Optional contenant la réservation.
    when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

    // Act
    // Exécution de la méthode findById() du service.
    Booking result = bookingService.findById(bookingId);

    // Assert
    // Vérifie que la réservation retournée est exactement celle fournie par le repository.
    assertSame(booking, result);

    // Vérifie que le service a bien demandé la réservation au repository.
    verify(bookingRepository).findById(bookingId);
  }

  /**
   * Vérifie que findById() lève une exception lorsque la réservation n'existe pas.
   *
   * Dans ce scénario :
   * - on recherche une réservation avec l'id 99 ;
   * - le repository retourne Optional.empty() ;
   * - le service doit lever une RuntimeException avec le message attendu.
   */
  @Test
  void findById_shouldThrowRuntimeException_whenBookingDoesNotExist() {
    // Arrange
    // Id d'une réservation inexistante.
    Long bookingId = 99L;

    // Configuration du mock :
    // Optional.empty() signifie que le repository n'a trouvé aucune réservation.
    when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

    // Act + Assert
    // Vérifie que l'appel à findById() provoque bien une RuntimeException.
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> bookingService.findById(bookingId)
    );

    // Vérifie que le message de l'exception est celui attendu.
    assertEquals("reservation non trouvée avec id : 99", exception.getMessage());

    // Vérifie que le service a bien tenté de rechercher la réservation.
    verify(bookingRepository).findById(bookingId);
  }

  /**
   * Vérifie que deleteById() transmet bien la suppression au repository.
   *
   * Dans ce scénario :
   * - on demande au service de supprimer la réservation avec l'id 1 ;
   * - le service doit appeler bookingRepository.deleteById(1L).
   */
  @Test
  void deleteById_shouldDeleteBookingById() {
    // Arrange
    // Id de la réservation à supprimer.
    Long bookingId = 1L;

    // Configuration optionnelle du mock :
    // deleteById() est une méthode void.
    // Cette ligne indique explicitement que le mock ne fait rien quand elle est appelée.
    // Elle peut être supprimée, car Mockito ne fait déjà rien par défaut pour les méthodes void.
    doNothing().when(bookingRepository).deleteById(bookingId);

    // Act
    // Exécution de la méthode de suppression du service.
    bookingService.deleteById(bookingId);

    // Assert
    // Vérifie que le service a bien appelé le repository pour supprimer la réservation.
    verify(bookingRepository).deleteById(bookingId);
  }
}