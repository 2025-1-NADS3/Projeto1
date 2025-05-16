package br.com.neonpay.neonpayacademy.model;

import java.io.Serializable;

// Classe Servico
public class Servico implements Serializable {
    private int id;
    private String imagemNome;
    private String nome;
    private String preco;

    // Construtor
    public Servico(int id, String imagemNome, String nome, String preco) {
        this.id = id;
        this.imagemNome = imagemNome;
        this.nome = nome;
        this.preco = preco;
    }

    // Get's da classe Servico
    public int getId() {
        return id;
    }

    public String getImagemNome() {
        return imagemNome;
    }

    public String getNome() {
        return nome;
    }

    public String getPreco() {
        return preco;
    }
}

