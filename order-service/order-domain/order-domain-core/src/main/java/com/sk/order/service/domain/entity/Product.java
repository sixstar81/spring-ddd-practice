package com.sk.order.service.domain.entity;

import com.sk.domain.entity.BaseEntity;
import com.sk.domain.valueobject.Money;
import com.sk.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;


    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }



}
