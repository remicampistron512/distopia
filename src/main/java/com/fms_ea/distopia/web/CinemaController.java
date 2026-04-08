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

@Controller
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

  private final CinemaService cinemaService;
  private final CityService cityService;
  private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

  @GetMapping
  public String listCinemas(  @RequestParam(name = "keyword", defaultValue = "") String kw,Model model) {
    if(!kw.isEmpty()){
      model.addAttribute("cinemas", cinemaService.findByNameContains(kw));
    } else {
      model.addAttribute("cinemas", cinemaService.findAll());
    }

    return "cinemas/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("cinema", new Cinema());
    model.addAttribute("cities", cityService.findAll());
    return "cinemas/form";
  }

  @GetMapping("/view/{id}")
  public String showCreateForm(@PathVariable Long id, Model model) {
    model.addAttribute("cinema", cinemaService.findByIdWithShowings(id));
    return "cinemas/view";
  }

  @PostMapping("/save")
  public String saveCinema(@ModelAttribute Cinema cinema,
      @RequestParam("image") MultipartFile image) {

    if (!image.isEmpty()) {
      try {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
          dir.mkdirs();
        }

        File dest = new File(dir, fileName);
        image.transferTo(dest);

        cinema.setImageUrl("/uploads/" + fileName);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    cinemaService.save(cinema);
    return "redirect:/cinemas/admin";
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

  @GetMapping("/admin")
  public String adminCinemas(Model model) {
    model.addAttribute("cinema", new Cinema());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("cities", cityService.findAll());
    return "admin/cinemas";
  }
}