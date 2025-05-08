package tech.wvs.desafiobtg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.wvs.desafiobtg.controller.dto.OrderResponse;
import tech.wvs.desafiobtg.dto.OrderCreatedEvent;
import tech.wvs.desafiobtg.entity.OrderEntity;
import tech.wvs.desafiobtg.entity.OrderItem;
import tech.wvs.desafiobtg.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository orderRepository) {
        this.repository = orderRepository;
    }

    public void save(OrderCreatedEvent event) {
        var entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setTotal(getTotal(event));
        entity.setItems(getOrderItemList(event));

        repository.save(entity);
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> getOrderItemList(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(item ->
                        new OrderItem(item.produto(), item.quantidade(), item.preco()))
                .toList();
    }

    public Page<OrderResponse> findById(Long customerId, Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize);

        return repository.findAllByCustomerId(customerId, pageRequest)
                .map(order -> new OrderResponse(order.getOrderId(), order.getTotal()));
    }
}
