package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "bookings")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Booking> bookings;
}