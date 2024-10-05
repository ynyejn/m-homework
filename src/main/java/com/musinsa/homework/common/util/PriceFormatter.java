package com.musinsa.homework.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    public static String format(int price) {
        return NUMBER_FORMAT.format(price);
    }

}