package com.gs.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


public class JwtValidationFilter implements WebFilter {

    private final String SECRET_KEY = "my-super-secure-secret-key-123456"; // Use env/config in production

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                        System.out.println(claims);
                // You can store claims in attributes if needed
                String username = claims.getSubject();

                // Extract roles (ensure they're stored as a List<String> in the token)
                List<String> roles = claims.get("roles", List.class);
                String rolesStr = String.join(",", roles); // Comma-separated: ROLE_USER,ROLE_ADMIN

                // Mutate request to add headers
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User", username)
                        .header("X-Roles", rolesStr)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                byte[] bytes = "Invalid or expired token".getBytes();
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(bytes)));
            }
        } else if (!path.contains("/auth")) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            byte[] bytes = "Authorization header missing or invalid".getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));
        }

        return chain.filter(exchange);
    }
}
