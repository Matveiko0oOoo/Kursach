package com.example.buysell.controllers;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.User;
import com.example.buysell.services.CartService;
import com.example.buysell.services.DeliveryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final CartService cartService;

    public DeliveryController(DeliveryService deliveryService, CartService cartService) {
        this.deliveryService = deliveryService;
        this.cartService = cartService;
    }

    @GetMapping("/delivery")
    public String viewDeliveries(@AuthenticationPrincipal User user, Model model) {
        List<Delivery> deliveries = deliveryService.getDeliveries(user);

        model.addAttribute("user", user);
        model.addAttribute("deliveries", deliveries);
        return "delivery";
    }


    @PostMapping("/delivery/submit")
    public String submitOrder(@AuthenticationPrincipal User user) {
        deliveryService.createDeliveryFromCart(user);
        cartService.clearCart(user);
        return "redirect:/delivery";
    }
}
