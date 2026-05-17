package com.example.proyectofinal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.ui.adapter.PerfumeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPerfumes;
    private PerfumeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPerfumes = findViewById(R.id.rvPerfumes);
        rvPerfumes.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PerfumeAdapter(new ArrayList<>());
        rvPerfumes.setAdapter(adapter);

        cargarPerfumes();
    }

    private void cargarPerfumes() {
        RetrofitClient.getApiService().getPerfumes().enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPerfumes(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar perfumes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
