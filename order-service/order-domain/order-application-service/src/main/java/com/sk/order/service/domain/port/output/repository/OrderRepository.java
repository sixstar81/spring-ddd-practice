package com.sk.order.service.domain.port.output.repository;

import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
