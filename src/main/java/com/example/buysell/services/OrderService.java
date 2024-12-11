package com.example.buysell.services;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.repositories.DeliveryRepository;
import com.example.buysell.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
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
        return getAllOrderedProducts().stream()
                .collect(Collectors.groupingBy(Product::getTitle, Collectors.counting()));
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
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        BaseFont baseFont = BaseFont.createFont("fonts/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);
        Font boldFont = new Font(baseFont, 20, Font.BOLD);
        Font boldFont1 = new Font(baseFont, 15, Font.BOLD);

        document.add(new Paragraph("BUYSELL", boldFont));
        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));
        document.add(new Paragraph("Отчет по заказанным товарам", font));
        document.add(new Paragraph("Дата: " + java.time.LocalDate.now(), font));
        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));

        document.add(new Paragraph("Список товаров:", font));
        for (Product product : products) {
            document.add(new Paragraph("Название: " + product.getTitle() + " | Цена: " + product.getPrice() + " руб.", font));
        }

        document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------", boldFont1));
        document.add(new Paragraph("\n", font));
        document.add(new Paragraph("Общая прибыль: " + totalRevenue + " BYN", font));

        document.close();
    }

    public void generateOrderReportXml(List<Product> products, double totalRevenue, HttpServletResponse response) throws XMLStreamException, IOException {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.xml\"");

        OutputStream outputStream = response.getOutputStream();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);

        xmlWriter.writeStartDocument("UTF-8", "1.0");
        xmlWriter.writeStartElement("orderReport");

        xmlWriter.writeStartElement("reportDate");
        xmlWriter.writeCharacters(java.time.LocalDate.now().toString());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("products");
        for (Product product : products) {
            xmlWriter.writeStartElement("product");

            xmlWriter.writeStartElement("title");
            xmlWriter.writeCharacters(product.getTitle());
            xmlWriter.writeEndElement();

            xmlWriter.writeStartElement("price");
            xmlWriter.writeCharacters(String.valueOf(product.getPrice()));
            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("totalRevenue");
        xmlWriter.writeCharacters(String.valueOf(totalRevenue));
        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();

        xmlWriter.flush();
        xmlWriter.close();
    }

    public void generateOrderJsonReport(List<Delivery> deliveries, List<Product> products, HttpServletResponse response) throws IOException {
        // Настройка ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Создаем корневой объект JSON
        ObjectNode rootNode = objectMapper.createObjectNode();

        // Сериализация доставок
        ArrayNode deliveriesNode = objectMapper.createArrayNode();
        for (Delivery delivery : deliveries) {
            ObjectNode deliveryNode = objectMapper.createObjectNode();
            deliveryNode.put("deliveryId", delivery.getDeliveryId());
            deliveryNode.put("deliveryName", delivery.getDeliveryName());
            deliveryNode.put("arrivalDate", delivery.getArrivalDate());
            deliveryNode.put("admissionDate", delivery.getAdmissionDate());
            deliveryNode.put("placeInStock", delivery.getPlaceInStock());

            // Список id товаров
            ArrayNode productIdsNode = objectMapper.createArrayNode();
            for (Long productId : delivery.getProductIds()) {
                productIdsNode.add(productId);
            }
            deliveryNode.set("productIds", productIdsNode);

            // Добавляем доставку в список
            deliveriesNode.add(deliveryNode);
        }

        rootNode.set("deliveries", deliveriesNode);

        // Сериализация всех продуктов (не только тех, которые есть в доставках)
        ArrayNode productsNode = objectMapper.createArrayNode();
        for (Product product : products) {
            ObjectNode productNode = objectMapper.createObjectNode();
            productNode.put("productId", product.getId());
            productNode.put("title", product.getTitle());
            productNode.put("price", product.getPrice());

            // Не включаем поле `bytes`
            productsNode.add(productNode);
        }

        rootNode.set("products", productsNode);

        // Устанавливаем заголовки для JSON
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.json\"");

        // Записываем JSON в ответ
        objectMapper.writeValue(response.getOutputStream(), rootNode);
    }


    public static class OrderReport {
        private List<Delivery> deliveries;
        private List<Product> products;

        public OrderReport(List<Delivery> deliveries, List<Product> products) {
            this.deliveries = deliveries;
            this.products = products;
        }

        public List<Delivery> getDeliveries() {
            return deliveries;
        }

        public void setDeliveries(List<Delivery> deliveries) {
            this.deliveries = deliveries;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public void generateOrderReportDocx(List<Product> products, double totalRevenue, HttpServletResponse response) throws IOException {
        // Создаем новый документ DOCX
        XWPFDocument document = new XWPFDocument();

        // Добавляем заголовок
        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("BUYSELL - Отчет по заказанным товарам");
        titleRun.setBold(true);
        titleRun.setFontSize(16);

        // Добавляем дату
        XWPFParagraph dateParagraph = document.createParagraph();
        XWPFRun dateRun = dateParagraph.createRun();
        dateRun.setText("Дата отчета: " + java.time.LocalDate.now().toString());
        dateRun.setFontSize(12);

        // Разделитель
        document.createParagraph().createRun().addBreak();

        // Добавляем список товаров
        XWPFParagraph productHeader = document.createParagraph();
        XWPFRun productHeaderRun = productHeader.createRun();
        productHeaderRun.setText("Список товаров:");
        productHeaderRun.setBold(true);

        for (Product product : products) {
            XWPFParagraph productParagraph = document.createParagraph();
            XWPFRun productRun = productParagraph.createRun();
            productRun.setText("Название: " + product.getTitle() + " | Цена: " + product.getPrice() + " руб.");
        }

        // Разделитель
        document.createParagraph().createRun().addBreak();

        // Добавляем итоговую прибыль
        XWPFParagraph totalRevenueParagraph = document.createParagraph();
        XWPFRun totalRevenueRun = totalRevenueParagraph.createRun();
        totalRevenueRun.setText("Общая прибыль: " + totalRevenue + " BYN");

        // Устанавливаем заголовки для DOCX
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.docx\"");

        // Записываем документ в поток ответа
        document.write(response.getOutputStream());

        // Закрываем документ
        document.close();
    }

    public void generateOrderReportXlsx(List<Product> products, double totalRevenue, HttpServletResponse response) throws IOException {
        // Создаем новый рабочий лист Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Report");

        // Создаем строку заголовков
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Название товара");

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Цена");

        // Заполняем данными
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getTitle());
            row.createCell(1).setCellValue(product.getPrice());
        }

        // Добавляем итоговую прибыль в конец
        Row totalRow = sheet.createRow(rowNum++);
        totalRow.createCell(0).setCellValue("Общая прибыль");
        totalRow.createCell(1).setCellValue(totalRevenue);

        // Устанавливаем заголовки для ответа
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"order_report.xlsx\"");

        // Записываем Excel файл в поток ответа
        workbook.write(response.getOutputStream());

        // Закрываем рабочую книгу
        workbook.close();
    }

}
