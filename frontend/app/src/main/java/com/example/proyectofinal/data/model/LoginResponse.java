package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;
    private int id;
    @SerializedName("nombre_de_usuario")
    private String nombreDeUsuario;
    private String rol;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreDeUsuario() { return nombreDeUsuario; }
    public void setNombreDeUsuario(String nombreDeUsuario) { this.nombreDeUsuario = nombreDeUsuario; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
