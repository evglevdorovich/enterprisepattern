package com.learning.enterprisepatterns.domainmodel.model;

import com.learning.enterprisepatterns.common.model.Money;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
public class Contract {
    @Id
    private String id;
    private List<RevenueRecognition> revenueRecognitions;
    private Money revenue;
    private Product product;
    private Date dateSigned;

    public Money recognizedRevenue(Date asOf) {
        return revenueRecognitions.stream()
                .filter(revenueRecognition -> revenueRecognition.isRecognizableBy(asOf))
                .reduce(Money.dollars(0), (money, recognition) -> money.add(recognition.getAmount()), Money::add);
    }

    public void addRevenueRecognition(RevenueRecognition revenueRecognition) {
        revenueRecognitions.add(revenueRecognition);
    }

    public void calculateRecognitions() {
        product.calculateRecognitions(this);
    }
}
