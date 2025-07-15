package com.gs.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gs.event.LoanEvent;

@Service
public class CreditScoreConsumer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @KafkaListener(topics = "loan-events", groupId = "credit-score-group",
                   containerFactory = "kafkaListenerContainerFactory")
    public void handleLoanEvent(LoanEvent loan) {
        System.out.println("💡 Received Loan Event: \nLoan ID " + loan.getLoanId() + " \nCustomer Id " + loan.getCustomerId() + " \nAmount " + loan.getAmount());

        String score;
        if (loan.getAmount() < 500000) {
            score = "Good";
        } else {
            score = "Moderate";
        }

        // Store in Redis with expiry
        redisTemplate.opsForValue().set("score:" + loan.getCustomerId(), score, Duration.ofMinutes(30));

        System.out.println("✅ Credit score stored: " + score);
    }

    public String getScore(String customerId) {
        String score = redisTemplate.opsForValue().get("score:" + customerId);
        
        if (score == null) {
            return "No score found for customer ID: " + customerId;
        }
        return "Credit score for customer ID " + customerId + ": " + score;
    }
}
