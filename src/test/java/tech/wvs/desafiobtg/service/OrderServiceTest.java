package tech.wvs.desafiobtg.service;

import org.bson.Document;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import tech.wvs.desafiobtg.entity.OrderEntity;
import tech.wvs.desafiobtg.factory.OrderCreatedEventFactory;
import tech.wvs.desafiobtg.factory.OrderEntityFactory;
import tech.wvs.desafiobtg.repository.OrderRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderService orderService;

    @Captor
    ArgumentCaptor<OrderEntity> orderEntityCaptor;

    @Captor
    ArgumentCaptor<Aggregation> aggregationCaptor;

    @Nested
    class Save {

        @Test
        @DisplayName("Should Call Repository Save")
        void shouldCallRepositorySave() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();

            // Act
            orderService.save(event);

            // Assert
            verify(orderRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should Map Event to Entity with Success")
        void shouldMapEventToEntityWithSuccess() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithOneItem();

            // Act
            orderService.save(event);

            // Assert
            verify(orderRepository, times(1)).save(orderEntityCaptor.capture());

            var orderEntity = orderEntityCaptor.getValue();

            assertEquals(event.codigoPedido(), orderEntity.getOrderId());
            assertEquals(event.codigoCliente(), orderEntity.getCustomerId());
            assertNotNull(orderEntity.getTotal());

            assertEquals(event.itens().getFirst().produto(), orderEntity.getItems().getFirst().getProduct());
            assertEquals(event.itens().getFirst().quantidade(), orderEntity.getItems().getFirst().getQuantity());
            assertEquals(event.itens().getFirst().preco(), orderEntity.getItems().getFirst().getPrice());
        }


        @Test
        @DisplayName("Should Calculate Order Total with Success")
        void shouldCalculateOrderTotalWithSuccess() {
            // Arrange
            var event = OrderCreatedEventFactory.buildWithTwoItens();

            var totalItem1 = event.itens().getFirst().preco().multiply(BigDecimal.valueOf(event.itens().getFirst().quantidade()));
            var totalItem2 = event.itens().getLast().preco().multiply(BigDecimal.valueOf(event.itens().getLast().quantidade()));
            var orderTotal = totalItem1.add(totalItem2);

            // Act
            orderService.save(event);

            // Assert
            verify(orderRepository, times(1)).save(orderEntityCaptor.capture());

            var orderEntity = orderEntityCaptor.getValue();

            assertNotNull(orderEntity.getTotal());
            assertEquals(orderTotal, orderEntity.getTotal());
        }
    }


    @Nested
    class findByCustomerId {

        @Test
        @DisplayName("Should Call Repository FindAllByCustomerId")
        void shouldCallRepositoryFindAllByCustomerId() {
            // Arrange
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);

            doReturn(OrderEntityFactory.buildWithPage())
                    .when(orderRepository)
                    .findAllByCustomerId(eq(customerId), eq(pageRequest));
            // Act
            var response = orderService.findById(customerId, pageRequest);

            // Assert
            verify(orderRepository, times(1))
                    .findAllByCustomerId(eq(customerId), eq(pageRequest));

        }

        @Test
        @DisplayName("Should Map Response")
        void shouldMapResponse() {
            // Arrange
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            var page = OrderEntityFactory.buildWithPage();

            doReturn(OrderEntityFactory.buildWithPage())
                    .when(orderRepository)
                    .findAllByCustomerId(anyLong(), any());
            // Act
            var response = orderService.findById(customerId, pageRequest);

            // Assert
            assertEquals(page.getTotalPages(), response.getTotalPages());
            assertEquals(page.getTotalElements(), response.getTotalElements());
            assertEquals(page.getSize(), response.getSize());
            assertEquals(page.getNumber(), response.getNumber());

            assertEquals(page.getContent().getFirst().getOrderId(), response.getContent().getFirst().orderId());
            assertEquals(page.getContent().getFirst().getCustomerId(), response.getContent().getFirst().customerId());
            assertEquals(page.getContent().getFirst().getTotal(), response.getContent().getFirst().total());
        }
    }


    @Nested
    class findTotalOnOrdersByCustomerId {

        @Test
        @DisplayName("Should Call Mongo Template")
        void shouldCallMongoTemplate() {
            // Arrange
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);

            doReturn(new Document("total", totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(mongoTemplate).aggregate(any(Aggregation.class), anyString(), eq(Document.class));

            // Act
            var total = orderService.findTotalOnOrdersByCustomerId(customerId);

            // Assert
            verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), eq(Document.class));
            assertEquals(totalExpected, total);
        }

        @Test
        @DisplayName("Should user correct aggregation")
        void shouldUseCorrectAggregation() {
            // Arrange
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);

            doReturn(new Document("total", totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(mongoTemplate).aggregate(aggregationCaptor.capture(), anyString(), eq(Document.class));

            // Act
            orderService.findTotalOnOrdersByCustomerId(customerId);

            // Assert
            var aggregation = aggregationCaptor.getValue();
            var aggregationExpected = newAggregation(
                    match(Criteria.where("customerId").is(customerId)),
                    group().sum("total").as("total")
            );

            assertEquals(aggregationExpected.toString(), aggregation.toString());
        }

        @Test
        @DisplayName("Should query correct table")
        void shouldQueryCorrectTable() {
            // Arrange
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);

            doReturn(new Document("total", totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(mongoTemplate).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));

            // Act
            orderService.findTotalOnOrdersByCustomerId(customerId);

            // Assert
            verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));
        }
    }
}