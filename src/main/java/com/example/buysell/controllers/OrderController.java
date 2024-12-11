package com.example.buysell.controllers;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.OrderService;
import com.example.buysell.services.UserService;
import com.example.buysell.services.DeliveryService;
import com.example.buysell.services.ProductService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final DeliveryService deliveryService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService,
                           DeliveryService deliveryService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.deliveryService = deliveryService;
        this.productService = productService;
    }

    @GetMapping("/orders/all")
    public String getAllOrders(Model model, Principal principal) {
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("user", currentUser);
        }

        // Получаем все заказанные продукты
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        // Группируем продукты по названию и считаем их количество
        Map<String, Long> groupedProducts = orderService.getGroupedProductsByTitle();
        // Рассчитываем общую прибыль и количество товаров
        double totalRevenue = orderService.calculateTotalRevenue();
        int totalQuantity = orderService.calculateTotalQuantity();

        // Добавляем в модель для отображения на странице
        model.addAttribute("products", allOrderedProducts);
        model.addAttribute("groupedProducts", groupedProducts);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalQuantity", totalQuantity);

        return "all-orders";
    }

    @GetMapping("/orders/report/pdf")
    public void generateReport(HttpServletResponse response) throws IOException {
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        double totalRevenue = orderService.calculateTotalRevenue();

        try {
            // Генерация PDF отчета
            orderService.generateOrderReport(allOrderedProducts, totalRevenue, response);
        } catch (DocumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Ошибка при генерации PDF отчета.");
        }
    }

    @GetMapping("/orders/report/xml")
    public void downloadXmlReport(HttpServletResponse response) throws XMLStreamException, IOException {
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        double totalRevenue = orderService.calculateTotalRevenue();

        // Генерация XML отчета
        orderService.generateOrderReportXml(allOrderedProducts, totalRevenue, response);
    }

    @GetMapping("/orders/report/json")
    public void downloadJsonReport(HttpServletResponse response) throws IOException {
        // Получаем все доставки и все продукты
        List<Delivery> deliveries = deliveryService.getAllDeliveries();  // Получение всех доставок
        List<Product> products = productService.getAllProducts(); // Получение всех продуктов

        // Генерация JSON отчета
        orderService.generateOrderJsonReport(deliveries, products, response);
    }

    @GetMapping("/orders/report/docx")
    public void downloadDocxReport(HttpServletResponse response) throws IOException {
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        double totalRevenue = orderService.calculateTotalRevenue();

        // Генерация DOCX отчета
        orderService.generateOrderReportDocx(allOrderedProducts, totalRevenue, response);
    }

    @GetMapping("/orders/report/xlsx")
    public void downloadXlsxReport(HttpServletResponse response) throws IOException {
        List<Product> allOrderedProducts = orderService.getAllOrderedProducts();
        double totalRevenue = orderService.calculateTotalRevenue();

        // Генерация Excel отчета
        orderService.generateOrderReportXlsx(allOrderedProducts, totalRevenue, response);
    }

    @GetMapping("/orders/analytics")
    public ResponseEntity<Map<String, Object>> getAnalyticsData() {
        // Получаем сгруппированные данные о продуктах
        Map<String, Long> groupedProducts = orderService.getGroupedProductsByTitle();

        // Формируем данные для диаграммы
        Map<String, Object> response = new HashMap<>();
        response.put("labels", groupedProducts.keySet());
        response.put("values", groupedProducts.values());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/sales-analytics")
    public ResponseEntity<Map<String, Object>> getSalesAnalyticsData() {
        // Получаем данные о продажах по дням
        Map<String, Integer> salesByDate = orderService.getSalesByDate();

        // Формируем данные для графика
        Map<String, Object> response = new HashMap<>();
        response.put("dates", salesByDate.keySet());
        response.put("sales", salesByDate.values());

        return ResponseEntity.ok(response);
    }


}
