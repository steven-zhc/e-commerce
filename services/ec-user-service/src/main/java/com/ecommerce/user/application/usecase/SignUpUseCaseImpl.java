package com.ecommerce.user.application.usecase;

import com.ecommerce.core.event.EventBus;
import com.ecommerce.core.event.EventSource;
import com.ecommerce.core.event.EventStream;
import com.ecommerce.core.event.stream.SimpleEventStream;
import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.event.SignUpEvent;
import com.ecommerce.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class SignUpUseCaseImpl implements SignUpUseCase {
    private final EventSource eventSource;
    private final EventBus eventBus;

    public SignUpUseCaseImpl(EventBus eventBus, EventSource eventSource) {
        this.eventSource = eventSource;
        this.eventBus = eventBus;
    }

    @Override
    public SignUpCompleteEvent execute(SignUpEvent event) {
        // Save the sign-up event to event source
        eventSource.save(event);

        // Publish event to event bus
        eventBus.publish(event);

        EventStream eventStream = new SimpleEventStream();
        eventStream.offer(event);

        // Process the sign-up event
        User user = User.replay(eventStream);

        // Generate SignUpCompleteEvent
        SignUpCompleteEvent completeEvent = new SignUpCompleteEvent(
            event.getCommandId(),
            event.getAggregateId(),
            eventStream.peekLast().getVersion() + 1,
            user
        );

        // Save the complete event to event source
        eventSource.save(completeEvent);

        // Publish complete event to event bus
        eventBus.publish(completeEvent);

        return completeEvent;
    }

}
