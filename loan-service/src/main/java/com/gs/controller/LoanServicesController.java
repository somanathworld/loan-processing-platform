package com.gs.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gs.clients.UserServiceClient;
import com.gs.event.LoanEvent;
import com.gs.model.Loan;
import com.gs.model.LoanRequest;
import com.gs.model.LoanStatus;
import com.gs.service.ILoanMgmtService;
import com.gs.service.LoanKafkaProducer;

@RestController
@RequestMapping("/loans")
public class LoanServicesController {

    @Autowired
    private ILoanMgmtService loanMgmtService;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private KafkaTemplate<String, LoanEvent> kafkaTemplate;
    private static final String TOPIC = "loan-events";

    @Autowired 
    private LoanKafkaProducer kafkaProducer;

    @GetMapping("/status")
    public ResponseEntity<String> getLoanStatus() {
        // This is a placeholder for the actual implementation
        return ResponseEntity.ok("Loan status is currently unavailable.");
    }

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody LoanRequest request) {
        //Generate the unique id for loan
        String loanId = UUID.randomUUID().toString();

        // Fetch user status from user-service
        String status = userClient.getUserStatus(request.getCustomerId());

        if ("INACTIVE".equalsIgnoreCase(status)) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("User is not active");
        }

        Loan loan = new Loan();
        loan.setLoanId(loanId);
        loan.setCustomerId(request.getCustomerId());
        loan.setLoanAmount(request.getAmount());
        loan.setStatus(LoanStatus.PENDING);
        loanMgmtService.createLoan(loan);

        LoanEvent event = new LoanEvent();
        event.setLoanId(loanId);
        event.setCustomerId(request.getCustomerId());
        event.setAmount(request.getAmount());
        event.setStatus("PENDING");
        event.setCreatedAt(LocalDateTime.now());

        kafkaTemplate.send(TOPIC, loanId, event);

        return ResponseEntity.ok("Loan application submitted with ID: " + loanId);
    }

    @GetMapping("/approve/{loanId}")
    public ResponseEntity<String> approveLoan(@PathVariable String loanId) {
        System.out.println("/loans/approve");
        Loan loan = loanMgmtService.getLoanById(loanId);
        if (loan == null)
            return ResponseEntity.notFound().build();

        loan.setStatus(LoanStatus.APPROVED);
        loanMgmtService.updateLoanStatus(loanId, "APPROVED");

        // 🔥 Publish to Kafka
        LoanEvent event = new LoanEvent();
        event.setCustomerId(loan.getCustomerId());
        event.setLoanId(loan.getLoanId());
        event.setStatus("APPROVED");
        event.setCreatedAt(LocalDateTime.now());

        kafkaProducer.sendLoanStatusEvent("loan-status-events", loanId, event);

        return ResponseEntity.ok("Loan approved and event published.");
    }

}
