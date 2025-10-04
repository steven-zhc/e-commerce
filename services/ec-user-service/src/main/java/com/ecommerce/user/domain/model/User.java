package com.ecommerce.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventStream;
import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.event.SignUpEvent;
import com.ecommerce.user.domain.model.event.ValidateEmailEvent;

public class User {
    private Email email;
    private String password;
    private boolean validated;
    private LocalDateTime createdDate;
    private LocalDateTime validateDate;

    public User(SignUpEvent event) {
        this.email = new Email(event.getPayload().getEmail());
        this.password = Objects.requireNonNull(event.getPayload().getPassword(), "Password is required");
        this.validated = false;
    }

    public void signup(SignUpCompleteEvent event) {
        this.createdDate = event.getEventDate();
    }

    public void validateEmail(ValidateEmailEvent event) {
        this.validated = true;
        this.validateDate = event.getEventDate();
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValidated() {
        return validated;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public LocalDateTime getValidateDate() {
        return validateDate;
    }

public static User replay(EventStream eventStream) {
        User user = null;

        while (eventStream.hasNext()) {
            Event<?> event = eventStream.next();

            switch (event) {
                case SignUpEvent signUpEvent -> user = new User(signUpEvent);
                case SignUpCompleteEvent signUpCompleteEvent -> {
                    if (user != null) {
                        user.signup(signUpCompleteEvent);
                    }
                }
                case ValidateEmailEvent validateEmailEvent -> {
                    if (user != null) {
                        user.validateEmail(validateEmailEvent);
                    }
                }
                default -> {}
            }
        }

        return user;
    }
}
