package com.ecommerce.core.event.io;

import com.ecommerce.core.event.BaseEvent;
import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleEventIOTest {

    private SimpleEventIO simpleEventIO;

    @BeforeEach
    void setUp() {
        simpleEventIO = new SimpleEventIO();
        EventRegistry.clear();
        EventRegistry.register("USER_CREATED", UserCreatedEvent.class);
    }

    @Test
    void testSerialize() {
        UserCreatedPayload payload = new UserCreatedPayload("john@example.com", "John Doe");
        UserCreatedEvent event = new UserCreatedEvent("cmd-123", "user-456", 1L, payload, "USER_CREATED");

        String serialized = simpleEventIO.serialize(event);

        System.out.println(serialized); // For visual inspection

        assertNotNull(serialized);
        assertTrue(serialized.contains("user-456"));
        assertTrue(serialized.contains("1"));
        assertTrue(serialized.contains("cmd-123"));
        assertTrue(serialized.contains("USER_CREATED"));
        assertTrue(serialized.contains("john@example.com"));
        assertTrue(serialized.contains("John Doe"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDeserialize() {
        UserCreatedPayload payload = new UserCreatedPayload("jane@example.com", "Jane Smith");
        UserCreatedEvent originalEvent = new UserCreatedEvent("cmd-789", "user-999", 2L, payload, "USER_CREATED");

        String serialized = simpleEventIO.serialize(originalEvent);
        Event<UserCreatedPayload> deserializedEvent = simpleEventIO.deserialize(serialized);

        assertNotNull(deserializedEvent);
        assertEquals(originalEvent.getCommandId(), deserializedEvent.getCommandId());
        assertEquals(originalEvent.getAggregateId(), deserializedEvent.getAggregateId());
        assertEquals(originalEvent.getVersion(), deserializedEvent.getVersion());
        assertEquals(originalEvent.getEventType(), deserializedEvent.getEventType());
        assertEquals(originalEvent.getPayload().getEmail(), deserializedEvent.getPayload().getEmail());
        assertEquals(originalEvent.getPayload().getName(), deserializedEvent.getPayload().getName());
    }

    static class UserCreatedEvent extends BaseEvent<UserCreatedPayload> {
        public UserCreatedEvent(String commandId, String aggregateId, Long version, UserCreatedPayload payload, String eventType) {
            super(commandId, aggregateId, version, payload, eventType);
        }
    }

    static class UserCreatedPayload {
        private String email;
        private String name;

        public UserCreatedPayload() {}

        public UserCreatedPayload(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
