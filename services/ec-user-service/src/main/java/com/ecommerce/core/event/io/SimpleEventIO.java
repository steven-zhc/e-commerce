package com.ecommerce.core.event.io;

import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventIO;
import com.ecommerce.core.event.EventRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Utility class for serializing and deserializing events to/from simple text format.
 * Format: date :: aggregate id :: version :: command id :: event type :: payload (json)
 */
public class SimpleEventIO implements EventIO {
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    public static final String DELIMITER = " :: ";

    @Override
    public <T> String serialize(Event<T> event) {
        try {
            String payloadJson = objectMapper.writeValueAsString(event.getPayload());
            return event.getEventDate() + DELIMITER +
                   event.getAggregateId() + DELIMITER +
                   event.getVersion() + DELIMITER +
                   event.getCommandId() + DELIMITER +
                   event.getEventType() + DELIMITER +
                   payloadJson;
        } catch (Exception e) {
            throw new EventIOException("Failed to serialize event", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Event<T> deserialize(String blob) {
        try {
            String[] parts = blob.split(DELIMITER, 6);
            if (parts.length != 6) {
                throw new EventIOException("Invalid event format: expected 6 parts, got " + parts.length);
            }

            LocalDateTime eventDate = LocalDateTime.parse(parts[0]);
            String aggregateId = parts[1];
            Long version = Long.parseLong(parts[2]);
            String commandId = parts[3];
            String eventType = parts[4];
            String payloadJson = parts[5];

            // Get the concrete event class from registry based on event type (e.g., SignUpEvent)
            Class<? extends Event<?>> eventClass = EventRegistry.getEventClass(eventType);

            // Get the payload type T from the event class's generic type parameter
            Type genericSuperclass = eventClass.getGenericSuperclass();
            Class<?> payloadClass = Object.class;

            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0) {
                    payloadClass = (Class<?>) typeArguments[0];
                }
            }

            // Deserialize the payload JSON to its original type T
            T payload = (T) objectMapper.readValue(payloadJson, payloadClass);

            // Create the concrete event instance (e.g., new SignUpEvent(...))
            // Using constructor: (String commandId, String aggregateId, Long version, T payload, String eventType)
            java.lang.reflect.Constructor<?> constructor = eventClass.getConstructor(
                String.class, String.class, Long.class, payloadClass, String.class
            );

            // Instantiate the concrete event type (e.g., SignUpEvent instance)
            Event<T> concreteEvent = (Event<T>) constructor.newInstance(commandId, aggregateId, version, payload, eventType);

            // Set the eventDate field using reflection (since constructor doesn't accept it)
            setEventDateField(concreteEvent, eventDate);

            return concreteEvent;

        } catch (EventIOException e) {
            throw e;
        } catch (Exception e) {
            throw new EventIOException("Failed to deserialize event from blob: " + blob, e);
        }
    }

    /**
     * Sets the eventDate field on the event using reflection.
     */
    private void setEventDateField(Event<?> event, LocalDateTime eventDate) {
        try {
            java.lang.reflect.Field field = event.getClass().getSuperclass().getDeclaredField("eventDate");
            field.setAccessible(true);
            field.set(event, eventDate);
        } catch (Exception e) {
            throw new EventIOException("Failed to set eventDate field", e);
        }
    }

    /**
     * Exception thrown when event I/O operations fail.
     */
    public static class EventIOException extends RuntimeException {
        public EventIOException(String message) {
            super(message);
        }

        public EventIOException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
