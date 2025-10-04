package com.ecommerce.user.application.usecase;

import com.ecommerce.user.domain.model.event.SignUpCompleteEvent;
import com.ecommerce.user.domain.model.event.SignUpEvent;

public interface SignUpUseCase {
    SignUpCompleteEvent execute(SignUpEvent event);
}
