package com.ecommerce.core.event;

public interface EventStream {
    boolean hasNext();
    Event<?> next();
    Event<?> peek();
    Event<?> peekLast();
    void offer(Event<?> event);
    int size();
}
