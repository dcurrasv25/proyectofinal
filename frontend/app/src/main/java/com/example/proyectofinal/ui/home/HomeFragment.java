package com.example.proyectofinal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private RecyclerView rvPerfumes;
    private PerfumeAdapter adapter;
    private SearchView searchView;
    private List<Perfume> allPerfumes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvPerfumes = view.findViewById(R.id.rvPerfumes);
        searchView = view.findViewById(R.id.searchView);
        rvPerfumes.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new PerfumeAdapter(new ArrayList<>());
        rvPerfumes.setAdapter(adapter);

        // Mostrar FAB de añadir perfume sólo si es administrador
        com.google.android.material.floatingactionbutton.FloatingActionButton fabAddPerfume = view.findViewById(R.id.fabAddPerfume);
        if (getContext() != null) {
            android.content.SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE);
            String rol = prefs.getString("rol", "usuario");
            if ("admin".equals(rol)) {
                fabAddPerfume.setVisibility(View.VISIBLE);
                fabAddPerfume.setOnClickListener(v -> {
                    android.content.Intent intent = new android.content.Intent(getContext(), AddPerfumeActivity.class);
                    startActivity(intent);
                });
            }
        }

        cargarPerfumes();
        configurarBuscador();

        return view;
    }

    private void configurarBuscador() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return false;
            }
        });
    }

    private void filtrar(String texto) {
        List<Perfume> filtrados = new ArrayList<>();
        for (Perfume p : allPerfumes) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase()) || 
                (p.getMarca() != null && p.getMarca().toLowerCase().contains(texto.toLowerCase()))) {
                filtrados.add(p);
            }
        }
        adapter.setPerfumes(filtrados);
    }

    private void cargarPerfumes() {
        RetrofitClient.getApiService().getPerfumes().enqueue(new Callback<List<Perfume>>() {
            @Override
            public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allPerfumes = response.body();
                    adapter.setPerfumes(allPerfumes);
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error al cargar perfumes", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Perfume>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPerfumes();
    }
}
