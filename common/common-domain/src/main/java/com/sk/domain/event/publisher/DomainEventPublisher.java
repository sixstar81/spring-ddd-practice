package com.sk.domain.event.publisher;

import com.sk.domain.event.DomainEvent;

public interface DomainEventPublisher  <T extends DomainEvent> {
    void publisher(T domainEvent);

}
