package com.ecommerce.user.domain.model.event;

import com.ecommerce.core.event.BaseEvent;
import com.ecommerce.user.domain.model.User;

public class ValidateEmailCompleteEvent extends BaseEvent<User> {
    public ValidateEmailCompleteEvent(String commandId, String aggregateId, Long version, User payload) {
        super(commandId, aggregateId, version, payload, "USER_VALIDATE_EMAIL_COMPLETE");
    }
}
