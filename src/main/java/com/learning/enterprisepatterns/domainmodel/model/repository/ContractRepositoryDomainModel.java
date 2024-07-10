package com.learning.enterprisepatterns.domainmodel.model.repository;

import com.learning.enterprisepatterns.domainmodel.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractRepositoryDomainModel extends MongoRepository<Contract, String> {
}
