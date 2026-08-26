package br.com.teste.dao;

import br.com.teste.model.User;
import br.com.teste.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public void cadastrar(User user) {
        String sql = "INSERT INTO tb_users(nome, cpf) values (?, ?)";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getCpf());
            stmt.execute();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }

    public List<User> listarTodos() {
        String sql = "SELECT * FROM tb_users";
        List<User> users = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()){

            while (resultado.next()){
                int id =  resultado.getInt("id");
                String nome = resultado.getString("nome");
                String cpf = resultado.getString("cpf");
                users.add(new User(id, nome, cpf));
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    return users;
    }

    public Optional<User> listarPorId(int id){
        User user = null;
        String sql = "SELECT * FROM tb_users WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    user =  new User(id, nome, cpf);
                }
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return Optional.ofNullable(user);
    }
}
