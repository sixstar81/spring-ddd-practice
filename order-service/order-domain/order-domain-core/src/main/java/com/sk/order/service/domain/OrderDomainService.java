package com.sk.order.service.domain;

import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCancelledEvent;
import com.sk.order.service.domain.event.OrderCreatedEvent;
import com.sk.order.service.domain.event.OrderPaidEvent;

import java.util.List;

/*--* domain service 에서 event 를 반환하고 있다. (도메인 이벤트 처리 프로세스 방식 중 1)
*     domain entity 는 다른 aggregate 에서 직접 호출하지 않고, domain service 를 이용한다.
* */

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
