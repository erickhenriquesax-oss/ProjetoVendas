package br.com.teste.model;

public class Produtos {

    private int id;
    private String nome;
    private float valor;

    public Produtos() {}

    public Produtos(float valor, String nome, int id) {
        this.valor = valor;
        this.nome = nome;
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float preco) {
        this.valor = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Valor: " + valor;
    }
}
