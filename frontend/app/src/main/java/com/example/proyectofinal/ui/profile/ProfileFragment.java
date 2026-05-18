package com.example.proyectofinal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;

public class ProfileFragment extends Fragment {

    private RecyclerView rvFavorites;
    private RecyclerView rvCart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvCart = view.findViewById(R.id.rvCart);
        
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // TODO: Cargar favoritos y carrito y setear adaptadores
        
        return view;
    }
}
