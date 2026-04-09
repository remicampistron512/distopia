package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entity representing a movie.
 * A movie can have multiple showings.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "showings")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Movie title */
  private String title;

  /** Release year */
  private int year;

  /** Duration in minutes */
  private int duration;

  /** Official release date */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate releaseDate;

  /** Critics rating */
  private double criticsRating;

  /** Community rating */
  private double communityRating;

  /** Movie genre */
  private String genre;

  /** Movie director */
  private String director;

  /** Poster image URL */
  private String imageUrl;

  /** List of actors */
  @ElementCollection
  private List<String> actors;

  /** Showings linked to this movie */
  @OneToMany(mappedBy = "movie")
  private List<Showing> showings;
}