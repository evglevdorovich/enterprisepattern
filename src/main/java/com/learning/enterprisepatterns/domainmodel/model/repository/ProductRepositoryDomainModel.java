package com.learning.enterprisepatterns.domainmodel.model.repository;

import com.learning.enterprisepatterns.domainmodel.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepositoryDomainModel extends MongoRepository<Product, String> {
}
