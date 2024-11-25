package com.sk.order.service.domain;

import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.dto.create.CreateOrderResponse;
import com.sk.order.service.domain.entity.Customer;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.exception.OrderDomainException;
import com.sk.order.service.domain.mapper.OrderDataMapper;
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
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        //1. 해당 고객이 있는지 여부를 확인한다.
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        return null;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        return restaurantRepository.findRestaurantInformation(restaurant)
                .orElseThrow(()-> new OrderDomainException("Could not find restaurant with id " + createOrderCommand.getRestaurantId()));
    }

    private void checkCustomer(UUID customerId) {
        customerRepository.findCustomer(customerId)
                .orElseThrow(()->new OrderDomainException("could not find customer with customer id :" + customerId));
    }
}
