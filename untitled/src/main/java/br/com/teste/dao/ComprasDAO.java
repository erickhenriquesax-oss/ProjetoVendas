package br.com.teste.dao;

import br.com.teste.jdbc.ConnectionFactory;
import br.com.teste.model.Compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComprasDAO {

    public int cadastrar(Compras compras) {
        String sql = "INSERT INTO tb_compras (id_user, data_compra, valor_total) values (?, ?, null) RETURNING id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, compras.getId_user());
            stmt.setDate(2, java.sql.Date.valueOf(compras.getData_compra()));

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("id");
                }
                throw new RuntimeException("Não foi possível recuperar o id da compra cadastrada.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Compras> listarTodasCompras() {
        String sql = "SELECT * FROM tb_compras";
        List<Compras> compras = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {
                int id = resultado.getInt("id");
                int id_user = resultado.getInt("id_user");
                LocalDate data = resultado.getDate("data_compra").toLocalDate();
                float valor_total = resultado.getFloat("valor_total");
                compras.add(new Compras(id, id_user, data, valor_total));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return compras;
    }

    public Optional<Compras> buscarPorId(int id) {
        Compras compras = null;
        String sql = "SELECT * FROM tb_compras WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    int id_user = resultado.getInt("id_user");
                    String data_compra = resultado.getString("data_compra");
                    float valor_total = resultado.getFloat("valor_total");
                    compras = new Compras(id, id_user, LocalDate.parse(data_compra), valor_total);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(compras);
    }

    public void excluir(int id) {
        String sql = "DELETE FROM tb_compras WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarValorTotal(int id, float valor_total) {
        String sql = "UPDATE tb_compras SET valor_total = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, valor_total);
            stmt.setInt(2, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}