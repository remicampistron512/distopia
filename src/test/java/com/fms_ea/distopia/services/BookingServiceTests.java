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

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock
  private BookingRepository bookingRepository;

  private BookingService bookingService;

  @BeforeEach
  void setUp() {
    bookingService = new BookingService(bookingRepository);
  }

  @Test
  void save_shouldReturnSavedBooking() {
    // Arrange
    Booking booking = new Booking();
    Booking savedBooking = new Booking();

    when(bookingRepository.save(booking)).thenReturn(savedBooking);

    // Act
    Booking result = bookingService.save(booking);

    // Assert
    assertSame(savedBooking, result);
    verify(bookingRepository).save(booking);
  }

  @Test
  void findAll_shouldReturnAllBookings() {
    // Arrange
    Booking booking1 = new Booking();
    Booking booking2 = new Booking();

    List<Booking> bookings = List.of(booking1, booking2);

    when(bookingRepository.findAll()).thenReturn(bookings);

    // Act
    List<Booking> result = bookingService.findAll();

    // Assert
    assertEquals(2, result.size());
    assertSame(bookings, result);
    verify(bookingRepository).findAll();
  }

  @Test
  void findById_shouldReturnBooking_whenBookingExists() {
    // Arrange
    Long bookingId = 1L;
    Booking booking = new Booking();

    when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

    // Act
    Booking result = bookingService.findById(bookingId);

    // Assert
    assertSame(booking, result);
    verify(bookingRepository).findById(bookingId);
  }

  @Test
  void findById_shouldThrowRuntimeException_whenBookingDoesNotExist() {
    // Arrange
    Long bookingId = 99L;

    when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

    // Act + Assert
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> bookingService.findById(bookingId)
    );

    assertEquals("reservation non trouvée avec id : 99", exception.getMessage());
    verify(bookingRepository).findById(bookingId);
  }

  @Test
  void deleteById_shouldDeleteBookingById() {
    // Arrange
    Long bookingId = 1L;

    doNothing().when(bookingRepository).deleteById(bookingId);

    // Act
    bookingService.deleteById(bookingId);

    // Assert
    verify(bookingRepository).deleteById(bookingId);
  }
}