package com.kenny.conversor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class HttpRateProvider implements RateProvider {
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Cache simple de tasas por base (TTL 10 minutos)
    private final Map<String, CachedRates> cache = new HashMap<>();
    private static final long TTL_SECONDS = 600;

    // Conjunto dinámico de códigos válidos (se carga desde API la primera vez)
    private Set<String> supportedCodes;

    @Override
    public Map<String, Double> getRates(String base) throws Exception {
        base = base.toUpperCase();

        // Cache (TTL 10 min)
        var now = java.time.Instant.now();
        var cached = cache.get(base);
        if (cached != null && now.getEpochSecond() - cached.ts <= TTL_SECONDS) {
            return cached.rates;
        }

        String url = "https://open.er-api.com/v6/latest/" + base;
        var req = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .timeout(java.time.Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        var res = http.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) {
            throw new RuntimeException("HTTP " + res.statusCode() + " -> " + res.body());
        }

        var root = com.google.gson.JsonParser.parseString(res.body()).getAsJsonObject();

        // Aceptar ambos formatos:
        // - { "result":"success", "rates": { ... } }
        // - { "result":"success", "conversion_rates": { ... } }
        if (!root.has("result") || !"success".equalsIgnoreCase(root.get("result").getAsString())) {
            throw new RuntimeException("API error: " + res.body());
        }

        com.google.gson.JsonObject map = null;
        if (root.has("rates") && root.get("rates").isJsonObject()) {
            map = root.getAsJsonObject("rates");
        } else if (root.has("conversion_rates") && root.get("conversion_rates").isJsonObject()) {
            map = root.getAsJsonObject("conversion_rates");
        } else {
            throw new RuntimeException("No se encontraron tasas en la respuesta: " + res.body());
        }

        java.util.Map<String, Double> out = new java.util.HashMap<>();
        for (var e : map.entrySet()) {
            out.put(e.getKey(), e.getValue().getAsDouble());
        }
        out.put(base, 1.0);

        cache.put(base, new CachedRates(out, now.getEpochSecond()));

        // Poblar listado ISO si hace falta
        if (supportedCodes == null) supportedCodes = java.util.Collections.unmodifiableSet(out.keySet());

        return out;
    }


    /** Devuelve el set completo de códigos ISO válidos (aprendidos de la API). */
    public Set<String> getSupportedCodes() throws Exception {
        if (supportedCodes != null) return supportedCodes;
        // Aprende desde USD para poblar el listado
        getRates("USD");
        return supportedCodes != null ? supportedCodes : Set.of("USD");
    }

    private static class CachedRates {
        final Map<String, Double> rates;
        final long ts;
        CachedRates(Map<String, Double> rates, long ts) {
            this.rates = rates;
            this.ts = ts;
        }
    }
}
