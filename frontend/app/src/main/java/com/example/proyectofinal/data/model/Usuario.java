package com.example.proyectofinal.data.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    @SerializedName("nombre_de_usuario")
    private String nombreDeUsuario;
    private String correo;
    @SerializedName("contrasena")
    private String contrasena;
    private String rol;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreDeUsuario() { return nombreDeUsuario; }
    public void setNombreDeUsuario(String nombreDeUsuario) { this.nombreDeUsuario = nombreDeUsuario; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
