package com.example.proyectofinal.data.local;

import com.example.proyectofinal.data.model.Perfume;

public class CartItem {
    private Perfume perfume;
    private int cantidad;

    public CartItem(Perfume perfume, int cantidad) {
        this.perfume = perfume;
        this.cantidad = cantidad;
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
