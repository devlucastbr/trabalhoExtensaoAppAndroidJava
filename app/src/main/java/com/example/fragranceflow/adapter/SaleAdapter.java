package com.example.fragranceflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fragranceflow.R;
import com.example.fragranceflow.model.Sale;

import java.util.List;

public class SaleAdapter extends ArrayAdapter<Sale> {
    public SaleAdapter(Context context, List<Sale> sales) {
        super(context, 0, sales);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutiliza a view existente se possível
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_sale, parent, false);
        }

        // Obtém o produto para a posição atual
        Sale sale = getItem(position);

        // Configura os campos do layout
        TextView textVendaId = convertView.findViewById(R.id.textVendaId);
        TextView textClienteId = convertView.findViewById(R.id.textClienteId);
        TextView textProdutoId = convertView.findViewById(R.id.textProdutoId);
        TextView textQuantidade = convertView.findViewById(R.id.textQuantidade);
        TextView textDataVenda = convertView.findViewById(R.id.textDataVenda);

        // Define o texto dos campos com os dados do produto
        textVendaId.setText("CÓDIGO DA VENDA: " + sale.getId());
        textClienteId.setText("Código do cliente: " + sale.getClienteId());
        textProdutoId.setText("Código do produto: " + sale.getProdutoId());
        textQuantidade.setText("Quantidade: " + sale.getQuantidade());
        textDataVenda.setText("Data da venda: " + sale.getDataVendaFormatada());

        return convertView;
    }
}
