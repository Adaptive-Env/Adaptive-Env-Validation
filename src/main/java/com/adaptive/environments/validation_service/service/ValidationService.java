package com.adaptive.environments.validation_service.service;

import com.adaptive.environments.validation_service.model.device.DeviceData;
import com.adaptive.environments.validation_service.model.validation.ValidationError;
import com.adaptive.environments.validation_service.validators.Validator;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    private final List<Validator> validators;
    private final MeterRegistry meterRegistry;
    public ValidationService(List<Validator> validators, MeterRegistry meterRegistry) {
        this.validators = validators;
        this.meterRegistry = meterRegistry;
    }

    public boolean validate(DeviceData data) {
        List<ValidationError> errors = validators.stream()
                .map(validator -> validator.validate(data))
                .filter(result -> !result.isValid())
                .flatMap(result -> result.getErrors().stream())
                .toList();

        if (errors.isEmpty()) {
            reactToSuccess(data.getDeviceId());
            return true;
        } else {
            errors.forEach(this::reactToError);
            return false;
        }
    }

    private void reactToError(ValidationError error) {
        log.warn(error.getMessage());
        meterRegistry.counter("iot.message.errors", "reason", error.getErrorReason().name()).increment();
    }
    private void reactToSuccess(String deviceID) {
        log.info("[Kafka] DeviceData passed validation: {}", deviceID);
        meterRegistry.counter("iot.message.success", "reason", "success").increment();
    }
}
