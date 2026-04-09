package com.fms_ea.distopia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity representing an application user.
 * A user can have multiple bookings.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "bookings")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** Username used for authentication */
  private String username;

  /** User password */
  private String password;

  /** User first name */
  private String firstName;

  /** User last name */
  private String lastName;

  /** User role (e.g., ADMIN, USER) */
  private String role;

  /** List of bookings associated with the user */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Booking> bookings;
}