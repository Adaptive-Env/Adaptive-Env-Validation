package com.adaptive.environments.validation_service.kafka;

import com.adaptive.environments.validation_service.model.device.DeviceData;
import com.adaptive.environments.validation_service.model.validation.ErrorReason;
import com.adaptive.environments.validation_service.service.ValidationPipelineService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Service
public class KafkaDeviceDataReceiver {
    private static final Logger log = LoggerFactory.getLogger(KafkaDeviceDataReceiver.class);

    private final KafkaReceiver<String, DeviceData> kafkaReceiver;
    private final ValidationPipelineService pipelineService;
    private final MeterRegistry meterRegistry;

    public KafkaDeviceDataReceiver(KafkaReceiver<String, DeviceData> kafkaReceiver,
                                   ValidationPipelineService pipelineService,
                                   MeterRegistry meterRegistry) {
        this.kafkaReceiver = kafkaReceiver;
        this.pipelineService = pipelineService;
        this.meterRegistry = meterRegistry;
        startReceiving();
    }

    private void startReceiving() {
        kafkaReceiver.receive()
                .doOnNext(this::processRecord)
                .subscribe();
    }

    private void processRecord(ReceiverRecord<String, DeviceData> record) {
        try {
            pipelineService.processAndForward(record.value());
        } catch (Exception e) {
            log.error("[Kafka] Exception during message processing", e);
            meterRegistry.counter("iot.message.errors", "reason", ErrorReason.PROCESSING_ERROR.name()).increment();
        } finally {
            record.receiverOffset().acknowledge();
        }
    }

}