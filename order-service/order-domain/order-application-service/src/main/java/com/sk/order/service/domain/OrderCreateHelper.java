package com.sk.order.service.domain;

import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCreatedEvent;
import com.sk.order.service.domain.exception.OrderDomainException;
import com.sk.order.service.domain.mapper.OrderDataMapper;
import com.sk.order.service.domain.port.output.repository.CustomerRepository;
import com.sk.order.service.domain.port.output.repository.OrderRepository;
import com.sk.order.service.domain.port.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper  {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService, OrderRepository orderRepository, CustomerRepository customerRepository, RestaurantRepository restaurantRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand){
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        //--* Order 엔티티를 만들었으니, 이제 Order Domain Service 를 호출
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order is created with id : {}", orderCreatedEvent.getOrder().getId().getValue() );
        return orderCreatedEvent;
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

    private Order saveOrder(Order order) {
        Order save = orderRepository.save(order);
        if (save == null) {
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id {}", order.getId().getValue());
        return save;
    }


}
