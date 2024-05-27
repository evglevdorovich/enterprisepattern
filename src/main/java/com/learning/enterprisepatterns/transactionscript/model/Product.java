package com.learning.enterprisepatterns.transactionscript.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("products")
public class Product {
    @Id
    private long id;
    private String name;
    private String type;
}
