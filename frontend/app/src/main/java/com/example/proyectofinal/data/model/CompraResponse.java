package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class CompraResponse {
    @SerializedName("id_compra")
    private int idCompra;
    private int usuario;
    private String fecha;
    private java.util.List<LineaPedidoResponse> lineas;

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public java.util.List<LineaPedidoResponse> getLineas() {
        return lineas;
    }

    public void setLineas(java.util.List<LineaPedidoResponse> lineas) {
        this.lineas = lineas;
    }
}
