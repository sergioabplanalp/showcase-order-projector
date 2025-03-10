package com.showcase.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {
    private final static Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);
    private final EventBus eventBus;

    public KafkaMessageListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @KafkaListener(topics = "${messaging.kafka.topic}")
    public void consume(ConsumerRecord<String, Object> record) {
        Object message = record.value();
        logger.info("Received Kafka message: {}", message);
        eventBus.publish(GenericDomainEventMessage.asEventMessage(message));
    }
}
