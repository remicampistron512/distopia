package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cinema", "movie", "bookings"})
public class Showing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime startDateTime;
  private BigDecimal price;
  private int availableSeats;

  @ManyToOne
  @JoinColumn(name = "cinema_id")
  private Cinema cinema;

  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @OneToMany(mappedBy = "showing", cascade = CascadeType.ALL)
  private List<Booking> bookings;
}