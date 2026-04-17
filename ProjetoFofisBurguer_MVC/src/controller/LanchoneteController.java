package controller;

import model.ItemPedido;
import model.Pedido;
import model.Produto;
import view.LanchoneteView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LanchoneteController {

    private final LanchoneteView view;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ArrayList<Produto> lanches = new ArrayList<>();
    private final ArrayList<Produto> bebidas  = new ArrayList<>();
    private final ArrayList<Pedido>  pedidos  = new ArrayList<>();

    public LanchoneteController(LanchoneteView view) {
        this.view = view;
        carregarCardapio();
    }

    //inicialização
    private void carregarCardapio() {
        lanches.add(new Produto("X-Salada",             20.0, "Lanche"));
        lanches.add(new Produto("X-Calabres",           22.0, "Lanche"));
        lanches.add(new Produto("X-Bacon",              24.0, "Lanche"));
        lanches.add(new Produto("X-Paplo ViTar",        26.0, "Lanche"));
        lanches.add(new Produto("X-Frango com Catupiry",28.0, "Lanche"));
        lanches.add(new Produto("X-Tudo",               32.0, "Lanche"));

        bebidas.add(new Produto("Refrigerante 250ml", 4.0,  "Bebida"));
        bebidas.add(new Produto("Cerveja 250ml",      8.0,  "Bebida"));
        bebidas.add(new Produto("Refrigerante 1lt",   10.0, "Bebida"));
        bebidas.add(new Produto("Suco 800ml",         12.0, "Bebida"));
        bebidas.add(new Produto("Cerveja 800ml",      16.0, "Bebida"));
    }

    //tela inicial do sistema
    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> novoPedido();
                case 2 -> adicionarItensEmAberto();
                case 3 -> efetuarPagamento();
                case 4 -> verPedidosAbertos();
                case 5 -> verPedidosParciais();
                case 6 -> verPedidosPagos();
                case 7 -> faturamentoPorData();
                case 0 -> view.exibirMensagem("Encerrando... Ate logo!");
                default -> view.exibirMensagem("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    //caso 1: Novo pedido
    private void novoPedido() {
        String nome = view.lerNomeCliente();
        if (nome.trim().isEmpty()) {
            view.exibirMensagem("Nome invalido.");
            return;
        }

        Pedido pedido = new Pedido(nome.trim());
        fluxoAdicionarItens(pedido);

        try {
            pedido.finalizar();
        } catch (Exception e) {
            view.exibirMensagem("Erro ao finalizar: " + e.getMessage());
            return;
        }

        view.exibirTotalPedido(pedido.total());

        if (view.perguntarPagarAgora() == 1) {
            fluxoPagamento(pedido);
        } else {
            view.exibirMensagem("Pedido salvo em aberto!");
        }

        pedidos.add(pedido);
    }

    //caso 2: Adicionar itens a pedido em aberto
    private void adicionarItensEmAberto() {
        ArrayList<Pedido> naoPagos = filtrarPorStatus("aberto", "parcial");
        if (naoPagos.isEmpty()) {
            view.exibirMensagem("Nenhum pedido em aberto ou parcial.");
            return;
        }

        int idx = view.escolherPedidoDaLista(naoPagos);
        if (idx >= 1 && idx <= naoPagos.size()) {
            fluxoAdicionarItens(naoPagos.get(idx - 1));
            view.exibirMensagem("Itens adicionados!");
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    //caso 3: Efetuar pagamento

    private void efetuarPagamento() {
        ArrayList<Pedido> pendentes = filtrarPorStatus("aberto", "parcial");
        if (pendentes.isEmpty()) {
            view.exibirMensagem("Nenhum pedido pendente de pagamento.");
            return;
        }

        int idx = view.escolherPedidoPendente(pendentes);
        if (idx >= 1 && idx <= pendentes.size()) {
            fluxoPagamento(pendentes.get(idx - 1));
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    //caso 4, 5, 6: Listagens
    private void verPedidosAbertos() {
        ArrayList<Pedido> lista = filtrarPorStatus("aberto");
        if (lista.isEmpty()) {
            view.exibirMensagem("Nenhum pedido totalmente em aberto.");
            return;
        }
        double total = lista.stream().mapToDouble(Pedido::totalEmAberto).sum();
        view.exibirListaPedidos("PEDIDOS EM ABERTO", lista, total, "Total geral em aberto");
    }

    private void verPedidosParciais() {
        ArrayList<Pedido> lista = filtrarPorStatus("parcial");
        if (lista.isEmpty()) {
            view.exibirMensagem("Nenhum pedido parcialmente pago.");
            return;
        }
        double total = lista.stream().mapToDouble(Pedido::totalEmAberto).sum();
        view.exibirListaPedidos("PEDIDOS PARCIALMENTE PAGOS", lista, total,
                "Total ainda em aberto (parciais)");
    }

    private void verPedidosPagos() {
        ArrayList<Pedido> lista = filtrarPorStatus("pago");
        if (lista.isEmpty()) {
            view.exibirMensagem("Nenhum pedido totalmente pago.");
            return;
        }
        double total = lista.stream().mapToDouble(Pedido::total).sum();
        view.exibirListaPedidos("PEDIDOS PAGOS", lista, total, "Total arrecadado (pagos)");
    }

    //caso 7: Faturamento
    private void faturamentoPorData() {
        int tipo = view.escolherTipoFaturamento();
        double fat = 0;

        if (tipo == 1) {
            String dataStr = view.lerData("Informe a data");
            try {
                LocalDate dia = LocalDate.parse(dataStr, fmt);
                for (Pedido p : pedidos) {
                    if (p.estaTotalmentePago() && p.getData().equals(dia))
                        fat += p.total();
                }
                view.exibirFaturamentoDia(dataStr, fat);
            } catch (Exception e) {
                view.exibirMensagem("Data invalida. Use o formato DD-MM-YYYY.");
            }

        } else if (tipo == 2) {
            String inicioStr = view.lerData("Data inicio");
            String fimStr    = view.lerData("Data fim  ");
            try {
                LocalDate inicio = LocalDate.parse(inicioStr, fmt);
                LocalDate fim    = LocalDate.parse(fimStr,    fmt);
                if (inicio.isAfter(fim)) {
                    view.exibirMensagem("Data de inicio nao pode ser depois da data fim.");
                    return;
                }
                for (Pedido p : pedidos) {
                    if (p.estaTotalmentePago()
                            && !p.getData().isBefore(inicio)
                            && !p.getData().isAfter(fim))
                        fat += p.total();
                }
                view.exibirFaturamentoPeriodo(inicioStr, fimStr, fat);
            } catch (Exception e) {
                view.exibirMensagem("Data invalida. Use o formato DD-MM-YYYY.");
            }
        } else {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    //fluxos auxiliares
    private void fluxoAdicionarItens(Pedido pedido) {
        int escolha;
        do {
            escolha = view.escolherCategoria();
            if (escolha == 1) {
                fluxoSelecionarProduto(lanches, pedido);
            } else if (escolha == 2) {
                fluxoSelecionarProduto(bebidas, pedido);
            } else if (escolha != 0) {
                view.exibirMensagem("Opcao invalida.");
            }
        } while (escolha != 0);
    }

    private void fluxoSelecionarProduto(ArrayList<Produto> lista, Pedido pedido) {
        int idx = view.escolherProduto(lista);
        if (idx >= 1 && idx <= lista.size()) {
            int qtd = view.lerQuantidade();
            if (qtd <= 0) {
                view.exibirMensagem("Quantidade invalida, item nao adicionado.");
                return;
            }
            pedido.adicionarItem(new ItemPedido(lista.get(idx - 1), qtd));
            view.exibirMensagem("Item adicionado!");
        } else if (idx != 0) {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    private void fluxoPagamento(Pedido pedido) {
        int forma = view.escolherFormaPagamento(pedido.totalEmAberto());

        if (forma == 1) {
            pagarTudo(pedido);

        } else if (forma == 2) {
            int pessoas = view.lerNumeroPessoas();
            if (pessoas <= 0) {
                view.exibirMensagem("Numero de pessoas invalido.");
                return;
            }
            view.exibirValorPorPessoa(pedido.totalEmAberto() / pessoas);
            pagarTudo(pedido);

        } else if (forma == 3) {
            pagarPorItem(pedido);

        } else {
            view.exibirMensagem("Opcao invalida.");
        }
    }

    private void pagarTudo(Pedido pedido) {
        for (ItemPedido item : pedido.getItens())
            item.pagarQuantidade(item.getQuantidade() - item.getQuantidadePaga());
        view.exibirMensagem("Pedido totalmente pago!");
    }

    private void pagarPorItem(Pedido pedido) {
        int continuar;
        do {
            int itemIdx = view.escolherItemParaPagar(pedido.getItens());
            if (itemIdx == -1) break;  // todos pagos
            if (itemIdx == 0) break;   // usuario escolheu parar

            if (itemIdx >= 1 && itemIdx <= pedido.getItens().size()) {
                ItemPedido item = pedido.getItens().get(itemIdx - 1);
                int restante = item.getQuantidade() - item.getQuantidadePaga();
                if (restante == 0) {
                    view.exibirMensagem("Este item ja foi totalmente pago.");
                } else {
                    int qtd = view.lerQuantidadeParaPagar(restante);
                    try {
                        item.pagarQuantidade(qtd);
                        view.exibirMensagem("Pago!");
                    } catch (Exception e) {
                        view.exibirMensagem("Erro: " + e.getMessage());
                    }
                }
            } else {
                view.exibirMensagem("Opcao invalida.");
            }

            if (pedido.estaTotalmentePago()) {
                view.exibirMensagem("Pedido totalmente pago!");
                break;
            }

            continuar = view.perguntarContinuarPagando();
        } while (continuar == 1);
    }

    //utilitário
    private ArrayList<Pedido> filtrarPorStatus(String... statuses) {
        ArrayList<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            String s = p.status();
            for (String filtro : statuses) {
                if (s.equalsIgnoreCase(filtro)) {
                    resultado.add(p);
                    break;
                }
            }
        }
        return resultado;
    }
}
