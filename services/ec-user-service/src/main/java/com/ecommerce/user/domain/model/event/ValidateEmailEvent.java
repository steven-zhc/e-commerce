package com.ecommerce.user.domain.model.event;

import com.ecommerce.core.event.BaseEvent;

public class ValidateEmailEvent extends BaseEvent<ValidateEmailEvent.ValidateEmailPayload> {

    public ValidateEmailEvent(String commandId, String aggregateId, Long version, ValidateEmailPayload payload) {
        super(commandId, aggregateId, version, payload, "USER_VALIDATE_EMAIL");
    }

    public static class ValidateEmailPayload {
        private final String email;

        public ValidateEmailPayload(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}
