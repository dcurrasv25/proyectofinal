package com.example.proyectofinal.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.local.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvNombre.setText(item.getPerfume().getNombre());
        holder.tvPrecio.setText(String.format("%.2f €", item.getPerfume().getPrecio()));
        holder.tvCantidad.setText("Cantidad: " + item.getCantidad());

        if (item.getPerfume().getImagen() != null && !item.getPerfume().getImagen().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPerfume().getImagen())
                    .into(holder.ivPerfume);
        } else {
            holder.ivPerfume.setImageResource(android.R.color.darker_gray);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidad;
        ImageView ivPerfume;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvCartNombre);
            tvPrecio = itemView.findViewById(R.id.tvCartPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCartCantidad);
            ivPerfume = itemView.findViewById(R.id.ivCartPerfume);
        }
    }
}
