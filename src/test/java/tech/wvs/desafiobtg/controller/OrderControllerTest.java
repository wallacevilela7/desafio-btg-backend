package tech.wvs.desafiobtg.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import tech.wvs.desafiobtg.controller.dto.OrderResponse;
import tech.wvs.desafiobtg.factory.OrderResponseFactory;
import tech.wvs.desafiobtg.service.OrderService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @Captor
    ArgumentCaptor<Long> customerIdCaptor;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

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
        @DisplayName("Should pass correct parameters to service")
        void shouldPassCorrectParameterToService() {
            // Arrange
            var customerId = 1L;
            var page = 0;
            var pageSize = 5;

            //mockando o comportamento do metodo findById da service
            doReturn(OrderResponseFactory.buildWithOneItem())
                    .when(orderService).findById(customerIdCaptor.capture(), pageRequestCaptor.capture());

            //mockando o comportamento do metodo findTotalOnOrdersByCustomerId da service
            doReturn(BigDecimal.valueOf(20.50))
                    .when(orderService).findTotalOnOrdersByCustomerId(customerIdCaptor.capture());

            // Act
            var response = orderController.listOrders(customerId, page, pageSize);

            // Assert
            assertEquals(2, customerIdCaptor.getAllValues().size());
            assertEquals(customerId, customerIdCaptor.getAllValues().get(0));
            assertEquals(customerId, customerIdCaptor.getAllValues().get(1));
            assertEquals(page, pageRequestCaptor.getValue().getPageNumber());
            assertEquals(pageSize, pageRequestCaptor.getValue().getPageSize());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            // Arrange
            var customerId = 1L;
            var page = 0;
            var pageSize = 5;
            var totalOnOrders = BigDecimal.valueOf(20.50);
            var pagination = OrderResponseFactory.buildWithOneItem();

            doReturn(pagination)
                    .when(orderService).findById(anyLong(), any());

            doReturn(totalOnOrders)
                    .when(orderService).findTotalOnOrdersByCustomerId(anyLong());

            // Act
            var response = orderController.listOrders(customerId, page, pageSize);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().data());
            assertNotNull(response.getBody().paginationResponse());
            assertNotNull(response.getBody().summary());


            assertEquals(totalOnOrders, response.getBody().summary().get("totalOnOrders"));

            assertEquals(pagination.getTotalElements(), response.getBody().paginationResponse().totalOrders());
            assertEquals(pagination.getTotalPages(), response.getBody().paginationResponse().totalPages());
            assertEquals(pagination.getNumber(), response.getBody().paginationResponse().page());
            assertEquals(pagination.getSize(), response.getBody().paginationResponse().pageSize());

            assertEquals(pagination.getContent(), response.getBody().data());


        }
    }
}