package com.showcase.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "${messaging.kafka.topic}")
    public void consume(ConsumerRecord<String, Object> record) {
        // todo: Handle consumed event
    }
}
