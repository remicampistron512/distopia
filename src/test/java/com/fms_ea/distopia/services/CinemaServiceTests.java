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

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

  @Mock
  private CinemaRepository cinemaRepository;

  private CinemaService cinemaService;

  @BeforeEach
  void setUp() {
    cinemaService = new CinemaService(cinemaRepository);
  }

  @Test
  void findAll_shouldReturnAllCinemas() {
    // Arrange
    Cinema cinema1 = new Cinema();
    Cinema cinema2 = new Cinema();

    List<Cinema> cinemas = List.of(cinema1, cinema2);

    when(cinemaRepository.findAll()).thenReturn(cinemas);

    // Act
    List<Cinema> result = cinemaService.findAll();

    // Assert
    assertSame(cinemas, result);
    assertEquals(2, result.size());

    verify(cinemaRepository).findAll();
  }

  @Test
  void findById_shouldReturnCinema_whenCinemaExists() {
    // Arrange
    Long cinemaId = 1L;
    Cinema cinema = new Cinema();

    when(cinemaRepository.findById(cinemaId)).thenReturn(Optional.of(cinema));

    // Act
    Cinema result = cinemaService.findById(cinemaId);

    // Assert
    assertSame(cinema, result);

    verify(cinemaRepository).findById(cinemaId);
  }

  @Test
  void findById_shouldThrowRuntimeException_whenCinemaDoesNotExist() {
    // Arrange
    Long cinemaId = 99L;

    when(cinemaRepository.findById(cinemaId)).thenReturn(Optional.empty());

    // Act + Assert
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> cinemaService.findById(cinemaId)
    );

    assertEquals("Cinéma non trouvé avec id : 99", exception.getMessage());

    verify(cinemaRepository).findById(cinemaId);
  }

  @Test
  void findByIdWithShowings_shouldReturnCinema_whenCinemaExists() {
    // Arrange
    Long cinemaId = 1L;
    Cinema cinema = new Cinema();

    when(cinemaRepository.findByIdWithShowings(cinemaId)).thenReturn(Optional.of(cinema));

    // Act
    Cinema result = cinemaService.findByIdWithShowings(cinemaId);

    // Assert
    assertSame(cinema, result);

    verify(cinemaRepository).findByIdWithShowings(cinemaId);
  }

  @Test
  void findByIdWithShowings_shouldThrowRuntimeException_whenCinemaDoesNotExist() {
    // Arrange
    Long cinemaId = 99L;

    when(cinemaRepository.findByIdWithShowings(cinemaId)).thenReturn(Optional.empty());

    // Act + Assert
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> cinemaService.findByIdWithShowings(cinemaId)
    );

    assertEquals("Cinéma non trouvé", exception.getMessage());

    verify(cinemaRepository).findByIdWithShowings(cinemaId);
  }

  @Test
  void save_shouldReturnSavedCinema() {
    // Arrange
    Cinema cinema = new Cinema();
    Cinema savedCinema = new Cinema();

    when(cinemaRepository.save(cinema)).thenReturn(savedCinema);

    // Act
    Cinema result = cinemaService.save(cinema);

    // Assert
    assertSame(savedCinema, result);

    verify(cinemaRepository).save(cinema);
  }

  @Test
  void deleteById_shouldDeleteCinemaById() {
    // Arrange
    Long cinemaId = 1L;

    doNothing().when(cinemaRepository).deleteById(cinemaId);

    // Act
    cinemaService.deleteById(cinemaId);

    // Assert
    verify(cinemaRepository).deleteById(cinemaId);
  }

  @Test
  void findByNameContains_shouldReturnMatchingCinemas() {
    // Arrange
    String keyword = "gaumont";

    Cinema cinema1 = new Cinema();
    Cinema cinema2 = new Cinema();

    List<Cinema> cinemas = List.of(cinema1, cinema2);

    when(cinemaRepository.findByNameContains(keyword)).thenReturn(cinemas);

    // Act
    Object result = cinemaService.findByNameContains(keyword);

    // Assert
    assertSame(cinemas, result);

    verify(cinemaRepository).findByNameContains(keyword);
  }
}