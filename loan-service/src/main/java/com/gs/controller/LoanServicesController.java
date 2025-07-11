package com.gs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanServicesController {

    @GetMapping("/status")
    public ResponseEntity<String> getLoanStatus() {
        // This is a placeholder for the actual implementation
        return ResponseEntity.ok("Loan status is currently unavailable.");
    }

}
