package com.example.proyectofinal.data.api;

import com.example.proyectofinal.data.model.Categoria;
import com.example.proyectofinal.data.model.LoginResponse;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.data.model.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("iniciar-sesion/")
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @POST("usuarios/")
    Call<Usuario> register(@Body Usuario usuario);

    @GET("perfumes/")
    Call<List<Perfume>> getPerfumes();

    @GET("perfumes/{id}/")
    Call<Perfume> getPerfume(@Path("id") int id);

    @GET("categorias/")
    Call<List<Categoria>> getCategorias();

    @GET("usuarios/{id}/favoritos/")
    Call<List<Perfume>> getFavoritos(@Path("id") int id);

    @POST("usuarios/{id}/favoritos/{perfume_id}/")
    Call<Void> addFavorito(@Path("id") int id, @Path("perfume_id") int perfumeId);

    @DELETE("usuarios/{id}/favoritos/{perfume_id}/")
    Call<Void> removeFavorito(@Path("id") int id, @Path("perfume_id") int perfumeId);

    @GET("categorias/{id}/perfumes/")
    Call<List<Perfume>> getPerfumesPorCategoria(@Path("id") int id);

    @POST("compras/")
    Call<com.example.proyectofinal.data.model.CompraResponse> crearCompra(@Body com.example.proyectofinal.data.model.CompraRequest request);

    @POST("lineas_pedido/")
    Call<Void> crearLineaPedido(@Body com.example.proyectofinal.data.model.LineaPedidoRequest request);

    @GET("usuarios/{id}/compras/")
    Call<List<com.example.proyectofinal.data.model.CompraResponse>> getCompras(@Path("id") int id);
}
