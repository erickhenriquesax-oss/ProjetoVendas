package br.com.teste.dao;

import br.com.teste.jdbc.ConnectionFactory;
import br.com.teste.model.Produtos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutosDAO {
    public void cadastrar(Produtos prod){
        String sql = "INSERT INTO tb_produtos (nome, valor) VALUES (?, ?)";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, prod.getNome());
            stmt.setFloat(2, prod.getValor());
            stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Produtos> listarTodos(){
        String sql = "SELECT * FROM tb_produtos";
        List<Produtos> produtos = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()){
            while (resultado.next()){
                int id = resultado.getInt("id");
                String nome = resultado.getString("nome");
                float valor = resultado.getFloat("valor");
                produtos.add(new Produtos(valor, nome, id));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return produtos;
    }

    public Optional<Produtos> buscarPorId(int id){
        Produtos produto = null;
        String sql = "SELECT * FROM tb_produtos WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);

            try (ResultSet resultado = stmt.executeQuery()){
                if (resultado.next()){
                    String nome = resultado.getString("nome");
                    float valor = resultado.getFloat("valor");
                    produto = new Produtos(valor, nome, id);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(produto);
    }

    public void atualizar(Produtos prod){
        String sql =  "UPDATE tb_produtos SET nome=?, valor=? WHERE id=?";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, prod.getNome());
            stmt.setFloat(2, prod.getValor());
            stmt.setInt(3, prod.getId());
            stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void excluir(int id){
        String sql = "DELETE FROM tb_produtos WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.execute();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
