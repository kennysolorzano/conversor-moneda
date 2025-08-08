// src/main/java/com/kenny/conversor/ConverterService.java
package com.kenny.conversor;

import java.util.Map;
import java.util.Set;

public class ConverterService {
    public static final Set<String> SUPPORTED = Set.of("USD","ARS","BOB","BRL","CLP","COP");

    private final RateProvider provider;

    public ConverterService(RateProvider provider) {
        this.provider = provider;
    }

    public double convert(String from, String to, double amount) throws Exception {
        from = from.toUpperCase();
        to = to.toUpperCase();

        if (!SUPPORTED.contains(from) || !SUPPORTED.contains(to)) {
            throw new IllegalArgumentException("Moneda no soportada. Permitidas: " + SUPPORTED);
        }
        if (from.equals(to)) return amount;

        // Pide tasas con base = from (más simple)
        Map<String, Double> ratesFrom = provider.getRates(from);
        Double rate = ratesFrom.get(to);
        if (rate == null) throw new IllegalStateException("No hay tasa " + from + "->" + to);
        return amount * rate;
    }
}
