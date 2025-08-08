package com.kenny.conversor.http;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public final class Requests {
    private Requests() {}

    // Ej: https://api.exchangerate.host/convert?from=USD&to=EUR&amount=1
    public static HttpRequest convert(String from, String to) {
        String url = String.format(
                "https://api.exchangerate.host/convert?from=%s&to=%s&amount=1",
                from, to
        );
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .header("Accept", "application/json")
                .build();
    }
}
