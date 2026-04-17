public class Produto {

    String nome;
    double preco;
    String categoria;

    public Produto(String nome, double preco, String categoria) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preco esta negativo, e nao pode ser negativo");
        }
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }
}
