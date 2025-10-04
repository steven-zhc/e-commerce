package com.ecommerce.user.presentation.controller;

import com.ecommerce.user.application.command.ValidateEmailCommand;
import com.ecommerce.user.domain.model.event.ValidateEmailCompleteEvent;
import com.ecommerce.user.domain.model.event.ValidateEmailEvent;
import com.ecommerce.user.application.usecase.ValidateEmailUseCase;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.presentation.dto.ValidateEmailRequest;
import com.ecommerce.user.presentation.dto.ValidateEmailResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ValidateEmailController {

    private final ValidateEmailUseCase validateEmailUseCase;

    public ValidateEmailController(ValidateEmailUseCase validateEmailUseCase) {
        this.validateEmailUseCase = validateEmailUseCase;
    }

    @PutMapping("/validate-email")
    public ResponseEntity<ValidateEmailResponse> validateEmail(@Valid @RequestBody ValidateEmailRequest request) {
        // Create command from request
        ValidateEmailCommand command = new ValidateEmailCommand(request.getEmail());

        // Generate ValidateEmailEvent from command
        ValidateEmailEvent.ValidateEmailPayload payload = new ValidateEmailEvent.ValidateEmailPayload(
            command.getPayload().getEmail()
        );
        ValidateEmailEvent validateEmailEvent = new ValidateEmailEvent(
            command.getCommandId(),
            command.getPayload().getEmail(), // aggregateId (email as user identifier)
            1L, // version
            payload
        );

        // Execute use case
        ValidateEmailCompleteEvent completeEvent = validateEmailUseCase.execute(validateEmailEvent);

        // Map to response
        User user = completeEvent.getPayload();
        ValidateEmailResponse response = new ValidateEmailResponse(
            user.getEmail().getValue(),
            null, // firstName - not in User model anymore
            null, // lastName - not in User model anymore
            null, // phoneNumber - not in User model anymore
            user.isValidated() ? "validated" : "not validated"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
