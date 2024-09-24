package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragranceflow.MainActivity;
import com.example.fragranceflow.R;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Customer;
import com.example.fragranceflow.utils.RegexUtils;

public class UpdateCustomerActivity extends Activity {

    private DatabaseHelper dbHelper;

    private EditText edtNome, edtTelefone;
    private Button btnAtualizarCliente;
    private int clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        dbHelper = new DatabaseHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        btnAtualizarCliente = findViewById(R.id.btnAtualizarCliente);

        // Obter ID do Produto passado pela Intent, em name era produto_id
        clienteId = getIntent().getIntExtra("clienteId", -1);

        if (clienteId != -1) {
            carregarDadosCliente(clienteId);
        } else {
            // Trate o erro se o produtoId não for passado corretamente
            Toast.makeText(this, "Erro ao carregar o cliente", Toast.LENGTH_SHORT).show();
        }

        btnAtualizarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarCustomer();
            }
        });

    }

    private void carregarDadosCliente(int id) {
        Customer cliente = dbHelper.getCustomer(id);

        if (cliente != null) {
            edtNome.setText(cliente.getNome());
            edtTelefone.setText(cliente.getTelefone());
        } else {
            // Cliente não encontrado no banco
            Toast.makeText(this, "Cliente não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarCustomer() {
        String nome = edtNome.getText().toString().toUpperCase().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String telefoneValido = "^\\d{12}$";

        // Verificação de telefone usando regex
        if (!RegexUtils.matchesPattern(telefone, telefoneValido)) {
            Toast.makeText(this, "Telefone inválido. Tente algo como: 083996445577", Toast.LENGTH_LONG).show();
            return;
        }

        if (nome.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Customer cliente = new Customer(0, nome, telefone);
        dbHelper.updateCustomer(cliente); // Atualizar no banco de dados
        Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(UpdateCustomerActivity.this, MainActivity.class);
        //startActivity(intent);
        finish();
    }
}
