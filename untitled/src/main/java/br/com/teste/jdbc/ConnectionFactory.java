package br.com.teste.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Fallbacks para campos não sensíveis
        if (url == null || url.isEmpty()) {
            url = "jdbc:postgresql://localhost:5432/testeErick";
        }
        if (user == null || user.isEmpty()) {
            user = "postgres";
        }
        
        // Validação de segurança para a senha
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Erro de Segurança: A variável de ambiente 'DB_PASSWORD' não está configurada!");
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}