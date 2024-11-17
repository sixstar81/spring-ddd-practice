package com.sk.order.service.domain.entity;

import com.sk.domain.entity.AggregateRoot;
import com.sk.domain.valueobject.*;
import com.sk.order.service.domain.exception.OrderDomainException;
import com.sk.order.service.domain.valueobject.OrderItemId;
import com.sk.order.service.domain.valueobject.StreetAddress;
import com.sk.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
    }

    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("order is not correct state for pay operation!")
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approved(){
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("order is not correct state for approved!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCanceled(List<String> failureMessages){
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("order is not correct state for canceling");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void canceled(List<String> failureMessages){
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("order is not correct state for canceled");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(msg -> !msg.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    private void validateInitialOrder() {
        if(orderStatus != null || getId() != null){
            throw new OrderDomainException("Order is not correct state for initialization");
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price is greater than zero!");
        }
    }

    private void validateItemPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);
        if(price.notEquals(orderItemsTotal)){
            throw new OrderDomainException("Total price :" + price.getAmount() + " is not" +
                    " equal to Order item total : " + orderItemsTotal);
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid())
            throw new OrderDomainException("Order Item Price is not valid. " +
                    " order item price : " + orderItem.getPrice().getAmount() +
                    " product:  " + orderItem.getProduct().getId().getValue());
    }

    private void initializeOrderItems() {
        long itemId = 1L;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }


    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
