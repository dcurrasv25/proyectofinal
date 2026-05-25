package com.example.proyectofinal.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal.R;
import com.example.proyectofinal.ui.login.LoginActivity;

public class SettingsFragment extends Fragment {

    private TextView tvSesionInfo;
    private Button btnCerrarSesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvSesionInfo = view.findViewById(R.id.tvSesionInfo);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);
            if (userId == -1) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            prefs.edit().clear().apply();
            com.example.proyectofinal.data.api.RetrofitClient.clearToken();
            Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        actualizarEstadoSesion();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarEstadoSesion();
    }

    private void actualizarEstadoSesion() {
        if (getContext() == null) {
            return;
        }
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        boolean conSesion = userId != -1;

        if (conSesion) {
            String username = prefs.getString("username", null);
            if (username != null && !username.isEmpty()) {
                tvSesionInfo.setText("Sesión iniciada como " + username);
            } else {
                tvSesionInfo.setText("Sesión iniciada (usuario #" + userId + ")");
            }
            btnCerrarSesion.setText("Cerrar sesión");
        } else {
            tvSesionInfo.setText("No has iniciado sesión");
            btnCerrarSesion.setText("Iniciar sesión");
        }
    }
}
