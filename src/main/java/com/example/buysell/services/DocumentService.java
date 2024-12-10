package com.example.buysell.services;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.repositories.CartItemRepository;
import com.example.buysell.repositories.ProductRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DocumentService {

    private final DeliveryService deliveryService;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Autowired
    public DocumentService(DeliveryService deliveryService, CartItemRepository cartItemRepository, ProductService productService) {
        this.deliveryService = deliveryService;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public InputStream generatePdfReceipt(Long deliveryId) {
        // Получаем доставку по ID
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);

        // Получаем список идентификаторов продуктов для этой доставки
        List<Long> productIds = delivery.getProductIds();

        // Получаем CartItem для этих идентификаторов
        List<CartItem> cartItems = cartItemRepository.findByIdInAndUser(productIds, delivery.getUser());

        // Получение продуктов по их ID
        List<Product> products = productService.findProductsByIds(productIds);

        // Создание PDF-документа
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            BaseFont baseFont = BaseFont.createFont("fonts/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 12);

            // Создаем жирный шрифт
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(baseFont, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font boldFont1 = new com.itextpdf.text.Font(baseFont, 15, com.itextpdf.text.Font.BOLD);

            document.add(new Paragraph("BUYSELL", boldFont));
            document.add(new Paragraph("Благодарим за доверие!", boldFont1));
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));
            document.add(new Paragraph("Название доставки: " + delivery.getDeliveryName(), font));
            document.add(new Paragraph("Дата отправления: " + delivery.getAdmissionDate(), font));
            document.add(new Paragraph("Ожидаемая дата прибытия: " + delivery.getArrivalDate(), font));

            document.add(new Paragraph("Список товаров:", font));
            double totalPrice = 0.0; // Переменная для хранения общей стоимости

            for (Product product : products) {
                double productPrice = product.getPrice();
                document.add(new Paragraph(product.getTitle() + " - " + productPrice + " BYN", font));
                totalPrice += productPrice; // Добавляем цену товара к общей стоимости
            }

            // Добавляем итоговую стоимость в документ
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));
            document.add(new Paragraph("Итого: " + String.format("%.2f BYN", totalPrice), boldFont1));

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Закрытие документа в случае ошибки
            try {
                document.close();
            } catch (Exception closeException) {
                closeException.printStackTrace();
            }
            return null; // Возвращаем null, если произошла ошибка
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}