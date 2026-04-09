package com.fms_ea.distopia.controllers;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.services.CinemaService;
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
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

  private final CinemaService cinemaService;
  private final CityService cityService;

  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  @GetMapping
  public String listCinemas(
      @RequestParam(name = "keyword", defaultValue = "") String kw,
      Model model) {

    if (!kw.isEmpty()) {
      model.addAttribute("cinemas", cinemaService.findByNameContains(kw));
      model.addAttribute("keyword", kw);
    } else {
      model.addAttribute("cinemas", cinemaService.findAll());
    }

    model.addAttribute("currentPage", "cinemas");
    return "cinemas/list";
  }


  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("cinema", new Cinema());
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "cinemas");
    return "cinemas/form";
  }


  @GetMapping("/view/{id}")
  public String showCinema(@PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Cinema cinema = cinemaService.findByIdWithShowings(id);

    if (cinema == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Cinéma introuvable");
      return "redirect:/cinemas";
    }

    model.addAttribute("cinema", cinema);
    model.addAttribute("currentPage", "cinemas");
    return "cinemas/view";
  }


  @PostMapping("/save")
  public String saveCinema(
      @ModelAttribute Cinema cinema,
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

        cinema.setImageUrl("/uploads/" + fileName);
      }

      cinemaService.save(cinema);
      redirectAttributes.addFlashAttribute("successMessage", "Cinéma enregistré");

    } catch (IOException e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "Erreur lors de l'enregistrement de l'image");
    }

    return "redirect:/cinemas/admin";
  }


  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    Cinema cinema = cinemaService.findById(id);

    if (cinema == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Cinéma introuvable");
      return "redirect:/cinemas/admin";
    }

    model.addAttribute("cinema", cinema);
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "cinemas/form");

    return "cinemas/form";
  }


  @GetMapping("/delete/{id}")
  public String deleteCinema(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    Cinema cinema = cinemaService.findById(id);

    if (cinema == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Cinéma introuvable");
      return "redirect:/cinemas/admin";
    }

    cinemaService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Cinéma supprimé");

    return "redirect:/cinemas/admin";
  }


  @GetMapping("/admin")
  public String adminCinemas(Model model) {
    model.addAttribute("cinema", new Cinema());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "admin/cinemas");

    return "admin/cinemas";
  }
}