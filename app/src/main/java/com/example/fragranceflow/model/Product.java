package com.example.fragranceflow.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product {
    private int id;
    private String nome;
    private int quantidade;
    private String marca;
    private float custo_unitario;
    private int margem_lucro;
    private float valor_venda;
    private String categoria;
    private String volume;
    private String validade;


    public Product(int id, String nome, int quantidade, String marca, float custo_unitario, int margem_lucro, float valor_venda, String categoria, String volume, String validade) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.marca = marca;
        this.custo_unitario = custo_unitario;
        this.margem_lucro = margem_lucro;
        this.valor_venda = valor_venda;
        this.categoria = categoria;
        this.volume = volume;
        this.validade = validade;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public float getCustoUnitario() {
        return custo_unitario;
    }

    public void setCustoUnitario(float custo_unitario) {
        this.custo_unitario = custo_unitario;
    }

    public int getMargemLucro() {
        return margem_lucro;
    }

    public void setMargemLucro(int margem_lucro) {
        this.margem_lucro = margem_lucro;
    }

    public float getValorVenda() {
        return valor_venda;
    }

    public void setValorVenda(float valor_venda) {
        this.valor_venda = valor_venda;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    // Método para obter a validade no formato dd/MM/yyyy
    public String getValidadeFormatada() {
        if (this.validade == null || this.validade.isEmpty()) {
            return "Sem validade";  // Tratamento para validade nula ou vazia
        }

        // Formato ISO 8601 (armazenado no banco)
        SimpleDateFormat formatoIso = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Formato de saída para exibir (dd/MM/yyyy)
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Converte a validade de string para Date e depois formata para dd/MM/yyyy
            Date data = formatoIso.parse(this.validade);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Data inválida";  // Em caso de erro de formatação
        }
    }
}
