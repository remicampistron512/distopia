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

/**
 * Tests unitaires de AuthService.
 *
 * Cette classe vérifie uniquement la logique métier du service d'authentification.
 * Le UserRepository est remplacé par un mock afin d'isoler AuthService
 * et de ne pas dépendre d'une base de données.
 *
 * Cas testés :
 * - authentification réussie ;
 * - utilisateur introuvable ;
 * - mot de passe incorrect.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  /**
   * Mock du repository utilisateur.
   *
   * Ce mock permet de contrôler précisément ce que retourne findByUsername()
   * selon le scénario testé.
   */
  @Mock
  private UserRepository userRepository;

  /**
   * Service réellement testé.
   *
   * Contrairement au repository, AuthService n'est pas mocké :
   * on veut exécuter son vrai code.
   */
  private AuthService authService;

  /**
   * Initialisation exécutée avant chaque test.
   *
   * On crée une nouvelle instance de AuthService avec le repository mocké.
   * Cela garantit que chaque test démarre avec un état propre.
   */
  @BeforeEach
  void setUp() {
    authService = new AuthService(userRepository);
  }

  /**
   * Vérifie qu'un utilisateur est retourné lorsque :
   * - le username existe ;
   * - le mot de passe fourni correspond au mot de passe stocké.
   *
   * Ce test valide le cas nominal de la méthode authenticate().
   */
  @Test
  void authenticate_shouldReturnUser_whenUsernameAndPasswordAreValid() {
    // Arrange
    // Préparation des identifiants envoyés à la méthode authenticate().
    String username = "john";
    String password = "secret";

    // Création d'un utilisateur simulant un utilisateur existant en base.
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);

    // Configuration du mock :
    // lorsque AuthService cherche l'utilisateur "john",
    // le repository retourne l'utilisateur préparé ci-dessus.
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Act
    // Exécution de la méthode réellement testée.
    User authenticatedUser = authService.authenticate(username, password);

    // Assert
    // Vérifie que le service retourne bien un utilisateur.
    assertNotNull(authenticatedUser);

    // Vérifie que l'utilisateur retourné correspond aux informations attendues.
    assertEquals(username, authenticatedUser.getUsername());
    assertEquals(password, authenticatedUser.getPassword());

    // Vérifie que AuthService a bien demandé l'utilisateur au repository.
    verify(userRepository).findByUsername(username);
  }

  /**
   * Vérifie qu'une exception UserNotFoundException est levée lorsque :
   * - le username fourni n'existe pas en base.
   *
   * Dans ce scénario, le repository retourne Optional.empty().
   * AuthService doit alors refuser l'authentification immédiatement.
   */
  @Test
  void authenticate_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
    // Arrange
    // Préparation d'identifiants pour un utilisateur inexistant.
    String username = "unknown";
    String password = "secret";

    // Configuration du mock :
    // lorsque AuthService cherche l'utilisateur "unknown",
    // le repository simule une absence de résultat.
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act + Assert
    // On vérifie que l'appel à authenticate() lève bien UserNotFoundException.
    UserNotFoundException exception = assertThrows(
        UserNotFoundException.class,
        () -> authService.authenticate(username, password)
    );

    // Vérifie que le message de l'exception correspond au message attendu.
    assertEquals("Utilisateur introuvable", exception.getMessage());

    // Vérifie que AuthService a bien tenté de rechercher l'utilisateur.
    verify(userRepository).findByUsername(username);
  }

  /**
   * Vérifie qu'une exception BadCredentialsException est levée lorsque :
   * - le username existe ;
   * - le mot de passe fourni ne correspond pas au mot de passe stocké.
   *
   * Dans ce scénario, l'utilisateur est bien trouvé,
   * mais AuthService doit refuser l'authentification à cause du mot de passe.
   */
  @Test
  void authenticate_shouldThrowBadCredentialsException_whenPasswordIsIncorrect() {
    // Arrange
    // Préparation du username et des deux mots de passe :
    // celui stocké dans l'utilisateur, et celui saisi au moment de la connexion.
    String username = "john";
    String storedPassword = "secret";
    String providedPassword = "wrong-password";

    // Création d'un utilisateur simulant un utilisateur existant en base.
    User user = new User();
    user.setUsername(username);
    user.setPassword(storedPassword);

    // Configuration du mock :
    // le repository trouve bien l'utilisateur "john".
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Act + Assert
    // On vérifie que l'appel à authenticate() lève BadCredentialsException
    // parce que le mot de passe fourni est différent du mot de passe stocké.
    BadCredentialsException exception = assertThrows(
        BadCredentialsException.class,
        () -> authService.authenticate(username, providedPassword)
    );

    // Vérifie que le message de l'exception correspond au message attendu.
    assertEquals("Mot de passe incorrect", exception.getMessage());

    // Vérifie que AuthService a bien demandé l'utilisateur au repository.
    verify(userRepository).findByUsername(username);
  }
}