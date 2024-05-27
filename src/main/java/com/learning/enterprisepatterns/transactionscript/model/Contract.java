package com.learning.enterprisepatterns.transactionscript.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("contracts")
public class Contract {
    @Id
    private long id;
    private Product product;
    private double revenue;
    private Date dateSigned;
}
