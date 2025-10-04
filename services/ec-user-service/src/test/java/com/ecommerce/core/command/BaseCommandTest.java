package com.ecommerce.core.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseCommandTest {

    @Test
    void demonstrateUsageWithCustomCommand() {
        // Given: Create a custom command extending BaseCommand
        CreateUserPayload payload = new CreateUserPayload("john@example.com", "John Doe");
        CreateUserCommand command = new CreateUserCommand(payload);

        // When: Access command properties
        String commandId = command.getCommandId();
        String email = command.getPayload().getEmail();
        String name = command.getPayload().getName();

        // Then: All properties should be accessible
        assertNotNull(commandId);
        assertEquals("john@example.com", email);
        assertEquals("John Doe", name);

        // And: Command can be serialized
        String json = CommandSerializer.serialize(command);
        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains("john@example.com"));
    }

    // Example custom command implementation
    static class CreateUserCommand extends BaseCommand<CreateUserPayload> {
        public CreateUserCommand(CreateUserPayload payload) {
            super(payload);
        }

    }

    // Example payload class
    static class CreateUserPayload {
        private String email;
        private String name;

        public CreateUserPayload() {}

        public CreateUserPayload(String email, String name) {
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
