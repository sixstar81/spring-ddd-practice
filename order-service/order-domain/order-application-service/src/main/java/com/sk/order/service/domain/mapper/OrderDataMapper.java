package com.sk.order.service.domain.mapper;

import com.sk.domain.valueobject.CustomerId;
import com.sk.domain.valueobject.Money;
import com.sk.domain.valueobject.ProductId;
import com.sk.domain.valueobject.RestaurantId;
import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.dto.create.CreateOrderResponse;
import com.sk.order.service.domain.dto.create.OrderAddress;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.OrderItem;
import com.sk.order.service.domain.entity.Product;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sk.order.service.domain.entity.Restaurant.Builder.builder;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand){
        //product id 만 가지고 product 생성 후, 나머지 정보는 repository 에서 불러와 셋팅
        return builder().id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(item ->
                        new Product(new ProductId(item.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }

    public  CreateOrderResponse orderToCreateOrderResponse(Order order){
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.sk.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream().map(
                orderItem ->
                        OrderItem.builder()
                                .product(new Product(new ProductId(orderItem.getProductId())))
                                .price(new Money(orderItem.getPrice()))
                                .quantity(orderItem.getQuantity())
                                .subTotal(new Money(orderItem.getSubTotal()))
                                .build()
        ).toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }
}
