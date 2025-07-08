package com.gs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @GetMapping("/status")
    public String getAuthStatus() {
        return "Authentication Service is running";
    }
}
