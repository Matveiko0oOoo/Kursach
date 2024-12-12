package com.example.buysell.controllers;

import com.example.buysell.models.User;
import com.example.buysell.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Principal principal, Model model) {
        if (error != null) {
            model.addAttribute("error", true); // Устанавливаем флаг ошибки
        }
        if (principal != null) {
            return "redirect:/"; // Или на другую страницу
        }
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model){
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user, model)) {
            return "registration"; // Вернуться к форме регистрации с ошибкой
        }
        return "redirect:/login";
    }


    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal){
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products", user.getProducts());
        return "user-info";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }


    @GetMapping("/profile/edit/{id}")
    public String editProfile(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        if (user.getId().equals(id)) { // Убедитесь, что пользователь редактирует свой профиль
            model.addAttribute("user", user);
            return "edit-profile"; // Укажите шаблон для редактирования профиля
        }
        return "redirect:/profile"; // Если ID не совпадает, перенаправьте на профиль
    }

    @PostMapping("/profile/edit/{id}")
    public String updateUserProfile(@PathVariable Long id,
                                    @RequestParam String name,
                                    @RequestParam String numberPhone,
                                    @RequestParam("avatarImage") MultipartFile avatarImage,
                                    Principal principal, Model model) {
        Optional<User> updated = userService.updateUserProfile(id, name, numberPhone, avatarImage);
        if (updated.isPresent()) {
            return "redirect:/profile"; // Redirect after successful update
        } else {
            model.addAttribute("errorMessage", "Ошибка при обновлении профиля.");
            return "edit-profile"; // Return to edit form with error message
        }
    }
}
