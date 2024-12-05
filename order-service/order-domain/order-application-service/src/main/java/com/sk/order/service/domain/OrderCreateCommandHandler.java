package com.sk.order.service.domain;

import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.dto.create.CreateOrderResponse;
import com.sk.order.service.domain.entity.Customer;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCreatedEvent;
import com.sk.order.service.domain.exception.OrderDomainException;
import com.sk.order.service.domain.mapper.OrderDataMapper;
import com.sk.order.service.domain.port.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.sk.order.service.domain.port.output.repository.CustomerRepository;
import com.sk.order.service.domain.port.output.repository.OrderRepository;
import com.sk.order.service.domain.port.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    //--* Application Service 에서 Domain Service 를 호출해야 한다.

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        //1. 해당 고객이 있는지 여부를 확인한다.
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id {}", orderCreatedEvent.getOrder().getId().getValue() );
        // git checkout -b publish-event-option-1
        orderCreatedPaymentRequestMessagePublisher.publisher(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }
}
