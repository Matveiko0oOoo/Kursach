package com.example.buysell.services;

import com.example.buysell.models.*;
import com.example.buysell.repositories.CartItemRepository;
import com.example.buysell.repositories.CityRepository;
import com.example.buysell.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
    private final CartItemRepository cartItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final CityRepository cityRepository;
    private final ProductService productService; // добавлено поле
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public DeliveryService(CartItemRepository cartItemRepository,
                           DeliveryRepository deliveryRepository,
                           CityRepository cityRepository,
                           ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.cityRepository = cityRepository;
        this.productService = productService; // инициализация
    }

    @Transactional
    public void createDeliveryFromCart(User user, PickUpPoint pickupPoint, List<Long> selectedItemIds) {
        // Извлекаем элементы корзины по их ID
        List<CartItem> selectedCartItems = cartItemRepository.findByIdInAndUser(selectedItemIds, user);

        // Получаем список productId из CartItem
        List<Long> productIds = selectedCartItems.stream()
                .map(cartItem -> cartItem.getProduct().getId())
                .toList();

        // Создаем объект доставки
        Delivery delivery = new Delivery();
        delivery.setDeliveryName("Доставка №" + UUID.randomUUID());
        delivery.setUser(user);
        delivery.setPickUpPoint(pickupPoint);
        delivery.setProductIds(productIds);

        // Устанавливаем даты отправки и прибытия
        int randomDaysToArrival = ThreadLocalRandom.current().nextInt(3, 6); // Диапазон: от 3 до 5 дней
        delivery.setAdmissionDate(LocalDate.now().format(formatter)); // Текущая дата
        delivery.setArrivalDate(LocalDate.now().plusDays(randomDaysToArrival).format(formatter)); // Дата прибытия

        // Генерируем случайное место на складе
        delivery.setPlaceInStock(generateRandomPlaceInStock());

        // Сохраняем доставку
        deliveryRepository.save(delivery);

        // Удаляем выбранные товары из корзины
        cartItemRepository.deleteSelectedItems(selectedItemIds, user);
    }

    public List<PickUpPoint> getPickupPoints(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("Город не найден"));
        return city.getPickUpPoints(); // Предполагаем, что у города есть связь с пунктами выдачи
    }

    private String generateRandomPlaceInStock() {
        Random random = new Random();
        int placeNumber = random.nextInt(200) + 1;
        return "Место на складе №" + placeNumber;
    }

    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Доставка не найдена с id: " + deliveryId));
    }

    public List<Delivery> getDeliveries(User user) {
        return deliveryRepository.findByUser(user);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll(); // Получаем все доставки
    }
}