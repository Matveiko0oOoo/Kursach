package com.example.buysell.repositories;

import com.example.buysell.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByDeliveryName(String deliveryName);
    List<Delivery> findByArrivalDate(LocalDate arrivalDate);
    List<Delivery> findByPlaceInStock(String placeInStock);
}