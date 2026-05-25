package com.example.proyectofinal.ui;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.local.CartManager;
import com.example.proyectofinal.data.model.Perfume;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfumeDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImagen;
    private TextView tvDetailNombre, tvDetailMarca, tvDetailPrecio, tvDetailTipo, tvDetailGenero;
    private Button btnComprar;
    private ImageButton btnFavorito, btnEditar;
    private Perfume currentPerfume;
    private boolean isFavorito = false;
    private int perfumeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume_detail);

        ivDetailImagen = findViewById(R.id.ivDetailImagen);
        tvDetailNombre = findViewById(R.id.tvDetailNombre);
        tvDetailMarca = findViewById(R.id.tvDetailMarca);
        tvDetailPrecio = findViewById(R.id.tvDetailPrecio);
        tvDetailTipo = findViewById(R.id.tvDetailTipo);
        tvDetailGenero = findViewById(R.id.tvDetailGenero);
        btnComprar = findViewById(R.id.btnComprar);
        btnFavorito = findViewById(R.id.btnFavorito);
        btnEditar = findViewById(R.id.btnEditar);

        // Get data from intent
        perfumeId = getIntent().getIntExtra("PERFUME_ID", -1);
        String nombre = getIntent().getStringExtra("PERFUME_NOMBRE");
        String marca = getIntent().getStringExtra("PERFUME_MARCA");
        double precio = getIntent().getDoubleExtra("PERFUME_PRECIO", 0.0);
        String imagen = getIntent().getStringExtra("PERFUME_IMAGEN");
        String tipo = getIntent().getStringExtra("PERFUME_TIPO");
        String genero = getIntent().getStringExtra("PERFUME_GENERO");

        // Reconstruct perfume for cart
        currentPerfume = new Perfume();
        currentPerfume.setId(perfumeId);
        currentPerfume.setNombre(nombre);
        currentPerfume.setMarca(marca);
        currentPerfume.setPrecio(precio);
        currentPerfume.setImagen(imagen);
        currentPerfume.setTipo(tipo);
        currentPerfume.setGenero(genero);

        // Set data to views
        tvDetailNombre.setText(nombre != null ? nombre : "Desconocido");
        tvDetailMarca.setText(marca != null ? marca : "Desconocido");
        tvDetailPrecio.setText(String.format("%.2f €", precio));
        tvDetailTipo.setText("Tipo: " + (tipo != null ? tipo : ""));
        tvDetailGenero.setText("Género: " + (genero != null ? genero : ""));

        if (imagen != null && !imagen.isEmpty()) {
            Glide.with(this)
                    .load(imagen)
                    .into(ivDetailImagen);
        }

        // Inicializar la estrella en gris y comprobar si es favorito
        actualizarColorEstrella();
        comprobarSiEsFavorito(perfumeId);

        // Mostrar botón de edición si el usuario es administrador
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String rol = prefs.getString("rol", "usuario");
        if ("admin".equals(rol)) {
            btnEditar.setVisibility(android.view.View.VISIBLE);
            btnEditar.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(PerfumeDetailActivity.this, com.example.proyectofinal.ui.home.AddPerfumeActivity.class);
                intent.putExtra("PERFUME_ID", perfumeId);
                intent.putExtra("IS_EDIT_MODE", true);
                startActivity(intent);
            });
        }

        btnComprar.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(currentPerfume);
            Toast.makeText(this, currentPerfume.getNombre() + " añadido al carrito", Toast.LENGTH_SHORT).show();
        });

        btnFavorito.setOnClickListener(v -> {
            int userId = prefs.getInt("user_id", -1);
            if (userId != -1) {
                if (isFavorito) {
                    // Quitar de favoritos
                    RetrofitClient.getApiService().removeFavorito(userId, perfumeId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                isFavorito = false;
                                actualizarColorEstrella();
                                Toast.makeText(PerfumeDetailActivity.this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PerfumeDetailActivity.this, "Error al eliminar favorito", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(PerfumeDetailActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Añadir a favoritos
                    RetrofitClient.getApiService().addFavorito(userId, perfumeId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                isFavorito = true;
                                actualizarColorEstrella();
                                Toast.makeText(PerfumeDetailActivity.this, "Guardado en favoritos", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PerfumeDetailActivity.this, "Error al guardar favorito", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(PerfumeDetailActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Inicia sesión para guardar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (perfumeId != -1) {
            cargarDetallesPerfume(perfumeId);
        }
    }

    private void cargarDetallesPerfume(int id) {
        RetrofitClient.getApiService().getPerfume(id).enqueue(new Callback<Perfume>() {
            @Override
            public void onResponse(Call<Perfume> call, Response<Perfume> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentPerfume = response.body();
                    tvDetailNombre.setText(currentPerfume.getNombre());
                    tvDetailMarca.setText(currentPerfume.getMarca());
                    tvDetailPrecio.setText(String.format("%.2f €", currentPerfume.getPrecio()));
                    tvDetailTipo.setText("Tipo: " + (currentPerfume.getTipo() != null ? currentPerfume.getTipo() : ""));
                    tvDetailGenero.setText("Género: " + (currentPerfume.getGenero() != null ? currentPerfume.getGenero() : ""));
                    if (currentPerfume.getImagen() != null && !currentPerfume.getImagen().isEmpty()) {
                        Glide.with(PerfumeDetailActivity.this)
                                .load(currentPerfume.getImagen())
                                .into(ivDetailImagen);
                    }
                }
            }

            @Override
            public void onFailure(Call<Perfume> call, Throwable t) {
                // Fallo silencioso
            }
        });
    }

    /**
     * Comprueba si el perfume actual está en la lista de favoritos del usuario.
     */
    private void comprobarSiEsFavorito(int perfumeId) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId == -1) return;

        RetrofitClient.getApiService().getFavoritos(userId).enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Perfume p : response.body()) {
                        if (p.getId() == perfumeId) {
                            isFavorito = true;
                            actualizarColorEstrella();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                // Si falla la comprobación, la estrella se queda gris
            }
        });
    }

    /**
     * Cambia el color de la estrella: amarillo si es favorito, gris si no lo es.
     */
    private void actualizarColorEstrella() {
        if (isFavorito) {
            btnFavorito.setColorFilter(
                    ContextCompat.getColor(this, R.color.black),
                    PorterDuff.Mode.SRC_IN);
        } else {
            btnFavorito.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_hint),
                    PorterDuff.Mode.SRC_IN);
        }
    }
}
