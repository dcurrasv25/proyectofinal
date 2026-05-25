package com.example.proyectofinal.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.model.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.widget.ImageView ivLoginLogo = findViewById(R.id.ivLoginLogo);
        android.widget.TextView tvLoginTitle = findViewById(R.id.tvLoginTitle);
        android.widget.TextView tvLoginSubtitle = findViewById(R.id.tvLoginSubtitle);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        android.widget.TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        // Preparar estado inicial para la animación de entrada
        ivLoginLogo.setAlpha(0f);
        ivLoginLogo.setTranslationY(-80f);
        tvLoginTitle.setAlpha(0f);
        tvLoginTitle.setTranslationY(40f);
        tvLoginSubtitle.setAlpha(0f);
        tvLoginSubtitle.setTranslationY(40f);

        // Ejecutar animaciones fluidas de entrada (Intro Anim)
        ivLoginLogo.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();

        tvLoginTitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(200)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();

        tvLoginSubtitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(450)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
        });

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("nombre_de_usuario", username);
        credentials.put("contrasena", password);

        RetrofitClient.getApiService().login(credentials).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", response.body().getId());
                    editor.putString("token", response.body().getToken());
                    if (response.body().getNombreDeUsuario() != null) {
                        editor.putString("username", response.body().getNombreDeUsuario());
                    }
                    if (response.body().getRol() != null) {
                        editor.putString("rol", response.body().getRol());
                    }
                    editor.apply();

                    // Set token on the network client
                    RetrofitClient.authToken = response.body().getToken();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
