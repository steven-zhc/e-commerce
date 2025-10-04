package com.ecommerce.core.event;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry {

    private static final Map<String, Class<? extends Event<?>>> eventTypeMap = new HashMap<>();

    /**
     * Register an event type with its corresponding class
     */
    public static void register(String eventType, Class<? extends Event<?>> eventClass) {
        eventTypeMap.put(eventType, eventClass);
    }

    /**
     * Get the event class for a given event type
     */
    public static Class<? extends Event<?>> getEventClass(String eventType) {
        Class<? extends Event<?>> eventClass = eventTypeMap.get(eventType);
        if (eventClass == null) {
            throw new IllegalArgumentException("No event class registered for type: " + eventType);
        }
        return eventClass;
    }

    /**
     * Check if an event type is registered
     */
    public static boolean isRegistered(String eventType) {
        return eventTypeMap.containsKey(eventType);
    }

    /**
     * Clear all registered event types (mainly for testing)
     */
    public static void clear() {
        eventTypeMap.clear();
    }
}
