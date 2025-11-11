package com.bookstore.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {

    private static final Locale VIETNAM_LOCALE = new Locale("vi", "VN");
    private static final NumberFormat VIETNAM_CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(VIETNAM_LOCALE);

    public static String formatVND(BigDecimal price) {
        if (price == null) {
            return "₫0";
        }
        return VIETNAM_CURRENCY_FORMAT.format(price).replace("₫", "₫ ");
    }

    public static String formatVND(Double price) {
        if (price == null) {
            return "₫0";
        }
        return formatVND(BigDecimal.valueOf(price));
    }

    public static String formatVND(Long price) {
        if (price == null) {
            return "₫0";
        }
        return formatVND(BigDecimal.valueOf(price));
    }

    public static String formatDiscountPercentage(BigDecimal originalPrice, BigDecimal discountPrice) {
        if (originalPrice == null || discountPrice == null ||
                originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return "0%";
        }

        BigDecimal discountAmount = originalPrice.subtract(discountPrice);
        BigDecimal percentage = discountAmount.divide(originalPrice, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return percentage.setScale(0, BigDecimal.ROUND_HALF_UP) + "%";
    }

    public static String formatCompactNumber(Long number) {
        if (number == null) {
            return "0";
        }

        if (number < 1000) {
            return number.toString();
        } else if (number < 1000000) {
            return String.format("%.1fK", number / 1000.0);
        } else {
            return String.format("%.1fM", number / 1000000.0);
        }
    }

    public static BigDecimal calculateDiscountAmount(BigDecimal originalPrice, BigDecimal discountPrice) {
        if (originalPrice == null || discountPrice == null) {
            return BigDecimal.ZERO;
        }
        return originalPrice.subtract(discountPrice);
    }
}