package br.com.neonpay.neonpayacademy.model;

public class ExtractItem {
    public enum Tipo {
        ENTRADA, SAIDA
    }

    private String descricao;
    private String data;
    private String valor;
    private Tipo tipo;
    private String chavePix;

    public ExtractItem(String descricao, String data, String valor, Tipo tipo, String chavePix) {
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.chavePix = chavePix;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return data;
    }

    public String getValor() {
        return valor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getChavePix() {
        return chavePix;
    }
}
