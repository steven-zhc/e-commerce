package com.ecommerce.core.event;

public interface EventBus {
    /**
     * Get event type
     */
    Class getEventType();

    /**
     * Publish an event on event bus.
     * @param event
     */
    void publish(Event event);

    /**
     * Subscribe event handler on Event Bus. The handler will be notified
     * when the related event are fired.
     * @param handler
     */
    void subscribe(EventHandler<? extends Event> handler);

    /**
     * Unsubscribe handler from event bus.
     * @param handler
     */
    void unsubscribe(EventHandler<? extends Event> handler);

    /**
     * Initial event bus
     */
    void init();
    /**
     * Start event bus.
     * @return
     */
    boolean startup();

    /**
     * Shutdown event bus
     * @return
     */
    boolean shutdown();

    /**
     *
     * @return
     */
    long getBusSize();
}
