package com.sk.order.service.domain;

import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.dto.create.CreateOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        return null;
    }
}
