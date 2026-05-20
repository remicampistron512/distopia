package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Booking;
import com.fms_ea.distopia.repositories.BookingRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;
  /**
   * Saves a booking.
   *
   * @param booking cinema to save
   * @return saved cinema
   */
  public Booking save(Booking booking) {
    return bookingRepository.save(booking);
  }

  public List<Booking> findAll() {
    return bookingRepository.findAll();
  }

  public Booking findById(Long id) {
    return bookingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("reservation non trouvée avec id : " + id));
  }

  public void deleteById(Long id) {
    bookingRepository.deleteById(id);
  }
}
