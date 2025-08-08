package com.kenny.conversor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class HttpRateProvider implements RateProvider {
    private static final Set<String> SUPPORTED = Set.of("USD","ARS","BOB","BRL","CLP","COP");

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public Map<String, Double> getRates(String base) throws Exception {
        base = base.toUpperCase();
        String symbols = String.join(",", SUPPORTED); // solo para referencia

        // ✅ Endpoint sin API key
        // Devuelve: { "result":"success", "base_code":"USD", "conversion_rates": { "ARS": ..., ... } }
        String url = "https://open.er-api.com/v6/latest/" + base;

        // Traza para confirmar que usamos el endpoint correcto
        System.out.println("GET " + url);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) {
            throw new RuntimeException("HTTP " + res.statusCode() + " -> " + res.body());
        }

        JsonObject root = JsonParser.parseString(res.body()).getAsJsonObject();

        // open.er-api.com usa "result" y "conversion_rates"
        if (root.has("result") && !"success".equalsIgnoreCase(root.get("result").getAsString())) {
            throw new RuntimeException("API error: " + res.body());
        }

        JsonObject map = null;
        if (root.has("conversion_rates") && root.get("conversion_rates").isJsonObject()) {
            map = root.getAsJsonObject("conversion_rates");
        } else if (root.has("rates") && root.get("rates").isJsonObject()) {
            map = root.getAsJsonObject("rates");
        } else {
            throw new RuntimeException("No se encontraron tasas en la respuesta: " + res.body());
        }

        Map<String, Double> out = new HashMap<>();
        for (String code : SUPPORTED) {
            if (map.has(code)) {
                out.put(code, map.get(code).getAsDouble());
            }
        }
        out.put(base, 1.0);
        return out;
    }

}
