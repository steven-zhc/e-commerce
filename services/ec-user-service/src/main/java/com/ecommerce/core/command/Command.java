package com.ecommerce.core.command;

import java.time.LocalDateTime;

public interface Command<T> {
    String getCommandId();
    LocalDateTime getCommandDate();
    T getPayload();
}
