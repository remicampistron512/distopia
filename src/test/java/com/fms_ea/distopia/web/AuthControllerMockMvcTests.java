package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.SessionUser;
import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.repositories.UserRepository;
import com.fms_ea.distopia.services.AuthService;
import com.fms_ea.distopia.services.BadCredentialsException;
import com.fms_ea.distopia.services.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test du contrôleur AuthController avec MockMvc.
 *
 * Objectif :
 * - tester les routes HTTP du contrôleur sans lancer un vrai serveur web ;
 * - vérifier les vues retournées ;
 * - vérifier les redirections ;
 * - vérifier les messages flash ;
 * - vérifier la gestion de la session utilisateur.
 */
@WebMvcTest(AuthController.class)
class AuthControllerMockMvcTest {

  /**
   * MockMvc permet de simuler des requêtes HTTP vers le contrôleur.
   *
   * Exemple :
   * mockMvc.perform(get("/login"))
   *
   * Cela permet de tester le comportement web du contrôleur
   * sans démarrer Tomcat et sans navigateur.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Faux AuthService injecté dans le contexte de test Spring.
   *
   * AuthController dépend de AuthService.
   * Comme on teste uniquement le contrôleur, on ne veut pas utiliser
   * le vrai service d'authentification.
   *
   * Mockito permet de définir manuellement son comportement.
   */
  @MockitoBean
  private AuthService authService;

  /**
   * Faux UserRepository injecté dans le contexte de test.
   *
   * Dans AuthController, il existe un champ :
   *
   * @Autowired
   * private UserRepository userRepository;
   *
   * Même s'il n'est pas utilisé dans le contrôleur, Spring doit quand même
   * pouvoir l'injecter pendant le test.
   *
   * C'est pour cela qu'on le déclare ici comme mock.
   */
  @MockitoBean
  private UserRepository userRepository;

  /**
   * Test de la route GET /login.
   *
   * Ce test vérifie que lorsqu'on accède à la page de connexion,
   * le contrôleur retourne bien la vue "login".
   */
  @Test
  void login_shouldReturnLoginView() throws Exception {
    mockMvc.perform(get("/login"))        // Simule une requête HTTP GET vers /login
        .andExpect(status().isOk())       // Vérifie que la réponse HTTP est 200 OK
        .andExpect(view().name("login")); // Vérifie que la vue retournée est "login"
  }

  /**
   * Test du cas où la connexion réussit.
   *
   * Scénario :
   * - l'utilisateur envoie username = admin et password = password ;
   * - AuthService retourne un utilisateur valide ;
   * - le contrôleur crée un SessionUser ;
   * - le SessionUser est stocké dans la session sous le nom "loggedUser" ;
   * - un message flash de succès est ajouté ;
   * - l'utilisateur est redirigé vers la page d'accueil.
   */
  @Test
  void processLogin_shouldRedirectToHome_whenCredentialsAreValid() throws Exception {
    // Arrange
    // Création d'un faux utilisateur retourné par le service d'authentification.
    User user = new User();
    user.setId(1L);
    user.setUsername("admin");
    user.setPassword("password");
    user.setRole("ADMIN");

    // On indique à Mockito :
    // quand authService.authenticate("admin", "password") est appelé,
    // alors il doit retourner l'utilisateur créé ci-dessus.
    when(authService.authenticate("admin", "password")).thenReturn(user);

    // Act + Assert
    // On simule l'envoi du formulaire de connexion en POST vers /login.
    MvcResult result = mockMvc.perform(post("/login")
            .param("username", "admin")      // Simule le champ username du formulaire
            .param("password", "password"))  // Simule le champ password du formulaire
        .andExpect(status().is3xxRedirection()) // Vérifie qu'il y a une redirection HTTP
        .andExpect(redirectedUrl("/"))          // Vérifie que la redirection va vers "/"
        .andExpect(flash().attribute("successMessage", "Connexion réussie")) // Vérifie le message flash
        .andReturn();                           // Récupère le résultat complet de la requête

    // On récupère la session HTTP après la requête.
    HttpSession session = result.getRequest().getSession(false);

    // La session doit exister, car une connexion réussie doit créer/mettre à jour une session.
    assertNotNull(session);

    // On récupère l'objet stocké dans la session avec la clé "loggedUser".
    Object loggedUserObject = session.getAttribute("loggedUser");

    // L'objet doit bien exister.
    assertNotNull(loggedUserObject);

    // On vérifie que l'objet stocké est bien de type SessionUser.
    assertInstanceOf(SessionUser.class, loggedUserObject);

    // On caste l'objet pour pouvoir vérifier ses valeurs.
    SessionUser loggedUser = (SessionUser) loggedUserObject;

    // On vérifie que les données stockées en session correspondent au User authentifié.
    assertEquals(1L, loggedUser.getId());
    assertEquals("admin", loggedUser.getUsername());
    assertEquals("ADMIN", loggedUser.getRole());

    // On vérifie que le contrôleur a bien appelé le service d'authentification
    // avec les identifiants reçus depuis le formulaire.
    verify(authService).authenticate("admin", "password");
  }

  /**
   * Test du cas où l'utilisateur n'existe pas.
   *
   * Scénario :
   * - l'utilisateur envoie un username inconnu ;
   * - AuthService lève UserNotFoundException ;
   * - le contrôleur ajoute un message flash d'erreur ;
   * - l'utilisateur est redirigé vers /login ;
   * - aucun utilisateur n'est stocké en session.
   */
  @Test
  void processLogin_shouldRedirectToLogin_whenUserDoesNotExist() throws Exception {
    // Arrange
    // On simule une exception lancée par AuthService
    // lorsque les identifiants sont incorrects car l'utilisateur n'existe pas.
    when(authService.authenticate("unknown", "password"))
        .thenThrow(new UserNotFoundException("Utilisateur introuvable"));

    // Act + Assert
    MvcResult result = mockMvc.perform(post("/login")
            .param("username", "unknown")
            .param("password", "password"))
        .andExpect(status().is3xxRedirection()) // Vérifie qu'une redirection est faite
        .andExpect(redirectedUrl("/login"))     // Vérifie le retour vers la page login
        .andExpect(flash().attribute("errorMessage", "Utilisateur introuvable")) // Vérifie l'erreur flash
        .andReturn();

    // On récupère la session si elle existe.
    HttpSession session = result.getRequest().getSession(false);

    // Si une session existe, elle ne doit pas contenir d'utilisateur connecté.
    if (session != null) {
      assertNull(session.getAttribute("loggedUser"));
    }

    // Vérifie que le service a bien été appelé avec les paramètres du formulaire.
    verify(authService).authenticate("unknown", "password");
  }

  /**
   * Test du cas où le mot de passe est incorrect.
   *
   * Scénario :
   * - l'utilisateur existe ;
   * - le mot de passe est faux ;
   * - AuthService lève BadCredentialsException ;
   * - le contrôleur ajoute un message flash d'erreur ;
   * - l'utilisateur est redirigé vers /login ;
   * - aucun utilisateur n'est stocké en session.
   */
  @Test
  void processLogin_shouldRedirectToLogin_whenPasswordIsIncorrect() throws Exception {
    // Arrange
    // On simule une exception lancée par AuthService
    // lorsque le mot de passe est incorrect.
    when(authService.authenticate("admin", "wrong-password"))
        .thenThrow(new BadCredentialsException("Mot de passe incorrect"));

    // Act + Assert
    MvcResult result = mockMvc.perform(post("/login")
            .param("username", "admin")
            .param("password", "wrong-password"))
        .andExpect(status().is3xxRedirection()) // Vérifie la redirection
        .andExpect(redirectedUrl("/login"))     // Vérifie que l'utilisateur retourne sur /login
        .andExpect(flash().attribute("errorMessage", "Mot de passe incorrect")) // Vérifie le message d'erreur
        .andReturn();

    // On récupère la session si elle existe.
    HttpSession session = result.getRequest().getSession(false);

    // Si une session existe, elle ne doit pas contenir d'utilisateur connecté.
    if (session != null) {
      assertNull(session.getAttribute("loggedUser"));
    }

    // Vérifie que AuthService a bien été appelé avec les identifiants envoyés.
    verify(authService).authenticate("admin", "wrong-password");
  }

  /**
   * Test de la déconnexion.
   *
   * Scénario :
   * - une session contient un utilisateur connecté ;
   * - l'utilisateur appelle GET /logout ;
   * - le contrôleur invalide la session ;
   * - un message flash de succès est ajouté ;
   * - l'utilisateur est redirigé vers /login.
   */
  @Test
  void logout_shouldInvalidateSessionAndRedirectToLogin() throws Exception {
    // Act + Assert
    MvcResult result = mockMvc.perform(get("/logout")
            // On simule une session contenant déjà un utilisateur connecté.
            .sessionAttr("loggedUser", new SessionUser(1L, "admin", "ADMIN")))
        .andExpect(status().is3xxRedirection()) // Vérifie qu'il y a une redirection
        .andExpect(redirectedUrl("/login"))     // Vérifie que la redirection va vers /login
        .andExpect(flash().attribute("successMessage", "Déconnexion réussie")) // Vérifie le message flash
        .andReturn();

    // On récupère la session utilisée pendant la requête.
    HttpSession session = result.getRequest().getSession(false);

    // La référence de session existe encore côté test,
    // mais elle a normalement été invalidée.
    assertNotNull(session);

    // Une session invalidée ne peut plus être utilisée.
    // Appeler getAttribute dessus doit lancer IllegalStateException.
    assertThrows(IllegalStateException.class, () -> session.getAttribute("loggedUser"));
  }
}