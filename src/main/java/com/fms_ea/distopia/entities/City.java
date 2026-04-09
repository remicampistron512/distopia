package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity representing a city.
 * A city can contain multiple cinemas.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cinemas")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** City name */
  private String name;

  /** Image URL representing the city */
  private String imageUrl;

  /** Cinemas located in this city */
  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<Cinema> cinemas;
}