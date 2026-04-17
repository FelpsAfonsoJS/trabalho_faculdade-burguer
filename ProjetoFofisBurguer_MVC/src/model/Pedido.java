package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Pedido {

    String cliente;
    LocalDate data;
    ArrayList<ItemPedido> itens = new ArrayList<>();

    public Pedido(String cliente) {
        if (cliente == null || cliente.isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente invalido");
        }
        this.cliente = cliente;
        this.data = LocalDate.now();
    }

    public ArrayList<ItemPedido> getItens() {
        return itens;
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item invalido");
        }
        itens.add(item);
    }

    public void finalizar() {
        if (itens.isEmpty()) {
            throw new IllegalStateException("Pedido vazio, adicione ao menos um item");
        }
    }

    public double total() {
        double total = 0;
        for (ItemPedido i : itens) total += i.subtotal();
        return total;
    }

    public double totalEmAberto() {
        double total = 0;
        for (ItemPedido i : itens) total += i.valorEmAberto();
        return total;
    }

    public boolean estaTotalmentePago() {
        if (itens.isEmpty()) return false;
        for (ItemPedido i : itens) {
            if (!i.estaPago()) return false;
        }
        return true;
    }

    public String status() {
        if (itens.isEmpty())          return "aberto";
        if (estaTotalmentePago())     return "pago";
        if (totalEmAberto() < total()) return "parcial";
        return "aberto";
    }

    public String getDataFormatada() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(fmt);
    }

    public LocalDate getData() {
        return data;
    }

    public String getCliente() {
        return cliente;
    }
}
