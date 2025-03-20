package com.adaptive.environments.validation_service.model.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ValidationResult {
    private final boolean valid;
    private final List<ValidationError> errors;

    public ValidationResult(boolean valid, List<ValidationError> errors) {
        this.valid = valid;
        this.errors = errors;
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, Collections.emptyList());
    }

    public static ValidationResult error(ValidationError errorMsg) {
        return new ValidationResult(false, List.of(errorMsg));
    }

    public static ValidationResult errors(List<ValidationError> errors) {
        return new ValidationResult(false, errors);
    }

}
