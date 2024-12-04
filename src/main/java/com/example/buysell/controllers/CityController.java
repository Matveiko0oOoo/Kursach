package com.example.buysell.controllers;

import com.example.buysell.models.City;
import com.example.buysell.models.User; // Импортируйте модель User
import com.example.buysell.services.CityService;
import com.example.buysell.services.UserService; // Импортируйте UserService
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cities")
public class CityController {
    private final CityService cityService;
    private final UserService userService; // Добавьте UserService

    @GetMapping
    public String showCities(Model model, Principal principal) {
        // Получаем пользователя по его email или имени
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user); // Добавляем пользователя в модель

        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);
        model.addAttribute("newCity", new City());
        return "manage-cities"; // Убедитесь, что этот шаблон существует
    }

    @PostMapping("/add")
    public String addCity(@ModelAttribute City newCity) {
        cityService.saveCity(newCity);
        return "redirect:/admin/cities";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Long id) {
        cityService.deleteCityById(id);
        return "redirect:/admin/cities";
    }

}