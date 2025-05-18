package br.com.neonpay.neonpayacademy.model;

import java.io.Serializable;

// Classe Product
public class Product implements Serializable {

    private int id;
    private String nome;
    private String pontos;
    private String descricao;
    private String imagemNome;

    // Construtor
    public Product(int id, String nome, String preco, String descricao, String imagemNome) {
        this.id = id;
        this.nome = nome;
        this.pontos = preco;
        this.descricao = descricao;
        this.imagemNome = imagemNome;
    }

    // Get's da classe Product
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public String getPontos() {
        return pontos;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getImagemNome() {
        return imagemNome;
    }

}
