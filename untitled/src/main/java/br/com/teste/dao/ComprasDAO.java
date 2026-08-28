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

    public void cadastrar(Compras compras) {
        String sql = "INSERT INTO compras (id_user, data_compra, valor_total) values (?, ?, null)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, compras.getId_user());
            stmt.setString(2, compras.getData_compra().toString());
            stmt.setFloat(3, compras.getValor_total());
            stmt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Compras> listarTodasCompras() {
        String sql = "SELECT * FROM compras";
        List<Compras> compras = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {
                int id = resultado.getInt("id");
                LocalDate data = resultado.getDate("data_compra").toLocalDate();
                float valor_total = resultado.getFloat("valor_total");
                compras.add(new Compras(id, data, valor_total));
            }

        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return compras;
    }

    public Optional<Compras> buscarPorId(int id) {
        Compras compras = null;
        String sql = "SELECT * FROM compras WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try(ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {

                }
            }

        }catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
