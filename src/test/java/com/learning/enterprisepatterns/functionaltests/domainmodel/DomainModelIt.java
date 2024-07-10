package com.learning.enterprisepatterns.functionaltests.domainmodel;

import com.learning.enterprisepatterns.common.model.Money;
import com.learning.enterprisepatterns.domainmodel.model.Contract;
import com.learning.enterprisepatterns.domainmodel.model.Product;
import com.learning.enterprisepatterns.domainmodel.model.repository.ContractRepositoryDomainModel;
import com.learning.enterprisepatterns.domainmodel.model.repository.ProductRepositoryDomainModel;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
public class DomainModelIt {
    @Autowired
    private ProductRepositoryDomainModel productRepositoryDomainModel;
    @Autowired
    private ContractRepositoryDomainModel contractRepositoryDomainModel;

    @Test
    void shouldSaveAndRetrieveProduct() {
        val product = Product.newDatabase("database");
        val savedProductId = productRepositoryDomainModel.save(product).getId();

        val retrievedProduct = productRepositoryDomainModel.findById(savedProductId);
        assertThat(retrievedProduct).isNotEmpty();
    }

    @Test
    void shouldSaveAndRetrieveContract() {
        val contract = new Contract();
        val product = Product.newDatabase("database");
        contract.setDateSigned(Date.from(Instant.now()));
        contract.setProduct(product);
        contract.setRevenue(Money.dollars(10));

        val savedContractId = contractRepositoryDomainModel.save(contract).getId();

        val retrievedProduct = contractRepositoryDomainModel.findById(savedContractId);
        assertThat(retrievedProduct).isNotEmpty();
    }
}
