package com.ecommerce.user.config;

import com.ecommerce.core.event.EventBus;
import com.ecommerce.core.event.EventSource;
import com.ecommerce.core.event.bus.SimpleEventBus;
import com.ecommerce.core.event.source.SimpleFileEventSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSourceConfiguration {

    @Bean
    public EventSource eventSource(@Value("${event.source.file.path:./data/events.log}") String filePath) {
        return new SimpleFileEventSource(filePath);
    }

    @Bean
    public EventBus eventBus() {
        SimpleEventBus eventBus = new SimpleEventBus();
        eventBus.init();
        eventBus.startup();
        return eventBus;
    }
}
