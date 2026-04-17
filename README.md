# trabalho_faculdade-burguer

# Sistema de Controle de Lanchonete (Fofis Burguer)

## Descrição do Sistema

O sistema foi desenvolvido para substituir o controle manual de pedidos em papel em uma lanchonete.  
Permite cadastrar produtos, registrar pedidos, adicionar múltiplos itens por pedido e calcular automaticamente os valores, reduzindo erros operacionais.

---

## Tabelas Identificadas

### Produto
Armazena os produtos disponíveis para venda.

| Campo     | Tipo    | Descrição                          |
|----------|--------|----------------------------------|
| nome     | String | Nome do produto                  |
| preco    | double | Preço do produto                 |
| categoria| String | Categoria (Lanche ou Bebida)     |

---

### Pedido
Representa um pedido realizado por um cliente.

| Campo   | Tipo                 | Descrição                      |
|--------|----------------------|--------------------------------|
| cliente| String               | Nome do cliente               |
| data   | LocalDate            | Data do pedido                |
| itens  | Lista<ItemPedido>    | Itens do pedido               |

---

### ItemPedido
Relaciona os produtos ao pedido e controla o pagamento.

| Campo           | Tipo    | Descrição                                  |
|----------------|--------|--------------------------------------------|
| produto        | Produto| Produto selecionado                         |
| quantidade     | int    | Quantidade do produto                      |
| quantidadePaga | int    | Quantidade já paga                         |

---

## Regras de Negócio

RN01 — Produto deve possuir nome válido  
Não é permitido cadastrar produtos com nome vazio.

RN02 — Produto não pode ter preço negativo  
O preço deve ser maior ou igual a zero.

RN03 — Pedido deve possuir cliente  
Não é permitido criar pedidos sem nome do cliente.

RN04 — Item deve possuir produto válido  
Não é permitido adicionar itens com produto nulo.

RN05 — Quantidade deve ser maior que zero  
O sistema não aceita valores inválidos ao adicionar itens.

RN06 — Pedido deve ter pelo menos um item para ser finalizado  
Não é permitido finalizar pedidos vazios.

RN07 — Cálculo automático do total  
O total do pedido é a soma dos subtotais dos itens.

RN08 — Cálculo automático do valor em aberto  
O sistema calcula o valor restante a pagar.

RN09 — Controle de pagamento parcial  
O sistema permite pagar parcialmente os itens do pedido.

RN10 — Não é permitido pagar mais do que o consumido  
A quantidade paga não pode ultrapassar a quantidade total do item.

RN11 — Status do pedido automático  
O pedido pode assumir três estados:
- aberto  
- parcial  
- pago  

RN12 — Consulta de pedidos por data  
O sistema permite listar pedidos realizados em um dia específico.

RN13 — Cálculo de faturamento  
O sistema calcula o faturamento com base nos pedidos pagos.

RN14 — Faturamento por período  
Permite calcular faturamento entre duas datas.

RN15 — Data do pedido automática  
A data do pedido é registrada automaticamente na criação.

---

## Regras Adicionais

RN16 — Separação por categoria  
Os produtos são organizados em categorias (Lanches e Bebidas).

RN17 — Adição de itens em pedidos existentes  
É possível adicionar itens a pedidos em aberto ou parciais.

RN18 — Pagamento dividido  
O sistema permite dividir o valor entre múltiplas pessoas.

RN19 — Pagamento individual por item  
Permite pagar itens separadamente.

RN20 — Exibição detalhada dos pedidos  
O sistema apresenta informações completas dos pedidos, incluindo itens, valores e status.

---

## Estrutura dos Projetos

ProjetoFofisBurguer/  
├── src/  
│   ├── Produto.java  
│   ├── Pedido.java  
│   ├── ItemPedido.java  
│   └── Main.java  

ProjetoFofisBurguer_MVC/  
├── controller/  
│   └── LanchoneteController.java  
├── model/  
│   ├── Produto.java  
│   ├── Pedido.java  
│   └── ItemPedido.java  
├── view/  
│   └── LanchoneteView.java  
└── Main.java  
