package com.ecommerce.core.event;

public interface EventSource {

    <T> void save(Event<T> event);

    void save(EventStream eventStream);

    EventStream loadEventStream(String aggregateId);

    EventStream loadEventStream(String aggregateId, int fromVersion);

}
