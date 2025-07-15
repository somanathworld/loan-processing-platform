package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gs.event.LoanEvent;

@Service
public class LoanKafkaProducer {

    @Autowired
    private KafkaTemplate<String, LoanEvent> kafkaTemplate;

    public void sendLoanStatusEvent(String topic, String loanId, LoanEvent event) {
        kafkaTemplate.send(topic, loanId, event);
    }
}
