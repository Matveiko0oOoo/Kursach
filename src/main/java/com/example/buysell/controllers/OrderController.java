package com.example.buysell.controllers;

import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.OrderService;
import com.example.buysell.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders/all")
    public String getAllOrders(Model model, Principal principal) {
        // Получение текущего пользователя
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("user", currentUser); // Передача пользователя в модель
        }

        // Получаем все заказанные товары
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        model.addAttribute("products", allOrderedProducts);

        return "all-orders"; // Шаблон отображения заказов
    }
}
