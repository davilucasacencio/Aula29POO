package br.com.bookstore.main;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;

import java.util.List;

/**
 * Roteiro canônico de testes da BookStore.
 */
public class Main {

    public static void main(String[] args) {

        // Inversão de Dependência (DIP): a variável é do tipo do CONTRATO, não da implementação.
        LivroDAO dao = new LivroDAOSqlite();

        // 1) Persiste 3 obras e confere se os IDs 1, 2 e 3 foram gerados.
        System.out.println("=== 1) Cadastrando livros ===");
        Livro l1 = new Livro("Clean Code", "Robert C. Martin", 89.90);
        Livro l2 = new Livro("Design Patterns", "Erich Gamma", 129.50);
        Livro l3 = new Livro("Refactoring", "Martin Fowler", 110.00);

        dao.salvar(l1);
        dao.salvar(l2);
        dao.salvar(l3);

        System.out.println("ID gerado para '" + l1.getTitulo() + "': " + l1.getId());
        System.out.println("ID gerado para '" + l2.getTitulo() + "': " + l2.getId());
        System.out.println("ID gerado para '" + l3.getTitulo() + "': " + l3.getId());

        // 2) Consulta o ID 2 com o estilo funcional do Optional.
        System.out.println("\n=== 2) Buscando o livro de ID 2 ===");
        dao.buscarPorId(2L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Aviso: nenhum livro encontrado com o ID 2.")
        );

        // 3) Consulta um ID inexistente: sem NullPointerException, só um aviso amigável.
        System.out.println("\n=== 3) Buscando um ID inexistente (999) ===");
        dao.buscarPorId(999L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Aviso: nenhum livro encontrado com o ID 999 (sem NullPointerException!).")
        );

        // 4) Lista tudo com cabeçalho tabular.
        System.out.println("\n=== 4) Catálogo completo ===");
        List<Livro> todos = dao.listarTodos();
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-8s | %-20s | %-25s | %s%n", "ID", "TÍTULO", "AUTOR", "PREÇO");
        System.out.println("-------------------------------------------------------------------------");
        todos.forEach(System.out::println);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Total de livros: " + todos.size());
    }
}
