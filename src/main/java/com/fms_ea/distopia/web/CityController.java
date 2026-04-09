package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.services.CityService;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

  private final CityService cityService;
  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  @GetMapping
  public String listCities(Model model) {
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "cities");
    return "cities/list";
  }

  @GetMapping("/view/{id}")
  public String showCity(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    City city = cityService.findByIdWithCinemas(id);

    if (city == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Ville introuvable");
      return "redirect:/cities";
    }

    model.addAttribute("city", city);
    model.addAttribute("currentPage", "cities");

    return "cities/view";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("city", new City());
    model.addAttribute("currentPage", "cities");
    return "cities/form";
  }

  @PostMapping("/save")
  public String saveCity(
      @ModelAttribute City city,
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

        city.setImageUrl("/uploads/" + fileName);
      }

      cityService.save(city);
      redirectAttributes.addFlashAttribute("successMessage", "Ville enregistrée");

    } catch (IOException e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "Erreur lors de l'enregistrement de l'image");
    }

    return "redirect:/cities/admin";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    City city = cityService.findById(id);

    if (city == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Ville introuvable");
      return "redirect:/cities/admin";
    }

    model.addAttribute("city", city);
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "admin/cities");

    return "admin/cities";
  }

  @GetMapping("/delete/{id}")
  public String deleteCity(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    City city = cityService.findById(id);

    if (city == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Ville introuvable");
      return "redirect:/cities/admin";
    }

    cityService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Ville supprimée");

    return "redirect:/cities/admin";
  }

  @GetMapping("/admin")
  public String adminCities(Model model) {
    model.addAttribute("city", new City());
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "admin/cities");

    return "admin/cities";
  }
}
