package com.ecommerce.core.event;

public interface EventIO {
    public <T> String serialize(Event<T> event);
    public <T> Event<T> deserialize(String blob);
}
