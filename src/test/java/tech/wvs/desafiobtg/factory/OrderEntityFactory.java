package tech.wvs.desafiobtg.factory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import tech.wvs.desafiobtg.entity.OrderEntity;
import tech.wvs.desafiobtg.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderEntityFactory {

    public static OrderEntity build() {
        var itens = new OrderItem("notebook", 1, BigDecimal.valueOf(20.50));

        var entity = new OrderEntity();
        entity.setOrderId(1L);
        entity.setCustomerId(2L);
        entity.setTotal(BigDecimal.valueOf(20.50));
        entity.setItems(List.of(itens));

        return entity;
    }


    public static Page<OrderEntity> buildWithPage() {
        return new PageImpl<>(List.of(build()));
    }
}
