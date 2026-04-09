package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.repositories.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for user management.
 * Handles user retrieval and persistence.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * Returns all users.
   *
   * @return list of users
   */
  public List<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * Finds a user by id.
   *
   * @param id user id
   * @return user or null if not found
   */
  public User findById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  /**
   * Saves a user.
   *
   * @param user user to save
   * @return saved user
   */
  public User save(User user) {
    return userRepository.save(user);
  }

  /**
   * Deletes a user by id.
   *
   * @param id user id
   */
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }
}