public class ItemPedido {

    Produto produto;
    int quantidade;
    int quantidadePaga = 0;

    public ItemPedido(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto invalido");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double subtotal() {
        return produto.preco * quantidade;
    }

    public double valorEmAberto() {
        return produto.preco * (quantidade - quantidadePaga);
    }

    public void pagarQuantidade(int qtd) {
        if (qtd <= 0) {
            throw new IllegalArgumentException("Quantidade invalida para pagamento");
        }
        if (quantidadePaga + qtd > quantidade) {
            throw new IllegalArgumentException("Pagamento excede quantidade restante");
        }
        quantidadePaga += qtd;
    }

    public boolean estaPago() {
        return quantidadePaga == quantidade;
    }
}
