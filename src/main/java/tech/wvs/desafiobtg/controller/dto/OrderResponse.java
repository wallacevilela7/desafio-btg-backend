package tech.wvs.desafiobtg.controller.dto;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            BigDecimal total) {
}
