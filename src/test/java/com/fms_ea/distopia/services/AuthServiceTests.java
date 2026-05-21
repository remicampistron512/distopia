package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  private AuthService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthService(userRepository);
  }

  @Test
  void authenticate_shouldReturnUser_whenUsernameAndPasswordAreValid() {
    // Arrange
    String username = "john";
    String password = "secret";

    User user = new User();
    user.setUsername(username);
    user.setPassword(password);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Act
    User authenticatedUser = authService.authenticate(username, password);

    // Assert
    assertNotNull(authenticatedUser);
    assertEquals(username, authenticatedUser.getUsername());
    assertEquals(password, authenticatedUser.getPassword());

    verify(userRepository).findByUsername(username);
  }

  @Test
  void authenticate_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
    // Arrange
    String username = "unknown";
    String password = "secret";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act + Assert
    UserNotFoundException exception = assertThrows(
        UserNotFoundException.class,
        () -> authService.authenticate(username, password)
    );

    assertEquals("Utilisateur introuvable", exception.getMessage());

    verify(userRepository).findByUsername(username);
  }

  @Test
  void authenticate_shouldThrowBadCredentialsException_whenPasswordIsIncorrect() {
    // Arrange
    String username = "john";
    String correctPassword = "secret";
    String wrongPassword = "wrong-password";

    User user = new User();
    user.setUsername(username);
    user.setPassword(correctPassword);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Act + Assert
    BadCredentialsException exception = assertThrows(
        BadCredentialsException.class,
        () -> authService.authenticate(username, wrongPassword)
    );

    assertEquals("Mot de passe incorrect", exception.getMessage());

    verify(userRepository).findByUsername(username);
  }
}