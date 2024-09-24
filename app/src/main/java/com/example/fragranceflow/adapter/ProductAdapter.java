package com.example.fragranceflow.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.fragranceflow.R;
import com.example.fragranceflow.model.Product;
import com.example.fragranceflow.view.ProductListActivity;
import com.example.fragranceflow.view.UpdateProductActivity;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutiliza a view existente se possível
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_product, parent, false);
        }

        // Encontrar o botão de editar
        Button btnUpdateProduto = convertView.findViewById(R.id.btnUpdateProduto);

        // Adicionar o listener ao botão de editar
        btnUpdateProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter o produto correspondente
                Product produto = getItem(position);

                // Criar um Intent para abrir a EditarProdutoActivity
                Intent intent = new Intent(getContext(), UpdateProductActivity.class);

                // Passar o ID ou dados do produto para a atividade de edição, em name era produto_id
                intent.putExtra("produtoId", produto.getId());

                // Iniciar a atividade
                getContext().startActivity(intent);
            }
        });

        // Obtém o produto para a posição atual
        Product product = getItem(position);

        // Configura os campos do layout
        TextView textNome = convertView.findViewById(R.id.textNome);
        TextView textProdutoId = convertView.findViewById(R.id.textProdutoId);
        TextView textQuantidade = convertView.findViewById(R.id.textQuantidade);
        TextView textCustoUnitario = convertView.findViewById(R.id.textCustoUnitario);
        TextView textValorVenda = convertView.findViewById(R.id.textValorVenda);
        TextView textMargemLucro = convertView.findViewById(R.id.textMargemLucro);
        TextView textMarca = convertView.findViewById(R.id.textMarca);
        TextView textValidade = convertView.findViewById(R.id.textValidade);

        // Define o texto dos campos com os dados do produto
        textNome.setText(product.getNome());
        textProdutoId.setText("Código do produto: " + product.getId());
        textQuantidade.setText("Quantidade: " + product.getQuantidade());
        textCustoUnitario.setText("Custo Unitário: R$ " + String.format("%.2f", product.getCustoUnitario()));
        textValorVenda.setText("Valor de venda: R$ " + String.format("%.2f", product.getValorVenda()));
        textMargemLucro.setText("Margem de lucro: " + product.getMargemLucro() + "%");
        textMarca.setText("Marca: " + product.getMarca());
        textValidade.setText("Validade: " + product.getValidadeFormatada());

        return convertView;
    }
}