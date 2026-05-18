package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class LineaPedidoResponse {
    @SerializedName("id_linea")
    private int idLinea;
    private int cantidad;
    @SerializedName("precio_unitario")
    private double precioUnitario;
    @SerializedName("perfume_detalle")
    private Perfume perfumeDetalle;

    public int getIdLinea() { return idLinea; }
    public void setIdLinea(int idLinea) { this.idLinea = idLinea; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Perfume getPerfumeDetalle() { return perfumeDetalle; }
    public void setPerfumeDetalle(Perfume perfumeDetalle) { this.perfumeDetalle = perfumeDetalle; }
}
