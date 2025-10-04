package com.ecommerce.core.event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
