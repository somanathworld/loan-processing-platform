package com.gs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/status")
    public String getUserStatus() {
        return "User Service is running";
    }

    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/status/admin")
    public String getUserStatus2() {
        return "User Service is running";
    }

    @GetMapping("/public")
    public String anyoneCanAccess() {
        return "Public endpoint";
    }
}
