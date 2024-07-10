package com.learning.enterprisepatterns.domainmodel.model.strategy;

import com.learning.enterprisepatterns.domainmodel.model.Contract;
import com.learning.enterprisepatterns.domainmodel.model.RevenueRecognition;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("recognition.complete")
public class CompleteRecognitionStrategy extends RecognitionStrategy {
    @Override
    public void calculateRevenueRecognitions(Contract contract) {
        contract.addRevenueRecognition(new RevenueRecognition(contract.getRevenue(),
                contract.getDateSigned()));
    }
}
