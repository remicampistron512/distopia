package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

  private final CityService cityService;

  @GetMapping
  public String listCities(Model model) {
    model.addAttribute("cities", cityService.findAll());
    return "cities/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("city", new City());
    return "cities/form";
  }

  @PostMapping("/save")
  public String saveCity(@ModelAttribute City city) {
    cityService.save(city);
    return "redirect:/cities";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("city", cityService.findById(id));
    return "cities/form";
  }

  @GetMapping("/delete/{id}")
  public String deleteCity(@PathVariable Long id) {
    cityService.deleteById(id);
    return "redirect:/cities";
  }
}