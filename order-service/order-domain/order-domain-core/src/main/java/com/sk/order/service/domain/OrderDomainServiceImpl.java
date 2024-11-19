package com.sk.order.service.domain;

import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Product;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCancelledEvent;
import com.sk.order.service.domain.event.OrderCreatedEvent;
import com.sk.order.service.domain.event.OrderPaidEvent;
import com.sk.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("order with id : {} is initiated", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("order with id : {} is paid.", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    //--* 최종 단계 이므로 이벤트가 필요 없다. (이벤트가 전달 될 후속 작업이 없음을 의미한다.)
    @Override
    public void approveOrder(Order order) {
        order.approved();
        log.info("order with id : {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCanceled(failureMessages);
        log.info("order payment is cancelling for order is {}" , order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    //취소 처리의 마지막 단계이므로 이벤트를 반환하지 않는다.
    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.canceled(failureMessages);
        log.info("order id {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive())
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue()
             + "is not active");
    }

    //order 에 제품 정보를 Restaurant 제품 기준으로 업데이트
    private void setOrderProductInformationOld(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(product -> {
            Product currentProduct = orderItem.getProduct();
            if(currentProduct.equals(product)){
                currentProduct.updateWithConfirmedNameAndPrice(product.getName(), product.getPrice());
            }
        }));
    }
    /*--* 워 코드는 order.getItems 를 순회하며, 내부적으로 Restaurant 의 product 를 중첩하여 탐색하면서
    * 동일한 product 를 찾는 구조로 시간복잡도가 O(n x m ) 이다. restaurant 를 Map 구조로 변환하여
    * 탐색을 O(n + m)로 변환할 수 있다. */
    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<Product, Product> productMap = restaurant.getProducts().stream().collect(
                Collectors.toMap(product -> product, product -> product)
        );
        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            if(productMap.containsKey(currentProduct)){
                Product restaurantProduct = productMap.get(currentProduct);
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        });
    }
}
