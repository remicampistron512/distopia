package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "showing"})
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private int numberOfSeats;
  private LocalDateTime bookedAt;
  private BigDecimal totalPrice;
  private String status;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "showing_id")
  private Showing showing;
}