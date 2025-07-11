package com.gs.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my-super-secure-secret-key-123456"; // Replace with your secret key

    public String generateToken(UserDetails userDetails) {

    Map<String, Object> claims = new HashMap<>();
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    claims.put("roles", authorities.stream()
        .map(GrantedAuthority::getAuthority)
        //.map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
        .collect(Collectors.toList()));
    
        // Logic to generate JWT token
        return Jwts.builder()
                .setClaims(claims) // Add custom claims
                .setSubject(userDetails.getUsername()) // Set username and roles
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set current time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        // Logic to extract username from JWT token
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        // Logic to validate JWT token
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

}
