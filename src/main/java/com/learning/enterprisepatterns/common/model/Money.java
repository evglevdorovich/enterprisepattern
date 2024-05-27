package com.learning.enterprisepatterns.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Currency;

@Data
@NoArgsConstructor
public final class Money implements Comparable<Money> {

    private static int[] CENTS = new int[]{1, 10, 100, 1000};
    /**
     * The monetary amount in the smallest unit (e.g., cents, pence).
     * Using long to avoid floating-point issues.
     */
    private long amount;

    private Currency currency;

    public static Money dollars(double amount) {
        return new Money(amount, Currency.getInstance("USD"));
    }

    public Money(long amount, Currency currency) {
        this.currency = currency;
        this.amount = amount * centFactor();
    }

    /**
     * Constructor that accepts an amount in the standard unit (e.g., dollars).
     * The amount will be converted to the smallest unit.
     *
     * @param amount   The amount in the standard unit.
     * @param currency The currency of the amount.
     */
    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.amount = Math.round(amount * centFactor());
    }

    public Money(BigDecimal amount, Currency currency, MathContext mathContext) {
        this.currency = currency;
        this.amount = amount.multiply(BigDecimal.valueOf(centFactor()), mathContext).longValue();
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
    }

    public Money add(Money other) {
        assertSameCurrencyAs(other);
        return newMoney(amount + other.amount);
    }

    public Money subtract(Money other) {
        assertSameCurrencyAs(other);
        return newMoney(amount - other.amount);
    }

    public Money multiply(double amount) {
        return multiply(BigDecimal.valueOf(amount), MathContext.DECIMAL64);
    }

    public Money multiply(BigDecimal amount) {
        return multiply(amount, MathContext.DECIMAL64);
    }

    public Money multiply(BigDecimal other, MathContext mathContext) {
        return new Money(other.multiply(getAmount(), mathContext), currency, mathContext);
    }

    public Money[] allocate(long equalParts) {
        val ratio = new long[(int) equalParts];
        Arrays.fill(ratio, 1);
        return allocate(ratio);
    }

    public Money[] allocate(long[] ratios) {
        val totalRatios = Arrays.stream(ratios).sum();
        val moneys = Arrays.stream(ratios)
                .mapToObj(ratio -> multiply((double) ratio / totalRatios))
                .toArray(Money[]::new);

        val total = Arrays.stream(moneys).reduce(Money::add).orElseThrow();
        val remainder = amount - total.amount;

        for (int i = 0; i < remainder; i++) {
            moneys[i].amount++;
        }
        return moneys;
    }

    @Override
    public int compareTo(@NonNull Money other) {
        assertSameCurrencyAs(other);
        return Long.compare(amount, other.amount);
    }

    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }

    /**
     * Calculate the factor by which the amount should be multiplied
     * to convert it to the smallest unit.
     *
     * @return The cent factor based on the currency's default fraction digits.
     */
    private int centFactor() {
        // could be done via Math.round(Math.pow(10, currency.getDefaultFractionDigits()));
        // but we want to avoid unnecessary rounding and it's much easier because we usually have less than
        // 4 fraction digits
        return CENTS[currency.getDefaultFractionDigits()];
    }

    private void assertSameCurrencyAs(Money arg) {
        Assert.isTrue(arg.getCurrency().equals(this.currency), "currency does not match");
    }

    private Money newMoney(long amount) {
        val money = new Money();
        money.amount = amount;
        money.currency = currency;
        return money;
    }

    // Why doesn't the Currency class know its default fraction digits?

    // The Currency class should not be concerned with how the monetary values are stored or composed.
    // It focuses on currency-specific properties, such as the code and symbol.
    // The Money class handles the storage and arithmetic of monetary values, which allows for flexibility
    // in how these values are represented (e.g., using BigDecimal, long, double, etc.).
}
