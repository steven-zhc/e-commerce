package com.ecommerce.core.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CommandSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Serialize a Command to JSON string
     */
    public static <T> String serialize(Command<T> command) {
        try {
            return objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new CommandSerializationException("Failed to serialize command", e);
        }
    }

    /**
     * Deserialize JSON string to Command
     */
    public static <T> Command<T> deserialize(String json, Class<? extends Command<T>> commandClass) {
        try {
            return objectMapper.readValue(json, commandClass);
        } catch (JsonProcessingException e) {
            throw new CommandSerializationException("Failed to deserialize command from JSON", e);
        }
    }

    /**
     * Get the ObjectMapper instance for custom operations
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static class CommandSerializationException extends RuntimeException {
        public CommandSerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
