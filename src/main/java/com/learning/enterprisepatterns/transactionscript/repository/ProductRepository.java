package com.learning.enterprisepatterns.transactionscript.repository;

import com.learning.enterprisepatterns.transactionscript.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
