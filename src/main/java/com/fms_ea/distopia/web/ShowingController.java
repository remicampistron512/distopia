package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.MovieService;
import com.fms_ea.distopia.services.ShowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/showings")
@RequiredArgsConstructor
public class ShowingController {

  private final ShowingService showingService;
  private final CinemaService cinemaService;
  private final MovieService movieService;

  @GetMapping
  public String listShowings(Model model) {
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("currentPage", "showings");
    return "showings/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("showing", new Showing());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    return "showings/form";
  }

  @PostMapping("/save")
  public String saveShowing(@RequestParam(required = false) Long id,
      @RequestParam String startDateTime,
      @RequestParam int availableSeats,
      @RequestParam double price,
      @RequestParam Long cinemaId,
      @RequestParam Long movieId) {

    Showing showing = (id != null && id != 0) ? showingService.findById(id) : new Showing();

    showing.setStartDateTime(java.time.LocalDateTime.parse(startDateTime));
    showing.setAvailableSeats(availableSeats);
    showing.setPrice(java.math.BigDecimal.valueOf(price));
    showing.setCinema(cinemaService.findById(cinemaId));
    showing.setMovie(movieService.findById(movieId));

    showingService.save(showing);
    return "redirect:/showings/admin";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("showing", showingService.findById(id));
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    return "showings/form";
  }

  @GetMapping("/delete/{id}")
  public String deleteShowing(@PathVariable Long id) {
    showingService.deleteById(id);
    return "redirect:/showings";
  }

  @GetMapping("/admin")
  public String adminShowings(Model model) {
    model.addAttribute("showing", new Showing());
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    model.addAttribute("currentPage", "admin/showings");
    return "admin/showings";
  }

  @GetMapping("/admin/edit/{id}")
  public String editShowingFromAdmin(@PathVariable Long id, Model model) {
    model.addAttribute("showing", showingService.findById(id));
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("movies", movieService.findAll());
    return "admin/showings";
  }
}