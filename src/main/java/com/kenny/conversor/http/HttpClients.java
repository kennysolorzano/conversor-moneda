package com.kenny.conversor.http;

import java.net.http.HttpClient;
import java.time.Duration;

public final class HttpClients {
    private static final HttpClient DEFAULT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private HttpClients() {}
    public static HttpClient get() { return DEFAULT; }
}