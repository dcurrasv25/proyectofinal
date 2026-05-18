package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class LineaPedidoRequest {
    private int compra;
    private int perfume;
    private int cantidad;
    @SerializedName("precio_unitario")
    private double precioUnitario;

    public LineaPedidoRequest(int compra, int perfume, int cantidad, double precioUnitario) {
        this.compra = compra;
        this.perfume = perfume;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getCompra() { return compra; }
    public void setCompra(int compra) { this.compra = compra; }

    public int getPerfume() { return perfume; }
    public void setPerfume(int perfume) { this.perfume = perfume; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
