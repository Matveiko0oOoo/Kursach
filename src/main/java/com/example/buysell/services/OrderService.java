package com.example.buysell.services;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.repositories.DeliveryRepository;
import com.example.buysell.repositories.ProductRepository;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.IOException;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(DeliveryRepository deliveryRepository, ProductRepository productRepository) {
        this.deliveryRepository = deliveryRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getAllOrderedProducts() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .flatMap(delivery -> delivery.getProductIds().stream())
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + productId + " не найден")))
                .toList();
    }

    public Map<String, Long> getGroupedProductsByTitle() {
        // Группируем товары по названию и подсчитываем их количество
        return getAllOrderedProducts().stream()
                .collect(Collectors.groupingBy(
                        Product::getTitle, // Ключ: название товара
                        Collectors.counting() // Значение: количество
                ));
    }

    public double calculateTotalRevenue() {
        return getAllOrderedProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public int calculateTotalQuantity() {
        return getAllOrderedProducts().size();
    }

    public void generateOrderReport(List<Product> products, double totalRevenue, HttpServletResponse response) throws DocumentException, IOException {
        // Настройка заголовков для PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.pdf\"");

        // Создаем новый документ PDF
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        // Открываем документ для записи
        document.open();

        // Загрузка кастомного шрифта
        BaseFont baseFont = BaseFont.createFont("fonts/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        // Создаем жирный шрифт
        Font boldFont = new Font(baseFont, 20, Font.BOLD);
        Font boldFont1 = new Font(baseFont, 15, Font.BOLD);

        // Добавляем заголовок с жирным шрифтом
        document.add(new Paragraph("BUYSELL", boldFont));

        // Добавляем разделитель
        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));

        // Заголовок отчета
        document.add(new Paragraph("Отчет по заказанным товарам", font));
        document.add(new Paragraph("Дата: " + java.time.LocalDate.now(), font));
        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));

        // Таблица товаров
        document.add(new Paragraph("Список товаров:", font));
        for (Product product : products) {
            document.add(new Paragraph("Название: " + product.getTitle() + " | Цена: " + product.getPrice() + " руб.", font));
        }

        // Итоговая информация
        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));
        document.add(new Paragraph("\n", font));
        document.add(new Paragraph("Общая прибыль: " + totalRevenue + " BYN", font));

        // Закрываем документ
        document.close();
    }


}

