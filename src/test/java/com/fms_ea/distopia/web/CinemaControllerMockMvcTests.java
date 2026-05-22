package com.fms_ea.distopia.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.CityService;
import java.util.List;
import java.io.File;
import javax.net.ssl.SSLEngineResult.Status;
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

@WebMvcTest(CinemaController.class)
public class CinemaControllerMockMvcTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CinemaService cinemaService;

  @MockitoBean
  private CityService cityService;

  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  @Test
  void listCinemas_shouldDisplayAllCinemas_ifNoKeywords() throws Exception {
    // Arrange
    List<Cinema> cinemas = List.of(new Cinema(), new Cinema());
    when(cinemaService.findAll()).thenReturn(cinemas);

    // Act + Assert
    mockMvc.perform(get("/cinemas"))
        .andExpect(status().isOk())

        .andExpect(view().name("cinemas/list"))

        .andExpect(model().attribute("currentPage", equalTo("cinemas")))

        .andExpect(model().attribute("cinemas", sameInstance(cinemas)));

    verify(cinemaService).findAll();
  }

  @Test
  void listCinemas_shouldDisplayFilteredCinemas_ifKeywords() throws Exception {
    // Arrange
    String kw = "UGC";
    List<Cinema> cinemas = List.of(new Cinema(), new Cinema());
    when(cinemaService.findByNameContains(kw)).thenReturn(cinemas);

    // Act + Assert
    mockMvc.perform(get("/cinemas").param("keyword", kw))
        .andExpect(status().isOk())

        .andExpect(view().name("cinemas/list"))

        .andExpect(model().attribute("currentPage", equalTo("cinemas")))

        .andExpect(model().attribute("cinemas", sameInstance(cinemas)));

    verify(cinemaService).findByNameContains(kw);

    verify(cinemaService, never()).findAll();
  }

  @Test
  void saveCinema_shouldSaveCinemaWithoutImage_ifImageIsEmpty() throws Exception {
    // Arrange
    MockMultipartFile emptyImage = new MockMultipartFile(
        "image",
        "",
        "image/jpeg",
        new byte[0]
    );

    // Act + Assert
    mockMvc.perform(multipart("/cinemas/save").file(emptyImage))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/cinemas/admin"))
        .andExpect(flash().attribute("successMessage", equalTo("Cinéma enregistré")));

    ArgumentCaptor<Cinema> cinemaCaptor = ArgumentCaptor.forClass(Cinema.class);
    verify(cinemaService).save(cinemaCaptor.capture());

    Cinema savedCinema = cinemaCaptor.getValue();
    assertEquals(null, savedCinema.getImageUrl());
  }

  @Test
  void saveCinema_shouldSaveCinemaWithImage_ifImageIsNotEmpty() throws Exception {
    // Arrange
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

    ArgumentCaptor<Cinema> cinemaCaptor = ArgumentCaptor.forClass(Cinema.class);
    verify(cinemaService).save(cinemaCaptor.capture());

    Cinema savedCinema = cinemaCaptor.getValue();

    assertTrue(savedCinema.getImageUrl().startsWith("/uploads/"));
    assertTrue(savedCinema.getImageUrl().endsWith("_cinema.jpg"));

    // Nettoyage du fichier créé pendant le test
    Path uploadedFilePath = Path.of(
        System.getProperty("user.dir"),
        savedCinema.getImageUrl().substring(1)
    );

    Files.deleteIfExists(uploadedFilePath);
  }

  @Test
  void saveCinema_shouldRedirectWithErrorMessage_ifImageUploadFails() throws Exception {
    // Arrange
    Cinema cinema = new Cinema();

    MultipartFile image = mock(MultipartFile.class);
    RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

    org.mockito.Mockito.when(image.isEmpty()).thenReturn(false);
    org.mockito.Mockito.when(image.getOriginalFilename()).thenReturn("cinema.jpg");

    doThrow(new IOException("Upload failed"))
        .when(image)
        .transferTo(any(File.class));

    CinemaController controller = new CinemaController(cinemaService, cityService);

    // Act
    String viewName = controller.saveCinema(cinema, image, redirectAttributes);

    // Assert
    assertEquals("redirect:/cinemas/admin", viewName);

    assertEquals(
        "Erreur lors de l'enregistrement de l'image",
        redirectAttributes.getFlashAttributes().get("errorMessage")
    );

    verify(cinemaService, never()).save(any(Cinema.class));
  }
}
