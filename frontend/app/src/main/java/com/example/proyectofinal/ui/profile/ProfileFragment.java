package com.example.proyectofinal.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.local.CartManager;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.ui.adapter.PerfumeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private RecyclerView rvFavorites;
    private RecyclerView rvCart;
    private PerfumeAdapter favoritesAdapter;
    private PerfumeAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvCart = view.findViewById(R.id.rvCart);
        
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        
        favoritesAdapter = new PerfumeAdapter(new ArrayList<>());
        cartAdapter = new PerfumeAdapter(CartManager.getInstance().getCartItems());

        rvFavorites.setAdapter(favoritesAdapter);
        rvCart.setAdapter(cartAdapter);

        cargarFavoritos();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update cart in case items were added
        cartAdapter.setPerfumes(CartManager.getInstance().getCartItems());
        cargarFavoritos();
    }

    private void cargarFavoritos() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            RetrofitClient.getApiService().getFavoritos(userId).enqueue(new Callback<List<Perfume>>() {
                @Override
                public void onResponse(Call<List<Perfume>> call, Response<List<Perfume>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        favoritesAdapter.setPerfumes(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Perfume>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
