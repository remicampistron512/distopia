package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.User;
import com.fms_ea.distopia.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * Public list of users
   * @param model data passed to the view
   * @return users list view
   */
  @GetMapping
  public String listUsers(Model model) {
    List<User> users = userService.findAll();
    model.addAttribute("users", users);
    model.addAttribute("currentPage", "users");
    return "users/list";
  }

  /**
   * Admin page: form + users list
   * @param model data passed to the view
   * @return admin users page
   */
  @GetMapping("/admin")
  public String adminUsers(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("users", userService.findAll());
    model.addAttribute("currentPage", "users/admin");
    return "admin/users";
  }

  /**
   * Save a user from admin page
   * @param user the user object
   * @param bindingResult contains validation errors
   * @param model data passed to the view
   * @param redirectAttributes used to pass success messages after redirect
   * @return destination view
   */
  @PostMapping("/save")
  public String saveUser(
      @Valid User user,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("user", user);
      model.addAttribute("users", userService.findAll());
      model.addAttribute("currentPage", "users/admin");
      return "admin/users";
    }

    userService.save(user);
    redirectAttributes.addFlashAttribute("successMessage", "Utilisateur enregistré");
    return "redirect:/users/admin";
  }

  /**
   * Load a user into the admin form for editing
   * @param id the user id
   * @param model data passed to the view
   * @param redirectAttributes used to pass messages after redirect
   * @return admin users page
   */
  @GetMapping("/edit/{id}")
  public String showEditForm(
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes) {

    User user = userService.findById(id);

    if (user == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable");
      return "redirect:/users/admin";
    }

    model.addAttribute("user", user);
    model.addAttribute("users", userService.findAll());
    model.addAttribute("currentPage", "users/admin");
    return "users/form";
  }

  /**
   * Delete a user
   * @param id the user id
   * @param redirectAttributes used to pass messages after redirect
   * @return redirect to admin users page
   */
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    User user = userService.findById(id);

    if (user == null) {
      redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable");
      return "redirect:/users/admin";
    }

    userService.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Utilisateur supprimé");
    return "redirect:/users/admin";
  }
}