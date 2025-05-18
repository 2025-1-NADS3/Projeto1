package br.com.bankpay.bankpayacademy.model;

import java.io.Serializable;

// Classe Canteen
public class Canteen implements Serializable {
    private int id;
    private String nome;
    private String preco;
    private String descricao;
    private String imagemNome;

    // Construtor
    public Canteen(int id, String nome, String preco, String descricao, String imagemNome) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.imagemNome = imagemNome;
    }

    // Get's da classe Canteen
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public String getPreco() {
        return preco;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getImagemNome() {
        return imagemNome;
    }
}
