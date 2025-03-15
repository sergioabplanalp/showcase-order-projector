package com.showcase.messaging;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Set;

public class RabbitMQMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQMessageListener.class);

    private final EventBus eventBus;
    private final Set<String> supportedEvents;
    private final MessageConverter messageConverter;

    public RabbitMQMessageListener(EventBus eventBus, Set<String> supportedEvents, Jackson2JsonMessageConverter messageConverter) {
        this.eventBus = eventBus;
        this.supportedEvents = supportedEvents;
        this.messageConverter = messageConverter;
    }

    @RabbitListener(queues = "${messaging.rabbitmq.queueName}")
    public void handle(Message message) {
        String eventType = message.getMessageProperties().getHeader("__TypeId__");
        if (eventType == null) {
            logger.debug("Cannot process message without type id. Skipping.");
            return;
        }

        if (!supportedEvents.contains(eventType)) {
            logger.debug("Message type {} is not supported.", eventType);
            return;
        }

        Object payload = messageConverter.fromMessage(message);
        logger.info("Received RabbitMQ message -> {}", payload);
        EventMessage<Object> eventMessage = GenericDomainEventMessage.asEventMessage(payload);
        eventBus.publish(eventMessage);
    }
}
