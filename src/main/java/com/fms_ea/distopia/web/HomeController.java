package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.services.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("movies", homeService.getFeaturedMovies());
    model.addAttribute("cinemas", homeService.getTopCinemas());
    model.addAttribute("cities", homeService.getAllCities());
    model.addAttribute("showings", homeService.getUpcomingShowings());
    model.addAttribute("currentPage", "home");
    return "home";
  }
}