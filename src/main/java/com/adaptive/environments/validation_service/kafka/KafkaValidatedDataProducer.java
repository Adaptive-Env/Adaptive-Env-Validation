package com.adaptive.environments.validation_service.kafka;

import com.adaptive.environments.validation_service.model.device.ValidatedData;
import com.adaptive.environments.validation_service.model.validation.ErrorReason;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
public class KafkaValidatedDataProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaValidatedDataProducer.class);

    private final KafkaSender<String, ValidatedData> kafkaSender;
    private final MeterRegistry meterRegistry;

    public KafkaValidatedDataProducer(KafkaSender<String, ValidatedData> kafkaSender,
                                      MeterRegistry meterRegistry) {
        this.kafkaSender = kafkaSender;
        this.meterRegistry = meterRegistry;
    }

    public void sendValidatedData(String topic, ValidatedData data) {
        ProducerRecord<String, ValidatedData> producerRecord = new ProducerRecord<>(topic, data.getDeviceData().getDeviceId(), data);
        SenderRecord<String, ValidatedData, String> record = SenderRecord.create(producerRecord, null);

        kafkaSender.send(Flux.just(record))
                .doOnNext(result -> {
                    log.info("[Kafka] Successfully sent {}", data.getDeviceData());
                    meterRegistry.counter("iot.message.success", "reason", "success").increment();
                })
                .subscribe();
    }
}
