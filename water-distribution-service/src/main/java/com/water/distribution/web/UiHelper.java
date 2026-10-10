package com.water.distribution.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Small helper exposed to Thymeleaf templates as the bean "ui",
 * e.g. ${@ui.percent(t.currentLevelLitres, t.capacityLitres)}.
 */
@Component("ui")
public class UiHelper {

    private static final Set<String> GOOD = Set.of(
            "ACTIVE", "APPROVED", "COMPLETED", "OPERATIONAL", "SCHEDULED", "IN_PROGRESS");
    private static final Set<String> WARN = Set.of(
            "PENDING", "DRAFT", "MAINTENANCE", "UNDER_MAINTENANCE", "MEDIUM");
    private static final Set<String> BAD = Set.of(
            "REJECTED", "CANCELLED", "INACTIVE", "DAMAGED", "HIGH");

    /** value / total as a whole percentage between 0 and 100. */
    public int percent(BigDecimal value, BigDecimal total) {
        if (value == null || total == null || total.signum() <= 0) {
            return 0;
        }
        int p = value.multiply(BigDecimal.valueOf(100))
                .divide(total, 0, RoundingMode.HALF_UP)
                .intValue();
        return Math.max(0, Math.min(100, p));
    }

    /** Same as percent() but for Integer values (litres allocated / available). */
    public int percentOfInt(Integer value, Integer total) {
        if (value == null || total == null || total <= 0) {
            return 0;
        }
        return percent(BigDecimal.valueOf(value), BigDecimal.valueOf(total));
    }

    /** Colour class for a tank-level bar: low levels are red. */
    public String tankBar(int percent) {
        if (percent <= 25) {
            return "bar-low";
        }
        if (percent <= 50) {
            return "bar-mid";
        }
        return "bar-high";
    }

    /** CSS classes for a coloured status / priority badge. */
    public String badge(String value) {
        if (value == null) {
            return "badge neutral";
        }
        String v = value.trim().toUpperCase();
        if (GOOD.contains(v)) {
            return "badge ok";
        }
        if (WARN.contains(v)) {
            return "badge warn";
        }
        if (BAD.contains(v)) {
            return "badge bad";
        }
        return "badge neutral";
    }

    /** Returns the base options, plus the current value if it is not already one of them. */
    public static List<String> withCurrent(List<String> base, String current) {
        List<String> result = new ArrayList<>(base);
        if (current != null && !current.isBlank()
                && result.stream().noneMatch(o -> o.equalsIgnoreCase(current))) {
            result.add(current);
        }
        return result;
    }
}
