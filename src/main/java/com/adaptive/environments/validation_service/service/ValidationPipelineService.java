package com.adaptive.environments.validation_service.service;

import com.adaptive.environments.validation_service.kafka.KafkaValidatedDataProducer;
import com.adaptive.environments.validation_service.model.device.DeviceData;
import com.adaptive.environments.validation_service.model.device.ValidatedData;
import com.adaptive.environments.validation_service.security.DeviceIdentityHashGenerator;
import org.springframework.stereotype.Service;

@Service
public class ValidationPipelineService {

    private final ValidationService validationService;
    private final DeviceIdentityHashGenerator hashGenerator;
    private final KafkaValidatedDataProducer kafkaProducer;

    public ValidationPipelineService(ValidationService validationService,
                                     DeviceIdentityHashGenerator hashGenerator,
                                     KafkaValidatedDataProducer kafkaProducer) {
        this.validationService = validationService;
        this.hashGenerator = hashGenerator;
        this.kafkaProducer = kafkaProducer;
    }

    public void processAndForward(DeviceData deviceData) {
        boolean isValid = validationService.validate(deviceData);
        if (isValid) {
            String hash = hashGenerator.hash(deviceData.getDeviceId());
            ValidatedData validatedData = ValidatedData.builder()
                    .deviceData(deviceData)
                    .hash(hash)
                    .build();

            kafkaProducer.sendValidatedData("iot.validated-data", validatedData);
        }
    }
}