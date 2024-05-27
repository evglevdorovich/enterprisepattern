package com.learning.enterprisepatterns.functionaltests;

import com.learning.enterprisepatterns.common.model.Money;
import com.learning.enterprisepatterns.transactionscript.service.RecognitionService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class RecognitionIT {

    @Autowired
    private RecognitionService recognitionService;

    @Test
    void testRecognizedRevenue() {
        val contract = 1L;
        val calendar = Calendar.getInstance();
        calendar.set(2024, 8, 10);
        val expectedMoney = Money.dollars(1000);

        val result = recognitionService.recognizedRevenue(contract, Date.from(calendar.toInstant()));
        assertThat(result).isEqualTo(expectedMoney);
    }

    @Test
    void calculateRecognition() {
        recognitionService.calculateRevenueRecognitions(1);
    }
}
