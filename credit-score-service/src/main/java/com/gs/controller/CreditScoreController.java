package com.gs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gs.service.CreditScoreConsumer;

@RestController
@RequestMapping("/scores")
public class CreditScoreController {
    
    @Autowired
    private CreditScoreConsumer creditScoreConsumer;

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getScore(@PathVariable String customerId) {
        
        String score = creditScoreConsumer.getScore(customerId);

        return ResponseEntity.ok(score);
    }
}
