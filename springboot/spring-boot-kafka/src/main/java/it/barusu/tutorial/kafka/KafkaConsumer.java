package it.barusu.tutorial.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "core-metrics-qa", groupId = "kafka-influxdb-local")
    public void listening(ConsumerRecord<?, ?> record) {
        log.info("receive message: {} => {}", record.key(), record.value());
    }
}
