package br.com.teste.model;

import java.time.LocalDate;

public class Compras {

    private int id;
    private int id_user;
    private LocalDate data_compra;
    private float valor_total;

    public Compras() {}

    public Compras(int id_user, LocalDate data_compra, float valor_total) {
        this.id_user = id_user;
        this.data_compra = data_compra;
        this.valor_total = valor_total;
    }

    public Compras(int id, int id_user, LocalDate data_compra, float valor_total ) {
        this.valor_total = valor_total;
        this.data_compra = data_compra;
        this.id_user = id_user;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public LocalDate getData_compra() {
        return data_compra;
    }

    public void setData_compra(LocalDate data_compra) {
        this.data_compra = data_compra;
    }

    public float getValor_total() {
        return valor_total;
    }

    public void setValor_total(float valor_total) {
        this.valor_total = valor_total;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Usuário ID: " + id_user +
                " | Data: " + data_compra + " | Total: " + valor_total;
    }
}

