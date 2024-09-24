package com.example.fragranceflow.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragranceflow.R;
import com.example.fragranceflow.adapter.SaleAdapter;
import com.example.fragranceflow.controller.DatabaseHelper;
import com.example.fragranceflow.model.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleListActivity extends Activity {
    private DatabaseHelper dbHelper;
    private ListView lstVendas;
    private List<Sale> vendasList;
    private ArrayAdapter<Sale> vendasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

        dbHelper = new DatabaseHelper(this);
        lstVendas = findViewById(R.id.lstVendas);

        vendasList = new ArrayList<>();
        //produtosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtosList);
        vendasAdapter = new SaleAdapter(this, vendasList);
        lstVendas.setAdapter(vendasAdapter);

        loadVendas();

        lstVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sale venda = vendasList.get(position);
                Toast.makeText(SaleListActivity.this, "Id Venda: " + venda.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadVendas() {
        vendasList.clear();
        vendasList.addAll(dbHelper.getAllSales());
        vendasAdapter.notifyDataSetChanged();
    }
}
