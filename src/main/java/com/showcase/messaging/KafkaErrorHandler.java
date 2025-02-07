package com.showcase.messaging;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.lang.NonNull;

public class KafkaErrorHandler implements CommonErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(KafkaErrorHandler.class);

    private static final String PAYLOAD_TYPE_HEADER = "__TypeId__";

    @Override
    public void handleOtherException(@NonNull Exception exception, @NonNull Consumer<?, ?> consumer, @NonNull MessageListenerContainer container, boolean batchListener) {
        if (exception instanceof RecordDeserializationException ex) {
            String payloadType = new String(ex.headers().headers(PAYLOAD_TYPE_HEADER).iterator().next().value());
            logger.warn("Could not deserialize record of type: {}. Skipping message.", payloadType);

            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        }
    }
}
