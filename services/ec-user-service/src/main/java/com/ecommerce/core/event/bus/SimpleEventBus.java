package com.ecommerce.core.event.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventBus;
import com.ecommerce.core.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleEventBus implements EventBus {
    private static final Logger logger = LoggerFactory.getLogger(SimpleEventBus.class);

    private final List<EventHandler<? extends Event>> handlers = new ArrayList<>();
    private final ConcurrentLinkedQueue<Event> eventQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean running = false;

    @Override
    public Class getEventType() {
        return Event.class;
    }

    @Override
    public void publish(Event event) {
        logger.debug("Publishing event: {}", event.getEventType());
        eventQueue.offer(event);
        processEvents();
    }

    @Override
    public void subscribe(EventHandler<? extends Event> handler) {
        logger.info("Subscribing handler: {}", handler.getClass().getSimpleName());
        handlers.add(handler);
    }

    @Override
    public void unsubscribe(EventHandler<? extends Event> handler) {
        logger.info("Unsubscribing handler: {}", handler.getClass().getSimpleName());
        handlers.remove(handler);
    }

    @Override
    public void init() {
        logger.info("Initializing SimpleEventBus");
        running = false;
    }

    @Override
    public boolean startup() {
        logger.info("Starting SimpleEventBus");
        running = true;
        return true;
    }

    @Override
    public boolean shutdown() {
        logger.info("Shutting down SimpleEventBus");
        running = false;
        eventQueue.clear();
        return true;
    }

    @Override
    public long getBusSize() {
        return eventQueue.size();
    }

    @SuppressWarnings("unchecked")
    private void processEvents() {
        Event event;
        while ((event = eventQueue.poll()) != null) {
            for (EventHandler handler : handlers) {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    logger.error("Error handling event {} with handler {}",
                        event.getEventType(), handler.getClass().getSimpleName(), e);
                }
            }
        }
    }
}
