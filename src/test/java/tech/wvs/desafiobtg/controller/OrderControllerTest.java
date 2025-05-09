package tech.wvs.desafiobtg.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import tech.wvs.desafiobtg.factory.OrderResponseFactory;
import tech.wvs.desafiobtg.service.OrderService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;


    @Nested
    class listOrders {
        @Test
        @DisplayName("Should return HttpStatus OK")
        void shouldReturnHttpOk() {
            // Arrange
            var customerId = 1L;
            var page = 0;
            var pageSize = 5;

            doReturn(OrderResponseFactory.buildWithOneItem())
                    .when(orderService).findById(anyLong(), any());

            doReturn(BigDecimal.valueOf(20.50))
                    .when(orderService).findTotalOnOrdersByCustomerId(anyLong());

            // Act
            var response = orderController.listOrders(customerId, page, pageSize);

            // Assert
            assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        }


        @Test
        void shouldPassCorrectParameterToService() {
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
        }
    }
}