package com.example.buysell.controllers;

import com.example.buysell.models.*;
import com.example.buysell.repositories.PickUpPointRepository;
import com.example.buysell.services.CartService;
import com.example.buysell.services.DeliveryService;
import com.example.buysell.services.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final CartService cartService;
    private final PickUpPointRepository pickUpPointRepository;
    private final ProductService productService; // Добавлено поле для ProductService

    // Инициализация с помощью конструктора
    public DeliveryController(DeliveryService deliveryService, CartService cartService,
                              PickUpPointRepository pickUpPointRepository, ProductService productService) {
        this.deliveryService = deliveryService;
        this.cartService = cartService;
        this.pickUpPointRepository = pickUpPointRepository;
        this.productService = productService; // Инициализация ProductService
    }

    @GetMapping("/delivery")
    public String viewDeliveries(@AuthenticationPrincipal User user, Model model) {
        List<Delivery> deliveries = deliveryService.getDeliveries(user);

        Map<String, List<Product>> deliveryProducts = new HashMap<>();
        for (Delivery delivery : deliveries) {
            List<Product> products = productService.findProductsByIds(delivery.getProductIds());
            deliveryProducts.put(String.valueOf(delivery.getDeliveryId()), products);
        }

        model.addAttribute("user", user);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("deliveryProducts", deliveryProducts);
        return "delivery";
    }


    @PostMapping("/delivery/submit")
    public String submitOrder(@AuthenticationPrincipal User user,
                              @RequestParam("pickupPointId") Long pickupPointId,
                              @RequestParam("selectedItems") List<Long> selectedItems) {
        // Получаем пункт выдачи
        PickUpPoint pickupPoint = pickUpPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new IllegalArgumentException("Пункт выдачи не найден"));

        // Создаем доставку с выбранными товарами
        deliveryService.createDeliveryFromCart(user, pickupPoint, selectedItems);

        return "redirect:/delivery";
    }

    @GetMapping("/delivery/pickup-points")
    @ResponseBody
    public List<PickUpPoint> getPickupPoints(@RequestParam("cityId") Long cityId) {
        return deliveryService.getPickupPoints(cityId);
    }
}