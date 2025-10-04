package com.ecommerce.user.application.usecase;

import com.ecommerce.user.domain.model.event.ValidateEmailCompleteEvent;
import com.ecommerce.user.domain.model.event.ValidateEmailEvent;

public interface ValidateEmailUseCase {
    ValidateEmailCompleteEvent execute(ValidateEmailEvent event);
}
