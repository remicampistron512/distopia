package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.MovieService;
import com.fms_ea.distopia.services.ShowingService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for showing management.
 * Handles public display and administration.
 */
@Controller
@RequestMapping("/showings")
@RequiredArgsConstructor
public class ShowingController {

  private final ShowingService showingService;
  private final CinemaService cinemaService;
  private final MovieService movieService;

  /**
   * Displays the list of showings.
   *
   * @param model view model
   * @return showing list view
   */
  @GetMapping
  public String listShowings(Model model) {
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("currentPage", "showings");
    return "showings/list";
  }

  /**
   * Displays the showing creation form.
   *
   * @param model view model
   * @return showing form view
   */
  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("showing", new Showing());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "showings");
    return "showings/form";
  }

  /**
   * Saves a showing.
   *
   * @param id showing id
   * @param startDateTime showing date and time
   * @param availableSeats available seats
   * @param price ticket price
   * @param cinemaId cinema id
   * @param movieId movie id
   * @param redirectAttributes flash messages
   * @return redirect to admin page
   */
  @PostMapping("/save")
  public String saveShowing(
      @RequestParam(required = false) Long id,
      @RequestParam String startDateTime,
      @RequestParam int availableSeats,
      @RequestParam double price,
      @RequestParam Long cinemaId,
      @RequestParam Long movieId,
      RedirectAttributes redirectAttributes) {

    Showing showing = (id != null && id != 0)
        ? showingService.findById(id)
        : new Showing();

    if (id != null && id != 0 && showing == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Séance introuvable");
      return "redirect:/showings/admin";
    }

    if (cinemaService.findById(cinemaId) == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Cinéma introuvable");
      return "redirect:/showings/admin";
    }

    if (movieService.findById(movieId) == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Film introuvable");
      return "redirect:/showings/admin";
    }

    try {
      showing.setStartDateTime(LocalDateTime.parse(startDateTime));
      showing.setAvailableSeats(availableSeats);
      showing.setPrice(BigDecimal.valueOf(price));
      showing.setCinema(cinemaService.findById(cinemaId));
      showing.setMovie(movieService.findById(movieId));

      showingService.save(showing);

      redirectAttributes.addFlashAttribute("successMessage", "Séance enregistrée");

    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement");
    }

    return "redirect:/showings/admin";
  }

  /**
   * Displays the showing edit form.
   *
   * @param id showing id
   * @param model view model
   * @param redirectAttributes flash messages
   * @return admin view or redirect
   */
  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Showing showing = showingService.findById(id);

    if (showing == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Séance introuvable");
      return "redirect:/showings/admin";
    }

    model.addAttribute("showing", showing);
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/showings");

    return "admin/showings";
  }

  /**
   * Deletes a showing.
   *
   * @param id showing id
   * @param redirectAttributes flash messages
   * @return redirect to admin page
   */
  @GetMapping("/delete/{id}")
  public String deleteShowing(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    Showing showing = showingService.findById(id);

    if (showing == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Séance introuvable");
      return "redirect:/showings/admin";
    }

    showingService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Séance supprimée");

    return "redirect:/showings/admin";
  }

  /**
   * Displays the showing administration page.
   *
   * @param model view model
   * @return admin showing view
   */
  @GetMapping("/admin")
  public String adminShowings(Model model) {
    model.addAttribute("showing", new Showing());
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/showings");
    return "admin/showings";
  }

  /**
   * Displays the admin edit form for a showing.
   *
   * @param id showing id
   * @param model view model
   * @param redirectAttributes flash messages
   * @return showing form or redirect
   */
  @GetMapping("/admin/edit/{id}")
  public String editShowingFromAdmin(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Showing showing = showingService.findById(id);

    if (showing == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Séance introuvable");
      return "redirect:/showings/admin";
    }

    model.addAttribute("showing", showing);
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/showings");

    return "showings/form";
  }
}