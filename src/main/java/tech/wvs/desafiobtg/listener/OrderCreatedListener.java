package tech.wvs.desafiobtg.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import tech.wvs.desafiobtg.dto.OrderCreatedEvent;
import tech.wvs.desafiobtg.service.OrderService;

import static tech.wvs.desafiobtg.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final OrderService orderService;

    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    //Metodo que consome a fila
    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        log.info("Message consumed: {}" , message);

        orderService.save(message.getPayload());
    }

}
