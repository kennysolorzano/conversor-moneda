package com.kenny.conversor.http;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class Responses {
    private Responses() {}

    public static String ensure2xx(HttpResponse<String> res) throws IOException {
        int code = res.statusCode();
        if (code / 100 != 2) {
            throw new IOException("HTTP " + code + " -> " + res.body());
        }
        return res.body();
    }
}