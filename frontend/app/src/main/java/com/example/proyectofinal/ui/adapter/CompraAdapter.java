package com.example.proyectofinal.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.data.model.CompraResponse;
import com.example.proyectofinal.data.model.LineaPedidoResponse;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private List<CompraResponse> compras;

    public CompraAdapter(List<CompraResponse> compras) {
        this.compras = compras;
    }

    public void setCompras(List<CompraResponse> compras) {
        this.compras = compras;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        CompraResponse compra = compras.get(position);
        
        // El backend devuelve fecha con hora, formateamos simplificado (ej: 2024-05-18T12:00 -> 2024-05-18)
        String fecha = compra.getFecha() != null && compra.getFecha().length() >= 10 ? compra.getFecha().substring(0, 10) : "N/A";
        
        holder.tvCompraInfo.setText("Pedido #" + compra.getIdCompra() + " - " + fecha);

        StringBuilder lineasStr = new StringBuilder();
        double total = 0.0;

        if (compra.getLineas() != null) {
            for (LineaPedidoResponse linea : compra.getLineas()) {
                String nombrePerfume = linea.getPerfumeDetalle() != null ? linea.getPerfumeDetalle().getNombre() : "Perfume Desconocido";
                lineasStr.append("- ").append(linea.getCantidad()).append("x ")
                         .append(nombrePerfume)
                         .append(" (").append(String.format("%.2f", linea.getPrecioUnitario())).append("€)\n");
                total += (linea.getCantidad() * linea.getPrecioUnitario());
            }
        } else {
            lineasStr.append("Sin detalles");
        }

        holder.tvCompraLineas.setText(lineasStr.toString().trim());
        holder.tvCompraTotal.setText(String.format("Total: %.2f €", total));
    }

    @Override
    public int getItemCount() {
        return compras != null ? compras.size() : 0;
    }

    static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompraInfo, tvCompraLineas, tvCompraTotal;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompraInfo = itemView.findViewById(R.id.tvCompraInfo);
            tvCompraLineas = itemView.findViewById(R.id.tvCompraLineas);
            tvCompraTotal = itemView.findViewById(R.id.tvCompraTotal);
        }
    }
}
