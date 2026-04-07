package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping
  public String listMovies(Model model) {
    model.addAttribute("movies", movieService.findAll());
    return "movies/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("movie", new Movie());
    return "movies/form";
  }

  @PostMapping("/save")
  public String saveMovie(@ModelAttribute Movie movie) {
    movieService.save(movie,"");
    return "redirect:/movies";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("movie", movieService.findById(id));
    return "movies/form";
  }

  @GetMapping("/delete/{id}")
  public String deleteMovie(@PathVariable Long id) {
    movieService.deleteById(id);
    return "redirect:/movies";
  }
}