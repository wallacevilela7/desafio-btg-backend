package tech.wvs.desafiobtg.dto;

import tech.wvs.desafiobtg.entity.OrderItem;

import java.util.List;

public record OrderCreatedEvent(Long codigoPedido,
                                Long codigoCliente,
                                List<OrderItemEvent> itens) {
}
