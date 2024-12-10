package com.example.buysell.repositories;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    CartItem findByProductAndUser(Product product, User user);

    @Query("SELECT c FROM CartItem c WHERE c.id IN :ids AND c.user = :user")
    List<CartItem> findByIdInAndUser(@Param("ids") List<Long> ids, @Param("user") User user);

    List<CartItem> findByDeliveryAndUser(Delivery delivery, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.id IN :ids AND c.user = :user")
    void deleteSelectedItems(@Param("ids") List<Long> ids, @Param("user") User user);

}
