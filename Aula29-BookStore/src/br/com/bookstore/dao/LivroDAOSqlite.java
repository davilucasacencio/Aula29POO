package br.com.bookstore.dao;

import br.com.bookstore.factory.ConexaoFactory;
import br.com.bookstore.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * INFRAESTRUTURA: implementação do contrato LivroDAO usando JDBC + SQLite.
 */
public class LivroDAOSqlite implements LivroDAO {

    // Inicialização determinística: garante que a tabela exista assim que o DAO é criado.
    public LivroDAOSqlite() {
        String sql = "CREATE TABLE IF NOT EXISTS livros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, "
                + "preco REAL NOT NULL);";

        try (Connection conn = ConexaoFactory.obterConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela livros: " + e.getMessage(), e);
        }
    }

    // Mapeamento centralizado: transforma a linha atual do ResultSet em um objeto Livro.
    private Livro mapearLinha(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        double preco = rs.getDouble("preco");
        return new Livro(id, titulo, autor, preco);
    }

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, preco) VALUES (?, ?, ?);";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Parâmetros começam no índice 1 e são tipados (evita SQL Injection).
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setDouble(3, livro.getPreco());

            stmt.executeUpdate();

            // Resgata o ID autoincrementado e sincroniza com o objeto.
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    livro.setId(chaves.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String sql = "SELECT id, titulo, autor, preco FROM livros WHERE id = ?;";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                // Achou: devolve o livro dentro da "caixinha" Optional. Não achou: caixinha vazia.
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar livro por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, preco FROM livros ORDER BY id;";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                livros.add(mapearLinha(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }

        return livros;
    }
}
