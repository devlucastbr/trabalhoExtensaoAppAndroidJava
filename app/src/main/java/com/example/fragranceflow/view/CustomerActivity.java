package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.CustomerAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Customer;
import com.example.fragranceflow.model.Product;
import com.example.fragranceflow.utils.RegexUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends Activity {

    private DatabaseHelper dbHelper;

    private EditText edtNome, edtTelefone;
    private Button btnAddCliente;
    private ListView lstClientes;
    private List<Customer> clientesList;
    private ArrayAdapter<Customer> clientesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        dbHelper = new DatabaseHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        lstClientes = findViewById(R.id.lstClientes);
        btnAddCliente = findViewById(R.id.btnAddCliente);

        clientesList = new ArrayList<>();
        //produtosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtosList);
        clientesAdapter = new CustomerAdapter(this, clientesList);
        lstClientes.setAdapter(clientesAdapter);

        loadClientes();
        
        btnAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });

        lstClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pegue o cliente selecionado da lista
                Customer cliente = (Customer) parent.getItemAtPosition(position);

                // Inicie a EditarProdutoActivity e passe o id do produto, em name era produto_id
                Intent intent = new Intent(CustomerActivity.this, UpdateCustomerActivity.class);
                intent.putExtra("clienteId", cliente.getId());
                startActivity(intent); //Era:startActivity(intent); O '1' é o código de requisição para identificar o retorno
            }
        });

    }

    private void addCustomer() {
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
        try {
            long id = dbHelper.addCustomer(cliente);
            if (id > 0) {
                cliente.setId((int) id);
                clientesList.add(cliente);
                clientesAdapter.notifyDataSetChanged();
                edtNome.setText("");
                edtTelefone.setText("");
                Toast.makeText(this, "Cliente adicionado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao adicionar cliente", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Exibe uma mensagem de erro caso algo dê errado
            Toast.makeText(this, "Erro ao adicionar cliente: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void loadClientes() {
        clientesList.clear();
        clientesList.addAll(dbHelper.getAllCustomers());
        clientesAdapter.notifyDataSetChanged();
    }
}
