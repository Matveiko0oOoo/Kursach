package com.example.buysell.services;

import com.example.buysell.models.CartItem;
import com.example.buysell.models.Delivery;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user); // Fetch cart items for the specific user
    }

    public double calculateTotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void addToCart(Product product, int quantity, User user) {
        CartItem existingCartItem = cartItemRepository.findByProductAndUser(product, user);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem); // Update existing item
        } else {
            CartItem newCartItem = new CartItem(product, quantity, user);
            cartItemRepository.save(newCartItem); // Save new item
        }
    }

    public void clearCart(User user) {
        List<CartItem> userCartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(userCartItems);
    }

    public void removeFromCart(Long itemId, User user) {
        cartItemRepository.deleteById(itemId); // Remove the cart item by ID
    }

    public List<CartItem> getCartItemsByDelivery(Delivery delivery, User user) {
        return cartItemRepository.findByDeliveryAndUser(delivery, user);  // Получаем товары корзины по delivery и user
    }

}