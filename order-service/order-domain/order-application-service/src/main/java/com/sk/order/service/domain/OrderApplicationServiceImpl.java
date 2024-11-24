package com.sk.order.service.domain;

import com.sk.order.service.domain.dto.create.CreateOrderCommand;
import com.sk.order.service.domain.dto.create.CreateOrderResponse;
import com.sk.order.service.domain.dto.track.TrackOrderQuery;
import com.sk.order.service.domain.dto.track.TrackOrderResponse;
import com.sk.order.service.domain.port.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

//--* controller 가 아닌 service  등에서 유효성 검증을 할 때는 @Validate 를 class 위에 붙여주고,
// @Valid 를 메서드의 파라미터 앞에 붙여준다.
@Validated
@Slf4j
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    //--* service 구현 class 를 좀 더 간결하게 하기 위해 주요 로직을 아래 handler 에 위임함
    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
