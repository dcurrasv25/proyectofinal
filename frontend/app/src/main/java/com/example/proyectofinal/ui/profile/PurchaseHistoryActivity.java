package com.example.proyectofinal.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.model.CompraResponse;
import com.example.proyectofinal.ui.adapter.CompraAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private RecyclerView rvCompras;
    private CompraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        setTitle("Mi Historial de Compras");

        rvCompras = findViewById(R.id.rvCompras);
        rvCompras.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new CompraAdapter(new ArrayList<>());
        rvCompras.setAdapter(adapter);

        cargarHistorial();
    }

    private void cargarHistorial() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            RetrofitClient.getApiService().getCompras(userId).enqueue(new Callback<List<CompraResponse>>() {
                @Override
                public void onResponse(Call<List<CompraResponse>> call, Response<List<CompraResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        adapter.setCompras(response.body());
                    } else {
                        Toast.makeText(PurchaseHistoryActivity.this, "Error al cargar historial", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CompraResponse>> call, Throwable t) {
                    Toast.makeText(PurchaseHistoryActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Por favor inicia sesión", Toast.LENGTH_SHORT).show();
        }
    }
}
