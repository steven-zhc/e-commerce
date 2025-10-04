package com.ecommerce.core.event.stream;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventStream;

public class SimpleEventStream implements EventStream {

    private Deque<Event<?>> stream;

    public SimpleEventStream() {
        stream = new ConcurrentLinkedDeque<>();
    }

    public SimpleEventStream(Collection<? extends Event<?>> events) {
        stream = new ConcurrentLinkedDeque<>(events);
    }

    @Override
    public boolean hasNext() {
        return !this.stream.isEmpty();
    }

    @Override
    public Event<?> next() {
        return stream.remove();
    }

    @Override
    public Event<?> peek() {
        return stream.peek();
    }

    @Override
    public Event<?> peekLast() {
        return stream.peekLast();
    }

    @Override
    public void offer(Event<?> event) {
        stream.offer(event);
    }

    @Override
    public int size() {
        return stream.size();
    }

}
