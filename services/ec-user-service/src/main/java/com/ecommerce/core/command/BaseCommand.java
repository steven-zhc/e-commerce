package com.ecommerce.core.command;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class BaseCommand<T> implements Command<T> {
    private final String commandId;
    private final LocalDateTime commandDate;
    private final T payload;

    public BaseCommand(T payload) {
        this.commandId = UUID.randomUUID().toString();
        this.commandDate = LocalDateTime.now();
        this.payload = Objects.requireNonNull(payload, "Payload is required");
    }

    protected BaseCommand(String commandId, LocalDateTime commandDate, T payload) {
        this.commandId = commandId;
        this.commandDate = commandDate;
        this.payload = payload;
    }

    @Override
    public String getCommandId() {
        return commandId;
    }

    @Override
    public LocalDateTime getCommandDate() {
        return commandDate;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
