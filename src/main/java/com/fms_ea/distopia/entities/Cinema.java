package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"city", "showings"})
public class Cinema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private String address;
  private double communityRating;
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
  private List<Showing> showings;
}