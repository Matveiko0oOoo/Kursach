package com.example.buysell.repositories;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    CartItem findByProductAndUser(Product product, User user);
    void deleteById(Long id);
}