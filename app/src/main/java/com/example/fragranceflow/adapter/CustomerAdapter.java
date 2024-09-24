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
import com.example.fragranceflow.model.Customer;
import com.example.fragranceflow.model.Product;
import com.example.fragranceflow.view.UpdateCustomerActivity;
import com.example.fragranceflow.view.UpdateProductActivity;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    public CustomerAdapter(Context context, List<Customer> customers) {
        super(context, 0, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutiliza a view existente se possível
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_customer, parent, false);
        }

        // Encontrar o botão de editar
        Button btnUpdateCliente = convertView.findViewById(R.id.btnUpdateCliente);

        // Adicionar o listener ao botão de editar
        btnUpdateCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter o produto correspondente
                Customer cliente = getItem(position);

                // Criar um Intent para abrir a EditarClienteActivity
                Intent intent = new Intent(getContext(), UpdateCustomerActivity.class);

                // Passar o ID ou dados do produto para a atividade de edição, em name era produto_id
                intent.putExtra("clienteId", cliente.getId());

                // Iniciar a atividade
                getContext().startActivity(intent);
            }
        });

        // Obtém o produto para a posição atual
        Customer customer = getItem(position);

        // Configura os campos do layout
        TextView textNome = convertView.findViewById(R.id.textNome);
        TextView textClienteId = convertView.findViewById(R.id.textClienteId);
        TextView textTelefone = convertView.findViewById(R.id.textTelefone);

        // Define o texto dos campos com os dados do produto
        textNome.setText(customer.getNome());
        textClienteId.setText("Código do cliente: " + customer.getId());
        textTelefone.setText("Telefone: " + customer.getTelefone());

        return convertView;
    }
}
