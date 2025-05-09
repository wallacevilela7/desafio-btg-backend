package tech.wvs.desafiobtg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.wvs.desafiobtg.controller.dto.ApiResponse;
import tech.wvs.desafiobtg.controller.dto.OrderResponse;
import tech.wvs.desafiobtg.controller.dto.PaginationResponse;
import tech.wvs.desafiobtg.service.OrderService;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        var response = orderService.findById(customerId, page, pageSize);
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                response.getContent(),
                new PaginationResponse(
                        response.getNumber(),
                        response.getSize(),
                        response.getTotalElements(),
                        response.getTotalPages()
                )
        ));
    }
}
