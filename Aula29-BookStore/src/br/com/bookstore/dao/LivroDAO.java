package br.com.bookstore.dao;

import br.com.bookstore.model.Livro;

import java.util.List;
import java.util.Optional;

/**
 * CONTRATO DAO: a camada de negócio depende só desta interface (DIP),
 * sem saber se os dados vêm de SQLite, PostgreSQL ou de um Mock em memória.
 */
public interface LivroDAO {

    void salvar(Livro livro);

    Optional<Livro> buscarPorId(Long id);

    List<Livro> listarTodos();
}
