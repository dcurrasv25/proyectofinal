package com.example.proyectofinal.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.api.RetrofitClient;
import com.example.proyectofinal.data.local.CartItem;
import com.example.proyectofinal.data.local.CartManager;
import com.example.proyectofinal.data.model.CompraRequest;
import com.example.proyectofinal.data.model.CompraResponse;
import com.example.proyectofinal.data.model.LineaPedidoRequest;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.ui.adapter.CartAdapter;
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
    private CartAdapter cartAdapter;
    private TextView tvTotal;
    private Button btnCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvCart = view.findViewById(R.id.rvCart);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        Button btnVerHistorial = view.findViewById(R.id.btnVerHistorial);
        
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        
        favoritesAdapter = new PerfumeAdapter(new ArrayList<>());
        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems());
        cartAdapter.setOnCartItemDeleteListener(item -> {
            CartManager.getInstance().removeEntireItemFromCart(item.getPerfume());
            cartAdapter.setCartItems(CartManager.getInstance().getCartItems());
            actualizarTotal();
            Toast.makeText(getContext(), item.getPerfume().getNombre() + " eliminado", Toast.LENGTH_SHORT).show();
        });

        rvFavorites.setAdapter(favoritesAdapter);
        rvCart.setAdapter(cartAdapter);

        cargarFavoritos();
        actualizarTotal();

        btnCheckout.setOnClickListener(v -> procesarPago());
        
        btnVerHistorial.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(getContext(), PurchaseHistoryActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update cart in case items were added
        cartAdapter.setCartItems(CartManager.getInstance().getCartItems());
        cargarFavoritos();
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            total += (item.getPerfume().getPrecio() * item.getCantidad());
        }
        tvTotal.setText(String.format("Total: %.2f €", total));
    }

    private void procesarPago() {
        List<CartItem> items = CartManager.getInstance().getCartItems();
        if (items.isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Debes iniciar sesión para comprar", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Simulando pasarela de pago...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 1. Crear la Compra
        RetrofitClient.getApiService().crearCompra(new CompraRequest(userId)).enqueue(new Callback<CompraResponse>() {
            @Override
            public void onResponse(Call<CompraResponse> call, Response<CompraResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int compraId = response.body().getIdCompra();
                    crearLineasPedido(compraId, items, progressDialog);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error al iniciar compra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompraResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearLineasPedido(int compraId, List<CartItem> items, ProgressDialog dialog) {
        int totalItems = items.size();
        final int[] completedItems = {0};

        for (CartItem item : items) {
            LineaPedidoRequest linea = new LineaPedidoRequest(compraId, item.getPerfume().getId(), item.getCantidad(), item.getPerfume().getPrecio());
            RetrofitClient.getApiService().crearLineaPedido(linea).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    completedItems[0]++;
                    checkCompletion(completedItems[0], totalItems, dialog);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    completedItems[0]++;
                    checkCompletion(completedItems[0], totalItems, dialog);
                }
            });
        }
    }

    private void checkCompletion(int completed, int total, ProgressDialog dialog) {
        if (completed == total) {
            dialog.dismiss();
            Toast.makeText(getContext(), "¡Pago exitoso! Gracias por tu compra.", Toast.LENGTH_LONG).show();
            CartManager.getInstance().clearCart();
            cartAdapter.setCartItems(CartManager.getInstance().getCartItems());
            actualizarTotal();
        }
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
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
