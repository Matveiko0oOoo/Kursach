package com.example.buysell.controllers;

import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.OrderService;
import com.example.buysell.services.UserService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("user", currentUser);
        }

        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        Map<String, Long> groupedProducts = orderService.getGroupedProductsByTitle();
        double totalRevenue = orderService.calculateTotalRevenue();
        int totalQuantity = orderService.calculateTotalQuantity();

        model.addAttribute("products", allOrderedProducts);
        model.addAttribute("groupedProducts", groupedProducts);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalQuantity", totalQuantity);

        return "all-orders";
    }

    @GetMapping("/orders/report")
    public void generateReport(HttpServletResponse response) throws IOException {
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        double totalRevenue = orderService.calculateTotalRevenue();

        try {
            orderService.generateOrderReport(allOrderedProducts, totalRevenue, response);
        } catch (DocumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Ошибка при генерации PDF отчета.");
        }
    }




}
