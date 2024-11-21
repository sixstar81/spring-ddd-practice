package com.sk.order.service.domain.port.output.message.publisher.payment;

import com.sk.domain.event.publisher.DomainEventPublisher;
import com.sk.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent > {
}
