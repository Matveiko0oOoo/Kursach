package com.example.buysell.services;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Delivery;
import com.example.buysell.models.User;
import com.example.buysell.repositories.CartItemRepository;
import com.example.buysell.repositories.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class DeliveryService {

    private final CartItemRepository cartItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DeliveryService(CartItemRepository cartItemRepository, DeliveryRepository deliveryRepository) {
        this.cartItemRepository = cartItemRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public void createDeliveryFromCart(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Корзина пуста.");
        }

        Delivery delivery = new Delivery();
        delivery.setDeliveryName("Доставка №" + UUID.randomUUID());
        delivery.setAdmissionDate(LocalDate.now().format(formatter)); // Форматирование даты отправления
        delivery.setArrivalDate(LocalDate.now().plusDays(3).format(formatter)); // Форматирование даты прибытия
        delivery.setPlaceInStock(generateRandomPlaceInStock());
        delivery.setUser(user);
        delivery.setItems(cartItems);

        deliveryRepository.save(delivery);
    }

    private String generateRandomPlaceInStock() {
        Random random = new Random();
        int placeNumber = random.nextInt(200) + 1;
        return "Место на складе №" + placeNumber;
    }

    public List<Delivery> getDeliveries(User user) {
        return deliveryRepository.findByUser(user);
    }
}
