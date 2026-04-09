package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for user persistence.
 * Provides database access for User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by username.
   *
   * @param username username to search
   * @return matching user if found
   */
  Optional<User> findByUsername(String username);
}