package com.adaptive.environments.validation_service.validators;

import com.adaptive.environments.validation_service.model.device.DeviceData;
import com.adaptive.environments.validation_service.model.validation.ErrorReason;
import com.adaptive.environments.validation_service.model.validation.ValidationError;
import com.adaptive.environments.validation_service.model.validation.ValidationResult;
import com.adaptive.environments.validation_service.security.AuthKeyRegistry;

import org.springframework.stereotype.Component;

@Component
public class AuthValidator implements Validator {

    private final AuthKeyRegistry authKeyRegistry;

    public AuthValidator(AuthKeyRegistry authKeyRegistry) {
        this.authKeyRegistry = authKeyRegistry;
    }

    @Override
    public ValidationResult validate(DeviceData data) {
        if (!authKeyRegistry.isValid(data.getDeviceId(), data.getAuthKey())) {
            String errorMsg = String.format("[Kafka] Unauthorized device: %s %s", data.getDeviceId(), data.getAuthKey());
            ValidationError error = new ValidationError(errorMsg, ErrorReason.INVALID_AUTH);
            return ValidationResult.error(error);
        }
        return ValidationResult.ok();
    }
}
