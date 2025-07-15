package com.gs.event;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LoanStatusEvent {
    private String loanId;
    private String customerId;
    private double amount;
    private String status;
    private LocalDateTime createdAt;
}