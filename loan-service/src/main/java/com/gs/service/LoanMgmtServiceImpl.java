package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gs.repository.ILoanRepository;
import com.gs.model.Loan;
import com.gs.model.LoanStatus;

@Service
public class LoanMgmtServiceImpl implements ILoanMgmtService {
    
    @Autowired
    private ILoanRepository loanRepository;

    @Override
    public String createLoan(Loan loan) {
        Loan savedLoan = loanRepository.save(loan);
        return savedLoan.getLoanId();
    }

    @Override
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

    @Override
    public void updateLoanStatus(String loanId, String status) {
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan != null) {
            loan.setStatus(LoanStatus.valueOf(status));
            loanRepository.save(loan);
        }
        
    }
}
