package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.services.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Classe de test du contrôleur HomeController avec MockMvc.
 *
 * Objectif :
 * - tester la route HTTP GET "/" ;
 * - vérifier que le contrôleur retourne la bonne vue ;
 * - vérifier que le modèle contient les bons attributs ;
 * - vérifier que HomeService est bien appelé.
 *
 * @WebMvcTest charge uniquement la couche web nécessaire au test.
 * Ici, Spring charge HomeController, MockMvc et les éléments MVC utiles,
 * mais il ne charge pas toute l'application.
 */
@WebMvcTest(HomeController.class)
class HomeControllerMockMvcTest {

  /**
   * MockMvc permet de simuler des requêtes HTTP sans lancer de vrai serveur.
   *
   * Exemple :
   * mockMvc.perform(get("/"))
   *
   * Cela permet de tester le contrôleur presque comme si une vraie requête
   * arrivait dans l'application.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Faux HomeService injecté dans le contexte Spring de test.
   *
   * HomeController dépend de HomeService.
   * Comme on teste uniquement le contrôleur, on remplace HomeService
   * par un mock Mockito.
   *
   * Cela évite d'appeler les vrais services, repositories ou la base de données.
   */
  @MockitoBean
  private HomeService homeService;

  /**
   * Test de la page d'accueil.
   *
   * Scénario :
   * - une requête GET est envoyée sur "/" ;
   * - HomeController appelle HomeService pour récupérer :
   *   - les films mis en avant ;
   *   - les cinémas principaux ;
   *   - les villes ;
   *   - les prochaines séances ;
   * - le contrôleur ajoute ces données au Model ;
   * - le contrôleur retourne la vue "home".
   */
  @Test
  void home_shouldDisplayHomePageWithModelAttributes() throws Exception {
    // Arrange
    // On prépare de fausses données qui seront retournées par le HomeService mocké.
    List<Movie> movies = List.of(new Movie(), new Movie());
    List<Cinema> cinemas = List.of(new Cinema());
    List<City> cities = List.of(new City(), new City());
    List<Showing> showings = List.of(new Showing());

    // Quand le contrôleur appellera homeService.getFeaturedMovies(),
    // Mockito retournera la liste movies préparée ci-dessus.
    when(homeService.getFeaturedMovies()).thenReturn(movies);

    // Quand le contrôleur appellera homeService.getTopCinemas(),
    // Mockito retournera la liste cinemas.
    when(homeService.getTopCinemas()).thenReturn(cinemas);

    // Quand le contrôleur appellera homeService.getAllCities(),
    // Mockito retournera la liste cities.
    when(homeService.getAllCities()).thenReturn(cities);

    // Quand le contrôleur appellera homeService.getUpcomingShowings(),
    // Mockito retournera la liste showings.
    when(homeService.getUpcomingShowings()).thenReturn(showings);

    // Act + Assert
    // On simule une requête HTTP GET sur l'URL "/".
    mockMvc.perform(get("/"))
        // Vérifie que la réponse HTTP est 200 OK.
        .andExpect(status().isOk())

        // Vérifie que le contrôleur retourne bien la vue "home".
        .andExpect(view().name("home"))

        // Vérifie que le Model contient les attributs attendus
        // et que leurs valeurs sont exactement les mêmes instances que leurs listes respectives

        .andExpect(model().attribute("movies", sameInstance(movies)))

        .andExpect(model().attribute("cinemas", sameInstance(cinemas)))

        .andExpect(model().attribute("cities", sameInstance(cities)))

        .andExpect(model().attribute("showings", sameInstance(showings)))

        // Vérifie que le Model contient "currentPage" avec la valeur "home".
        .andExpect(model().attribute("currentPage", equalTo("home")));

    // Vérifie que le contrôleur a bien demandé les films mis en avant au service.
    verify(homeService).getFeaturedMovies();

    // Vérifie que le contrôleur a bien demandé les cinémas principaux au service.
    verify(homeService).getTopCinemas();

    // Vérifie que le contrôleur a bien demandé toutes les villes au service.
    verify(homeService).getAllCities();

    // Vérifie que le contrôleur a bien demandé les prochaines séances au service.
    verify(homeService).getUpcomingShowings();
  }
}