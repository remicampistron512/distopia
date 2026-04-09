package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entity representing a movie showing.
 * A showing is linked to one cinema, one movie, and can have bookings.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cinema", "movie", "bookings"})
public class Showing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Showing date and time */
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime startDateTime;

  /** Ticket price */
  private BigDecimal price;

  /** Number of available seats */
  private int availableSeats;

  /** Cinema where the showing takes place */
  @ManyToOne
  @JoinColumn(name = "cinema_id")
  private Cinema cinema;

  /** Movie shown during this showing */
  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  /** Bookings linked to this showing */
  @OneToMany(mappedBy = "showing", cascade = CascadeType.ALL)
  private List<Booking> bookings;
}