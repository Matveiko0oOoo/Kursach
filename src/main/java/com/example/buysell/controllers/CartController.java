package com.example.buysell.controllers;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.CartService;
import com.example.buysell.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String viewCart(Model model, @AuthenticationPrincipal User user) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        double total = cartService.calculateTotal(cartItems);

        model.addAttribute("user", user != null ? user : new User());
        model.addAttribute("cart", cartItems);
        model.addAttribute("total", total);

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
}