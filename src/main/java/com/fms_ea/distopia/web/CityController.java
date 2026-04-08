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

@Controller
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

  private final CityService cityService;
  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  @GetMapping
  public String listCities(Model model) {
    model.addAttribute("cities", cityService.findAll());
    return "cities/list";
  }

  @GetMapping("/view/{id}")
  public String showCreateForm(@PathVariable Long id, Model model) {
    model.addAttribute("city", cityService.findByIdWithCinemas(id));
    return "cities/view";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("city", new City());
    return "cities/form";
  }

  @PostMapping("/save")
  public String saveCity(@ModelAttribute City city, @RequestParam("image") MultipartFile image) {

    if (!image.isEmpty()) {
      try {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
          dir.mkdirs();
        }

        File dest = new File(dir, fileName);
        image.transferTo(dest);

        city.setImageUrl("/uploads/" + fileName);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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

  @GetMapping("/admin")
  public String adminCities(Model model) {
    model.addAttribute("city", new City());
    model.addAttribute("cities", cityService.findAll());
    return "admin/cities";
  }
}