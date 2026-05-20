package com.example.proyectofinal.ui.notes;

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

public class NotePerfumesActivity extends AppCompatActivity {

    private RecyclerView rvPerfumes;
    private PerfumeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume_list);

        int notaId = getIntent().getIntExtra("NOTA_ID", -1);
        String notaNombre = getIntent().getStringExtra("NOTA_NOMBRE");

        if (notaNombre != null) {
            setTitle("Nota: " + notaNombre);
        }

        rvPerfumes = findViewById(R.id.rvPerfumes);
        rvPerfumes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PerfumeAdapter(new ArrayList<>());
        rvPerfumes.setAdapter(adapter);

        if (notaId != -1) {
            cargarPerfumesPorNota(notaId);
        }
    }

    private void cargarPerfumesPorNota(int notaId) {
        RetrofitClient.getApiService().getPerfumesPorNota(notaId).enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPerfumes(response.body());
                } else {
                    Toast.makeText(NotePerfumesActivity.this, "Error al cargar perfumes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                Toast.makeText(NotePerfumesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

