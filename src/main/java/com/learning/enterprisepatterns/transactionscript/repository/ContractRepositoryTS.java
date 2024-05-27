package com.learning.enterprisepatterns.transactionscript.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryTS implements ContractRepository {
    private final JdbcTemplate jdbcTemplate;
    //we are mixing there contracts and products, because it's the idea of the transaction script
    private static final String FIND_CONTRACT_BY_ID = """
            SELECT
                contracts.revenue AS revenue,
                contracts.date_signed AS date_signed,
                products.type AS type
            FROM
                contracts
            INNER JOIN
                products
            ON
                contracts.product = products.id
            WHERE
                contracts.id = ?""";

    @Override
    public SqlRowSet findById(Long id) {
        return jdbcTemplate.queryForRowSet(FIND_CONTRACT_BY_ID, id);
    }
}
