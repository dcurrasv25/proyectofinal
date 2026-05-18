package com.example.proyectofinal.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.local.CartManager;
import com.example.proyectofinal.data.model.Perfume;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfumeDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImagen;
    private TextView tvDetailNombre, tvDetailMarca, tvDetailPrecio, tvDetailTipo, tvDetailGenero;
    private Button btnComprar;
    private ImageButton btnFavorito;
    private Perfume currentPerfume;

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

        // Get data from intent
        int id = getIntent().getIntExtra("PERFUME_ID", -1);
        String nombre = getIntent().getStringExtra("PERFUME_NOMBRE");
        String marca = getIntent().getStringExtra("PERFUME_MARCA");
        double precio = getIntent().getDoubleExtra("PERFUME_PRECIO", 0.0);
        String imagen = getIntent().getStringExtra("PERFUME_IMAGEN");
        String tipo = getIntent().getStringExtra("PERFUME_TIPO");
        String genero = getIntent().getStringExtra("PERFUME_GENERO");

        // Reconstruct perfume for cart
        currentPerfume = new Perfume();
        currentPerfume.setId(id);
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

        btnComprar.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(currentPerfume);
            Toast.makeText(this, nombre + " añadido al carrito", Toast.LENGTH_SHORT).show();
        });

        btnFavorito.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);
            if (userId != -1) {
                RetrofitClient.getApiService().addFavorito(userId, id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
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
            } else {
                Toast.makeText(this, "Inicia sesión para guardar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
