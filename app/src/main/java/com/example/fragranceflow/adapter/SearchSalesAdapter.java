package com.example.fragranceflow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragranceflow.R;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Sale;

import java.text.DecimalFormat;
import java.util.List;

public class SearchSalesAdapter extends RecyclerView.Adapter<SearchSalesAdapter.VendaViewHolder>{
    private List<Sale> vendas;

    public SearchSalesAdapter(List<Sale> vendas) {
        this.vendas = vendas;
    }

    @NonNull
    @Override
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_search_sale, parent, false);
        return new VendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        Sale venda = vendas.get(position);
        holder.bind(venda);
    }

    @Override
    public int getItemCount() {
        return vendas.size();
    }

    public class VendaViewHolder extends RecyclerView.ViewHolder {
        private TextView textVendaId,textCliente, textProduto, textValorTotal, textLucro, textMargem;

        public VendaViewHolder(@NonNull View itemView) {
            super(itemView);
            textVendaId = itemView.findViewById(R.id.textVendaId);
            textCliente = itemView.findViewById(R.id.textCliente);
            textProduto = itemView.findViewById(R.id.textProduto);
            textValorTotal = itemView.findViewById(R.id.textValorTotal);
            textLucro = itemView.findViewById(R.id.textLucro);
            textMargem = itemView.findViewById(R.id.textMargem);
        }

        public void bind(Sale venda) {
            // Preenche os dados do card com os valores da venda
            DatabaseHelper dbHelper = new DatabaseHelper(itemView.getContext());
            int vendaId = venda.getId();
            String nomeCliente = dbHelper.getCustomer(venda.getClienteId()).getNome();
            String nomeProduto = dbHelper.getProduct(venda.getProdutoId()).getNome();
            double valorTotal = dbHelper.getValorTotalVenda(venda.getId());
            double lucro = dbHelper.getLucroVenda(venda.getId());
            double margem = dbHelper.getMargemVenda(venda.getId());

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            textVendaId.setText("Codigo da Venda: " + vendaId);
            textCliente.setText("Cliente: " + nomeCliente);
            textProduto.setText("Produto: " + nomeProduto);
            textValorTotal.setText("Valor Total: R$ " + decimalFormat.format(valorTotal));
            textLucro.setText("Lucro: R$ " + decimalFormat.format(lucro));
            textMargem.setText("Margem: " + decimalFormat.format(margem) + "%");
        }
    }
}
