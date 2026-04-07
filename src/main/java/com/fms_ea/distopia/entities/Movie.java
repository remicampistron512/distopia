package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "showings")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;
  private int year;
  private int duration;
  private LocalDate releaseDate;
  private double criticsRating;
  private double communityRating;
  private String genre;
  private String director;

  @ElementCollection
  private List<String> actors;

  @OneToMany(mappedBy = "movie")
  private List<Showing> showings;
}