package com.example.buysell.controllers;

import com.example.buysell.models.*;
import com.example.buysell.repositories.CityRepository;
import com.example.buysell.repositories.PickUpPointRepository;
import com.example.buysell.services.CartService;
import com.example.buysell.services.PickUpPointService;
import com.example.buysell.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PickUpPointService pickUpPointService;

    @Autowired
    private CityRepository cityRepository; // Добавляем поле для репозитория городов

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String viewCart(@AuthenticationPrincipal User user, Model model, @RequestParam(required = false) Long cityId) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        List<City> cities = cityRepository.findAll();
        List<PickUpPoint> pickupPoints = pickUpPointService.getAllPickupPoints(); // Получаем все пункты выдачи

        model.addAttribute("cart", cartItems);
        model.addAttribute("cities", cities);
        model.addAttribute("total", cartService.calculateTotal(cartItems));
        model.addAttribute("user", user);
        model.addAttribute("pickupPoints", pickupPoints);
        model.addAttribute("cityId", cityId != null ? cityId : ""); // Передача cityId в шаблон

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, @AuthenticationPrincipal User user, Model model) {
        Product product = productService.getProductById(productId);
        if (product != null && user != null) {
            cartService.addToCart(product, quantity, user);
        }
        return "redirect:/cart"; // Redirect to cart page
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long itemId, @AuthenticationPrincipal User user) {
        cartService.removeFromCart(itemId, user);
        return "redirect:/cart"; // Redirect to cart page
    }

    @PostMapping("/cart/clear")
    public String clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return "redirect:/cart"; // Redirect to cart page
    }

    @GetMapping("/cart/pickup-points")
    @ResponseBody
    public List<PickUpPoint> getPickUpPointsByCity(@RequestParam Long cityId) {
        if (cityId == null) {
            throw new IllegalArgumentException("cityId cannot be null");
        }
        return pickUpPointService.getPickUpPointsByCity(cityId);
    }


}