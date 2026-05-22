package com.fms_ea.distopia.web;

// Imports statiques pour rendre les assertions MockMvc, Mockito et JUnit plus lisibles
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.CityService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

/**
 * Classe de test du contrôleur CinemaController.
 *
 * @WebMvcTest permet de tester uniquement la couche web Spring MVC,
 * sans charger toute l'application.
 *
 * Ici, on teste les routes du contrôleur CinemaController avec MockMvc.
 * Les services CinemaService et CityService sont remplacés par des mocks.
 */
@WebMvcTest(CinemaController.class)
public class CinemaControllerMockMvcTests {

  /**
   * MockMvc permet de simuler des requêtes HTTP vers le contrôleur,
   * par exemple GET /cinemas ou POST /cinemas/save.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Mock du service CinemaService.
   * On peut définir son comportement avec when(...).thenReturn(...)
   * et vérifier ses appels avec verify(...).
   */
  @MockitoBean
  private CinemaService cinemaService;

  /**
   * Mock du service CityService.
   * Il est nécessaire car CinemaController dépend probablement de ce service.
   */
  @MockitoBean
  private CityService cityService;

  /**
   * Chemin vers le dossier d'upload.
   * Ici, cette variable n'est pas utilisée directement dans les tests.
   */
  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  /**
   * Vérifie que la page /cinemas affiche tous les cinémas
   * lorsqu'aucun mot-clé de recherche n'est fourni.
   */
  @Test
  void listCinemas_shouldDisplayAllCinemas_ifNoKeywords() throws Exception {
    // Arrange : préparation des données de test
    List<Cinema> cinemas = List.of(new Cinema(), new Cinema());

    // On simule le comportement du service :
    // quand findAll() est appelé, il retourne la liste cinemas.
    when(cinemaService.findAll()).thenReturn(cinemas);

    // Act + Assert : exécution de la requête puis vérification du résultat
    mockMvc.perform(get("/cinemas"))
        .andExpect(status().isOk())
        .andExpect(view().name("cinemas/list"))
        .andExpect(model().attribute("currentPage", equalTo("cinemas")))
        .andExpect(model().attribute("cinemas", sameInstance(cinemas)));

    // Vérifie que la méthode findAll() du service a bien été appelée.
    verify(cinemaService).findAll();
  }

  /**
   * Vérifie que la page /cinemas affiche uniquement les cinémas filtrés
   * lorsqu'un mot-clé est fourni dans la requête.
   */
  @Test
  void listCinemas_shouldDisplayFilteredCinemas_ifKeywords() throws Exception {
    // Arrange
    String kw = "UGC";
    List<Cinema> cinemas = List.of(new Cinema(), new Cinema());

    // On simule une recherche par nom contenant le mot-clé "UGC".
    when(cinemaService.findByNameContains(kw)).thenReturn(cinemas);

    // Act + Assert
    mockMvc.perform(get("/cinemas").param("keyword", kw))
        .andExpect(status().isOk())
        .andExpect(view().name("cinemas/list"))
        .andExpect(model().attribute("currentPage", equalTo("cinemas")))
        .andExpect(model().attribute("cinemas", sameInstance(cinemas)));

    // Vérifie que la recherche filtrée a bien été appelée.
    verify(cinemaService).findByNameContains(kw);

    // Vérifie que findAll() n'a pas été appelé,
    // car on utilise un mot-clé de recherche.
    verify(cinemaService, never()).findAll();
  }

  /**
   * Vérifie que le contrôleur sauvegarde un cinéma sans image
   * lorsque le fichier image envoyé est vide.
   */
  @Test
  void saveCinema_shouldSaveCinemaWithoutImage_ifImageIsEmpty() throws Exception {
    // Arrange : création d'un faux fichier vide
    MockMultipartFile emptyImage = new MockMultipartFile(
        "image",
        "",
        "image/jpeg",
        new byte[0]
    );

    // Act + Assert : simulation d'un formulaire multipart envoyé vers /cinemas/save
    mockMvc.perform(multipart("/cinemas/save").file(emptyImage))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/cinemas/admin"))
        .andExpect(flash().attribute("successMessage", equalTo("Cinéma enregistré")));

    // ArgumentCaptor permet de récupérer l'objet Cinema passé à cinemaService.save(...)
    ArgumentCaptor<Cinema> cinemaCaptor = ArgumentCaptor.forClass(Cinema.class);
    verify(cinemaService).save(cinemaCaptor.capture());

    // On récupère le cinéma sauvegardé pour vérifier ses propriétés.
    Cinema savedCinema = cinemaCaptor.getValue();

    // Comme aucune image n'a été envoyée, l'imageUrl doit rester null.
    assertEquals(null, savedCinema.getImageUrl());
  }

  /**
   * Vérifie que le contrôleur sauvegarde un cinéma avec une image
   * lorsque le fichier image envoyé n'est pas vide.
   */
  @Test
  void saveCinema_shouldSaveCinemaWithImage_ifImageIsNotEmpty() throws Exception {
    // Arrange : création d'un faux fichier image
    MockMultipartFile image = new MockMultipartFile(
        "image",
        "cinema.jpg",
        "image/jpeg",
        "fake image content".getBytes()
    );

    // Act + Assert
    mockMvc.perform(multipart("/cinemas/save").file(image))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/cinemas/admin"))
        .andExpect(flash().attribute("successMessage", equalTo("Cinéma enregistré")));

    // Capture du Cinema passé au service save(...)
    ArgumentCaptor<Cinema> cinemaCaptor = ArgumentCaptor.forClass(Cinema.class);
    verify(cinemaService).save(cinemaCaptor.capture());

    Cinema savedCinema = cinemaCaptor.getValue();

    // Vérifie que l'URL de l'image commence bien par /uploads/
    assertTrue(savedCinema.getImageUrl().startsWith("/uploads/"));

    // Vérifie que le nom final du fichier se termine par _cinema.jpg.
    // Cela indique probablement que le contrôleur ajoute un préfixe unique au fichier.
    assertTrue(savedCinema.getImageUrl().endsWith("_cinema.jpg"));

    // Nettoyage du fichier créé pendant le test pour éviter de polluer le dossier uploads.
    Path uploadedFilePath = Path.of(
        System.getProperty("user.dir"),
        savedCinema.getImageUrl().substring(1)
    );

    Files.deleteIfExists(uploadedFilePath);
  }

  /**
   * Vérifie que le contrôleur redirige avec un message d'erreur
   * si l'enregistrement de l'image échoue.
   *
   * Ce test appelle directement la méthode du contrôleur au lieu d'utiliser MockMvc,
   * car on veut simuler une exception lors de image.transferTo(...).
   */
  @Test
  void saveCinema_shouldRedirectWithErrorMessage_ifImageUploadFails() throws Exception {
    // Arrange
    Cinema cinema = new Cinema();

    // Création d'un mock de MultipartFile pour contrôler son comportement.
    MultipartFile image = mock(MultipartFile.class);

    // RedirectAttributes permet de récupérer les messages flash ajoutés par le contrôleur.
    RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

    // On simule une image non vide.
    org.mockito.Mockito.when(image.isEmpty()).thenReturn(false);
    org.mockito.Mockito.when(image.getOriginalFilename()).thenReturn("cinema.jpg");

    // On force une IOException lorsque le contrôleur tente d'enregistrer le fichier.
    doThrow(new IOException("Upload failed"))
        .when(image)
        .transferTo(any(File.class));

    // Création manuelle du contrôleur avec ses dépendances mockées.
    CinemaController controller = new CinemaController(cinemaService, cityService);

    // Act : appel direct de la méthode à tester
    String viewName = controller.saveCinema(cinema, image, redirectAttributes);

    // Assert : le contrôleur doit rediriger vers la page d'administration.
    assertEquals("redirect:/cinemas/admin", viewName);

    // Vérifie que le bon message d'erreur a été ajouté aux flash attributes.
    assertEquals(
        "Erreur lors de l'enregistrement de l'image",
        redirectAttributes.getFlashAttributes().get("errorMessage")
    );

    // Vérifie que le cinéma n'est pas sauvegardé si l'upload de l'image échoue.
    verify(cinemaService, never()).save(any(Cinema.class));
  }
}