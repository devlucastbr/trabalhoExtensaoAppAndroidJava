package com.example.fragranceflow.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Sale {
    private int id;
    private int cliente_id;
    private int produto_id;
    private int quantidade;
    private String data_venda;

    public Sale(int id, int cliente_id, int produto_id, int quantidade, String data_venda) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.produto_id = produto_id;
        this.quantidade = quantidade;
        this.data_venda = data_venda;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return cliente_id;
    }

    public void setClienteId(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getProdutoId() {
        return produto_id;
    }

    public void setProdutoId(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataVenda() {
        return data_venda;
    }

    public void setDataVenda(String data_venda) {
        this.data_venda = data_venda;
    }

    // Método para obter a data de venda no formato dd/MM/yyyy
    public String getDataVendaFormatada() {
        if (this.data_venda == null || this.data_venda.isEmpty()) {
            return "Sem data de venda";  // Tratamento para validade nula ou vazia
        }

        // Formato ISO 8601 (armazenado no banco)
        SimpleDateFormat formatoIso = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Formato de saída para exibir (dd/MM/yyyy)
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Converte a data de venda de string para Date e depois formata para dd/MM/yyyy
            Date data = formatoIso.parse(this.data_venda);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Data inválida";  // Em caso de erro de formatação
        }
    }

}
