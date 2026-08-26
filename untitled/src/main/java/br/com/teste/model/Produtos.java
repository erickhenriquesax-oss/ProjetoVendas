package br.com.teste.model;

public class Produtos {

    private int id;
    private String nome;
    private float preco;

    public Produtos() {}

    public Produtos(float preco, String nome, int id) {
        this.preco = preco;
        this.nome = nome;
        this.id = id;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
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
}
