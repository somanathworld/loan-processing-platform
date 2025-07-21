package com.gs.filter;

import org.springframework.context.annotation.Configuration;

import io.micrometer.observation.ObservationRegistry;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Hooks;

@Configuration
public class TracingConfig {

    private final ObservationRegistry observationRegistry;

    public TracingConfig(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @PostConstruct
    public void init() {
        // Propagate Micrometer Tracing Context into Reactor
        Hooks.enableAutomaticContextPropagation();
    }
}
