package com.example.buysell.repositories;

import com.example.buysell.models.Delivery;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUser(User user); // Ошибка здесь
    List<Delivery> findByIsIssuedFalse();
    List<Delivery> findByIsIssuedTrue();

    @Query("SELECT d FROM Delivery d WHERE d.isIssued = false ")
    List<Delivery> getAllDeliveries();

    Delivery findByProductIdsContains(Long productId);

}
