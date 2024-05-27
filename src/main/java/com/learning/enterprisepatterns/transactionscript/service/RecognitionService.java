package com.learning.enterprisepatterns.transactionscript.service;

import com.learning.enterprisepatterns.common.model.Money;
import com.learning.enterprisepatterns.transactionscript.repository.ContractRepository;
import com.learning.enterprisepatterns.transactionscript.repository.RevenueRecognitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RecognitionService {
    private final RevenueRecognitionRepository revenueRecognitionRepository;
    private final ContractRepository contractRepository;

    public Money recognizedRevenue(long contractNumber, Date asOf) {
        val rowSet = revenueRecognitionRepository.findByContractAndRecognizedOnBefore(contractNumber, asOf);
        var money = Money.dollars(0);
        while (rowSet.next()) {
            money = money.add(Money.dollars(rowSet.getDouble("amount")));
        }
        return money;
    }

    public void calculateRevenueRecognitions(long contractNumber) {
        val rowSet = contractRepository.findById(contractNumber);
        rowSet.next();
        val revenue = Money.dollars(rowSet.getDouble("revenue"));
        val dateSigned = rowSet.getDate("date_signed");
        val type = rowSet.getString("type");

        if (type.equals("S")) {
            val allocatedMoney = revenue.allocate(3);
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[0].getAmount().doubleValue(), dateSigned);
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[1].getAmount().doubleValue(), addDays(dateSigned, 60));
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[2].getAmount().doubleValue(), addDays(dateSigned, 90));
        } else if (type.equals("W")) {
            revenueRecognitionRepository.insertRecognition(contractNumber, revenue.getAmount().doubleValue(), dateSigned);
        } else {
            val allocatedMoney = revenue.allocate(3);
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[0].getAmount().doubleValue(), dateSigned);
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[1].getAmount().doubleValue(), addDays(dateSigned, 30));
            revenueRecognitionRepository.insertRecognition(contractNumber, allocatedMoney[2].getAmount().doubleValue(), addDays(dateSigned, 60));
        }
    }

    private static Date addDays(Date dateSigned, int days) {
        return Date.from(Instant.ofEpochMilli(dateSigned.getTime()).plus(days, ChronoUnit.DAYS));
    }
}
