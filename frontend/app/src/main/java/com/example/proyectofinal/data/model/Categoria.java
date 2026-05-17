package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class Categoria {
    @SerializedName("id_categoria")
    private int id;
    private String nombre;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
