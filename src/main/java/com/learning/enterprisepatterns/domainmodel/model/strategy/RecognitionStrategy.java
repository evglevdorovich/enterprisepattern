package com.learning.enterprisepatterns.domainmodel.model.strategy;

import com.learning.enterprisepatterns.domainmodel.model.Contract;

public abstract class RecognitionStrategy {
    public abstract void calculateRevenueRecognitions(Contract contract);
}
