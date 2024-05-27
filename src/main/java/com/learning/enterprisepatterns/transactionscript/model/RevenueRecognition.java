package com.learning.enterprisepatterns.transactionscript.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Data
@Table("revenue_recognitions")
public class RevenueRecognition {
    private long contract;
    private double amount;
    private Date recognizedOn;
}
