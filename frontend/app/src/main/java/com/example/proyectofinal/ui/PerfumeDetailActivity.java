package com.example.proyectofinal.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;

public class PerfumeDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImagen;
    private TextView tvDetailNombre, tvDetailMarca, tvDetailPrecio, tvDetailTipo, tvDetailGenero;
    private Button btnComprar;

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

        // Get data from intent
        int id = getIntent().getIntExtra("PERFUME_ID", -1);
        String nombre = getIntent().getStringExtra("PERFUME_NOMBRE");
        String marca = getIntent().getStringExtra("PERFUME_MARCA");
        double precio = getIntent().getDoubleExtra("PERFUME_PRECIO", 0.0);
        String imagen = getIntent().getStringExtra("PERFUME_IMAGEN");
        String tipo = getIntent().getStringExtra("PERFUME_TIPO");
        String genero = getIntent().getStringExtra("PERFUME_GENERO");

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
            // TODO: Agregar lógica para añadir al carrito
            Toast.makeText(this, nombre + " añadido al carrito", Toast.LENGTH_SHORT).show();
        });
    }
}
