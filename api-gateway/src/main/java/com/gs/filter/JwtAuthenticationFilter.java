package com.gs.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String SECRET_KEY = "my-super-secure-secret-key-123456"; // Use the same secret as auth-service

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                // Extract roles (ensure they're stored as a List<String> in the token)
                List<String> roles = claims.get("roles", List.class);
                String rolesStr = String.join(",", roles); // Comma-separated: ROLE_USER,ROLE_ADMIN

                // Mutate request to add headers
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User", username)
                        .header("X-Roles", rolesStr)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                byte[] bytes = "Invalid or expired token".getBytes();
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(bytes)));
            }
        } else if (!path.contains("/auth")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            byte[] bytes = "Authorization header missing or invalid".getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));
        }

        // Allow unauthenticated routes
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}