package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.ProductAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends Activity {

    private DatabaseHelper dbHelper;
    private ListView lstProdutos;
    private List<Product> produtosList;
    private ArrayAdapter<Product> produtosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dbHelper = new DatabaseHelper(this);
        lstProdutos = findViewById(R.id.lstProdutos);

        produtosList = new ArrayList<>();
        //produtosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtosList);
        produtosAdapter = new ProductAdapter(this, produtosList);
        lstProdutos.setAdapter(produtosAdapter);

        loadProdutos();

        lstProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pegue o produto selecionado da lista
                Product produto = (Product) parent.getItemAtPosition(position);

                // Inicie a EditarProdutoActivity e passe o id do produto, em name era produto_id
                Intent intent = new Intent(ProductListActivity.this, UpdateProductActivity.class);
                intent.putExtra("produtoId", produto.getId());
                startActivity(intent); //Era:startActivity(intent); O '1' é o código de requisição para identificar o retorno
            }
        });

    }

    private void loadProdutos() {
        produtosList.clear();
        produtosList.addAll(dbHelper.getAllProducts());
        produtosAdapter.notifyDataSetChanged();
    }

}
