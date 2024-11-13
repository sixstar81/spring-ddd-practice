package com.sk.order.service.domain.valueobject;

import com.sk.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {

    protected OrderItemId(Long value) {
        super(value);
    }
}
