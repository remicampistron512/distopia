package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a booking.
 * A booking is linked to one user and one showing.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "showing"})
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Number of seats booked */
  private int numberOfSeats;

  /** Booking date and time */
  private LocalDateTime bookedAt;

  /** Total booking price */
  private BigDecimal totalPrice;

  /** Booking status */
  private String status;

  /** User who made the booking */
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /** Showing linked to the booking */
  @ManyToOne
  @JoinColumn(name = "showing_id")
  private Showing showing;
}