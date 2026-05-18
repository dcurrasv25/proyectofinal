package com.example.proyectofinal.data.local;

import com.example.proyectofinal.data.model.Perfume;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Perfume> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Perfume perfume) {
        cartItems.add(perfume);
    }

    public void removeFromCart(Perfume perfume) {
        cartItems.remove(perfume);
    }

    public List<Perfume> getCartItems() {
        return cartItems;
    }
    
    public void clearCart() {
        cartItems.clear();
    }
}
