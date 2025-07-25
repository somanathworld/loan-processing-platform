package com.gs.fallback;

import com.gs.client.UserServiceClient;
import com.gs.dto.CustomerDTO;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;

import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceFallback {

    private final UserServiceClient userServiceClient;
    private final CircuitBreaker circuitBreaker;

    public UserServiceFallback(UserServiceClient userServiceClient, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.userServiceClient = userServiceClient;
        // Register or retrieve the circuit breaker by name
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("userServiceCB");
    }

    public CustomerDTO getUserStatus(String userId) {
        // Wrap Feign client call with circuit breaker logic manually
        return Try.ofCallable(() -> circuitBreaker.executeCallable(() ->
                userServiceClient.getUserInfo(userId)))
            .recover(throwable -> getFallbackUserStatus(userId, throwable))
            .get();
    }

    // Manual fallback method
    public CustomerDTO getFallbackUserStatus(String userId, Throwable t) {
        System.out.println("🔥 Manual fallback triggered for userId=" + userId + " Reason: " + t.getMessage());
        return null;
    }
}
