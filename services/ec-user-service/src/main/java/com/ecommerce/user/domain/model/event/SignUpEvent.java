package com.ecommerce.user.domain.model.event;

import com.ecommerce.core.event.BaseEvent;
import java.util.Objects;

public class SignUpEvent extends BaseEvent<SignUpEvent.SignUpPayload> {

    public SignUpEvent(String commandId, String aggregateId, Long version, SignUpPayload payload) {
        super(commandId, aggregateId, version, payload, "USER_SIGN_UP");
    }

    public static class SignUpPayload {
        private final String email;
        private final String password;

        public SignUpPayload(String email, String password) {
            this.email = Objects.requireNonNull(email, "Email is required");
            this.password = Objects.requireNonNull(password, "Password is required");
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
