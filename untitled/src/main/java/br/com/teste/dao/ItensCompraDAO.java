package br.com.teste.dao;

import br.com.teste.jdbc.ConnectionFactory;
import br.com.teste.model.ItensCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItensCompraDAO {
    public int cadastrar(ItensCompra itensCompra) {
        String sql = "INSERT INTO tb_itens_compra (id_produto, id_compra, quantidade, preco_unitario) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itensCompra.getId_produto());
            stmt.setInt(2, itensCompra.getId_compra());
            stmt.setInt(3, itensCompra.getQuantidade());
            stmt.setFloat(4, itensCompra.getValor_unitario());
            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("id");
                }
                throw new RuntimeException("Não foi possível recuperar o id da compra cadastrada.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ItensCompra> buscarPorCompraEProduto(int idCompra, int idProduto) {
        ItensCompra itensCompra = null;
        String sql = "SELECT * FROM tb_itens_compra WHERE id_compra = ? AND id_produto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompra);
            stmt.setInt(2, idProduto);

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    int id = resultado.getInt("id");
                    int id_compra = resultado.getInt("id_compra");
                    int id_produto = resultado.getInt("id_produto");
                    int quantidade = resultado.getInt("quantidade");
                    float valor_unitario = resultado.getFloat("valor_unitario");
                    itensCompra = new ItensCompra(id, id_produto, id_compra, quantidade, valor_unitario);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(itensCompra);
    }

    public void somarQuantidade(int idItem, int quantidadeAdicional) {
        String sql = "UPDATE tb_itens_compra SET quantidade = quantidade + ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidadeAdicional);
            stmt.setInt(2, idItem);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItensCompra> listarPorCompra(int idCompra) {
        List<ItensCompra> itens = new ArrayList<>();
        String sql = "SELECT * FROM tb_itens_compra WHERE id_compra = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompra);

            try (ResultSet resultado = stmt.executeQuery()) {
                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    int id_produto = resultado.getInt("id_produto");
                    int quantidade = resultado.getInt("quantidade");
                    float valor_unitario = resultado.getFloat("valor_unitario");
                    itens.add(new ItensCompra(id, id_produto, idCompra, quantidade, valor_unitario));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }
}