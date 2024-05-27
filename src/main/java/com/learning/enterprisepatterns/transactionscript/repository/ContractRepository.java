package com.learning.enterprisepatterns.transactionscript.repository;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ContractRepository {

    SqlRowSet findById(Long id);
}
