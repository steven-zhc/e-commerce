package com.ecommerce.core.event;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


public abstract class BaseEvent<T> implements Event<T> {
    private  String eventId;
    private  String commandId;
    private  String aggregateId;
    private  Long version;
    private  LocalDateTime eventDate;
    private  T payload;
    private  String eventType;

    public BaseEvent() {
       this.eventId = UUID.randomUUID().toString(); 
       this.eventDate = LocalDateTime.now();
    }
    
    public BaseEvent(String commandId, String aggregateId, Long version, T payload, String eventType) {
        this();
        this.commandId = commandId;
        this.aggregateId = aggregateId;
        this.version = version;
        this.payload = payload;
        this.eventType = eventType;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getCommandId() {
        return commandId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public LocalDateTime getEventDate() {
        return eventDate;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    // compare with type and event id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Event)) return false;
        Event<?> event = (Event<?>) o;
        return Objects.equals(eventId, event.getEventId()) &&
               Objects.equals(eventType, event.getEventType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventType);
    }
    
}
