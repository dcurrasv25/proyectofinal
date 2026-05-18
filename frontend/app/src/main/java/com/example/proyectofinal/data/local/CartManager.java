package com.example.proyectofinal.data.local;

import com.example.proyectofinal.data.model.Perfume;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

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
        for (CartItem item : cartItems) {
            if (item.getPerfume().getId() == perfume.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(perfume, 1));
    }

    public void removeFromCart(Perfume perfume) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getPerfume().getId() == perfume.getId()) {
                CartItem item = cartItems.get(i);
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                } else {
                    cartItems.remove(i);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void clearCart() {
        cartItems.clear();
    }
}
