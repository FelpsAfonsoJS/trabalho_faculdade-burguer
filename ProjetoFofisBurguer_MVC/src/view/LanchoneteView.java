package view;

import model.ItemPedido;
import model.Pedido;
import model.Produto;

import java.util.ArrayList;
import java.util.Scanner;

public class LanchoneteView {

    private final Scanner sc;

    public LanchoneteView(Scanner sc) {
        this.sc = sc;
    }

    //menu principal
    public int exibirMenuPrincipal() {
        System.out.println("\n   Lanchonete Fofis Burguer  ");
        System.out.println("1 - Novo Pedido");
        System.out.println("2 - Adicionar itens a pedido em aberto");
        System.out.println("3 - Efetuar pagamento");
        System.out.println("4 - Ver pedidos em aberto");
        System.out.println("5 - Ver pedidos parcialmente pagos");
        System.out.println("6 - Ver pedidos pagos");
        System.out.println("7 - Faturamento por data");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    //leitura de dados
    public String lerNomeCliente() {
        System.out.print("Nome do Cliente: ");
        return sc.nextLine();
    }

    public int perguntarPagarAgora() {
        System.out.println("1 - Pagar agora");
        System.out.println("2 - Deixar em aberto");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    public int escolherCategoria() {
        System.out.println("\n=== O que deseja adicionar? ===");
        System.out.println("1 - Lanches");
        System.out.println("2 - Bebidas");
        System.out.println("0 - Finalizar");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    public int escolherProduto(ArrayList<Produto> lista) {
        System.out.println();
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d - %s  R$ %.2f%n", i + 1,
                    lista.get(i).getNome(), lista.get(i).getPreco());
        }
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
        int idx = sc.nextInt();
        sc.nextLine();
        return idx;
    }

    public int lerQuantidade() {
        System.out.print("Quantidade: ");
        int qtd = sc.nextInt();
        sc.nextLine();
        return qtd;
    }

    public int escolherPedidoDaLista(ArrayList<Pedido> lista) {
        System.out.println("\n=== Pedidos em aberto / parciais ===");
        for (int i = 0; i < lista.size(); i++) {
            Pedido p = lista.get(i);
            System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f%n",
                    i + 1, p.getCliente(), p.total(), p.totalEmAberto());
        }
        System.out.println("0 - Voltar");
        System.out.print("Escolha o pedido: ");
        int idx = sc.nextInt();
        sc.nextLine();
        return idx;
    }

    public int escolherPedidoPendente(ArrayList<Pedido> lista) {
        System.out.println("\n=== Pedidos pendentes ===");
        for (int i = 0; i < lista.size(); i++) {
            Pedido p = lista.get(i);
            System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f | Status: %s%n",
                    i + 1, p.getCliente(), p.total(), p.totalEmAberto(), p.status());
        }
        System.out.println("0 - Voltar");
        System.out.print("Escolha o pedido: ");
        int idx = sc.nextInt();
        sc.nextLine();
        return idx;
    }

    //pagamento
    public int escolherFormaPagamento(double valorEmAberto) {
        System.out.printf("%nValor ainda em aberto: R$ %.2f%n", valorEmAberto);
        System.out.println("1 - Pagar tudo");
        System.out.println("2 - Dividir igualmente");
        System.out.println("3 - Pagar separado (item por item)");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    public int lerNumeroPessoas() {
        System.out.print("Quantas pessoas: ");
        int p = sc.nextInt();
        sc.nextLine();
        return p;
    }

    public void exibirValorPorPessoa(double valor) {
        System.out.printf("Cada um paga: R$ %.2f%n", valor);
    }

    public int escolherItemParaPagar(ArrayList<ItemPedido> itens) {
        System.out.println("\n===Itens pendentes===");
        boolean temPendente = false;
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido item = itens.get(i);
            int restante = item.getQuantidade() - item.getQuantidadePaga();
            if (restante > 0) {
                System.out.printf("%d - %s | Restante: %d | R$ %.2f%n",
                        i + 1, item.getProduto().getNome(), restante,
                        item.getProduto().getPreco() * restante);
                temPendente = true;
            }
        }
        if (!temPendente) {
            exibirMensagem("Todos os itens ja foram pagos!");
            return -1;
        }
        System.out.println("0 - Parar");
        System.out.print("Escolha item: ");
        int idx = sc.nextInt();
        sc.nextLine();
        return idx;
    }

    public int lerQuantidadeParaPagar(int max) {
        System.out.print("Quantidade a pagar (max " + max + "): ");
        int qtd = sc.nextInt();
        sc.nextLine();
        return qtd;
    }

    public int perguntarContinuarPagando() {
        System.out.print("Continuar pagando? 1-Sim | 0-Nao: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    //faturamento
    public int escolherTipoFaturamento() {
        System.out.println("1 - Por dia");
        System.out.println("2 - Por periodo");
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    public String lerData(String label) {
        System.out.print(label + " (DD-MM-YYYY): ");
        return sc.nextLine().trim();
    }

    public void exibirFaturamentoDia(String data, double valor) {
        System.out.printf("Faturamento em %s (somente pagos): R$ %.2f%n", data, valor);
    }

    public void exibirFaturamentoPeriodo(String inicio, String fim, double valor) {
        System.out.printf("Faturamento de %s a %s (somente pagos): R$ %.2f%n",
                inicio, fim, valor);
    }

    //exibição de pedidos
    public void exibirPedido(Pedido p) {
        System.out.println("\nCliente : " + p.getCliente());
        System.out.println("Data    : " + p.getDataFormatada());
        System.out.println("Status  : " + p.status());

        System.out.println("  [Lanches]");
        boolean temLanche = false;
        for (ItemPedido i : p.getItens()) {
            if (i.getProduto().getCategoria().equals("Lanche")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.getProduto().getNome(), i.getQuantidade(), i.subtotal(), i.getQuantidadePaga());
                temLanche = true;
            }
        }
        if (!temLanche) System.out.println("    (nenhum)");

        System.out.println("  [Bebidas]");
        boolean temBebida = false;
        for (ItemPedido i : p.getItens()) {
            if (i.getProduto().getCategoria().equals("Bebida")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.getProduto().getNome(), i.getQuantidade(), i.subtotal(), i.getQuantidadePaga());
                temBebida = true;
            }
        }
        if (!temBebida) System.out.println("    (nenhum)");

        System.out.printf("  Total: R$ %.2f | Em aberto: R$ %.2f%n",
                p.total(), p.totalEmAberto());
    }

    public void exibirListaPedidos(String titulo, ArrayList<Pedido> lista, double totalGeral, String labelTotal) {
        System.out.println("\n===" + titulo + "===");
        for (Pedido p : lista) exibirPedido(p);
        System.out.printf("%n" + labelTotal + ": R$ %.2f%n", totalGeral);
    }

    public void exibirTotalPedido(double total) {
        System.out.printf("%nTotal do pedido: R$ %.2f%n", total);
    }

    //mensagens simples
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}
