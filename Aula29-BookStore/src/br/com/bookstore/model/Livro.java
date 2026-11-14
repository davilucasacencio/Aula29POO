package br.com.bookstore.model;

/**
 * CAMADA MODEL: representa um livro do catálogo da BookStore.
 * Não conhece JDBC nem SQL (responsabilidade única).
 */
public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private double preco;

    // Construtor completo: usado pelo DAO para hidratar uma linha vinda do banco.
    public Livro(Long id, String titulo, String autor, double preco) {
        validar(titulo, autor, preco);
        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.preco = preco;
    }

    // Construtor de negócio: livro novo, ainda sem ID (o banco gera depois).
    public Livro(String titulo, String autor, double preco) {
        this(null, titulo, autor, preco);
    }

    // Blindagem de invariantes (Fail-Fast): impede a criação de livros inválidos.
    private static void validar(String titulo, String autor, double preco) {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("O título deve ter pelo menos 2 caracteres.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor não pode ser nulo ou vazio.");
        }
        if (preco <= 0.0) {
            throw new IllegalArgumentException("O preço de venda deve ser estritamente positivo");
        }
    }

    public Long getId() {
        return id;
    }

    // Único setter permitido: sincroniza a chave primária gerada pelo banco.
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3s | %-20s | %-25s | R$ %8.2f",
                id != null ? id : "-", titulo, autor, preco);
    }
}
