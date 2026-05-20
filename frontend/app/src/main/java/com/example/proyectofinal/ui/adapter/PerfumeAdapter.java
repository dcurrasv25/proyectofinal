package com.example.proyectofinal.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.R;
import com.example.proyectofinal.data.model.Perfume;
import com.example.proyectofinal.ui.PerfumeDetailActivity;

import java.util.List;

public class PerfumeAdapter extends RecyclerView.Adapter<PerfumeAdapter.PerfumeViewHolder> {

    private List<Perfume> perfumes;

    public PerfumeAdapter(List<Perfume> perfumes) {
        this.perfumes = perfumes;
    }

    public void setPerfumes(List<Perfume> perfumes) {
        this.perfumes = perfumes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PerfumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfume, parent, false);
        return new PerfumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfumeViewHolder holder, int position) {
        Perfume perfume = perfumes.get(position);
        holder.tvNombre.setText(perfume.getNombre());
        holder.tvMarca.setText(perfume.getMarca());
        holder.tvPrecio.setText(String.format("%.2f €", perfume.getPrecio()));

        if (perfume.getImagen() != null && !perfume.getImagen().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(perfume.getImagen())
                    .into(holder.ivPerfume);
        } else {
            holder.ivPerfume.setImageResource(R.color.image_placeholder);
        }

        holder.btnDetalles.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PerfumeDetailActivity.class);
            intent.putExtra("PERFUME_ID", perfume.getId());
            intent.putExtra("PERFUME_NOMBRE", perfume.getNombre());
            intent.putExtra("PERFUME_MARCA", perfume.getMarca());
            intent.putExtra("PERFUME_PRECIO", perfume.getPrecio());
            intent.putExtra("PERFUME_IMAGEN", perfume.getImagen());
            intent.putExtra("PERFUME_TIPO", perfume.getTipo());
            intent.putExtra("PERFUME_GENERO", perfume.getGenero());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return perfumes != null ? perfumes.size() : 0;
    }

    static class PerfumeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvPrecio;
        ImageView ivPerfume;
        Button btnDetalles;

        public PerfumeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivPerfume = itemView.findViewById(R.id.ivPerfume);
            btnDetalles = itemView.findViewById(R.id.btnDetalles);
        }
    }
}
