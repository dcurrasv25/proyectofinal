package com.example.proyectofinal.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.model.Nota;
import com.example.proyectofinal.ui.notes.NotePerfumesActivity;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> notas;

    public NotaAdapter(List<Nota> notas) {
        this.notas = notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = notas.get(position);
        holder.tvNombreNota.setText(nota.getNombre());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), NotePerfumesActivity.class);
            intent.putExtra("NOTA_ID", nota.getId());
            intent.putExtra("NOTA_NOMBRE", nota.getNombre());
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
                        .setTitle("Eliminar nota")
                        .setMessage("¿Estás seguro de que deseas eliminar la nota \"" + nota.getNombre() + "\"?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            com.example.proyectofinal.data.api.RetrofitClient.getApiService().eliminarNota(nota.getId()).enqueue(new retrofit2.Callback<Void>() {
                                @Override
                                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        int currentPos = holder.getAdapterPosition();
                                        if (currentPos != RecyclerView.NO_POSITION) {
                                            notas.remove(currentPos);
                                            notifyItemRemoved(currentPos);
                                            notifyItemRangeChanged(currentPos, notas.size());
                                        }
                                        Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT).show();
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
        return notas != null ? notas.size() : 0;
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreNota;
        android.widget.ImageButton btnEliminar;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreNota = itemView.findViewById(R.id.tvNombreNota);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}

