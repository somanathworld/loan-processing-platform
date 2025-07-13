package com.gs.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gs.event.LoanEvent;
import com.gs.model.LoanRequest;

@RestController
@RequestMapping("/loans")
public class LoanServicesController {

    @Autowired
    private KafkaTemplate<String, LoanEvent> kafkaTemplate;
    private static final String TOPIC = "loan-events";


    @GetMapping("/status")
    public ResponseEntity<String> getLoanStatus() {
        // This is a placeholder for the actual implementation
        return ResponseEntity.ok("Loan status is currently unavailable.");
    }

     @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody LoanRequest request) {
        String loanId = UUID.randomUUID().toString();

        LoanEvent event = new LoanEvent();
        event.setLoanId(loanId);
        event.setCustomerId(request.getCustomerId());
        event.setAmount(request.getAmount());
        event.setStatus("PENDING");
        event.setCreatedAt(LocalDateTime.now());

        kafkaTemplate.send(TOPIC, loanId, event);

        return ResponseEntity.ok("Loan application submitted with ID: " + loanId);
    }

}
