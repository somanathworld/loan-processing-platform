package com.gs;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.gs.event.LoanEvent;
import com.gs.service.LoanKafkaProducer;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    KafkaTemplate<String, LoanEvent> kafkaTemplate;

    @InjectMocks
    LoanKafkaProducer kafkaProducer;

    @Test
    void shouldSendCorrectKafkaMessage() {
        LoanEvent event = new LoanEvent();
        event.setLoanId("loan123");
        event.setCustomerId("cust123");
        event.setStatus("APPROVED");

        kafkaProducer.sendLoanStatusEvent("loan-status-events", event.getLoanId(), event);

        verify(kafkaTemplate).send(eq("loan-status-events"), eq(event.getLoanId()),eq(event));
    }
}
