package com.ecommerce.core.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandSerializerTest {

    @Test
    void shouldSerializeCommandToJson() {
        // Given: Create a test command with payload
        TestPayload payload = new TestPayload("test@example.com", "password123");
        Command<TestPayload> command = new BaseCommand<>(payload);

        // When: Serialize command to JSON
        String json = CommandSerializer.serialize(command);
        System.out.println(json);

        // Then: JSON should contain command data
        assertNotNull(json);
        assertTrue(json.contains("test@example.com"));
        assertTrue(json.contains("password123"));
        assertTrue(json.contains("commandId"));
        assertTrue(json.contains("commandDate"));
    }

    @Test
    void shouldDeserializeJsonToCommand() {
        // Given: Create and serialize a command
        TestPayload payload = new TestPayload("test@example.com", "password123");
        TestCommand originalCommand = new TestCommand(payload);
        String json = CommandSerializer.serialize(originalCommand);

        // When: Deserialize JSON back to command
        Command<TestPayload> deserializedCommand = CommandSerializer.deserialize(json, TestCommand.class);

        // Then: Deserialized command should match original
        assertNotNull(deserializedCommand);
        assertEquals(originalCommand.getCommandId(), deserializedCommand.getCommandId());
        assertEquals(originalCommand.getPayload().getEmail(), deserializedCommand.getPayload().getEmail());
        assertEquals(originalCommand.getPayload().getPassword(), deserializedCommand.getPayload().getPassword());
    }

    // Test payload class
    static class TestPayload {
        private String email;
        private String password;

        public TestPayload() {}

        public TestPayload(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Test command class
    static class TestCommand extends BaseCommand<TestPayload> {
        public TestCommand() {
            super(new TestPayload());
        }

        public TestCommand(TestPayload payload) {
            super(payload);
        }
    }
}
