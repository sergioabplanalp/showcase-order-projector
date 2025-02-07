package com.showcase.config;

import com.showcase.messaging.KafkaErrorHandler;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaMessagingConfiguration {

    private static final String TRUSTED_EVENT_PACKAGE = "com.showcase.*";

    @Value("${messaging.kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value("${messaging.kafka.groupId}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId
        );
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer(), valueDeserializer());
    }

    protected StringDeserializer keyDeserializer() {
        return new StringDeserializer();
    }

    protected JsonDeserializer<Object> valueDeserializer() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>(Object.class);
        deserializer.trustedPackages(TRUSTED_EVENT_PACKAGE);
        return deserializer;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler());
        return factory;
    }

    protected CommonErrorHandler kafkaErrorHandler() {
        return new KafkaErrorHandler();
    }
}
