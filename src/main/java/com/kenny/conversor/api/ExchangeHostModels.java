// src/main/java/com/kenny/conversor/api/ExchangeHostModels.java
package com.kenny.conversor.api;

import com.google.gson.annotations.SerializedName;

public class ExchangeHostModels {
    public static class ApiResponse {
        public Boolean success;
        public Query query;
        public Info info;
        public Double result;

        public static class Query {
            public String from;
            public String to;
            public Double amount;
        }
        public static class Info {
            @SerializedName("rate")
            public Double rate;
        }
    }
}
