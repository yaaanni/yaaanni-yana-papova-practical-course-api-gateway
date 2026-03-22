package com.example.APIGateway.security.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TraceIdFilter implements WebFilter {

    private final Tracer tracer;

    public TraceIdFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().beforeCommit(() -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null && currentSpan.context() != null) {
                String traceId = currentSpan.context().traceId();
                // Добавляем заголовок X-Trace-Id в реактивный ответ
                exchange.getResponse().getHeaders().add("X-Trace-Id", traceId);
            }
            return Mono.empty();
        });

        return chain.filter(exchange);
    }
}