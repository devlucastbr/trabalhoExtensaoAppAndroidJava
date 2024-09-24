package com.example.fragranceflow.model;

import com.example.fragranceflow.utils.RegexUtils;

public class Customer {
    private int id;
    private String nome;
    private String telefone;

    public Customer(int id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isTelefoneValido() {
        String telefoneRegex = "^\\d{12}$";
        return RegexUtils.matchesPattern(telefone, telefoneRegex);
    }
}
