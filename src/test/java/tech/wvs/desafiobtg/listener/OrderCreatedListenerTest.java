package tech.wvs.desafiobtg.listener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.support.MessageBuilder;
import tech.wvs.desafiobtg.factory.OrderCreatedEventFactory;
import tech.wvs.desafiobtg.service.OrderService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {


    @Mock
    OrderService orderService;

    @InjectMocks
    OrderCreatedListener orderCreatedListener;


    @Nested
    class Listen {
        @Test
        @DisplayName("Should call service with correct parameters")
        void shouldCallServiceWithCorrectParameters() {

            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();
            var message = MessageBuilder.withPayload(event).build();

            // Act
            orderCreatedListener.listen(message);

            // Assert
            verify(orderService, times(1)).save(eq(message.getPayload()));
        }
    }

}