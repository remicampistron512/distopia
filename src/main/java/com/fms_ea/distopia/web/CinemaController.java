package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

  private final CinemaService cinemaService;
  private final CityService cityService;

  @GetMapping
  public String listCinemas(Model model) {
    model.addAttribute("cinemas", cinemaService.findAll());
    return "cinemas/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("cinema", new Cinema());
    model.addAttribute("cities", cityService.findAll());
    return "cinemas/form";
  }

  @PostMapping("/save")
  public String saveCinema(@ModelAttribute Cinema cinema) {
    cinemaService.save(cinema);
    return "redirect:/cinemas";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("cinema", cinemaService.findById(id));
    model.addAttribute("cities", cityService.findAll());
    return "cinemas/form";
  }

  @GetMapping("/delete/{id}")
  public String deleteCinema(@PathVariable Long id) {
    cinemaService.deleteById(id);
    return "redirect:/cinemas";
  }
}