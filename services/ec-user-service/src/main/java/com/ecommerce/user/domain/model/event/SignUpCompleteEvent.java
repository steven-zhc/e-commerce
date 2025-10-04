package com.ecommerce.user.domain.model.event;

import com.ecommerce.core.event.BaseEvent;
import com.ecommerce.user.domain.model.User;

public class SignUpCompleteEvent extends BaseEvent<User> {
    public SignUpCompleteEvent(String commandId, String aggregateId, Long version, User payload) {
        super(commandId, aggregateId, version, payload, "USER_SIGN_UP_COMPLETE");
    }
}
