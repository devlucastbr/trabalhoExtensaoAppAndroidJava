package com.example.fragranceflow.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.SearchSalesAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Sale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchSalesActivity extends Activity {
    private EditText editDataInicio, editDataFinal;
    private Button btnBuscarVendas;
    private RecyclerView recyclerViewVendas;
    private SearchSalesAdapter vendaAdapter;
    private DatabaseHelper dbHelper;
    private TextView textValorTotal, textLucroTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sales);

        // Inicializando componentes da tela
        editDataInicio = findViewById(R.id.editDataInicio);
        editDataFinal = findViewById(R.id.editDataFinal);
        btnBuscarVendas = findViewById(R.id.btnBuscarVendas);
        recyclerViewVendas = findViewById(R.id.recyclerViewVendas);
        textValorTotal = findViewById(R.id.textValorTotal);
        textLucroTotal = findViewById(R.id.textLucroTotal);

        // Inicializando o RecyclerView
        recyclerViewVendas.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        // Ação do botão de buscar vendas
        btnBuscarVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarVendas();
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

    private void buscarVendas() {
        String dataInicio = editDataInicio.getText().toString().trim();
        String dataFinal = editDataFinal.getText().toString().trim();

        // Converte a data para o formato ISO 8601
        String dataIso8601_1 = converterParaIso8601(dataInicio);
        String dataIso8601_2 = converterParaIso8601(dataFinal);

        // Verifica se a conversão foi bem-sucedida
        if (dataIso8601_1 == null || dataIso8601_2 == null) {
            // Exibe uma mensagem de erro se a conversão falhar
            Toast.makeText(this, "Formato de data inválido, tente dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se as data preenchidas são vazias
        if (dataInicio.isEmpty() || dataFinal.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            // Obter vendas do intervalo de datas
            List<Sale> vendas = dbHelper.getVendasPorData(dataIso8601_1, dataIso8601_2);
            double valorTotal = dbHelper.getValorTotalVendasNoPeriodo(dataIso8601_1, dataIso8601_2);
            double lucroTotal = dbHelper.getLucroTotalVendasNoPeriodo(dataIso8601_1, dataIso8601_2);

            // Exibir o valor total e lucro total
            textValorTotal.setTextColor(Color.BLUE);
            textValorTotal.setText("Faturamento Total: R$ " + valorTotal);

            // Configura a cor do lucro/prejuízo
            if (lucroTotal >= 0) {
                textLucroTotal.setTextColor(Color.BLUE); // Cor azul para lucro
                textLucroTotal.setText("Lucro Total: R$ " + lucroTotal);
            } else {
                textLucroTotal.setTextColor(Color.RED); // Cor vermelha para prejuízo
                textLucroTotal.setText("Prejuízo Total: R$ " + lucroTotal);
            }

            if (vendas.isEmpty()) {
                Toast.makeText(this, "Nenhuma venda encontrada no intervalo.", Toast.LENGTH_LONG).show();
            } else {
                // Configura o adapter e exibe os resultados no RecyclerView
                vendaAdapter = new SearchSalesAdapter(vendas);
                recyclerViewVendas.setAdapter(vendaAdapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Formato de data inválido. Use o formato dd/MM/yyyy.", Toast.LENGTH_LONG).show();
        }
    }
}
