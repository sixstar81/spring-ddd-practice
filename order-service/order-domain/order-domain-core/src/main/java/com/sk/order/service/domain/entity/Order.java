package com.sk.order.service.domain.entity;

import com.sk.domain.entity.AggregateRoot;
import com.sk.domain.valueobject.CustomerId;
import com.sk.domain.valueobject.Money;
import com.sk.domain.valueobject.OrderId;
import com.sk.domain.valueobject.RestaurantId;
import com.sk.order.service.domain.valueobject.StreetAddress;

import java.util.List;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    public Order(CustomerId customerId, RestaurantId restaurantId, StreetAddress deliveryAddress, Money price, List<OrderItem> items) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.items = items;
    }
}
