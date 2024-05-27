package com.learning.enterprisepatterns.transactionscript.repository;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Date;

public interface RevenueRecognitionRepository {
    SqlRowSet findByContractAndRecognizedOnBefore(long contract, Date revenueDate);

    void insertRecognition(long contractNumber, double amount, Date revenueDate);
}
