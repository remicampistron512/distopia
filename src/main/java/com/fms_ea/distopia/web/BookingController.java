package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.Booking;
import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.services.BookingService;
import com.fms_ea.distopia.services.CinemaService;
import com.fms_ea.distopia.services.CityService;
import com.fms_ea.distopia.services.ShowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
  private final ShowingService showingService;
  private final BookingService bookingService;
  private final CinemaService cinemaService;
  private final CityService cityService;

  /**
   * Display the booking creation form.
   *  @param model view model
   *  @return booking form view
   */

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("booking", new Booking());
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("currentPage", "bookings");
    return "bookings/form";
  }

  @PostMapping("/save")
  public String saveBooking(
      @ModelAttribute Booking booking,
      RedirectAttributes redirectAttributes
  ){
    try {
      bookingService.save(booking);
    } catch (RuntimeException e){
      e.printStackTrace();
      redirectAttributes.addFlashAttribute(
          "errorMessage",
          "Erreur lors de l'enregistrement de la reservation");
    }
    return "redirect:/bookings/admin";
  }

  @GetMapping("/admin")
  public String adminCinemas(Model model) {
    model.addAttribute("booking", new Booking());
    model.addAttribute("bookings", bookingService.findAll());
    model.addAttribute("showings", showingService.findAll());
    model.addAttribute("cinemas", cinemaService.findAll());
    model.addAttribute("cities", cityService.findAll());
    model.addAttribute("currentPage", "admin/bookings");

    return "admin/bookings";
  }

  @GetMapping("/delete/{id}")
  public String deleteBooking(
      @PathVariable Long id,
      RedirectAttributes redirectAttributes) {

    Booking booking = bookingService.findById(id);

    if (booking == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Réservation introuvable");
      return "redirect:/bookings/admin";
    }

    bookingService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Réservation supprimée");

    return "redirect:/bookings/admin";
  }
}

