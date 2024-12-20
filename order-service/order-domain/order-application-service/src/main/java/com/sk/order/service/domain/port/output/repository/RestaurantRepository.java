package com.sk.order.service.domain.port.output.repository;

import com.sk.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant > findRestaurantInformation(Restaurant restaurant);
}
