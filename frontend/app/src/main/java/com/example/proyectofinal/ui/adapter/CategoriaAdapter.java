package com.example.proyectofinal.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;

    public CategoriaAdapter(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.tvNombreCategoria.setText(categoria.getNombre());
        
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(holder.itemView.getContext(), com.example.proyectofinal.ui.categories.CategoryPerfumesActivity.class);
            intent.putExtra("CATEGORIA_ID", categoria.getId());
            intent.putExtra("CATEGORIA_NOMBRE", categoria.getNombre());
            holder.itemView.getContext().startActivity(intent);
        });

        // Configurar botón de eliminar para administradores
        android.content.Context context = holder.itemView.getContext();
        android.content.SharedPreferences prefs = context.getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE);
        String rol = prefs.getString("rol", "usuario");
        
        if ("admin".equals(rol)) {
            holder.btnEliminar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Eliminar categoría")
                        .setMessage("¿Estás seguro de que deseas eliminar la categoría \"" + categoria.getNombre() + "\"?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            com.example.proyectofinal.data.api.RetrofitClient.getApiService().eliminarCategoria(categoria.getId()).enqueue(new retrofit2.Callback<Void>() {
                                @Override
                                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        int currentPos = holder.getAdapterPosition();
                                        if (currentPos != RecyclerView.NO_POSITION) {
                                            categorias.remove(currentPos);
                                            notifyItemRemoved(currentPos);
                                            notifyItemRangeChanged(currentPos, categorias.size());
                                        }
                                        Toast.makeText(context, "Categoría eliminada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error al eliminar categoría. Puede tener perfumes asociados.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                                    Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCategoria;
        android.widget.ImageButton btnEliminar;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCategoria = itemView.findViewById(R.id.tvNombreCategoria);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
