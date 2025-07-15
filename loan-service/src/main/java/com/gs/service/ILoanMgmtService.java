package com.gs.service;

import com.gs.model.Loan;

public interface ILoanMgmtService {

    String createLoan(Loan loan);
    Loan getLoanById(String loanId);
    void updateLoanStatus(String loanId, String status);
}
