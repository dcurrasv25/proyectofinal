package com.example.proyectofinal.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.model.Perfume;

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
    }

    @Override
    public int getItemCount() {
        return perfumes != null ? perfumes.size() : 0;
    }

    static class PerfumeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvPrecio;

        public PerfumeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
