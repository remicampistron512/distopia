package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cinemas")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private String imageUrl;

  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<Cinema> cinemas;
}