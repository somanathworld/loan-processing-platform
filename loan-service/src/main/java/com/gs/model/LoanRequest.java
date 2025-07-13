package com.gs.model;

import lombok.Data;

@Data
public class LoanRequest {
    private String customerId;
    private double amount;
    private int termInMonths;
    private String purpose;
}
