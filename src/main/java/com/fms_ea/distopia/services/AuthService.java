package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication.
 * Validates credentials against stored users.
 */
@Service
public class AuthService {

  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Authenticates a user using username and password.
   *
   * @param username provided username
   * @param password provided password
   * @return authenticated user
   * @throws UserNotFoundException if user does not exist
   * @throws BadCredentialsException if password is incorrect
   */
  public User authenticate(String username, String password) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

    if (!password.equals(user.getPassword())) {
      throw new BadCredentialsException("Mot de passe incorrect");
    }

    return user;
  }
}