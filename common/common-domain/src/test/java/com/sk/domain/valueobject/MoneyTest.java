package com.sk.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    public void testIsGreaterThanZero() {
        Money positiveMoney = new Money(new BigDecimal("10.00"));
        Money zeroMoney = new Money(new BigDecimal("0.00"));
        Money negativeMoney = new Money(new BigDecimal("-5.00"));

        assertTrue(positiveMoney.isGreaterThanZero());
        assertFalse(zeroMoney.isGreaterThanZero());
        assertFalse(negativeMoney.isGreaterThanZero());
    }

    @Test
    public void testIsGreaterThan() {
        Money money1 = new Money(new BigDecimal("10.00"));
        Money money2 = new Money(new BigDecimal("5.00"));
        Money money3 = new Money(new BigDecimal("10.00"));

        assertTrue(money1.isGreaterThan(money2));
        assertFalse(money2.isGreaterThan(money1));
        assertFalse(money1.isGreaterThan(money3)); // 같은 값
    }

    @Test
    public void testAdd() {
        Money money1 = new Money(new BigDecimal("10.00"));
        Money money2 = new Money(new BigDecimal("5.50"));
        Money expected = new Money(new BigDecimal("15.50"));

        assertEquals(expected, money1.add(money2));
    }

    @Test
    public void testSubtract() {
        Money money1 = new Money(new BigDecimal("10.00"));
        Money money2 = new Money(new BigDecimal("4.25"));
        Money expected = new Money(new BigDecimal("5.75"));

        assertEquals(expected, money1.subtract(money2));
    }

    @Test
    public void testMultiply() {
        Money money = new Money(new BigDecimal("3.33"));
        int multiplier = 3;
        Money expected = new Money(new BigDecimal("9.99")); // 3.33 * 3 = 9.99, 소수점 2째자리 반올림 적용

        assertEquals(expected, money.multiply(multiplier));
    }

    @Test
    public void testEquals() {
        Money money1 = new Money(new BigDecimal("10.00"));
        Money money2 = new Money(new BigDecimal("10.00"));
        Money money3 = new Money(new BigDecimal("15.00"));

        assertEquals(money1, money2);
        assertNotEquals(money1, money3);

    }

}