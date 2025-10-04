package com.ecommerce.user.application.command;

import com.ecommerce.core.command.BaseCommand;
import java.util.Objects;

public class SignUpCommand extends BaseCommand<SignUpCommand.SignUpPayload> {

    public SignUpCommand(String email, String password) {
        super(new SignUpPayload(email, password));
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
