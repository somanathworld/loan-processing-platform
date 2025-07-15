package com.gs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gs.model.Loan;

public interface ILoanRepository extends JpaRepository<Loan, String> {

}
