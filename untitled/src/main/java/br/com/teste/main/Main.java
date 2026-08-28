package br.com.teste.main;

import br.com.teste.dao.ProdutosDAO;
import br.com.teste.dao.UserDAO;
import br.com.teste.jdbc.ConnectionFactory;
import br.com.teste.model.Produtos;
import br.com.teste.model.User;

import java.sql.Connection;
import java.util.List;
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
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Menu Usuário");
            System.out.println("2. Menu Produtos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();       // ← leitura que estava faltando
            scan.nextLine();              // limpa o buffer

            switch (opcao) {
                case 1:
                    menuUsuario();
                    break;
                case 2:
                    menuProdutos();
                    break;
                case 0:
                    System.out.println("Tchau!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // ===================== MENU USUÁRIO =====================
    private static void menuUsuario() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU USUÁRIO ===");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar Usuário");
            System.out.println("5. Deletar Usuário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1 -> cadastrarUsuario();
                case 2 -> listarTodosUsuarios();
                case 3 -> listarUsuarioPorId();
                case 4 -> atualizarUser();
                case 5 -> deletarUsuario();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ===================== MENU PRODUTOS =====================
    private static void menuProdutos() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU PRODUTOS ===");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar Produto");
            System.out.println("5. Deletar Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarTodosProdutos();
                case 3 -> listarProdutoPorId();
                case 4 -> atualizarProduto();
                case 5 -> deletarProduto();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ================= AÇÕES DE PRODUTOS ===================
    private static void deletarProduto() {
        int opcao = -1;
        while (opcao != 0) {
            listarTodosProdutos();
            System.out.println("Digite o id do produto que deseja deletar");
            int id = scan.nextInt();
            scan.nextLine();

            ProdutosDAO prodDAO = new ProdutosDAO();
            prodDAO.excluir(id);
            System.out.println("Produto removido com sucesso!");

            System.out.println("Deseja deletar mais algum produto?");
            System.out.println("1. Deletar mais algum produto");
            System.out.println("0. Voltar");
            opcao = scan.nextInt();
            scan.nextLine();
        }
    }

    private static void atualizarProduto() {
        listarTodosProdutos();
        System.out.println("Digite o id do produto: ");
        int id = scan.nextInt();
        scan.nextLine();
        ProdutosDAO prodDAO = new ProdutosDAO();

        Produtos prodAtual = prodDAO.buscarPorId(id).orElse(null);

        if (prodAtual == null) {
            System.out.println("Produto não encontrado");
            return;
        }

        System.out.println("Novo nome [" + prodAtual.getNome() + "]: ");
        String novoNome = scan.nextLine();
        if (novoNome.isBlank()) {
            novoNome = prodAtual.getNome();
        }

        System.out.println("Novo valor [" + prodAtual.getValor() + "]: ");
        float novoValorStr = scan.nextFloat();
        float novoValor = novoValorStr;


        Produtos prodAtualizado = new Produtos(novoValor, novoNome, id);
        prodDAO.atualizar(prodAtualizado);
        System.out.println("Produto atualizado com sucesso!");
    }

    public static void listarProdutoPorId() {
        System.out.println("Digite o id do produto: ");
        int id = scan.nextInt();
        scan.nextLine();

        ProdutosDAO prodDAO = new ProdutosDAO();
        prodDAO.buscarPorId(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Produto não encontrado")
                );
    }
    public static void cadastrarProduto(){
        try {
            Produtos prod = new Produtos();
            System.out.println("Nome do produto:");
            prod.setNome(scan.nextLine());
            System.out.println("Preco do produto:");
            prod.setValor(scan.nextFloat());

            ProdutosDAO prodDAO = new ProdutosDAO();
            prodDAO.cadastrar(prod);
            System.out.println("Produto cadastrado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao cadastrar!");
        }
    }

    private static void listarTodosProdutos() {
        ProdutosDAO prodDAO = new ProdutosDAO();
        List<Produtos> prods = prodDAO.listarTodos();
        prods.forEach(System.out::println);
    }

    // ===================== AÇÕES DE USUÁRIO =====================
    public static void cadastrarUsuario() {
        User user = new User();
        System.out.println("Digite o nome do usuário: ");
        user.setNome(scan.nextLine());
        System.out.println("Digite o CPF do usuário: ");
        user.setCpf(scan.nextLine());

        UserDAO userDAO = new UserDAO();
        userDAO.cadastrar(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void listarTodosUsuarios() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.listarTodos();
        users.forEach(System.out::println);
    }

    public static void listarUsuarioPorId() {
        System.out.println("Digite o id do usuário: ");
        int id = scan.nextInt();
        scan.nextLine();

        UserDAO userDAO = new UserDAO();
        userDAO.listarPorId(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Usuário não encontrado.")
                );
    }

    private static void deletarUsuario() {
        int opcao = -1;
        while (opcao != 0) {
            listarTodosUsuarios();
            System.out.println("Digite o id do usuário deseja deletar");
            int id = scan.nextInt();
            scan.nextLine();

            UserDAO userDAO = new UserDAO();
            userDAO.excluir(id);
            System.out.println("Usuário removido com sucesso!");

            System.out.println("Deseja deletar mais algum usuário?");
            System.out.println("1. Deletar mais algum usuario");
            System.out.println("0. Voltar");
            opcao = scan.nextInt();
            scan.nextLine();
        }
    }

    private static void atualizarUser() {
        listarTodosUsuarios();
        System.out.println("Digite o id do Usuário ");
        int id = scan.nextInt();
        scan.nextLine();
        UserDAO userDAO = new UserDAO();

        User userAtual = userDAO.listarPorId(id).orElse(null);

        if (userAtual == null) {
            System.out.println("Produto não encontrado");
            return;
        }

        System.out.println("Novo nome [" + userAtual.getNome() + "]: ");
        String novoNome = scan.nextLine();
        if (novoNome.isBlank()) {
            novoNome = userAtual.getNome();
        }

        System.out.println("Novo CPF [" + userAtual.getCpf() + "]: ");
        String novoCPF = scan.nextLine();


        User userAtualizado = new User(id, novoNome, novoCPF);
        userDAO.atualizar(userAtualizado);
        System.out.println("Usuário atualizado com sucesso!");
    }
}
