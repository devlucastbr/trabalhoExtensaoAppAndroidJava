package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.ProductAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends Activity {

    private DatabaseHelper dbHelper;
    private EditText edtNome, edtQuantidade, edtCustoUnitario, edtMargemLucro, edtValorVenda, edtVolume, edtValidade;
    private Button btnAddProduto, btnVerTodosProdutos;
    private Spinner spinnerMarcas, spinnerCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        dbHelper = new DatabaseHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtQuantidade = findViewById(R.id.edtQuantidade);
        edtCustoUnitario = findViewById(R.id.edtCustoUnitario);
        edtMargemLucro = findViewById(R.id.edtMargemLucro);
        edtValorVenda = findViewById(R.id.edtValorVenda);
        edtVolume = findViewById(R.id.edtVolume);
        edtValidade = findViewById(R.id.edtValidade);
        btnAddProduto = findViewById(R.id.btnAddProduto);
        btnVerTodosProdutos = findViewById(R.id.btnVerTodosProdutos);
        // Configura o Spinner Marcas
        spinnerMarcas = findViewById(R.id.spinnerMarcas);
        // Configura o Spinner Categorias
        spinnerCategorias = findViewById(R.id.spinnerCategorias);

        // Cria um ArrayAdapter usando o array de marcas e um layout padrão para spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.marcas_array, android.R.layout.simple_spinner_item);

        // Cria um ArrayAdapter usando o array de categorias e um layout padrão para spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.categorias_array, android.R.layout.simple_spinner_item);

        // Especifica o layout a ser usado quando a lista de escolhas aparecer Marcas
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Especifica o layout a ser usado quando a lista de escolhas aparecer Categorias
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplica o adapter ao Spinner Marcas
        spinnerMarcas.setAdapter(adapter);

        // Aplica o adapter ao Spinner Categorias
        spinnerCategorias.setAdapter(adapter2);

        btnAddProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularValorVenda();
                addProduct();
                // Reseta os Spinner para a posição 0
                spinnerMarcas.setSelection(0);
                spinnerCategorias.setSelection(0);
            }
        });

        btnVerTodosProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, ProductListActivity.class);
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

    // Calcula o valor de venda
    private void calcularValorVenda() {
        if(!TextUtils.isEmpty(edtCustoUnitario.getText()) && !TextUtils.isEmpty(edtMargemLucro.getText())) {

            Float custo_unitario = Float.parseFloat(edtCustoUnitario.getText().toString());
            Integer margem_lucro = Integer.parseInt(edtMargemLucro.getText().toString());

            Float valor_venda = custo_unitario + (custo_unitario * margem_lucro / 100);

            edtValorVenda.setText(String.valueOf(valor_venda));
        } else {
            Toast.makeText(ProductActivity.this, "Preencha os campos de custo unitário e margem lucro", Toast.LENGTH_SHORT).show();
        }

    }

    private void addProduct() {
        String nome = edtNome.getText().toString().toUpperCase().trim();
        String quantidade = edtQuantidade.getText().toString();
        String marca = spinnerMarcas.getSelectedItem().toString();
        String custo_unitario = edtCustoUnitario.getText().toString();
        String margem_lucro = edtMargemLucro.getText().toString();
        String valor_venda = edtValorVenda.getText().toString();
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String volume = edtVolume.getText().toString().toLowerCase().trim();
        String validade = edtValidade.getText().toString().trim();

        // Converte a data para o formato ISO 8601
        String dataIso8601 = converterParaIso8601(validade);

        // Verifica se a conversão foi bem-sucedida
        if (dataIso8601 == null) {
            // Exibe uma mensagem de erro se a conversão falhar
            Toast.makeText(this, "Formato de data inválido, tente dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (marca.contains("Selecione uma marca")) {
            Toast.makeText(ProductActivity.this, "Selecione uma marca", Toast.LENGTH_SHORT).show();
        }*/

        if (nome.isEmpty() || quantidade.isEmpty() || marca.isEmpty() || marca.contains("Selecione uma marca") || custo_unitario.isEmpty() || margem_lucro.isEmpty() || valor_venda.isEmpty() || categoria.contains("Selecione uma categoria") || volume.isEmpty() || validade.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            Toast.makeText(ProductActivity.this, "Selecione uma marca", Toast.LENGTH_SHORT).show();
            Toast.makeText(ProductActivity.this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        Product produto = new Product(0, nome, Integer.parseInt(quantidade), marca, Float.parseFloat(custo_unitario), Integer.parseInt(margem_lucro), Float.parseFloat(valor_venda), categoria, volume,dataIso8601);
        long id = dbHelper.addProduct(produto);
        if (id > 0) {
            produto.setId((int) id);
            edtNome.setText("");
            edtQuantidade.setText("");
            spinnerMarcas.getSelectedItem();
            edtCustoUnitario.setText("");
            edtMargemLucro.setText("");
            edtValorVenda.setText("");
            spinnerCategorias.getSelectedItem();
            edtVolume.setText("");
            edtValidade.setText("");
            Toast.makeText(this, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
        }
    }

}
