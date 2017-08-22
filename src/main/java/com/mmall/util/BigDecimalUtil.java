package com.mmall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    public static BigDecimal add (double v1, double v2) {
        BigDecimal a1 = new BigDecimal(Double.toString(v1));
        BigDecimal a2 = new BigDecimal(Double.toString(v2));
        return a1.add(a2);
    }

    public static BigDecimal sub (double v1, double v2) {
        BigDecimal a1 = new BigDecimal(Double.toString(v1));
        BigDecimal a2 = new BigDecimal(Double.toString(v2));
        return a1.subtract(a2);
    }

    public static BigDecimal mul (double v1, double v2) {
        BigDecimal a1 = new BigDecimal(Double.toString(v1));
        BigDecimal a2 = new BigDecimal(Double.toString(v2));
        return a1.multiply(a2);
    }

    public static BigDecimal div (double v1, double v2) {
        BigDecimal a1 = new BigDecimal(Double.toString(v1));
        BigDecimal a2 = new BigDecimal(Double.toString(v2));
        return a1.divide(a2, 2, BigDecimal.ROUND_HALF_UP);
    }







}
