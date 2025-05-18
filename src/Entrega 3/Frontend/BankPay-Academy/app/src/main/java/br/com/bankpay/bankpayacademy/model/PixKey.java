package br.com.bankpay.bankpayacademy.model;

// Classe PixKey
public class PixKey {
    private String tipo;
    private String valor;

    // Construtor
    public PixKey(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    // Get's da classe PixKey
    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
}
