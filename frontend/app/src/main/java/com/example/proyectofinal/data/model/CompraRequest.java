package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class CompraRequest {
    private int usuario;

    public CompraRequest(int usuario) {
        this.usuario = usuario;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
