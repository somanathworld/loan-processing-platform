package com.gs.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gs.model.AuthRequest;
import com.gs.model.User;
import com.gs.security.JwtUtil;
import com.gs.service.CustomerUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private CustomerUserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/status")
    public String getAuthStatus() {
        return "Authentication Service is running";
    }

    @PostMapping("/health")
    public ResponseEntity<String> healthCheck() {   
        return ResponseEntity.ok("Authentication Service is healthy");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        System.out.println("AuthenticationController.register()");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(user.getRole() == null ? "USER": "ADMIN");
        String result = userDetailsService.saveUser(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
