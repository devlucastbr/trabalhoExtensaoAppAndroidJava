package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.SaleAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Sale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleActivity extends Activity {
    private DatabaseHelper dbHelper;

    private EditText edtClienteId, edtProdutoId, edtQuantidade, edtDataVenda;
    private Button btnAddVenda, btnVerTodasVendas, btnPesquisarVendasPorPeriodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        dbHelper = new DatabaseHelper(this);

        edtClienteId = findViewById(R.id.edtClienteId);
        edtProdutoId = findViewById(R.id.edtProdutoId);
        edtQuantidade = findViewById(R.id.edtQuantidade);
        edtDataVenda = findViewById(R.id.edtDataVenda);
        btnAddVenda = findViewById(R.id.btnAddVenda);
        btnVerTodasVendas = findViewById(R.id.btnVerTodasVendas);
        btnPesquisarVendasPorPeriodo = findViewById(R.id.btnPesquisarVendasPorPeriodo);

        btnAddVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSale();
            }
        });

        btnVerTodasVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleActivity.this, SaleListActivity.class);
                startActivity(intent);
            }
        });

        btnPesquisarVendasPorPeriodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleActivity.this, SearchSalesActivity.class);
                startActivity(intent);
            }
        });

    }

    // Função para converter de dd/MM/yyyy para yyyy-MM-dd (ISO 8601)
    public static String converterParaIso8601(String dataTexto) {
        // Formato de entrada (o formato que o usuário digita)
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Formato de saída (ISO 8601)
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Converte a string para um objeto Date
            Date data = formatoEntrada.parse(dataTexto);
            // Converte a data para o formato ISO 8601
            return formatoSaida.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Retorna null em caso de erro
        }
    }

    private void addSale() {
        String cliente_id = edtClienteId.getText().toString().toUpperCase().trim();
        String produto_id = edtProdutoId.getText().toString().trim();
        String quantidade = edtQuantidade.getText().toString();
        String data_venda = edtDataVenda.getText().toString().trim();

        // Converte a data para o formato ISO 8601
        String dataIso8601 = converterParaIso8601(data_venda);

        // Verifica se a conversão foi bem-sucedida
        if (dataIso8601 == null) {
            // Exibe uma mensagem de erro se a conversão falhar
            Toast.makeText(this, "Formato de data inválido, tente dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cliente_id.isEmpty() || produto_id.isEmpty() || quantidade.isEmpty() || data_venda.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Sale venda = new Sale(0, Integer.parseInt(cliente_id), Integer.parseInt(produto_id), Integer.parseInt(quantidade), dataIso8601);
        try {
            long id = dbHelper.addSale(venda);
            if (id > 0) {
                venda.setId((int) id);
                edtClienteId.setText("");
                edtProdutoId.setText("");
                edtQuantidade.setText("");
                edtDataVenda.setText("");
                Toast.makeText(this, "Venda realizada com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao realizar venda", Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
