package com.adaptive.environments.validation_service.validators;

import com.adaptive.environments.validation_service.model.device.DeviceData;
import com.adaptive.environments.validation_service.model.validation.ValidationResult;

@FunctionalInterface
public interface Validator {
    public ValidationResult validate(DeviceData input);

}
