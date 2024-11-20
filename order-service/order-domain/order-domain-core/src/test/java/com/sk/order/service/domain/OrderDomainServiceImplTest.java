package com.sk.order.service.domain;

import com.sk.domain.valueobject.OrderId;
import com.sk.domain.valueobject.RestaurantId;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.OrderItem;
import com.sk.order.service.domain.entity.Product;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCreatedEvent;
import com.sk.order.service.domain.exception.OrderDomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderDomainServiceImplTest {

    private OrderDomainServiceImpl orderDomainService;
    private Order orderMock;
    private Restaurant restaurantMock;

    @BeforeEach
    void setup(){
        orderDomainService = new OrderDomainServiceImpl();
        orderMock = mock(Order.class);
        restaurantMock = mock(Restaurant.class);
    }

    @Test
    void testValidateAndInitiateOrder_Success() {
        UUID orderId = UUID.randomUUID();
        // Given
        when(restaurantMock.isActive()).thenReturn(true);
        when(orderMock.getId()).thenReturn(new OrderId(orderId));

        // When
        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(orderMock, restaurantMock);

        // Then
        verify(orderMock).validateOrder();
        verify(orderMock).initializeOrder();
        assertNotNull(event);
        assertEquals(orderId, event.getOrder().getId().getValue());

    }

    @Test
    void testValidateAndInitiateOrder_RestaurantNotActive(){
        when(restaurantMock.getId()).thenReturn(new RestaurantId(UUID.randomUUID()));
        when(restaurantMock.isActive()).thenReturn(false);
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderDomainService.validateAndInitiateOrder(orderMock, restaurantMock));
        Assertions.assertThat(orderDomainException.getMessage()).contains("is not active");
    }

    @Test
    void testCancelOrder() {
        // Given
        when(orderMock.getId()).thenReturn(new OrderId(UUID.randomUUID()));
        List<String> failureMessages = List.of("Order cancellation reason");
        // When
        orderDomainService.cancelOrder(orderMock, failureMessages);
        // Then
        verify(orderMock).canceled(failureMessages);
    }
}