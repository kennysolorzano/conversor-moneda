package com.kenny.conversor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record ConversionRecord(LocalDateTime timestamp, String from, String to,
                               double amount, double rate, double result) {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String toPrettyString() {
        return String.format(Locale.US,
                "[%s] %.2f %s -> %.2f %s  (tasa: %.6f)",
                FMT.format(timestamp), amount, from, result, to, rate);
    }

    public String toCsvLine() {
        return String.format(Locale.US, "%s,%s,%s,%.6f,%.6f,%.6f",
                FMT.format(timestamp), from, to, amount, rate, result);
    }
}
