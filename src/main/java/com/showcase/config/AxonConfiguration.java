package com.showcase.config;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonConfiguration {
    private static final String DISABLE_CONSOLE_MESSAGE_SYSTEM_PROPERTY = "disable-axoniq-console-message";

    static {
        System.setProperty(DISABLE_CONSOLE_MESSAGE_SYSTEM_PROPERTY, "true");
    }

    @Bean
    @Primary
    public EventBus eventBus() {
        return SimpleEventBus.builder().build();
    }
}
