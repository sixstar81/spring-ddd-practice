package com.sk.order.service.domain.port.output.message.publisher.restaurantapproval;

import com.sk.domain.event.publisher.DomainEventPublisher;
import com.sk.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent > {
}
