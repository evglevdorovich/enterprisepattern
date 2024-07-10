package com.learning.enterprisepatterns.domainmodel.model;

import com.learning.enterprisepatterns.domainmodel.model.strategy.CompleteRecognitionStrategy;
import com.learning.enterprisepatterns.domainmodel.model.strategy.RecognitionStrategy;
import com.learning.enterprisepatterns.domainmodel.model.strategy.ThreeWayRecognitionStrategy;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Product {
    @Id
    private String id;
    private String name;
    private RecognitionStrategy recognitionStrategy;

    public Product(String name, RecognitionStrategy recognitionStrategy) {
        this.name = name;
        this.recognitionStrategy = recognitionStrategy;
    }

    public static Product newWordProcessor(String name) {
        return new Product(name, new CompleteRecognitionStrategy());
    }

    public static Product newSpreadsheet(String name) {
        return new Product(name, new ThreeWayRecognitionStrategy(60, 90));
    }

    public static Product newDatabase(String name) {
        return new Product(name, new ThreeWayRecognitionStrategy(30, 60));
    }

    public void calculateRecognitions(Contract contract) {
        recognitionStrategy.calculateRevenueRecognitions(contract);
    }
}
