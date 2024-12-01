package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the CartItem

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // The product in the cart

    @Column(name = "quantity", nullable = false)
    private int quantity; // Quantity of the product in the cart

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who owns the cart item

    // New constructor to accept product, quantity, and user
    public CartItem(Product product, int quantity, User user) {
        this.product = product;
        this.quantity = quantity;
        this.user = user;
    }
}