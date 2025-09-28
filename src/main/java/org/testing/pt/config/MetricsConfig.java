package org.testing.pt.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter userCreatedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("users_created_total")
                .description("Total number of users created")
                .register(meterRegistry);
    }

    @Bean
    public Counter userRetrievedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("users_retrieved_total")
                .description("Total number of user retrievals")
                .register(meterRegistry);
    }
}