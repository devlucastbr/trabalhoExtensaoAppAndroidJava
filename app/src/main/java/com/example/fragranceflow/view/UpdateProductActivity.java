package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateProductActivity extends Activity {

    private DatabaseHelper dbHelper;
    private EditText edtNome, edtQuantidade, edtCustoUnitario, edtMargemLucro, edtValorVenda, edtVolume, edtValidade;
    private Button btnSalvarProduto;
    private Spinner spinnerMarcas, spinnerCategorias;
    private int produtoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        dbHelper = new DatabaseHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtQuantidade = findViewById(R.id.edtQuantidade);
        edtCustoUnitario = findViewById(R.id.edtCustoUnitario);
        edtMargemLucro = findViewById(R.id.edtMargemLucro);
        edtValorVenda = findViewById(R.id.edtValorVenda);
        edtVolume = findViewById(R.id.edtVolume);
        edtValidade = findViewById(R.id.edtValidade);
        btnSalvarProduto = findViewById(R.id.btnSalvarProduto);
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

        // Obter ID do Produto passado pela Intent, em name era produto_id
        produtoId = getIntent().getIntExtra("produtoId", -1);

        if (produtoId != -1) {
            carregarDadosProduto(produtoId);
        } else {
            // Trate o erro se o produtoId não for passado corretamente
            Toast.makeText(this, "Erro ao carregar o produto", Toast.LENGTH_SHORT).show();
        }

        btnSalvarProduto.setOnClickListener(v -> {
            calcularValorVenda();
            salvarProduto();
        });
    }

    private void carregarDadosProduto(int id) {
        Product produto = dbHelper.getProduct(id);

        // Pega a marca salva do produto
        String marcaSalva = produto.getMarca();
        // Pega a categoria salva do produto
        String categoriaSalva = produto.getCategoria();

        // Carregar o array de marcas do strings.xml
        String[] marcas = getResources().getStringArray(R.array.marcas_array);
        // Carregar o array de categorias do strings.xml
        String[] categorias = getResources().getStringArray(R.array.categorias_array);

        if (produto != null ) {
            // Encontrar a posição da marca salva no Spinner
            for (int i = 0; i < marcas.length; i++) {
                if (marcas[i].equals(marcaSalva)) {
                    spinnerMarcas.setSelection(i);  // Definir a posição correta
                    break;
                }
            }
            // Encontrar a posição da categoria salva no Spinner
            for (int i = 0; i < categorias.length; i++) {
                if (categorias[i].equals(categoriaSalva)) {
                    spinnerCategorias.setSelection(i);  // Definir a posição correta
                    break;
                }
            }
            edtNome.setText(produto.getNome());
            edtQuantidade.setText(String.valueOf(produto.getQuantidade()));
            edtCustoUnitario.setText(String.valueOf(produto.getCustoUnitario()));
            edtMargemLucro.setText(String.valueOf(produto.getMargemLucro()));
            edtValorVenda.setText(String.valueOf(produto.getValorVenda()));
            edtVolume.setText(produto.getVolume());
            edtValidade.setText(produto.getValidadeFormatada());
        } else {
            // Produto não encontrado no banco
            Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(UpdateProductActivity.this, "Preencha os campos de custo unitário e margem lucro", Toast.LENGTH_SHORT).show();
        }

    }

    private void salvarProduto() {
        String nome = edtNome.getText().toString().trim();
        String quantidade = edtQuantidade.getText().toString();
        String marca = spinnerMarcas.getSelectedItem().toString();
        String custoUnitario = edtCustoUnitario.getText().toString();
        String margemLucro = edtMargemLucro.getText().toString();
        String valorVenda = edtValorVenda.getText().toString();
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String volume = edtVolume.getText().toString().trim();
        String validade = edtValidade.getText().toString().trim();

        // Converte a data para o formato ISO 8601
        String dataIso8601 = converterParaIso8601(validade);

        // Verifica se a conversão foi bem-sucedida
        if (dataIso8601 == null) {
            // Exibe uma mensagem de erro se a conversão falhar
            Toast.makeText(this, "Formato de data inválido, tente dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nome.isEmpty() || quantidade.isEmpty() || marca.isEmpty() || marca.contains("Selecione uma marca") || custoUnitario.isEmpty() || margemLucro.isEmpty() || valorVenda.isEmpty() || categoria.contains("Selecione uma categoria") || volume.isEmpty() || validade.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            Toast.makeText(UpdateProductActivity.this, "Selecione uma marca", Toast.LENGTH_SHORT).show();
            Toast.makeText(UpdateProductActivity.this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        Product produto = new Product(produtoId, nome, Integer.parseInt(quantidade), marca, Float.parseFloat(custoUnitario), Integer.parseInt(margemLucro), Float.parseFloat(valorVenda), categoria, volume, dataIso8601);
        dbHelper.updateProduct(produto); // Atualizar no banco de dados

        Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        // Define o resultado como OK para a Activity anterior
        //setResult(RESULT_OK); // Não tinha
        // Não tinha. Abre a ActivityB
        Intent intent = new Intent(UpdateProductActivity.this, ProductActivity.class);
        startActivity(intent);
        finish(); // Fecha a Activity após salvar
    }
}
