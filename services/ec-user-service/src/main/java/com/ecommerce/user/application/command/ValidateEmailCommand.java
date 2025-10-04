package com.ecommerce.user.application.command;

import com.ecommerce.core.command.BaseCommand;

public class ValidateEmailCommand extends BaseCommand<ValidateEmailCommand.ValidateEmailPayload> {

    public ValidateEmailCommand(String email) {
        super(new ValidateEmailPayload(email));
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
