package com.gs.filter;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    private final Tracer tracer;

    public LoggingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.deferContextual(ctx -> {
                    if (tracer.currentSpan() != null) {
                        String traceId = tracer.currentSpan().context().traceId();
                        String spanId = tracer.currentSpan().context().spanId();
                        String method = exchange.getRequest().getMethod().name();
                        String path = exchange.getRequest().getPath().value();

                        log.info("📡 [Gateway] {} {} | traceId={}, spanId={}", method, path, traceId, spanId);
                    } else {
                        log.info("📡 [Gateway] No trace context found.");
                    }
                    return Mono.empty();
                }));
    }

    @Override
    public int getOrder() {
        return -1; // high priority
    }
}
