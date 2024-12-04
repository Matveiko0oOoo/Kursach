package com.example.buysell.controllers;

import com.example.buysell.models.PickUpPoint;
import com.example.buysell.models.City;
import com.example.buysell.models.User;
import com.example.buysell.services.PickUpPointService;
import com.example.buysell.services.ProductService;
import com.example.buysell.services.CityService; // Импортируем CityService
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PickUpPointController {
    private final PickUpPointService pickUpPointService;
    private final ProductService productService;
    private final CityService cityService; // Внедряем CityService

    @GetMapping("/pickup/points/add")
    public String showAddPickupPointForm(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        List<PickUpPoint> pickupPoints = pickUpPointService.getAllPickupPoints();
        model.addAttribute("pickupPoints", pickupPoints);

        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);

        return "add-pickup-point";
    }

    @PostMapping("/pickup/points/add")
    public String addPickupPoint(@ModelAttribute PickUpPoint pickUpPoint, @RequestParam("city.id") Long cityId, Model model, Principal principal) {
        if (pickUpPoint.getName() == null || pickUpPoint.getName().isEmpty()) {
            model.addAttribute("error", "Название пункта выдачи не может быть пустым.");
        } else {
            City city = cityService.getCityById(cityId);
            pickUpPoint.setCity(city);

            pickUpPointService.savePickupPoint(pickUpPoint);
        }
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        List<PickUpPoint> pickupPoints = pickUpPointService.getAllPickupPoints();
        model.addAttribute("pickupPoints", pickupPoints);

        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);

        return "add-pickup-point"; // Остаемся на той же странице
    }

    @GetMapping("/pickup/points/delete/{id}")
    public String deletePickupPoint(@PathVariable Long id, Principal principal, Model model) {
        pickUpPointService.deletePickupPointById(id);

        // Обновляем данные для отображения
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        List<PickUpPoint> pickupPoints = pickUpPointService.getAllPickupPoints();
        model.addAttribute("pickupPoints", pickupPoints);

        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);

        return "add-pickup-point"; // Остаемся на странице списка пунктов выдачи
    }


}