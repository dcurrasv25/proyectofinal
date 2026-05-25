package com.example.proyectofinal.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout llSplashContainer = findViewById(R.id.llSplashContainer);
        ProgressBar pbSplashLoader = findViewById(R.id.pbSplashLoader);

        // Estado inicial de la animación
        llSplashContainer.setAlpha(0f);
        llSplashContainer.setScaleX(0.85f);
        llSplashContainer.setScaleY(0.85f);
        pbSplashLoader.setAlpha(0f);

        // Animación suave de entrada del contenedor central (Logo y textos)
        llSplashContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1200)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // Animación suave del indicador de carga
        pbSplashLoader.animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(600)
                .start();

        // Retardo para verificar la sesión y pasar a la siguiente pantalla
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);

            Intent intent;
            if (token != null) {
                // Si ya está logueado, configuramos el token en red y vamos al Main
                RetrofitClient.authToken = token;
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Si no, vamos al Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Transición suave de fundido
            finish();
        }, 2500); // 2.5 segundos de duración
    }
}
