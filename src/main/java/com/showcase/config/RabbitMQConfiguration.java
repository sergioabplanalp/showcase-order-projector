package com.showcase.config;

import com.showcase.inventory.events.ProductCreatedEvent;
import com.showcase.inventory.events.ProductUpdatedEvent;
import com.showcase.messaging.RabbitMQMessageListener;
import org.axonframework.eventhandling.EventBus;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue queue(@Value("${messaging.rabbitmq.queueName}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange exchange(@Value("${messaging.rabbitmq.exchangeName}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding inventoryBinding(Queue queue, TopicExchange exchange, @Value("${messaging.rabbitmq.routingKey}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public RabbitMQMessageListener rabbitMQMessageListener(EventBus eventBus) {
        return new RabbitMQMessageListener(eventBus, supportedEvents(), new Jackson2JsonMessageConverter());
    }

    public Set<String> supportedEvents() {
        return Set.of(
                ProductCreatedEvent.class.getName(),
                ProductUpdatedEvent.class.getName()
        );
    }
}
