package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.services.MovieService;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for movie management.
 * Handles public display, administration, and image upload.
 */
@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;
  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  /**
   * Displays the list of movies.
   *
   * @param model view model
   * @return movie list view
   */
  @GetMapping
  public String listMovies(Model model) {
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "movies");
    return "movies/list";
  }

  /**
   * Displays the movie creation form.
   *
   * @param model view model
   * @return movie form view
   */
  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("movie", new Movie());
    model.addAttribute("currentPage", "movies");
    return "movies/form";
  }

  /**
   * Displays movie details with showings.
   *
   * @param id movie id
   * @param model view model
   * @param redirectAttributes flash messages
   * @return movie detail view or redirect
   */
  @GetMapping("/view/{id}")
  public String showDetails(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Movie movie = movieService.findByIdWithShowings(id);

    if (movie == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Film introuvable");
      return "redirect:/movies";
    }

    model.addAttribute("movie", movie);
    model.addAttribute("currentPage", "movies");

    return "movies/view";
  }

  /**
   * Saves a movie.
   *
   * @param movie movie to save
   * @param actorsText actors list
   * @param image uploaded image
   * @param redirectAttributes flash messages
   * @return redirect to admin page
   */
  @PostMapping("/save")
  public String saveMovie(
      @ModelAttribute Movie movie,
      @RequestParam(required = false) String actorsText,
      @RequestParam("image") MultipartFile image,
      RedirectAttributes redirectAttributes) {

    try {

      if (!image.isEmpty()) {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
          dir.mkdirs();
        }

        File dest = new File(dir, fileName);
        image.transferTo(dest);

        movie.setImageUrl("/uploads/" + fileName);
      }

      movieService.save(movie, actorsText);
      redirectAttributes.addFlashAttribute("successMessage", "Film enregistré");

    } catch (IOException e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "Erreur lors de l'enregistrement de l'image");
    }

    return "redirect:/movies/admin";
  }

  /**
   * Displays the movie edit form from admin.
   *
   * @param id movie id
   * @param model view model
   * @param redirectAttributes flash messages
   * @return admin view or redirect
   */
  @GetMapping("/admin/edit/{id}")
  public String editMovieFromAdmin(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Movie movie = movieService.findById(id);

    if (movie == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Film introuvable");
      return "redirect:/movies/admin";
    }

    model.addAttribute("movie", movie);
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/movies");

    return "admin/movies";
  }

  /**
   * Displays the movie edit form.
   *
   * @param id movie id
   * @param model view model
   * @param redirectAttributes flash messages
   * @return movie form view or redirect
   */
  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Movie movie = movieService.findById(id);

    if (movie == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Film introuvable");
      return "redirect:/movies/admin";
    }

    model.addAttribute("movie", movie);
    model.addAttribute("currentPage", "movies");

    return "movies/form";
  }

  /**
   * Deletes a movie.
   *
   * @param id movie id
   * @param redirectAttributes flash messages
   * @return redirect to admin page
   */
  @GetMapping("/delete/{id}")
  public String deleteMovie(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    Movie movie = movieService.findById(id);

    if (movie == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Film introuvable");
      return "redirect:/movies/admin";
    }

    movieService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Film supprimé");

    return "redirect:/movies/admin";
  }

  /**
   * Displays the movie administration page.
   *
   * @param model view model
   * @return admin movie view
   */
  @GetMapping("/admin")
  public String adminMovies(Model model) {
    model.addAttribute("movie", new Movie());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/movies");

    return "admin/movies";
  }
}