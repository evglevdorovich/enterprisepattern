package com.learning.enterprisepatterns.domainmodel.model.strategy;

import com.learning.enterprisepatterns.common.model.Money;
import com.learning.enterprisepatterns.domainmodel.model.Contract;
import com.learning.enterprisepatterns.domainmodel.model.RevenueRecognition;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@TypeAlias("recognition.threeWay")
public class ThreeWayRecognitionStrategy extends RecognitionStrategy {
    private final int firstRecognitionOffset;
    private final int secondRecognitionOffset;

    public ThreeWayRecognitionStrategy(int firstRecognitionOffset,
                                       int secondRecognitionOffset) {
        this.firstRecognitionOffset = firstRecognitionOffset;
        this.secondRecognitionOffset = secondRecognitionOffset;
    }

    @Override
    public void calculateRevenueRecognitions(Contract contract) {
        Money[] allocation = contract.getRevenue().allocate(3);
        contract.addRevenueRecognition(new RevenueRecognition
                (allocation[0], contract.getDateSigned()));
        contract.addRevenueRecognition(new RevenueRecognition
                (allocation[1], addDays(contract.getDateSigned(), firstRecognitionOffset)));
        contract.addRevenueRecognition(new RevenueRecognition
                (allocation[2], addDays(contract.getDateSigned(), secondRecognitionOffset)));
    }

    private static Date addDays(Date dateSigned, int days) {
        return Date.from(Instant.ofEpochMilli(dateSigned.getTime()).plus(days, ChronoUnit.DAYS));
    }
}
