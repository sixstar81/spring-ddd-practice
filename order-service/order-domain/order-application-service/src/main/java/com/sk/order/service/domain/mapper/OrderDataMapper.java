package com.sk.order.service.domain.mapper;

import com.sk.domain.valueobject.ProductId;
import com.sk.domain.valueobject.RestaurantId;
import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.entity.Product;
import com.sk.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

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
}
