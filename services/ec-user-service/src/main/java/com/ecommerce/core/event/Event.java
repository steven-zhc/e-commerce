package com.ecommerce.core.event;

import java.time.LocalDateTime;

public interface Event<T> {
    String getEventId(); // unique id for the event
    String getCommandId(); // kinds of transaction id
    String getAggregateId(); // aggregate root id
    Long getVersion(); // version of the aggregate after this event
    LocalDateTime getEventDate(); //used for auditing, no feature base on it
    T getPayload(); // the actual event data
    String getEventType(); // meta info about the event type, will be used for deserialization
    void setVersion(Long version);
}
