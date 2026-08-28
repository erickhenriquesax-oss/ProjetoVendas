package br.com.teste.model;

public class ItensCompra {

    private int id;
    private int id_produto;
    private int id_compra;
    private int quantidade;
    private float valor_unitario;

    public ItensCompra() {}

    public ItensCompra(int id_produto, int id_compra, int quantidade, float valor_unitario) {
        this.id_produto = id_produto;
        this.id_compra = id_compra;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
    }

    public ItensCompra(int id, int id_produto, int id_compra, int quantidade, float valor_unitario) {
        this.id = id;
        this.id_produto = id_produto;
        this.id_compra = id_compra;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
    }

    public int getId() {
        return id;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getValor_unitario() {
        return valor_unitario;
    }

    public void setValor_unitario(float valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Produto ID: " + id_produto +
                " | Compra ID: " + id_compra +
                " | Quantidade: " + quantidade +
                " | Valor Unitário: " + valor_unitario;
    }
}