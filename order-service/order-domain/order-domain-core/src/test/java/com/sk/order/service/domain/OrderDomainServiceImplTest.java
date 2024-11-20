package com.sk.order.service.domain;

import com.sk.domain.valueobject.OrderId;
import com.sk.order.service.domain.entity.Order;
import com.sk.order.service.domain.entity.Product;
import com.sk.order.service.domain.entity.Restaurant;
import com.sk.order.service.domain.event.OrderCreatedEvent;
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

}