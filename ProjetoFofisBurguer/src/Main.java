import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Produto> lanches = new ArrayList<>();
        ArrayList<Produto> bebidas = new ArrayList<>();
        ArrayList<Pedido>  pedidos = new ArrayList<>();

        lanches.add(new Produto("X-Salada",20.0, "Lanche"));
        lanches.add(new Produto("X-Calabres",22.0, "Lanche"));
        lanches.add(new Produto("X-Bacon",24.0, "Lanche"));
        lanches.add(new Produto("X-Paplo ViTar",26.0, "Lanche"));
        lanches.add(new Produto("X-Frango com Catupiry",28.0, "Lanche"));
        lanches.add(new Produto("X-Tudo",32.0, "Lanche"));

        bebidas.add(new Produto("Refrigerante 250ml",4.0, "Bebida"));
        bebidas.add(new Produto("Cerveja 250ml",8.0, "Bebida"));
        bebidas.add(new Produto("Refrigerante 1lt",10.0, "Bebida"));
        bebidas.add(new Produto("Suco 800ml",12.0, "Bebida"));
        bebidas.add(new Produto("Cerveja 800ml",16.0, "Bebida"));

        int opcao;

        do {
            System.out.println("   Lanchonete Fofis Burguer  ");
            System.out.println("1 - Novo Pedido");
            System.out.println("2 - Adicionar itens a pedido em aberto");
            System.out.println("3 - Efetuar pagamento");
            System.out.println("4 - Ver pedidos em aberto");
            System.out.println("5 - Ver pedidos parcialmente pagos");
            System.out.println("6 - Ver pedidos pagos");
            System.out.println("7 - Faturamento por data");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {


                case 1:
                    System.out.print("Nome do Cliente: ");
                    String nome = sc.nextLine();

                    if (nome.trim().isEmpty()) {
                        System.out.println("Nome invalido.");
                        break;
                    }

                    Pedido pedido = new Pedido(nome.trim());
                    adicionarItens(sc, lanches, bebidas, pedido);

                    try {
                        pedido.finalizar();
                    } catch (Exception e) {
                        System.out.println("Erro ao finalizar: " + e.getMessage());
                        break;
                    }

                    System.out.printf("%nTotal do pedido: R$ %.2f%n", pedido.total());
                    System.out.println("1 - Pagar agora");
                    System.out.println("2 - Deixar em aberto");
                    System.out.print("Escolha: ");
                    int decisao = sc.nextInt();
                    sc.nextLine();

                    if (decisao == 1) {
                        realizarPagamento(sc, pedido);
                    } else {
                        System.out.println("Pedido salvo em aberto!");
                    }

                    pedidos.add(pedido);
                    break;


                case 2:
                    ArrayList<Pedido> naoPagos = filtrarPorStatus(pedidos, "aberto", "parcial");
                    if (naoPagos.isEmpty()) {
                        System.out.println("Nenhum pedido em aberto ou parcial.");
                        break;
                    }
                    System.out.println("\n--- Pedidos em aberto / parciais ---");
                    for (int i = 0; i < naoPagos.size(); i++) {
                        Pedido p = naoPagos.get(i);
                        System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f%n",
                                i + 1, p.cliente, p.total(), p.totalEmAberto());
                    }
                    System.out.println("0 - Voltar");
                    System.out.print("Escolha o pedido: ");
                    int idxAdd = sc.nextInt();
                    sc.nextLine();

                    if (idxAdd >= 1 && idxAdd <= naoPagos.size()) {
                        adicionarItens(sc, lanches, bebidas, naoPagos.get(idxAdd - 1));
                        System.out.println("Itens adicionados!");
                    } else if (idxAdd != 0) {
                        System.out.println("Opcao invalida.");
                    }
                    break;


                case 3:
                    ArrayList<Pedido> paraP = filtrarPorStatus(pedidos, "aberto", "parcial");
                    if (paraP.isEmpty()) {
                        System.out.println("Nenhum pedido pendente de pagamento.");
                        break;
                    }
                    System.out.println("\n--- Pedidos pendentes ---");
                    for (int i = 0; i < paraP.size(); i++) {
                        Pedido p = paraP.get(i);
                        System.out.printf("%d - %s | Total: R$ %.2f | Em aberto: R$ %.2f | Status: %s%n",
                                i + 1, p.cliente, p.total(), p.totalEmAberto(), p.status());
                    }
                    System.out.println("0 - Voltar");
                    System.out.print("Escolha o pedido: ");
                    int idxPag = sc.nextInt();
                    sc.nextLine();

                    if (idxPag >= 1 && idxPag <= paraP.size()) {
                        realizarPagamento(sc, paraP.get(idxPag - 1));
                    } else if (idxPag != 0) {
                        System.out.println("Opcao invalida.");
                    }
                    break;


                case 4:
                    ArrayList<Pedido> abertos = filtrarPorStatus(pedidos, "aberto");
                    if (abertos.isEmpty()) {
                        System.out.println("Nenhum pedido totalmente em aberto.");
                        break;
                    }
                    double totalAberto = 0;
                    System.out.println("\n===== PEDIDOS EM ABERTO =====");
                    for (Pedido p : abertos) {
                        imprimirPedido(p);
                        totalAberto += p.totalEmAberto();
                    }
                    System.out.printf("%nTotal geral em aberto: R$ %.2f%n", totalAberto);
                    break;


                case 5:
                    ArrayList<Pedido> parciais = filtrarPorStatus(pedidos, "parcial");
                    if (parciais.isEmpty()) {
                        System.out.println("Nenhum pedido parcialmente pago.");
                        break;
                    }
                    double totalParcial = 0;
                    System.out.println("\n===== PEDIDOS PARCIALMENTE PAGOS =====");
                    for (Pedido p : parciais) {
                        imprimirPedido(p);
                        totalParcial += p.totalEmAberto();
                    }
                    System.out.printf("%nTotal ainda em aberto (parciais): R$ %.2f%n", totalParcial);
                    break;


                case 6:
                    ArrayList<Pedido> pagos = filtrarPorStatus(pedidos, "pago");
                    if (pagos.isEmpty()) {
                        System.out.println("Nenhum pedido totalmente pago.");
                        break;
                    }
                    double totalPago = 0;
                    System.out.println("\n===== PEDIDOS PAGOS =====");
                    for (Pedido p : pagos) {
                        imprimirPedido(p);
                        totalPago += p.total();
                    }
                    System.out.printf("%nTotal arrecadado (pagos): R$ %.2f%n", totalPago);
                    break;

                case 7:
                    System.out.println("1 - Por dia");
                    System.out.println("2 - Por periodo");
                    System.out.print("Escolha: ");
                    int tipo = sc.nextInt();
                    sc.nextLine(); // limpa o buffer
                    double fat = 0;

                    if (tipo == 1) {
                        System.out.print("Informe a data (DD-MM-YYYY): ");
                        String dataStr = sc.nextLine().trim();
                        try {
                            LocalDate dia = LocalDate.parse(dataStr, fmt);
                            for (Pedido p : pedidos) {
                                if (p.estaTotalmentePago() && p.data.equals(dia))
                                    fat += p.total();
                            }
                            System.out.printf("Faturamento em %s (somente pagos): R$ %.2f%n", dataStr, fat);
                        } catch (Exception e) {
                            System.out.println("Data invalida. Use o formato DD-MM-YYYY.");
                        }
                    } else if (tipo == 2) {
                        System.out.print("Data inicio (DD-MM-YYYY): ");
                        String inicioStr = sc.nextLine().trim();
                        System.out.print("Data fim   (DD-MM-YYYY): ");
                        String fimStr = sc.nextLine().trim();
                        try {
                            LocalDate inicio = LocalDate.parse(inicioStr, fmt);
                            LocalDate fim    = LocalDate.parse(fimStr, fmt);
                            if (inicio.isAfter(fim)) {
                                System.out.println("Data de inicio nao pode ser depois da data fim.");
                                break;
                            }
                            for (Pedido p : pedidos) {
                                if (p.estaTotalmentePago()
                                        && !p.data.isBefore(inicio)
                                        && !p.data.isAfter(fim))
                                    fat += p.total();
                            }
                            System.out.printf("Faturamento de %s a %s (somente pagos): R$ %.2f%n",
                                    inicioStr, fimStr, fat);
                        } catch (Exception e) {
                            System.out.println("Data invalida. Use o formato DD-MM-YYYY.");
                        }
                    } else {
                        System.out.println("Opcao invalida.");
                    }
                    break;

                case 0:
                    System.out.println("Encerrando... Ate logo!");
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);

        sc.close();
    }

    public static void adicionarItens(Scanner sc,
                                      ArrayList<Produto> lanches,
                                      ArrayList<Produto> bebidas,
                                      Pedido pedido) {
        int escolha;
        do {
            System.out.println("\n--- O que deseja adicionar? ---");
            System.out.println("1 - Lanches");
            System.out.println("2 - Bebidas");
            System.out.println("0 - Finalizar");
            System.out.print("Escolha: ");
            escolha = sc.nextInt();
            sc.nextLine();

            if (escolha == 1) {
                selecionarItem(sc, lanches, pedido);
            } else if (escolha == 2) {
                selecionarItem(sc, bebidas, pedido);
            } else if (escolha != 0) {
                System.out.println("Opcao invalida.");
            }

        } while (escolha != 0);
    }

    public static void selecionarItem(Scanner sc,
                                      ArrayList<Produto> lista,
                                      Pedido pedido) {
        System.out.println();
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d - %s  R$ %.2f%n", i + 1, lista.get(i).nome, lista.get(i).preco);
        }
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
        int idx = sc.nextInt();
        sc.nextLine();

        if (idx >= 1 && idx <= lista.size()) {
            System.out.print("Quantidade: ");
            int qtd = sc.nextInt();
            sc.nextLine();

            if (qtd <= 0) {
                System.out.println("Quantidade invalida, item nao adicionado.");
                return;
            }

            pedido.adicionarItem(new ItemPedido(lista.get(idx - 1), qtd));
            System.out.println("Item adicionado!");
        } else if (idx != 0) {
            System.out.println("Opcao invalida.");
        }
    }

    public static void realizarPagamento(Scanner sc, Pedido pedido) {
        System.out.printf("%nValor ainda em aberto: R$ %.2f%n", pedido.totalEmAberto());
        System.out.println("1 - Pagar tudo");
        System.out.println("2 - Dividir igualmente");
        System.out.println("3 - Pagar separado (item por item)");
        System.out.print("Escolha: ");
        int opcaoPag = sc.nextInt();
        sc.nextLine();

        if (opcaoPag == 1) {
            for (ItemPedido item : pedido.itens)
                item.pagarQuantidade(item.quantidade - item.quantidadePaga);
            System.out.println("Pedido totalmente pago!");

        } else if (opcaoPag == 2) {
            System.out.print("Quantas pessoas: ");
            int pessoas = sc.nextInt();
            sc.nextLine();
            if (pessoas <= 0) {
                System.out.println("Numero de pessoas invalido.");
                return;
            }
            System.out.printf("Cada um paga: R$ %.2f%n", pedido.totalEmAberto() / pessoas);
            for (ItemPedido item : pedido.itens)
                item.pagarQuantidade(item.quantidade - item.quantidadePaga);
            System.out.println("Pedido totalmente pago!");

        } else if (opcaoPag == 3) {
            int continuar;
            do {
                System.out.println("\n--- Itens pendentes ---");
                boolean temPendente = false;
                for (int i = 0; i < pedido.itens.size(); i++) {
                    ItemPedido item = pedido.itens.get(i);
                    int restante = item.quantidade - item.quantidadePaga;
                    if (restante > 0) {
                        System.out.printf("%d - %s | Restante: %d | R$ %.2f%n",
                                i + 1, item.produto.nome, restante,
                                item.produto.preco * restante);
                        temPendente = true;
                    }
                }

                if (!temPendente) {
                    System.out.println("Todos os itens ja foram pagos!");
                    break;
                }

                System.out.println("0 - Parar");
                System.out.print("Escolha item: ");
                int itemIdx = sc.nextInt();
                sc.nextLine();

                if (itemIdx == 0) break;

                if (itemIdx >= 1 && itemIdx <= pedido.itens.size()) {
                    ItemPedido itemEscolhido = pedido.itens.get(itemIdx - 1);
                    int restante = itemEscolhido.quantidade - itemEscolhido.quantidadePaga;
                    if (restante == 0) {
                        System.out.println("Este item ja foi totalmente pago.");
                    } else {
                        System.out.print("Quantidade a pagar (max " + restante + "): ");
                        int qtd = sc.nextInt();
                        sc.nextLine();
                        try {
                            itemEscolhido.pagarQuantidade(qtd);
                            System.out.println("Pago!");
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("Opcao invalida.");
                }

                if (pedido.estaTotalmentePago()) {
                    System.out.println("Pedido totalmente pago!");
                    break;
                }

                System.out.print("Continuar pagando? 1-Sim | 0-Nao: ");
                continuar = sc.nextInt();
                sc.nextLine();
            } while (continuar == 1);

        } else {
            System.out.println("Opcao invalida.");
        }
    }


    public static void imprimirPedido(Pedido p) {
        System.out.println("\nCliente : " + p.cliente);
        System.out.println("Data    : " + p.getDataFormatada());
        System.out.println("Status  : " + p.status());

        System.out.println("  [Lanches]");
        boolean temLanche = false;
        for (ItemPedido i : p.itens) {
            if (i.produto.categoria.equals("Lanche")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.produto.nome, i.quantidade, i.subtotal(), i.quantidadePaga);
                temLanche = true;
            }
        }
        if (!temLanche) System.out.println("    (nenhum)");

        System.out.println("  [Bebidas]");
        boolean temBebida = false;
        for (ItemPedido i : p.itens) {
            if (i.produto.categoria.equals("Bebida")) {
                System.out.printf("    %s x%d = R$ %.2f  (pago: %d)%n",
                        i.produto.nome, i.quantidade, i.subtotal(), i.quantidadePaga);
                temBebida = true;
            }
        }
        if (!temBebida) System.out.println("    (nenhum)");

        System.out.printf("  Total: R$ %.2f | Em aberto: R$ %.2f%n", p.total(), p.totalEmAberto());
    }

    public static ArrayList<Pedido> filtrarPorStatus(ArrayList<Pedido> pedidos, String... statuses) {
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
