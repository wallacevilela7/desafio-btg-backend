package tech.wvs.desafiobtg.controller.dto;

public record PaginationResponse(Integer page,
                                 Integer pageSize,
                                 Long totalOrders,
                                 Integer totalPages) {
}
