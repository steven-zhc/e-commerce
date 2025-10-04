package com.ecommerce.user.config;

import com.ecommerce.core.event.EventRegistry;
import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.event.SignUpEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {

    @PostConstruct
    public void registerEvents() {
        // Register all event types with their corresponding classes
        EventRegistry.register("USER_SIGN_UP", SignUpEvent.class);
        EventRegistry.register("USER_SIGN_UP_COMPLETE", SignUpCompleteEvent.class);
    }
}
