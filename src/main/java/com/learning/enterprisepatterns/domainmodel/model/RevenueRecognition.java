package com.learning.enterprisepatterns.domainmodel.model;

import com.learning.enterprisepatterns.common.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class RevenueRecognition {
    private Money amount;
    private Date recognizedOn;

    public boolean isRecognizableBy(Date asOf) {
        return recognizedOn.after(asOf) && recognizedOn.equals(asOf);
    }
}
