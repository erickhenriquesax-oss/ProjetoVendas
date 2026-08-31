# Sistema de Vendas — Java + Maven + PostgreSQL

Sistema de console para gerenciamento de vendas, desenvolvido em Java com JDBC puro (sem ORM), Maven como gerenciador de dependências e PostgreSQL como banco de dados. O projeto utiliza conceitos de programação funcional (`Optional`, `Stream`, method references, expressões lambda) e segue uma arquitetura em camadas (model, dao, main).
Não há front-end ou interface, o objetivo do projeto é fixação e melhoria dos conhecimentos obtidos até então

## Índice

- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Modelo de dados](#modelo-de-dados)
- [Estrutura de pacotes](#estrutura-de-pacotes)
- [Configuração do ambiente](#configuração-do-ambiente)
- [Como executar](#como-executar)
- [Funcionalidades](#funcionalidades)
- [Fluxo de realização de compra](#fluxo-de-realização-de-compra)
- [Decisões de negócio](#decisões-de-negócio)
- [Roadmap / pendências](#roadmap--pendências)

## Tecnologias

- **Java 25** (JDK)
- **Maven** — gerenciamento de dependências e build
- **PostgreSQL** — banco de dados relacional
- **JDBC puro** (driver `org.postgresql:postgresql:42.7.8`) — sem uso de ORM/JPA
- **Programação funcional** — `Optional`, `Stream`, lambdas, method references, `switch` com arrow syntax

## Arquitetura

O projeto segue uma separação simples em camadas:

```
main - interação com o usuário via console (Scanner) e orquestração dos fluxos
dao - acesso a dados (CRUD via PreparedStatement)
model - classes de domínio (POJOs)
jdbc - fábrica de conexões com o banco
```

Não há uso de framework de injeção de dependência: cada DAO abre e fecha sua própria `Connection` a cada operação, via `try-with-resources`.

## Modelo de dados

### Entidades e relacionamentos

- **User** (`tb_users`) — cliente do sistema.
- **Produtos** (`tb_produtos`) — catálogo de produtos, com preço que pode variar ao longo do tempo.
- **Compras** (`tb_compras`) — cabeçalho de uma venda: usuário, data e valor total.
- **ItensCompra** (`tb_itens_compra`) — itens de uma compra: produto, quantidade e valor unitário "congelado" no momento da venda.

```
User (1) ────< Compras (1) ────< ItensCompra >──── (1) Produtos
```

- Um usuário pode ter várias compras.
- Uma compra tem vários itens.
- Um produto pode aparecer em vários itens, de várias compras diferentes.

### Tabelas

**tb_users**
| Coluna | Tipo      |
|--------|-----------|
| id     | integer (PK) |
| nome   | varchar   |
| cpf    | varchar   |

**tb_produtos**
| Coluna | Tipo      |
|--------|-----------|
| id     | integer (PK) |
| nome   | varchar   |
| valor  | real      |

**tb_compras**
| Coluna       | Tipo    |
|--------------|---------|
| id           | integer (PK) |
| id_user      | integer (FK → tb_users) |
| data_compra  | date    |
| valor_total  | numeric(10,2) — **aceita NULL** |

**tb_itens_compra**
| Coluna          | Tipo    |
|-----------------|---------|
| id              | integer (PK) |
| id_produto      | integer (FK → tb_produtos) |
| id_compra       | integer (FK → tb_compras) |
| quantidade      | integer |
| preco_unitario  | numeric |

> ⚠️ **Atenção ao nome da coluna:** na tabela `tb_itens_compra`, a coluna de preço se chama `preco_unitario` no banco, mas o campo correspondente no model Java é `valor_unitario` (`ItensCompra.getValor_unitario()`). O código já está ajustado para essa divergência — ao alterar o SQL dos métodos de `ItensCompraDAO`, sempre usar a string `"preco_unitario"` nas queries.

## Estrutura de pacotes

```
br.com.teste
├── main
│   └── Main.java              // menu principal e orquestração dos fluxos
├── model
│   ├── User.java
│   ├── Produtos.java
│   ├── Compras.java
│   └── ItensCompra.java
├── dao
│   ├── UserDAO.java
│   ├── ProdutosDAO.java
│   ├── ComprasDAO.java
│   └── ItensCompraDAO.java
└── jdbc
    └── ConnectionFactory.java
```

## Configuração do ambiente

A conexão com o banco é feita via `ConnectionFactory`, que lê as credenciais de variáveis de ambiente


## Funcionalidades

### Menu Usuário
- Cadastrar usuário (nome, CPF)
- Listar todos os usuários
- Buscar usuário por ID
- Atualizar usuário (nome/CPF, com opção de manter valor atual deixando em branco)
- Deletar usuário (com opção de deletar vários em sequência)

### Menu Produtos
- Cadastrar produto (nome, preço)
- Listar todos os produtos
- Buscar produto por ID
- Atualizar produto (nome/valor, com opção de manter valor atual deixando em branco)
- Deletar produto (com opção de deletar vários em sequência)

### Menu Compras
- **Realizar compra** — fluxo completo descrito abaixo.
- Listar compras por ID *(em desenvolvimento)*
- Buscar por compra e produto *(em desenvolvimento)*

## Fluxo de realização de compra

O fluxo de "Realizar Compra" foi desenhado para não depender de cálculos feitos em memória até o fim — o valor total só é fechado depois que os itens já estão persistidos no banco:

1. **Abertura da compra**: o usuário informa o `id` do cliente. A compra é imediatamente inserida em `tb_compras` com `data_compra = LocalDate.now()` e **`valor_total = NULL`**. O `id` gerado pelo banco (via `RETURNING id`) é capturado e usado no restante do fluxo.

2. **Loop de itens**: para cada produto informado:
   - Busca o produto no catálogo para obter o preço **atual**.
   - Verifica se aquele produto **já está** na compra (`buscarPorCompraEProduto`):
     - **Se já existe** → soma a quantidade nova à existente (`somarQuantidade`), **sem alterar o valor unitário já gravado** (o preço fica "congelado" no momento da primeira inserção daquele produto naquela compra).
     - **Se não existe** → insere um novo item, copiando o preço atual do produto para `valor_unitario`.
   - O usuário pode repetir esse passo quantas vezes quiser, até optar por encerrar.

3. **Fechamento da compra**: ao encerrar,
   - Busca todos os itens gravados daquela compra (`listarPorCompra`).
   - Calcula o valor total somando `quantidade × valor_unitario` de cada item (via `Stream.mapToDouble(...).sum()`).
   - Grava o total calculado em `tb_compras` (`atualizarValorTotal`).
   - Exibe um resumo na tela: ID da compra, nome de cada produto comprado, quantidade, valor unitário e valor total.

## Decisões de negócio

Algumas regras foram definidas explicitamente ao longo do desenvolvimento e devem ser respeitadas em qualquer evolução futura do sistema:

- **Valor total nasce nulo**: a compra é criada primeiro, sem total, e só é atualizada depois que todos os itens já foram gravados. Isso exige que a coluna `valor_total` aceite `NULL`.
- **Produto repetido na mesma compra soma a quantidade** em vez de criar uma nova linha — e o `valor_unitario` da linha original é mantido intacto, mesmo que o preço atual do produto tenha mudado entre as duas adições.

## Roadmap / pendências

- [ ] Implementar `listarComprasPorId` no menu de compras.
- [ ] Implementar listagem de todas as compras (`ComprasDAO.listarTodasCompras()` já existe e está pronto para uso).
- [ ] Adicionar `case 0` e tratamento de opção inválida no `switch` do `menuCompras()` — hoje o loop não tem saída própria além de fechar o programa.
- [ ] Revisar tratamento de exceções em `cadastrarProduto()` — hoje captura qualquer `Exception` e exibe apenas "Erro ao cadastrar!" sem detalhar a causa real, o que pode mascarar bugs (como já ocorreu com um problema de `Locale` no `Scanner`).
- [ ] Avaliar uso de transação (commit/rollback) no fluxo de "Realizar Compra": hoje cada `INSERT`/`UPDATE` abre e fecha sua própria conexão; uma falha no meio do processo pode deixar dados parcialmente gravados.
- [ ] Considerar extrair a lógica de "montar resumo de compra" (usada ao final de `realizarCompra`) em um método reutilizável, já que será necessária também nas telas de listagem de compras.