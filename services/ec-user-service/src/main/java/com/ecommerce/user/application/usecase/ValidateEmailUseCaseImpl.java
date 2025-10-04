package com.ecommerce.user.application.usecase;

import com.ecommerce.core.event.EventBus;
import com.ecommerce.core.event.EventSource;
import com.ecommerce.core.event.EventStream;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.event.ValidateEmailCompleteEvent;
import com.ecommerce.user.domain.model.event.ValidateEmailEvent;

import org.springframework.stereotype.Service;

@Service
public class ValidateEmailUseCaseImpl implements ValidateEmailUseCase {
    private final EventSource eventSource;
    private final EventBus eventBus;

    public ValidateEmailUseCaseImpl(EventSource eventSource, EventBus eventBus) {
        this.eventSource = eventSource;
        this.eventBus = eventBus;
    }

    @Override
    public ValidateEmailCompleteEvent execute(ValidateEmailEvent event) {
        // Load all events from event source for this aggregate
        EventStream eventStream = eventSource.loadEventStream(event.getAggregateId());
        if (eventStream.size() == 0) {
            throw new IllegalStateException("No events found for user");
        }

        // should compile this into core/framework, automatically increment version
        event.setVersion(eventStream.peekLast().getVersion() + 1);

        // Save the validate email event to event source
        eventSource.save(event);

        // Publish event to event bus
        eventBus.publish(event);

        eventStream.offer(event);

        // Replay events to reconstruct User model
        User user = User.replay(eventStream);

        if (user == null) {
            throw new IllegalStateException("User not found");
        }

        if (user.isValidated()) {
            throw new IllegalStateException("User is already validated");
        }

        // Generate ValidateEmailCompleteEvent
        ValidateEmailCompleteEvent completeEvent = new ValidateEmailCompleteEvent(
            event.getCommandId(),
            event.getAggregateId(),
            event.getVersion() + 1,
            user
        );

        // Save the complete event to event source
        eventSource.save(completeEvent);

        // Publish complete event to event bus
        eventBus.publish(completeEvent);

        return completeEvent;
    }

    
}
