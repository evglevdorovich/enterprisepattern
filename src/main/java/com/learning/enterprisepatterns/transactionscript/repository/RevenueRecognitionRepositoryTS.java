package com.learning.enterprisepatterns.transactionscript.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class RevenueRecognitionRepositoryTS implements RevenueRecognitionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String FIND_BY_CONTRACT_AND_RECOGNIZED_BEFORE_QUERY = """
            SELECT
                amount
            FROM
                revenue_recognitions
            WHERE
                contract = ?
            AND recognized_on < ?
            """;

    private final String INSERT_RECOGNITION_QUERY = """
            INSERT INTO
                revenue_recognitions
                (contract, amount,recognized_on)
            VALUES (?, ?, ?)
            """;

    @Override
    public SqlRowSet findByContractAndRecognizedOnBefore(long contract, Date revenueDate) {
        return jdbcTemplate.queryForRowSet(FIND_BY_CONTRACT_AND_RECOGNIZED_BEFORE_QUERY, contract, revenueDate);
    }

    @Override
    public void insertRecognition(long contractNumber, double amount, Date revenueDate) {
        jdbcTemplate.update(INSERT_RECOGNITION_QUERY, contractNumber, amount, revenueDate);
    }
}
