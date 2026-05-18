package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Perfume {
    @SerializedName("id_perfume")
    private int id;
    private String nombre;
    private String marca;
    private String tipo;
    private String genero;
    private double precio;
    private Integer categoria;
    private List<Integer> notas;
    private String imagen;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public Integer getCategoria() { return categoria; }
    public void setCategoria(Integer categoria) { this.categoria = categoria; }
    public List<Integer> getNotas() { return notas; }
    public void setNotas(List<Integer> notas) { this.notas = notas; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
