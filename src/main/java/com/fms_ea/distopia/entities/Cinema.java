package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity representing a cinema.
 * A cinema belongs to a city and can host multiple showings.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"city", "showings"})
public class Cinema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Cinema name */
  private String name;

  /** Cinema address */
  private String address;

  /** Community rating */
  private double communityRating;

  /** Image URL representing the cinema */
  private String imageUrl;

  /** City where the cinema is located */
  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  /** Showings hosted in this cinema */
  @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
  private List<Showing> showings;
}