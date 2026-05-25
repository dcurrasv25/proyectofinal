package com.example.proyectofinal.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/"; // URL para el emulador de Android a localhost
    private static Retrofit retrofit = null;
    public static String authToken = null; // Token estático para autenticación

    public static ApiService getApiService() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(logging);
            
            // Interceptor para añadir la cabecera Authorization automáticamente
            httpClientBuilder.addInterceptor(chain -> {
                okhttp3.Request original = chain.request();
                if (authToken != null && !authToken.isEmpty()) {
                    okhttp3.Request request = original.newBuilder()
                            .header("Authorization", "Token " + authToken)
                            .build();
                    return chain.proceed(request);
                }
                return chain.proceed(original);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public static void clearToken() {
        authToken = null;
        retrofit = null;
    }
}
