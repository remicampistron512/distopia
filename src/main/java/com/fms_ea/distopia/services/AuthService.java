package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User authenticate(String username, String password) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

    if (!password.equals(user.getPassword())) {
      throw new BadCredentialsException("Mot de passe incorrect");
    }

    return user;
  }
}