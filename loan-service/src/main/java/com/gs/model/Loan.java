package com.gs.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Loan {
    @Id
    private String loanId;

    private String customerId;
    private Double loanAmount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
}
