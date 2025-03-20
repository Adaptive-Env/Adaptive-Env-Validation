package com.adaptive.environments.validation_service.model.validation;

public enum ErrorReason {
    UNKNOWN_TYPE,
    DESERIALIZATION_ERROR,
    PROCESSING_ERROR,
    CORRUPTED_DATA,
    INVALID_AUTH
}
