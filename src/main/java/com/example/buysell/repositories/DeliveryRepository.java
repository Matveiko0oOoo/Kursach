package com.example.buysell.repositories;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUser(User user); // Ошибка здесь
}
