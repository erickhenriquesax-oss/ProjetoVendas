package br.com.teste.main;
import br.com.teste.dao.UserDAO;
import br.com.teste.jdbc.ConnectionFactory;
import br.com.teste.model.User;


import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public final static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection()) {

            System.out.println("Conexão realizada com sucesso!");

        } catch (Exception e) {
            System.out.println("Não foi possível conectar ao banco.");
            e.printStackTrace();
        }
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();
            scan.nextLine(); // Limpa o buffer do teclado

            switch(opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    listarTodos(); // chama o seu novo método
                    break;
                case 3:
                    listarUsuarioPorId(); // chama o seu novo método
                    break;
                case 0:
                    System.out.println("Tchau!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        };
        }

    private static void listarTodos() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.listarTodos();

        users.forEach(System.out::println);
    }

    public static void cadastrarUsuario() {
        User user = new User();
        System.out.println("Digite o nome do usuario: ");
        user.setNome(scan.nextLine());
        System.out.println("Digite o CPF do user: ");
        user.setCpf(scan.nextLine());

        UserDAO userDAO = new UserDAO();
        userDAO.cadastrar(user);
    }

    public static void listarUsuarioPorId() {
        UserDAO userDAO = new UserDAO();
        System.out.println("Digite o id do usuario: ");
        int id = scan.nextInt();
        scan.nextLine();
        userDAO.listarPorId(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Usuário não encontrado.")
                );
    }
}
