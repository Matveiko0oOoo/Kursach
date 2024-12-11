package com.example.buysell.services;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.repositories.DeliveryRepository;
import com.example.buysell.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        // Получаем все доставки
        List<Delivery> deliveries = deliveryRepository.findAll();

        // Получаем товары по ID из доставок
        return deliveries.stream()
                .flatMap(delivery -> delivery.getProductIds().stream()) // Получаем productId
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + productId + " не найден")))
                .toList();
    }
}
