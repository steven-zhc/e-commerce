package com.ecommerce.user.presentation.controller;

import com.ecommerce.core.event.EventSource;
import com.ecommerce.core.event.EventStream;
import com.ecommerce.user.application.command.SignUpCommand;
import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.event.SignUpEvent;
import com.ecommerce.user.application.usecase.SignUpUseCase;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.presentation.dto.SignUpRequest;
import com.ecommerce.user.presentation.dto.SignUpResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class SingUpController {

    private final SignUpUseCase signUpUseCase;
    private final EventSource eventSource;

    public SingUpController(SignUpUseCase signUpUseCase, EventSource eventSource) {
        this.signUpUseCase = signUpUseCase;
        this.eventSource = eventSource;
    }

    @PostMapping("/")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        // Create command from request
        SignUpCommand command = new SignUpCommand(request.getEmail(), request.getPassword());

        EventStream stream = eventSource.loadEventStream(command.getPayload().getEmail());
        if (stream.size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Generate SignUpEvent from command
        SignUpEvent.SignUpPayload payload = new SignUpEvent.SignUpPayload(
            command.getPayload().getEmail(),
            command.getPayload().getPassword()
        );
        SignUpEvent signUpEvent = new SignUpEvent(
            command.getCommandId(),
            command.getPayload().getEmail(), // aggregateId (email as user identifier)
            1L, // version
            payload
        );

        // Execute use case (event sourcing and event bus publishing handled inside)
        SignUpCompleteEvent completeEvent = signUpUseCase.execute(signUpEvent);

        // Map to response
        User user = completeEvent.getPayload();
        SignUpResponse response = new SignUpResponse(
                user.getEmail().getValue(),
                completeEvent.getEventDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
