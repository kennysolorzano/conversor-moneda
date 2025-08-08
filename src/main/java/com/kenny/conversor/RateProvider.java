package com.kenny.conversor;

import java.util.Map;

public interface RateProvider {
    /** Devuelve mapa de tasas hacia otras divisas tomando como base 'base'. */
    Map<String, Double> getRates(String base) throws Exception;
}
