package com.example.proyectofinal.ui.categories;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.ui.adapter.PerfumeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPerfumesActivity extends AppCompatActivity {

    private RecyclerView rvPerfumes;
    private PerfumeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Reusing activity_main layout without bottom nav for simplicity, wait no, activity_main has FragmentContainerView now. I will use a simple layout.
        
        // Let's create a simple layout programmatically or use fragment_home
        setContentView(R.layout.fragment_home);

        int categoriaId = getIntent().getIntExtra("CATEGORIA_ID", -1);
        String categoriaNombre = getIntent().getStringExtra("CATEGORIA_NOMBRE");

        if (categoriaNombre != null) {
            setTitle("Categoría: " + categoriaNombre);
        }

        rvPerfumes = findViewById(R.id.rvPerfumes);
        rvPerfumes.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PerfumeAdapter(new ArrayList<>());
        rvPerfumes.setAdapter(adapter);

        if (categoriaId != -1) {
            cargarPerfumesPorCategoria(categoriaId);
        }
    }

    private void cargarPerfumesPorCategoria(int categoriaId) {
        // Since backend has GET /categorias/{id}/perfumes/
        RetrofitClient.getApiService().getPerfumesPorCategoria(categoriaId).enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPerfumes(response.body());
                } else {
                    Toast.makeText(CategoryPerfumesActivity.this, "Error al cargar perfumes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                Toast.makeText(CategoryPerfumesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
