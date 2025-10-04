package com.ecommerce.user.domain.model.event.handler;

import com.ecommerce.core.event.EventHandler;
import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class SignUpCompleteEventHandler implements EventHandler<SignUpCompleteEvent> {

    private final UserRepository userRepository;

    public SignUpCompleteEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(SignUpCompleteEvent event) {
        // Get the user from the event payload
        User user = event.getPayload();

        // Save the user info to SQL database
        userRepository.save(user);
    }
}
